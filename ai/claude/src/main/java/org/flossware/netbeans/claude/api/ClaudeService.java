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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import org.flossware.netbeans.claude.exceptions.ClaudeException;
import org.openide.util.RequestProcessor;

/**
 * Service layer for Claude API interactions
 * Handles async operations and thread management
 *
 * <p>Thread-safe singleton with proper resource management through try-with-resources pattern.</p>
 */
public class ClaudeService implements AutoCloseable {

    private static ClaudeService instance;
    private static volatile boolean hasBeenShutdown = false;
    private final ClaudeClient client;
    private final RequestProcessor requestProcessor;
    private volatile boolean closed = false;

    private ClaudeService() {
        this.client = new ClaudeClient();
        this.requestProcessor = new RequestProcessor("Claude API", 3);
    }

    /**
     * Get the singleton instance of ClaudeService.
     *
     * @return the singleton instance
     * @throws IllegalStateException if the service has been shut down
     */
    public static synchronized ClaudeService getInstance() {
        if (hasBeenShutdown) {
            throw new IllegalStateException("Service has been shut down and cannot be restarted");
        }
        if (instance == null) {
            instance = new ClaudeService();
        }
        return instance;
    }

    /**
     * Shutdown the service and release all resources.
     *
     * <p>After shutdown, the service cannot be restarted. Any subsequent calls to
     * {@link #getInstance()} will throw {@link IllegalStateException}. This ensures
     * consistent singleton behavior and prevents accidental re-instantiation.</p>
     *
     * <p>Calling shutdown multiple times is safe and idempotent.</p>
     */
    public static synchronized void shutdown() {
        hasBeenShutdown = true;
        if (instance != null) {
            try {
                instance.close();
            } catch (Exception e) {
                // Log but don't rethrow
            }
            instance = null;
        }
    }

    /**
     * Send message asynchronously
     *
     * @param message The message to send
     * @return CompletableFuture with the response
     * @throws IllegalStateException if the service is closed
     */
    public CompletableFuture<String> sendMessageAsync(String message) {
        if (closed) {
            throw new IllegalStateException("Service has been closed");
        }
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
     *
     * @param message The message to send
     * @param codeContext The code context
     * @return CompletableFuture with the response
     * @throws IllegalStateException if the service is closed
     */
    public CompletableFuture<String> sendMessageWithContextAsync(String message, String codeContext) {
        if (closed) {
            throw new IllegalStateException("Service has been closed");
        }
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
        try {
            client.clearHistory();
        } catch (Exception e) {
            // Log but don't rethrow - gracefully handle clearing history
        }
    }

    /**
     * Check if API key is configured
     *
     * @return true if API key is configured, false otherwise
     */
    public boolean isConfigured() {
        return client.isConfigured();
    }

    /**
     * Get the Claude client
     *
     * @return The underlying ClaudeClient instance
     */
    public ClaudeClient getClient() {
        return client;
    }

    /**
     * Get conversation history size
     *
     * @return The number of messages in conversation history
     */
    public int getHistorySize() {
        return client.getHistorySize();
    }

    /**
     * Send message with streaming response
     *
     * @param message The message to send
     * @param onChunk Callback for each streamed chunk (called on background thread)
     * @return CompletableFuture with the complete response
     * @throws IllegalStateException if the service is closed
     */
    public CompletableFuture<String> sendMessageStreamingAsync(String message, Consumer<String> onChunk) {
        if (closed) {
            throw new IllegalStateException("Service has been closed");
        }
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
     *
     * @param message The message to send
     * @param codeContext The code context
     * @param onChunk Callback for each streamed chunk
     * @return CompletableFuture with the complete response
     * @throws IllegalStateException if the service is closed
     */
    public CompletableFuture<String> sendMessageWithContextStreamingAsync(String message, String codeContext, Consumer<String> onChunk) {
        if (closed) {
            throw new IllegalStateException("Service has been closed");
        }
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

    /**
     * Closes the service and releases resources
     *
     * <p>Implements AutoCloseable for try-with-resources pattern</p>
     */
    @Override
    public void close() throws Exception {
        closed = true;
        if (client != null) {
            client.close();
        }
        if (requestProcessor != null) {
            requestProcessor.stop();
        }
    }
}
