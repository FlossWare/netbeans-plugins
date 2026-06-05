/*
 * Copyright 2026 FlossWare.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.flossware.netbeans.claude.api;

import com.anthropic.client.AnthropicClient;
import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import com.anthropic.core.http.StreamResponse;
import com.anthropic.models.messages.ContentBlock;
import com.anthropic.models.messages.Message;
import com.anthropic.models.messages.MessageCreateParams;
import com.anthropic.models.messages.MessageParam;
import com.anthropic.models.messages.RawMessageStreamEvent;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import org.flossware.netbeans.ai.core.validation.MessageValidator;
import org.flossware.netbeans.claude.exceptions.ClaudeAuthException;
import org.flossware.netbeans.claude.exceptions.ClaudeConfigException;
import org.flossware.netbeans.claude.exceptions.ClaudeException;
import org.flossware.netbeans.claude.exceptions.ClaudeNetworkException;
import org.flossware.netbeans.claude.exceptions.ClaudeParseException;
import org.flossware.netbeans.claude.exceptions.ClaudeRateLimitException;
import org.openide.util.NbPreferences;

/**
 * Client for interacting with Claude API
 *
 * <p>Thread-safe implementation using CopyOnWriteArrayList for conversation history.
 * Provides synchronous and streaming message sending capabilities with automatic retry logic.</p>
 */
public class ClaudeClient implements AutoCloseable {

    private static final Logger LOGGER = Logger.getLogger(ClaudeClient.class.getName());

    private static final String PREF_API_KEY = "anthropic.api.key";
    private static final String PREF_MODEL = "anthropic.model";
    private static final String PREF_MAX_TOKENS = "anthropic.max.tokens";
    private static final String PREF_TEMPERATURE = "anthropic.temperature";
    private static final String PREF_API_URL = "anthropic.api.url";
    private static final String DEFAULT_MODEL = "claude-sonnet-4-5@20250929";
    private static final String DEFAULT_API_URL = "https://api.anthropic.com";

    private AnthropicClient client;
    private final List<MessageParam> conversationHistory;
    private final RetryPolicy retryPolicy;
    private final MessageValidator messageValidator;
    private volatile boolean closed = false;

    public ClaudeClient() {
        this.conversationHistory = new CopyOnWriteArrayList<>();
        this.retryPolicy = new RetryPolicy();
        this.messageValidator = MessageValidator.createStandard();
        initializeClient();
    }

    public ClaudeClient(RetryPolicy retryPolicy) {
        this.conversationHistory = new CopyOnWriteArrayList<>();
        this.retryPolicy = Objects.requireNonNull(retryPolicy, "retryPolicy cannot be null");
        this.messageValidator = MessageValidator.createStandard();
        initializeClient();
    }

    private void initializeClient() {
        String apiKey = getApiKey();
        if (apiKey != null && !apiKey.isEmpty()) {
            try {
                AnthropicOkHttpClient.Builder builder = AnthropicOkHttpClient.builder()
                        .apiKey(apiKey);

                // Get custom API URL if configured
                String apiUrl = getApiUrl();
                if (apiUrl != null && !apiUrl.isEmpty()) {
                    builder.baseUrl(apiUrl);
                }

                client = builder.build();
                LOGGER.log(Level.INFO, "Claude client initialized successfully");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to initialize Claude client", e);
                client = null;
            }
        }
    }

    /**
     * Get the API key from NetBeans preferences
     *
     * @return The configured API key, or empty string if not set
     */
    public String getApiKey() {
        Objects.requireNonNull(NbPreferences.class, "NetBeans Preferences not available");
        Preferences prefs = NbPreferences.forModule(ClaudeClient.class);
        return Objects.requireNonNull(prefs, "Preferences cannot be null").get(PREF_API_KEY, "");
    }

    /**
     * Get the API URL from NetBeans preferences
     *
     * @return The configured API URL, or default URL if not set
     */
    public String getApiUrl() {
        Objects.requireNonNull(NbPreferences.class, "NetBeans Preferences not available");
        Preferences prefs = NbPreferences.forModule(ClaudeClient.class);
        return Objects.requireNonNull(prefs, "Preferences cannot be null").get(PREF_API_URL, DEFAULT_API_URL);
    }

    /**
     * Set the API key in NetBeans preferences
     *
     * @param apiKey The API key to set (null or empty to remove)
     */
    public void setApiKey(String apiKey) {
        Objects.requireNonNull(NbPreferences.class, "NetBeans Preferences not available");
        Preferences prefs = NbPreferences.forModule(ClaudeClient.class);
        Objects.requireNonNull(prefs, "Preferences cannot be null");
        if (apiKey == null || apiKey.trim().isEmpty()) {
            prefs.remove(PREF_API_KEY);
        } else {
            prefs.put(PREF_API_KEY, apiKey.trim());
        }
        initializeClient();
    }

    /**
     * Set the API URL in NetBeans preferences
     *
     * @param apiUrl The API URL to set (null or empty to use default)
     */
    public void setApiUrl(String apiUrl) {
        Objects.requireNonNull(NbPreferences.class, "NetBeans Preferences not available");
        Preferences prefs = NbPreferences.forModule(ClaudeClient.class);
        Objects.requireNonNull(prefs, "Preferences cannot be null");
        if (apiUrl == null || apiUrl.trim().isEmpty()) {
            prefs.remove(PREF_API_URL);
        } else {
            prefs.put(PREF_API_URL, apiUrl.trim());
        }
        initializeClient();
    }

    /**
     * Check if API key is configured
     *
     * @return true if API key is configured, false otherwise
     */
    public boolean isConfigured() {
        String apiKey = getApiKey();
        return apiKey != null && !apiKey.isEmpty();
    }

    /**
     * Send a message to Claude and get a response
     *
     * @param userMessage The message to send (must not be null or empty)
     * @return The response from Claude
     * @throws ClaudeException if the message cannot be sent
     */
    public String sendMessage(String userMessage) throws ClaudeException {
        Objects.requireNonNull(userMessage, "userMessage cannot be null");

        // Validate input
        try {
            messageValidator.validateMessage(userMessage);
        } catch (IllegalArgumentException e) {
            throw new ClaudeException("Invalid message: " + e.getMessage(), e);
        }

        if (!isConfigured()) {
            LOGGER.log(Level.WARNING, "Attempted to send message without API key configured");
            throw new ClaudeConfigException("API key not configured. Please configure your Anthropic API key in Tools > Options > Claude");
        }

        // Synchronize check-and-use to prevent TOCTOU race condition
        // between closed flag check and client usage
        synchronized (this) {
            if (closed) {
                throw new ClaudeException("Client has been closed");
            }
            return retryPolicy.executeWithRetry(() -> sendMessageInternal(userMessage));
        }
    }

    private String sendMessageInternal(String userMessage) throws ClaudeException {
        LOGGER.log(Level.FINE, "Sending message to Claude API");

        // Get preferences
        Preferences prefs = NbPreferences.forModule(ClaudeClient.class);
        String model = prefs.get(PREF_MODEL, DEFAULT_MODEL);
        int maxTokens = prefs.getInt(PREF_MAX_TOKENS, 4096);
        double temperature = prefs.getDouble(PREF_TEMPERATURE, 1.0);

        // Build message parameters
        MessageCreateParams.Builder paramsBuilder = MessageCreateParams.builder()
                .model(model)
                .maxTokens((long) maxTokens)
                .temperature(temperature);

        // Add conversation history if exists
        for (MessageParam historyMsg : conversationHistory) {
            paramsBuilder.addMessage(historyMsg);
        }

        // Add current message
        paramsBuilder.addUserMessage(userMessage);

        // Send request
        Message response;
        try {
            response = client.messages().create(paramsBuilder.build());
        } catch (com.anthropic.errors.AnthropicServiceException e) {
            LOGGER.log(Level.SEVERE, "Claude API service error", e);
            int statusCode = e.statusCode();
            if (statusCode == 401 || statusCode == 403) {
                throw new ClaudeAuthException("Authentication failed: " + e.getMessage(), e);
            } else if (statusCode == 429) {
                throw new ClaudeRateLimitException("Rate limit exceeded: " + e.getMessage(), e);
            } else {
                throw new ClaudeNetworkException("API request failed: " + e.getMessage(), e);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error calling Claude API", e);
            throw new ClaudeParseException("Failed to parse API response: " + e.getMessage(), e);
        }

        // Store in conversation history
        conversationHistory.add(MessageParam.builder()
                .role(MessageParam.Role.USER)
                .content(userMessage)
                .build());
        conversationHistory.add(MessageParam.builder()
                .role(MessageParam.Role.ASSISTANT)
                .content(extractTextFromResponse(response))
                .build());

        LOGGER.log(Level.FINE, "Successfully received response from Claude API");

        // Extract text response
        return extractTextFromResponse(response);
    }

    /**
     * Send a message with code context
     *
     * @param userMessage The message to send (must not be null)
     * @param codeContext The code context (must not be null)
     * @return The response from Claude
     * @throws ClaudeException if the message cannot be sent
     */
    public String sendMessageWithContext(String userMessage, String codeContext) throws ClaudeException {
        Objects.requireNonNull(userMessage, "userMessage cannot be null");
        Objects.requireNonNull(codeContext, "codeContext cannot be null");

        // Validate inputs
        try {
            messageValidator.validateMessageWithContext(userMessage, codeContext);
        } catch (IllegalArgumentException e) {
            throw new ClaudeException("Invalid message or context: " + e.getMessage(), e);
        }

        String fullMessage = String.format(
            "Here is the code context:\n\n```\n%s\n```\n\nUser question: %s",
            codeContext,
            userMessage
        );
        return sendMessage(fullMessage);
    }

    /**
     * Clear conversation history
     *
     * <p>Thread-safe operation using CopyOnWriteArrayList</p>
     *
     * @throws ClaudeException if the client has been closed
     */
    public void clearHistory() throws ClaudeException {
        // Synchronize check-and-use to prevent TOCTOU race condition
        // between closed flag check and resource usage
        synchronized (this) {
            if (closed) {
                throw new ClaudeException("Client has been closed");
            }
            conversationHistory.clear();
        }
    }

    /**
     * Extract text content from Claude response
     */
    private String extractTextFromResponse(Message response) {
        StringBuilder result = new StringBuilder();
        for (ContentBlock block : response.content()) {
            block.text().ifPresent(textBlock -> result.append(textBlock.text()));
        }
        return result.toString();
    }

    /**
     * Get conversation history size
     *
     * <p>Thread-safe operation using CopyOnWriteArrayList</p>
     *
     * @return The number of messages in conversation history
     */
    public int getHistorySize() {
        return conversationHistory.size();
    }

    /**
     * Send a message with streaming response
     *
     * @param userMessage The message to send (must not be null)
     * @param onChunk Callback for each streamed chunk (must not be null)
     * @return The complete response
     * @throws ClaudeException if the message cannot be sent
     */
    public String sendMessageStreaming(String userMessage, Consumer<String> onChunk) throws ClaudeException {
        Objects.requireNonNull(userMessage, "userMessage cannot be null");
        Objects.requireNonNull(onChunk, "onChunk callback cannot be null");

        // Validate input
        try {
            messageValidator.validateMessage(userMessage);
        } catch (IllegalArgumentException e) {
            throw new ClaudeException("Invalid message: " + e.getMessage(), e);
        }

        if (!isConfigured()) {
            LOGGER.log(Level.WARNING, "Attempted to send streaming message without API key configured");
            throw new ClaudeConfigException("API key not configured. Please configure your Anthropic API key in Tools > Options > Claude");
        }

        // Synchronize check-and-use to prevent TOCTOU race condition
        // between closed flag check and client usage
        synchronized (this) {
            if (closed) {
                throw new ClaudeException("Client has been closed");
            }
            return retryPolicy.executeWithRetry(() -> sendMessageStreamingInternal(userMessage, onChunk));
        }
    }

    private String sendMessageStreamingInternal(String userMessage, Consumer<String> onChunk) throws ClaudeException {
        LOGGER.log(Level.FINE, "Sending streaming message to Claude API");

        // Get preferences
        Preferences prefs = NbPreferences.forModule(ClaudeClient.class);
        String model = prefs.get(PREF_MODEL, DEFAULT_MODEL);
        int maxTokens = prefs.getInt(PREF_MAX_TOKENS, 4096);
        double temperature = prefs.getDouble(PREF_TEMPERATURE, 1.0);

        // Build message parameters
        MessageCreateParams.Builder paramsBuilder = MessageCreateParams.builder()
                .model(model)
                .maxTokens((long) maxTokens)
                .temperature(temperature);

        // Add conversation history
        for (MessageParam historyMsg : conversationHistory) {
            paramsBuilder.addMessage(historyMsg);
        }

        // Add current message
        paramsBuilder.addUserMessage(userMessage);

        MessageCreateParams params = paramsBuilder.build();

        // Stream the response
        StringBuilder fullResponse = new StringBuilder();
        try (StreamResponse<RawMessageStreamEvent> streamResponse =
                client.messages().createStreaming(params)) {
            streamResponse.stream()
                    .flatMap(event -> event.contentBlockDelta().stream())
                    .flatMap(deltaEvent -> deltaEvent.delta().text().stream())
                    .forEach(textDelta -> {
                        String text = textDelta.text();
                        fullResponse.append(text);
                        if (onChunk != null) {
                            onChunk.accept(text);
                        }
                    });
        } catch (com.anthropic.errors.AnthropicServiceException e) {
            LOGGER.log(Level.SEVERE, "Claude API service error during streaming", e);
            int statusCode = e.statusCode();
            if (statusCode == 401 || statusCode == 403) {
                throw new ClaudeAuthException("Authentication failed: " + e.getMessage(), e);
            } else if (statusCode == 429) {
                throw new ClaudeRateLimitException("Rate limit exceeded: " + e.getMessage(), e);
            } else {
                throw new ClaudeNetworkException("API request failed: " + e.getMessage(), e);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error during streaming", e);
            throw new ClaudeParseException("Failed to parse streaming response: " + e.getMessage(), e);
        }

        String completeResponse = fullResponse.toString();

        // Store in conversation history
        conversationHistory.add(MessageParam.builder()
                .role(MessageParam.Role.USER)
                .content(userMessage)
                .build());
        conversationHistory.add(MessageParam.builder()
                .role(MessageParam.Role.ASSISTANT)
                .content(completeResponse)
                .build());

        LOGGER.log(Level.FINE, "Successfully received streaming response from Claude API");

        return completeResponse;
    }

    /**
     * Send a message with code context and streaming
     *
     * @param userMessage The message to send (must not be null)
     * @param codeContext The code context (must not be null)
     * @param onChunk Callback for each streamed chunk (must not be null)
     * @return The complete response
     * @throws ClaudeException if the message cannot be sent
     */
    public String sendMessageWithContextStreaming(String userMessage, String codeContext, Consumer<String> onChunk) throws ClaudeException {
        Objects.requireNonNull(userMessage, "userMessage cannot be null");
        Objects.requireNonNull(codeContext, "codeContext cannot be null");
        Objects.requireNonNull(onChunk, "onChunk callback cannot be null");

        // Validate inputs
        try {
            messageValidator.validateMessageWithContext(userMessage, codeContext);
        } catch (IllegalArgumentException e) {
            throw new ClaudeException("Invalid message or context: " + e.getMessage(), e);
        }

        String fullMessage = String.format(
            "Here is the code context:\n\n```\n%s\n```\n\nUser question: %s",
            codeContext,
            userMessage
        );
        return sendMessageStreaming(fullMessage, onChunk);
    }

    /**
     * Closes the Claude client and releases resources
     *
     * <p>Implements AutoCloseable for try-with-resources pattern</p>
     * <p>Synchronized to coordinate with other operations and prevent
     * TOCTOU race conditions during client closure</p>
     */
    @Override
    public synchronized void close() {
        closed = true;
        if (client != null) {
            try {
                client.close();
                LOGGER.log(Level.INFO, "Claude client closed successfully");
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error closing Claude client", e);
            }
        }
        conversationHistory.clear();
    }
}
