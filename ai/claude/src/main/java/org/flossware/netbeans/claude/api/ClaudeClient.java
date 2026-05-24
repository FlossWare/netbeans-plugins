/*
 * Copyright 2026 FlossWare.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.flossware.netbeans.claude.api;

import com.anthropic.client.AnthropicClient;
import com.anthropic.models.*;
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
    private final List<Message> conversationHistory;

    public ClaudeClient() {
        this.conversationHistory = new ArrayList<>();
        initializeClient();
    }

    private void initializeClient() {
        String apiKey = getApiKey();
        if (apiKey != null && !apiKey.isEmpty()) {
            client = AnthropicClient.builder()
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
        prefs.put(PREF_API_KEY, apiKey);
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
                .maxTokens(maxTokens)
                .temperature(temperature)
                .addMessage(MessageParam.builder()
                        .role(MessageParamRole.USER)
                        .content(ContentBlock.ofText(userMessage))
                        .build());

        // Add conversation history if exists
        for (Message historyMsg : conversationHistory) {
            paramsBuilder.addMessage(MessageParam.builder()
                    .role(MessageParamRole.of(historyMsg.role().value()))
                    .content(historyMsg.content().get(0))
                    .build());
        }

        // Send request
        Message response = client.messages().create(paramsBuilder.build());

        // Store in conversation history
        conversationHistory.add(Message.builder()
                .role(MessageRole.USER)
                .addContent(ContentBlock.ofText(userMessage))
                .build());
        conversationHistory.add(response);

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
            if (block.isText()) {
                result.append(block.asText().text());
            }
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
        List<MessageParam> messages = new ArrayList<>();

        // Add conversation history
        for (Message historyMsg : conversationHistory) {
            messages.add(MessageParam.builder()
                    .role(MessageParamRole.of(historyMsg.role().value()))
                    .content(historyMsg.content().get(0))
                    .build());
        }

        // Add current message
        messages.add(MessageParam.builder()
                .role(MessageParamRole.USER)
                .content(ContentBlock.ofText(userMessage))
                .build());

        MessageCreateParams params = MessageCreateParams.builder()
                .model(model)
                .maxTokens(maxTokens)
                .temperature(temperature)
                .messages(messages)
                .build();

        // Stream the response
        StringBuilder fullResponse = new StringBuilder();
        client.messages().stream(params).forEach(event -> {
            if (event.isContentBlockDelta()) {
                ContentBlockDelta delta = event.asContentBlockDelta();
                if (delta.delta().isTextDelta()) {
                    String text = delta.delta().asTextDelta().text();
                    fullResponse.append(text);
                    if (onChunk != null) {
                        onChunk.accept(text);
                    }
                }
            }
        });

        String completeResponse = fullResponse.toString();

        // Store in conversation history
        conversationHistory.add(Message.builder()
                .role(MessageRole.USER)
                .addContent(ContentBlock.ofText(userMessage))
                .build());
        conversationHistory.add(Message.builder()
                .role(MessageRole.ASSISTANT)
                .addContent(ContentBlock.ofText(completeResponse))
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
