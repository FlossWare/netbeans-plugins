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

package org.flossware.netbeans.gemini.api;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import org.openide.util.RequestProcessor;

/**
 * Service layer for Gemini API interactions
 * Handles async operations and thread management
 */
public class GeminiService {

    private static GeminiService instance;
    private final GeminiClient client;
    private final RequestProcessor requestProcessor;

    private GeminiService() {
        this.client = new GeminiClient();
        this.requestProcessor = new RequestProcessor("Gemini API", 3);
    }

    public static synchronized GeminiService getInstance() {
        if (instance == null) {
            instance = new GeminiService();
        }
        return instance;
    }

    /**
     * Send message asynchronously
     */
    public CompletableFuture<String> sendMessageAsync(String message) {
        CompletableFuture<String> future = new CompletableFuture<>();

        requestProcessor.post(() -> {
            try {
                String response = client.sendMessage(message);
                future.complete(response);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });

        return future;
    }

    /**
     * Send message with code context asynchronously
     */
    public CompletableFuture<String> sendMessageWithContextAsync(String message, String codeContext) {
        CompletableFuture<String> future = new CompletableFuture<>();

        requestProcessor.post(() -> {
            try {
                String response = client.sendMessageWithContext(message, codeContext);
                future.complete(response);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });

        return future;
    }

    /**
     * Clear conversation history
     */
    public void clearHistory() {
        client.clearHistory();
    }

    /**
     * Check if API key is configured
     */
    public boolean isConfigured() {
        return client.isConfigured();
    }

    /**
     * Get the Gemini client
     */
    public GeminiClient getClient() {
        return client;
    }

    /**
     * Get conversation history size
     */
    public int getHistorySize() {
        return client.getHistorySize();
    }

    /**
     * Send message with streaming response
     * @param message The message to send
     * @param onChunk Callback for each streamed chunk (called on background thread)
     * @return CompletableFuture with the complete response
     */
    public CompletableFuture<String> sendMessageStreamingAsync(String message, Consumer<String> onChunk) {
        CompletableFuture<String> future = new CompletableFuture<>();

        requestProcessor.post(() -> {
            try {
                String response = client.sendMessageStreaming(message, onChunk);
                future.complete(response);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });

        return future;
    }

    /**
     * Send message with code context and streaming
     */
    public CompletableFuture<String> sendMessageWithContextStreamingAsync(String message, String codeContext, Consumer<String> onChunk) {
        CompletableFuture<String> future = new CompletableFuture<>();

        requestProcessor.post(() -> {
            try {
                String response = client.sendMessageWithContextStreaming(message, codeContext, onChunk);
                future.complete(response);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });

        return future;
    }
}
