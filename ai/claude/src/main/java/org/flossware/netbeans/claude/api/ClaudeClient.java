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
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

/**
 * Client for interacting with Claude API
 */
public class ClaudeClient {

    private static final String PREF_API_KEY = "anthropic.api.key";
    private static final String PREF_MODEL = "anthropic.model";
    private static final String PREF_MAX_TOKENS = "anthropic.max.tokens";
    private static final String PREF_TEMPERATURE = "anthropic.temperature";
    private static final String DEFAULT_MODEL = "claude-sonnet-4-5@20250929";

    private AnthropicClient client;
    private final List<MessageParam> conversationHistory;

    public ClaudeClient() {
        this.conversationHistory = new ArrayList<>();
        initializeClient();
    }

    private void initializeClient() {
        String apiKey = getApiKey();
        if (apiKey != null && !apiKey.isEmpty()) {
            client = AnthropicOkHttpClient.builder()
                    .apiKey(apiKey)
                    .build();
        }
    }

    /**
     * Get the API key from NetBeans preferences
     */
    public String getApiKey() {
        Preferences prefs = NbPreferences.forModule(ClaudeClient.class);
        return prefs.get(PREF_API_KEY, "");
    }

    /**
     * Set the API key in NetBeans preferences
     */
    public void setApiKey(String apiKey) {
        Preferences prefs = NbPreferences.forModule(ClaudeClient.class);
        if (apiKey == null || apiKey.trim().isEmpty()) {
            prefs.remove(PREF_API_KEY);
        } else {
            prefs.put(PREF_API_KEY, apiKey.trim());
        }
        initializeClient();
    }

    /**
     * Check if API key is configured
     */
    public boolean isConfigured() {
        String apiKey = getApiKey();
        return apiKey != null && !apiKey.isEmpty();
    }

    /**
     * Send a message to Claude and get a response
     */
    public String sendMessage(String userMessage) throws Exception {
        if (!isConfigured()) {
            throw new IllegalStateException("API key not configured. Please configure your Anthropic API key in Tools > Options > Claude");
        }

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
        Message response = client.messages().create(paramsBuilder.build());

        // Store in conversation history
        conversationHistory.add(MessageParam.builder()
                .role(MessageParam.Role.USER)
                .content(userMessage)
                .build());
        conversationHistory.add(MessageParam.builder()
                .role(MessageParam.Role.ASSISTANT)
                .content(extractTextFromResponse(response))
                .build());

        // Extract text response
        return extractTextFromResponse(response);
    }

    /**
     * Send a message with code context
     */
    public String sendMessageWithContext(String userMessage, String codeContext) throws Exception {
        String fullMessage = String.format(
            "Here is the code context:\n\n```\n%s\n```\n\nUser question: %s",
            codeContext,
            userMessage
        );
        return sendMessage(fullMessage);
    }

    /**
     * Clear conversation history
     */
    public void clearHistory() {
        conversationHistory.clear();
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
     */
    public int getHistorySize() {
        return conversationHistory.size();
    }

    /**
     * Send a message with streaming response
     * @param userMessage The message to send
     * @param onChunk Callback for each streamed chunk
     * @return The complete response
     */
    public String sendMessageStreaming(String userMessage, Consumer<String> onChunk) throws Exception {
        if (!isConfigured()) {
            throw new IllegalStateException("API key not configured. Please configure your Anthropic API key in Tools > Options > Claude");
        }

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

        return completeResponse;
    }

    /**
     * Send a message with code context and streaming
     */
    public String sendMessageWithContextStreaming(String userMessage, String codeContext, Consumer<String> onChunk) throws Exception {
        String fullMessage = String.format(
            "Here is the code context:\n\n```\n%s\n```\n\nUser question: %s",
            codeContext,
            userMessage
        );
        return sendMessageStreaming(fullMessage, onChunk);
    }
}
