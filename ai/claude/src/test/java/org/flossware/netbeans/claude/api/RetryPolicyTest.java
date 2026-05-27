/*
 * Copyright 2026 FlossWare.
 */

package org.flossware.netbeans.claude.api;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import org.flossware.netbeans.claude.exceptions.ClaudeAuthException;
import org.flossware.netbeans.claude.exceptions.ClaudeConfigException;
import org.flossware.netbeans.claude.exceptions.ClaudeNetworkException;
import org.flossware.netbeans.claude.exceptions.ClaudeRateLimitException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Test coverage for RetryPolicy
 */
class RetryPolicyTest {

    private RetryPolicy retryPolicy;

    @BeforeEach
    void setUp() {
        retryPolicy = new RetryPolicy(3, 100, 5000);
    }

    @Test
    void testDefaultConstructor() {
        RetryPolicy defaultPolicy = new RetryPolicy();

        assertThat(defaultPolicy.getMaxRetries()).isEqualTo(3);
        assertThat(defaultPolicy.getInitialBackoffMs()).isEqualTo(1000);
        assertThat(defaultPolicy.getMaxBackoffMs()).isEqualTo(30000);
    }

    @Test
    void testCustomConstructor() {
        RetryPolicy customPolicy = new RetryPolicy(5, 500, 10000);

        assertThat(customPolicy.getMaxRetries()).isEqualTo(5);
        assertThat(customPolicy.getInitialBackoffMs()).isEqualTo(500);
        assertThat(customPolicy.getMaxBackoffMs()).isEqualTo(10000);
    }

    @Test
    void testConstructorValidation_MaxRetriesOutOfRange() {
        assertThatThrownBy(() -> new RetryPolicy(0, 1000, 5000))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("maxRetries must be between 1 and 10");

        assertThatThrownBy(() -> new RetryPolicy(11, 1000, 5000))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("maxRetries must be between 1 and 10");
    }

    @Test
    void testConstructorValidation_InitialBackoffOutOfRange() {
        assertThatThrownBy(() -> new RetryPolicy(3, 50, 5000))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("initialBackoffMs must be between 100 and 5000");

        assertThatThrownBy(() -> new RetryPolicy(3, 6000, 10000))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("initialBackoffMs must be between 100 and 5000");
    }

    @Test
    void testConstructorValidation_MaxBackoffOutOfRange() {
        assertThatThrownBy(() -> new RetryPolicy(3, 1000, 500))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("maxBackoffMs must be between 1000 and 60000");

        assertThatThrownBy(() -> new RetryPolicy(3, 1000, 70000))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("maxBackoffMs must be between 1000 and 60000");
    }

    @Test
    void testExecuteWithRetry_SuccessOnFirstAttempt() throws Exception {
        Callable<String> operation = () -> "success";

        String result = retryPolicy.executeWithRetry(operation);

        assertThat(result).isEqualTo("success");
    }

    @Test
    void testExecuteWithRetry_SuccessAfterRetries() throws Exception {
        AtomicInteger attempts = new AtomicInteger(0);

        Callable<String> operation = () -> {
            int currentAttempt = attempts.incrementAndGet();
            if (currentAttempt < 3) {
                throw new ClaudeNetworkException("Temporary failure");
            }
            return "success";
        };

        String result = retryPolicy.executeWithRetry(operation);

        assertThat(result).isEqualTo("success");
        assertThat(attempts.get()).isEqualTo(3);
    }

    @Test
    void testExecuteWithRetry_RateLimitWithRetryAfter() {
        AtomicInteger attempts = new AtomicInteger(0);

        Callable<String> operation = () -> {
            attempts.incrementAndGet();
            throw new ClaudeRateLimitException("Rate limited", 1); // 1 second retry-after
        };

        long startTime = System.currentTimeMillis();

        assertThatThrownBy(() -> retryPolicy.executeWithRetry(operation))
            .isInstanceOf(ClaudeRateLimitException.class);

        long duration = System.currentTimeMillis() - startTime;

        // Should have retried 3 times with ~1 second delay each
        assertThat(attempts.get()).isEqualTo(3);
        assertThat(duration).isGreaterThanOrEqualTo(2000); // At least 2 retries * 1000ms
    }

    @Test
    void testExecuteWithRetry_RateLimitWithoutRetryAfter() {
        AtomicInteger attempts = new AtomicInteger(0);

        Callable<String> operation = () -> {
            attempts.incrementAndGet();
            throw new ClaudeRateLimitException("Rate limited");
        };

        assertThatThrownBy(() -> retryPolicy.executeWithRetry(operation))
            .isInstanceOf(ClaudeRateLimitException.class);

        assertThat(attempts.get()).isEqualTo(3); // Max retries
    }

    @Test
    void testExecuteWithRetry_AuthExceptionNotRetried() {
        AtomicInteger attempts = new AtomicInteger(0);

        Callable<String> operation = () -> {
            attempts.incrementAndGet();
            throw new ClaudeAuthException("Invalid API key");
        };

        assertThatThrownBy(() -> retryPolicy.executeWithRetry(operation))
            .isInstanceOf(ClaudeAuthException.class);

        assertThat(attempts.get()).isEqualTo(1); // No retries for auth errors
    }

    @Test
    void testExecuteWithRetry_ConfigExceptionNotRetried() {
        AtomicInteger attempts = new AtomicInteger(0);

        Callable<String> operation = () -> {
            attempts.incrementAndGet();
            throw new ClaudeConfigException("API key not configured");
        };

        assertThatThrownBy(() -> retryPolicy.executeWithRetry(operation))
            .isInstanceOf(ClaudeConfigException.class);

        assertThat(attempts.get()).isEqualTo(1); // No retries for config errors
    }

    @Test
    void testExecuteWithRetry_NetworkExceptionRetried() {
        AtomicInteger attempts = new AtomicInteger(0);

        Callable<String> operation = () -> {
            attempts.incrementAndGet();
            throw new ClaudeNetworkException("Connection timeout");
        };

        assertThatThrownBy(() -> retryPolicy.executeWithRetry(operation))
            .isInstanceOf(ClaudeNetworkException.class);

        assertThat(attempts.get()).isEqualTo(3); // Retried max times
    }

    @Test
    void testExecuteWithRetry_NullOperation() {
        assertThatThrownBy(() -> retryPolicy.executeWithRetry(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("operation cannot be null");
    }

    @Test
    void testExecuteWithRetry_UnexpectedRuntimeException() {
        Callable<String> operation = () -> {
            throw new RuntimeException("Unexpected error");
        };

        assertThatThrownBy(() -> retryPolicy.executeWithRetry(operation))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Unexpected error");
    }

    @Test
    void testExecuteWithRetry_UnexpectedCheckedException() {
        Callable<String> operation = () -> {
            throw new Exception("Checked exception");
        };

        assertThatThrownBy(() -> retryPolicy.executeWithRetry(operation))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Unexpected error during retry")
            .hasCauseInstanceOf(Exception.class);
    }

    @Test
    void testBackoffIncreases() throws Exception {
        AtomicInteger attempts = new AtomicInteger(0);
        long[] backoffTimes = new long[3];

        Callable<String> operation = () -> {
            int attempt = attempts.getAndIncrement();
            if (attempt < 2) {
                long startTime = System.currentTimeMillis();
                throw new ClaudeNetworkException("Temporary failure");
            }
            return "success";
        };

        long totalStart = System.currentTimeMillis();
        retryPolicy.executeWithRetry(operation);
        long totalDuration = System.currentTimeMillis() - totalStart;

        // Should have exponential backoff (100ms base * 2^attempt + jitter)
        // Total should be at least 100ms (attempt 0) + 200ms (attempt 1) = 300ms
        assertThat(totalDuration).isGreaterThanOrEqualTo(300);
    }
}
