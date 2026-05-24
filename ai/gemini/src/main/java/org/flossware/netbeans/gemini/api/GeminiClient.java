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

package org.flossware.netbeans.gemini.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.prefs.Preferences;
import okhttp3.*;
import org.openide.util.NbPreferences;

/**
 * Client for interacting with Google Gemini API
 */
public class GeminiClient {

    private static final String PREF_API_KEY = "gemini.api.key";
    private static final String PREF_MODEL = "gemini.model";
    private static final String PREF_TEMPERATURE = "gemini.temperature";
    private static final String PREF_MAX_TOKENS = "gemini.max.tokens";
    private static final String DEFAULT_MODEL = "gemini-1.5-pro";
    private static final String API_BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/";

    private final OkHttpClient httpClient;
    private final Gson gson;
    private final List<ConversationMessage> conversationHistory;

    public GeminiClient() {
        this.httpClient = new OkHttpClient();
        this.gson = new Gson();
        this.conversationHistory = new ArrayList<>();
    }

    /**
     * Get the API key from NetBeans preferences
     */
    public String getApiKey() {
        Preferences prefs = NbPreferences.forModule(GeminiClient.class);
        return prefs.get(PREF_API_KEY, "");
    }

    /**
     * Set the API key in NetBeans preferences
     */
    public void setApiKey(String apiKey) {
        Preferences prefs = NbPreferences.forModule(GeminiClient.class);
        prefs.put(PREF_API_KEY, apiKey);
    }

    /**
     * Check if API key is configured
     */
    public boolean isConfigured() {
        String apiKey = getApiKey();
        return apiKey != null && !apiKey.isEmpty();
    }

    /**
     * Send a message to Gemini and get a response
     */
    public String sendMessage(String userMessage) throws IOException {
        if (!isConfigured()) {
            throw new IllegalStateException("API key not configured. Please configure your Google AI API key in Tools > Options > Gemini");
        }

        Preferences prefs = NbPreferences.forModule(GeminiClient.class);
        String model = prefs.get(PREF_MODEL, DEFAULT_MODEL);
        double temperature = prefs.getDouble(PREF_TEMPERATURE, 1.0);
        int maxTokens = prefs.getInt(PREF_MAX_TOKENS, 8192);

        // Build request
        JsonObject request = buildRequest(userMessage, temperature, maxTokens);

        // Send to API
        String url = API_BASE_URL + model + ":generateContent?key=" + getApiKey();
        String responseText = callApi(url, request);

        // Store in history
        conversationHistory.add(new ConversationMessage("user", userMessage));
        conversationHistory.add(new ConversationMessage("model", responseText));

        return responseText;
    }

    /**
     * Send a message with code context
     */
    public String sendMessageWithContext(String userMessage, String codeContext) throws IOException {
        String fullMessage = String.format(
            "Here is the code context:\n\n```\n%s\n```\n\nUser question: %s",
            codeContext,
            userMessage
        );
        return sendMessage(fullMessage);
    }

    /**
     * Send a message with streaming response
     */
    public String sendMessageStreaming(String userMessage, Consumer<String> onChunk) throws IOException {
        if (!isConfigured()) {
            throw new IllegalStateException("API key not configured");
        }

        Preferences prefs = NbPreferences.forModule(GeminiClient.class);
        String model = prefs.get(PREF_MODEL, DEFAULT_MODEL);
        double temperature = prefs.getDouble(PREF_TEMPERATURE, 1.0);
        int maxTokens = prefs.getInt(PREF_MAX_TOKENS, 8192);

        JsonObject request = buildRequest(userMessage, temperature, maxTokens);

        String url = API_BASE_URL + model + ":streamGenerateContent?key=" + getApiKey();
        String response = callApiStreaming(url, request, onChunk);

        conversationHistory.add(new ConversationMessage("user", userMessage));
        conversationHistory.add(new ConversationMessage("model", response));

        return response;
    }

    /**
     * Clear conversation history
     */
    public void clearHistory() {
        conversationHistory.clear();
    }

    /**
     * Get conversation history size
     */
    public int getHistorySize() {
        return conversationHistory.size();
    }

    /**
     * Build API request JSON
     */
    private JsonObject buildRequest(String message, double temperature, int maxTokens) {
        JsonObject request = new JsonObject();

        // Add contents
        JsonArray contents = new JsonArray();
        JsonObject content = new JsonObject();
        JsonArray parts = new JsonArray();
        JsonObject part = new JsonObject();
        part.addProperty("text", message);
        parts.add(part);
        content.add("parts", parts);
        contents.add(content);
        request.add("contents", contents);

        // Add generation config
        JsonObject generationConfig = new JsonObject();
        generationConfig.addProperty("temperature", temperature);
        generationConfig.addProperty("maxOutputTokens", maxTokens);
        request.add("generationConfig", generationConfig);

        return request;
    }

    /**
     * Call the API
     */
    private String callApi(String url, JsonObject requestBody) throws IOException {
        RequestBody body = RequestBody.create(
            requestBody.toString(),
            MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("API call failed: " + response.code() + " " + response.message());
            }

            String responseBody = response.body().string();
            return extractTextFromResponse(responseBody);
        }
    }

    /**
     * Call the API with streaming
     */
    private String callApiStreaming(String url, JsonObject requestBody, Consumer<String> onChunk) throws IOException {
        // For simplicity, using non-streaming API
        // Real streaming would use Server-Sent Events
        String response = callApi(url.replace(":streamGenerateContent", ":generateContent"), requestBody);

        // Simulate streaming by sending in chunks
        if (onChunk != null) {
            int chunkSize = 10;
            for (int i = 0; i < response.length(); i += chunkSize) {
                int end = Math.min(i + chunkSize, response.length());
                onChunk.accept(response.substring(i, end));
                try {
                    Thread.sleep(10); // Small delay to simulate streaming
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        return response;
    }

    /**
     * Extract text from API response
     */
    private String extractTextFromResponse(String responseBody) {
        JsonObject response = gson.fromJson(responseBody, JsonObject.class);

        if (response.has("candidates")) {
            JsonArray candidates = response.getAsJsonArray("candidates");
            if (candidates.size() > 0) {
                JsonObject candidate = candidates.get(0).getAsJsonObject();
                if (candidate.has("content")) {
                    JsonObject content = candidate.getAsJsonObject("content");
                    if (content.has("parts")) {
                        JsonArray parts = content.getAsJsonArray("parts");
                        if (parts.size() > 0) {
                            JsonObject part = parts.get(0).getAsJsonObject();
                            if (part.has("text")) {
                                return part.get("text").getAsString();
                            }
                        }
                    }
                }
            }
        }

        return "No response generated";
    }

    /**
     * Conversation message
     */
    private static class ConversationMessage {
        private final String role;
        private final String content;

        public ConversationMessage(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() { return role; }
        public String getContent() { return content; }
    }
}
