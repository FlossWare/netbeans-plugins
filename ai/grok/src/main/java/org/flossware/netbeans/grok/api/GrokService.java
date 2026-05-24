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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import org.openide.util.RequestProcessor;

/**
 * Service layer for Grok API interactions
 * Handles async operations and thread management
 */
public class GrokService {

    private static GrokService instance;
    private final GrokClient client;
    private final RequestProcessor requestProcessor;

    private GrokService() {
        this.client = new GrokClient();
        this.requestProcessor = new RequestProcessor("Grok API", 3);
    }

    public static synchronized GrokService getInstance() {
        if (instance == null) {
            instance = new GrokService();
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
     * Get the Grok client
     */
    public GrokClient getClient() {
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
