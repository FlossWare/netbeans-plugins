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

package org.flossware.netbeans.mistral.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.prefs.Preferences;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.openide.util.NbPreferences;

/**
 * Client for interacting with Mistral AI API
 */
public class MistralClient {

    private static final String PREF_API_KEY = "mistral.api.key";
    private static final String PREF_MODEL = "mistral.model";
    private static final String PREF_MAX_TOKENS = "mistral.max.tokens";
    private static final String PREF_TEMPERATURE = "mistral.temperature";
    private static final String MISTRAL_API_BASE_URL = "https://api.mistral.ai/v1/chat/completions";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private OkHttpClient httpClient;
    private final Gson gson;
    private String apiKey;
    private String model;
    private int maxTokens;
    private double temperature;
    private List<JsonObject> conversationHistory;

    public MistralClient() {
        this.gson = new Gson();
        loadSettings();
        conversationHistory = new ArrayList<>();
    }

    private void loadSettings() {
        Preferences prefs = NbPreferences.forModule(MistralClient.class);
        this.apiKey = prefs.get(PREF_API_KEY, "");
        this.model = prefs.get(PREF_MODEL, "mistral-large-latest");
        this.maxTokens = prefs.getInt(PREF_MAX_TOKENS, 4096);
        this.temperature = prefs.getDouble(PREF_TEMPERATURE, 0.7);

        if (!apiKey.isEmpty()) {
            this.httpClient = new OkHttpClient.Builder()
                    .readTimeout(Duration.ofSeconds(60))
                    .build();
        }
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
        if (!apiKey.isEmpty()) {
            this.httpClient = new OkHttpClient.Builder()
                    .readTimeout(Duration.ofSeconds(60))
                    .build();
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
    public String sendMessage(String message) throws IOException {
        if (httpClient == null || apiKey.isEmpty()) {
            throw new IllegalStateException("API key not configured");
        }

        // Add user message to history
        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        userMessage.addProperty("content", message);
        conversationHistory.add(userMessage);

        // Build request
        JsonObject requestJson = new JsonObject();
        requestJson.addProperty("model", model);
        requestJson.addProperty("max_tokens", maxTokens);
        requestJson.addProperty("temperature", temperature);

        JsonArray messagesArray = new JsonArray();
        for (JsonObject msg : conversationHistory) {
            messagesArray.add(msg);
        }
        requestJson.add("messages", messagesArray);

        RequestBody body = RequestBody.create(gson.toJson(requestJson), JSON);
        Request request = new Request.Builder()
                .url(MISTRAL_API_BASE_URL)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response.code() + " - " + response.message());
            }

            String responseBody = response.body().string();
            JsonObject responseJson = gson.fromJson(responseBody, JsonObject.class);

            String assistantMessage = responseJson
                    .getAsJsonArray("choices")
                    .get(0)
                    .getAsJsonObject()
                    .getAsJsonObject("message")
                    .get("content")
                    .getAsString();

            // Add assistant response to history
            JsonObject assistantMsg = new JsonObject();
            assistantMsg.addProperty("role", "assistant");
            assistantMsg.addProperty("content", assistantMessage);
            conversationHistory.add(assistantMsg);

            return assistantMessage;
        }
    }

    /**
     * Send a message asynchronously
     */
    public CompletableFuture<String> sendMessageAsync(String message) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return sendMessage(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Send a message with streaming response (synchronous)
     * Note: Mistral API supports streaming, but this is a simplified implementation
     */
    public String sendMessageStreaming(String message, Consumer<String> onChunk) throws IOException {
        // For now, we'll simulate streaming by sending the full message
        // Real streaming would require SSE (Server-Sent Events) support
        String response = sendMessage(message);
        if (onChunk != null) {
            onChunk.accept(response);
        }
        return response;
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
    public String sendMessageWithContext(String message, String context) throws IOException {
        String fullMessage = "Context:\n" + context + "\n\nQuestion:\n" + message;
        return sendMessage(fullMessage);
    }

    /**
     * Send a message with context asynchronously
     */
    public CompletableFuture<String> sendMessageWithContextAsync(String message, String context) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return sendMessageWithContext(message, context);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Send a message with context and streaming response
     */
    public String sendMessageWithContextStreaming(String message, String context, Consumer<String> onChunk) throws IOException {
        String fullMessage = "Context:\n" + context + "\n\nQuestion:\n" + message;
        return sendMessageStreaming(fullMessage, onChunk);
    }

    /**
     * Check if API key is configured
     */
    public boolean isConfigured() {
        return apiKey != null && !apiKey.isEmpty() && httpClient != null;
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
    public List<JsonObject> getConversationHistory() {
        return new ArrayList<>(conversationHistory);
    }
}
