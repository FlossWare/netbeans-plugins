package org.flossware.netbeans.claude.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test coverage for ClaudeRateLimitException.
 */
class ClaudeRateLimitExceptionTest {

    @Test
    void testConstructorWithMessage() {
        ClaudeRateLimitException exception = new ClaudeRateLimitException("Rate limit exceeded");

        assertThat(exception.getMessage()).isEqualTo("Rate limit exceeded");
        assertThat(exception.getCause()).isNull();
        assertThat(exception.getRetryAfterSeconds()).isNull();
    }

    @Test
    void testConstructorWithMessageAndRetryAfter() {
        ClaudeRateLimitException exception = new ClaudeRateLimitException("Rate limit exceeded", 60);

        assertThat(exception.getMessage()).isEqualTo("Rate limit exceeded");
        assertThat(exception.getRetryAfterSeconds()).isEqualTo(60);
    }

    @Test
    void testConstructorWithMessageAndCause() {
        Throwable cause = new RuntimeException("429 Too Many Requests");
        ClaudeRateLimitException exception = new ClaudeRateLimitException("Rate limit exceeded", cause);

        assertThat(exception.getMessage()).isEqualTo("Rate limit exceeded");
        assertThat(exception.getCause()).isSameAs(cause);
        assertThat(exception.getRetryAfterSeconds()).isNull();
    }

    @Test
    void testRetryAfterRetrievalWithSeconds() {
        ClaudeRateLimitException exception = new ClaudeRateLimitException("Rate limit exceeded", 120);

        assertThat(exception.getRetryAfterSeconds()).isEqualTo(120);
    }

    @Test
    void testRetryAfterRetrievalWithoutSeconds() {
        ClaudeRateLimitException exception = new ClaudeRateLimitException("Rate limit exceeded");

        assertThat(exception.getRetryAfterSeconds()).isNull();
    }

    @Test
    void testInheritance() {
        ClaudeRateLimitException exception = new ClaudeRateLimitException("Test");

        assertThat(exception).isInstanceOf(ClaudeException.class);
        assertThat(exception).isInstanceOf(Exception.class);
    }
}
