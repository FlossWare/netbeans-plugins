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

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.flossware.netbeans.claude.exceptions.ClaudeAuthException;
import org.flossware.netbeans.claude.exceptions.ClaudeConfigException;
import org.flossware.netbeans.claude.exceptions.ClaudeException;
import org.flossware.netbeans.claude.exceptions.ClaudeRateLimitException;

/**
 * Retry policy with exponential backoff and jitter for handling transient failures.
 * Implements best practices for API retry logic including:
 * - Exponential backoff to prevent overwhelming the server
 * - Jitter to prevent thundering herd problem
 * - Configurable retry attempts and backoff
 * - Special handling for rate limiting (429) responses
 */
public class RetryPolicy {

    private static final Logger LOGGER = Logger.getLogger(RetryPolicy.class.getName());

    private final int maxRetries;
    private final long initialBackoffMs;
    private final long maxBackoffMs;
    private final Random random;

    /**
     * Default retry policy: 3 retries, 1 second initial backoff, 30 second max backoff
     */
    public RetryPolicy() {
        this(3, 1000, 30000);
    }

    /**
     * Custom retry policy
     * @param maxRetries Maximum number of retry attempts (1-10)
     * @param initialBackoffMs Initial backoff in milliseconds (100-5000)
     * @param maxBackoffMs Maximum backoff in milliseconds (1000-60000)
     */
    public RetryPolicy(int maxRetries, long initialBackoffMs, long maxBackoffMs) {
        if (maxRetries < 1 || maxRetries > 10) {
            throw new IllegalArgumentException("maxRetries must be between 1 and 10");
        }
        if (initialBackoffMs < 100 || initialBackoffMs > 5000) {
            throw new IllegalArgumentException("initialBackoffMs must be between 100 and 5000");
        }
        if (maxBackoffMs < 1000 || maxBackoffMs > 60000) {
            throw new IllegalArgumentException("maxBackoffMs must be between 1000 and 60000");
        }

        this.maxRetries = maxRetries;
        this.initialBackoffMs = initialBackoffMs;
        this.maxBackoffMs = maxBackoffMs;
        this.random = new Random();
    }

    /**
     * Execute a callable with retry logic
     * @param <T> Return type
     * @param operation The operation to execute
     * @return The result of the operation
     * @throws ClaudeException If all retries are exhausted
     */
    public <T> T executeWithRetry(Callable<T> operation) throws ClaudeException {
        Objects.requireNonNull(operation, "operation cannot be null");

        ClaudeException lastException = null;

        for (int attempt = 0; attempt < maxRetries; attempt++) {
            try {
                LOGGER.log(Level.FINE, "Executing operation, attempt {0}/{1}",
                    new Object[]{attempt + 1, maxRetries});

                return operation.call();

            } catch (ClaudeRateLimitException e) {
                lastException = e;

                // Check if we should retry
                if (attempt == maxRetries - 1) {
                    LOGGER.log(Level.SEVERE, "Max retries exhausted for rate limit", e);
                    throw e;
                }

                // Use retry-after if provided, otherwise use exponential backoff
                long backoffMs;
                Integer retryAfter = e.getRetryAfterSeconds();
                if (retryAfter != null && retryAfter > 0) {
                    backoffMs = retryAfter * 1000L;
                    LOGGER.log(Level.WARNING, "Rate limited, retry after {0} seconds", retryAfter);
                } else {
                    backoffMs = calculateBackoff(attempt);
                    LOGGER.log(Level.WARNING, "Rate limited, backing off {0}ms", backoffMs);
                }

                sleep(backoffMs);

            } catch (ClaudeException e) {
                lastException = e;

                // Don't retry authentication or configuration errors
                if (e instanceof ClaudeAuthException ||
                    e instanceof ClaudeConfigException) {
                    LOGGER.log(Level.SEVERE, "Non-retryable error: {0}", e.getMessage());
                    throw e;
                }

                // Retry network and parse errors
                if (attempt == maxRetries - 1) {
                    LOGGER.log(Level.SEVERE, "Max retries exhausted", e);
                    throw e;
                }

                long backoffMs = calculateBackoff(attempt);
                LOGGER.log(Level.WARNING, "Transient error, retrying after {0}ms: {1}",
                    new Object[]{backoffMs, e.getMessage()});

                sleep(backoffMs);

            } catch (Exception e) {
                // Unexpected exception, don't retry
                LOGGER.log(Level.SEVERE, "Unexpected error during retry", e);
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                }
                throw new RuntimeException("Unexpected error during retry", e);
            }
        }

        // This should never happen, but just in case
        if (lastException != null) {
            throw lastException;
        }
        throw new IllegalStateException("Retry loop completed without result or exception");
    }

    /**
     * Calculate exponential backoff with jitter
     * Formula: min(maxBackoff, initialBackoff * 2^attempt + random(0, 1000))
     */
    private long calculateBackoff(int attempt) {
        // Exponential backoff: 2^attempt
        long exponentialBackoff = initialBackoffMs * (1L << attempt);

        // Cap at max backoff
        long backoff = Math.min(exponentialBackoff, maxBackoffMs);

        // Add jitter (0-1000ms) to prevent thundering herd
        long jitter = random.nextInt(1000);

        return backoff + jitter;
    }

    /**
     * Sleep for the specified duration, handling interruption
     */
    private void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.log(Level.WARNING, "Retry sleep interrupted", e);
        }
    }

    /**
     * Get maximum retry attempts
     */
    public int getMaxRetries() {
        return maxRetries;
    }

    /**
     * Get initial backoff in milliseconds
     */
    public long getInitialBackoffMs() {
        return initialBackoffMs;
    }

    /**
     * Get maximum backoff in milliseconds
     */
    public long getMaxBackoffMs() {
        return maxBackoffMs;
    }
}
