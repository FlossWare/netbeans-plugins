package org.flossware.netbeans.chatgpt.api;

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
import org.openide.util.NbPreferences;

/**
 * Client for interacting with OpenAI ChatGPT API
 */
public class ChatGPTClient {

    private static final String PREF_API_KEY = "openai.api.key";
    private static final String PREF_MODEL = "openai.model";
    private static final String PREF_MAX_TOKENS = "openai.max.tokens";
    private static final String PREF_TEMPERATURE = "openai.temperature";

    private OpenAiService service;
    private String apiKey;
    private String model;
    private int maxTokens;
    private double temperature;
    private List<ChatMessage> conversationHistory;

    public ChatGPTClient() {
        loadSettings();
        conversationHistory = new ArrayList<>();
    }

    private void loadSettings() {
        Preferences prefs = NbPreferences.forModule(ChatGPTClient.class);
        this.apiKey = prefs.get(PREF_API_KEY, "");
        this.model = prefs.get(PREF_MODEL, "gpt-4-turbo-preview");
        this.maxTokens = prefs.getInt(PREF_MAX_TOKENS, 4096);
        this.temperature = prefs.getDouble(PREF_TEMPERATURE, 0.7);

        if (!apiKey.isEmpty()) {
            this.service = new OpenAiService(apiKey, Duration.ofSeconds(60));
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
     * Send a message with streaming response
     */
    public void sendMessageStreaming(String message, Consumer<String> onChunk, Runnable onComplete) {
        CompletableFuture.runAsync(() -> {
            try {
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
                                onChunk.accept(content);
                            }
                        })
                        .doOnComplete(() -> {
                            conversationHistory.add(new ChatMessage(ChatMessageRole.ASSISTANT.value(), fullResponse.toString()));
                            onComplete.run();
                        })
                        .blockingSubscribe();

            } catch (Exception e) {
                onChunk.accept("Error: " + e.getMessage());
                onComplete.run();
            }
        });
    }

    /**
     * Send a message with additional context
     */
    public String sendMessageWithContext(String message, String context) {
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
