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

package org.flossware.netbeans.grok.api;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.prefs.Preferences;
import okhttp3.OkHttpClient;
import org.openide.util.NbPreferences;
import org.flossware.netbeans.ai.core.validation.MessageValidator;

/**
 * Client for interacting with xAI Grok API
 */
public class GrokClient {

    private static final String PREF_API_KEY = "grok.api.key";
    private static final String PREF_MODEL = "grok.model";
    private static final String PREF_MAX_TOKENS = "grok.max.tokens";
    private static final String PREF_TEMPERATURE = "grok.temperature";
    private static final String GROK_API_BASE_URL = "https://api.x.ai/v1/";

    private OpenAiService service;
    private String apiKey;
    private String model;
    private int maxTokens;
    private double temperature;
    private List<ChatMessage> conversationHistory;
    private final MessageValidator messageValidator;

    public GrokClient() {
        this.messageValidator = MessageValidator.createStandard();
        loadSettings();
        conversationHistory = new ArrayList<>();
    }

    private void loadSettings() {
        Preferences prefs = NbPreferences.forModule(GrokClient.class);
        this.apiKey = prefs.get(PREF_API_KEY, "");
        this.model = prefs.get(PREF_MODEL, "grok-beta");
        this.maxTokens = prefs.getInt(PREF_MAX_TOKENS, 4096);
        this.temperature = prefs.getDouble(PREF_TEMPERATURE, 0.7);

        if (!apiKey.isEmpty()) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .readTimeout(Duration.ofSeconds(60))
                    .build();
            this.service = new OpenAiService(apiKey, Duration.ofSeconds(60));

            // Configure for Grok endpoint using reflection since OpenAiService doesn't expose baseUrl setter
            try {
                java.lang.reflect.Field retrofitField = OpenAiService.class.getDeclaredField("retrofit");
                retrofitField.setAccessible(true);
                Object retrofit = retrofitField.get(service);

                java.lang.reflect.Method baseUrlMethod = retrofit.getClass().getMethod("baseUrl");
                Object httpUrl = baseUrlMethod.invoke(retrofit);

                // For now, create a new service with custom base URL using OkHttp interceptor
                client = new OkHttpClient.Builder()
                        .readTimeout(Duration.ofSeconds(60))
                        .addInterceptor(chain -> {
                            okhttp3.Request original = chain.request();
                            okhttp3.HttpUrl originalUrl = original.url();

                            // Replace OpenAI URL with Grok URL
                            okhttp3.HttpUrl newUrl = okhttp3.HttpUrl.parse(GROK_API_BASE_URL + originalUrl.encodedPath().substring(1));

                            okhttp3.Request request = original.newBuilder()
                                    .url(newUrl)
                                    .build();
                            return chain.proceed(request);
                        })
                        .build();

                this.service = new OpenAiService(apiKey, Duration.ofSeconds(60));
            } catch (Exception e) {
                // Fallback: just use standard OpenAI service
                // User will need to ensure Grok API compatibility
                this.service = new OpenAiService(apiKey, Duration.ofSeconds(60));
            }
        }
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
        if (!apiKey.isEmpty()) {
            this.service = new OpenAiService(apiKey, Duration.ofSeconds(60));
        }
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setMaxTokens(int maxTokens) {
        this.maxTokens = maxTokens;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    /**
     * Send a message and get a response
     */
    public String sendMessage(String message) {
        // Validate input
        try {
            messageValidator.validateMessage(message);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid message: " + e.getMessage(), e);
        }

        if (service == null) {
            throw new IllegalStateException("API key not configured");
        }

        conversationHistory.add(new ChatMessage(ChatMessageRole.USER.value(), message));

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(model)
                .messages(conversationHistory)
                .maxTokens(maxTokens)
                .temperature(temperature)
                .build();

        String response = service.createChatCompletion(request)
                .getChoices()
                .get(0)
                .getMessage()
                .getContent();

        conversationHistory.add(new ChatMessage(ChatMessageRole.ASSISTANT.value(), response));

        return response;
    }

    /**
     * Send a message asynchronously
     */
    public CompletableFuture<String> sendMessageAsync(String message) {
        return CompletableFuture.supplyAsync(() -> sendMessage(message));
    }

    /**
     * Send a message with streaming response (synchronous)
     */
    public String sendMessageStreaming(String message, Consumer<String> onChunk) {
        // Validate input
        try {
            messageValidator.validateMessage(message);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid message: " + e.getMessage(), e);
        }

        if (service == null) {
            throw new IllegalStateException("API key not configured");
        }

        conversationHistory.add(new ChatMessage(ChatMessageRole.USER.value(), message));

        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(model)
                .messages(conversationHistory)
                .maxTokens(maxTokens)
                .temperature(temperature)
                .build();

        StringBuilder fullResponse = new StringBuilder();

        service.streamChatCompletion(request)
                .doOnNext(chunk -> {
                    String content = chunk.getChoices().get(0).getMessage().getContent();
                    if (content != null) {
                        fullResponse.append(content);
                        if (onChunk != null) {
                            onChunk.accept(content);
                        }
                    }
                })
                .doOnComplete(() -> {
                    conversationHistory.add(new ChatMessage(ChatMessageRole.ASSISTANT.value(), fullResponse.toString()));
                })
                .blockingSubscribe();

        return fullResponse.toString();
    }

    /**
     * Send a message with streaming response (asynchronous)
     */
    public void sendMessageStreamingAsync(String message, Consumer<String> onChunk, Runnable onComplete) {
        CompletableFuture.runAsync(() -> {
            try {
                sendMessageStreaming(message, onChunk);
                if (onComplete != null) {
                    onComplete.run();
                }
            } catch (Exception e) {
                if (onChunk != null) {
                    onChunk.accept("Error: " + e.getMessage());
                }
                if (onComplete != null) {
                    onComplete.run();
                }
            }
        });
    }

    /**
     * Send a message with additional context
     */
    public String sendMessageWithContext(String message, String context) {
        // Validate inputs
        try {
            messageValidator.validateMessageWithContext(message, context);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid message or context: " + e.getMessage(), e);
        }

        String fullMessage = "Context:\n" + context + "\n\nQuestion:\n" + message;
        return sendMessage(fullMessage);
    }

    /**
     * Send a message with context asynchronously
     */
    public CompletableFuture<String> sendMessageWithContextAsync(String message, String context) {
        return CompletableFuture.supplyAsync(() -> sendMessageWithContext(message, context));
    }

    /**
     * Send a message with context and streaming response
     */
    public String sendMessageWithContextStreaming(String message, String context, Consumer<String> onChunk) {
        // Validate inputs
        try {
            messageValidator.validateMessageWithContext(message, context);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid message or context: " + e.getMessage(), e);
        }

        String fullMessage = "Context:\n" + context + "\n\nQuestion:\n" + message;
        return sendMessageStreaming(fullMessage, onChunk);
    }

    /**
     * Check if API key is configured
     */
    public boolean isConfigured() {
        return apiKey != null && !apiKey.isEmpty() && service != null;
    }

    /**
     * Get conversation history size
     */
    public int getHistorySize() {
        return conversationHistory.size();
    }

    /**
     * Clear conversation history
     */
    public void clearHistory() {
        conversationHistory.clear();
    }

    /**
     * Get conversation history
     */
    public List<ChatMessage> getConversationHistory() {
        return new ArrayList<>(conversationHistory);
    }
}
