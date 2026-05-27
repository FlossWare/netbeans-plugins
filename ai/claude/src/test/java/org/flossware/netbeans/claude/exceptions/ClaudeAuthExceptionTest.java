package org.flossware.netbeans.claude.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test coverage for ClaudeAuthException.
 */
class ClaudeAuthExceptionTest {

    @Test
    void testConstructorWithMessage() {
        ClaudeAuthException exception = new ClaudeAuthException("Invalid API key");

        assertThat(exception.getMessage()).isEqualTo("Invalid API key");
        assertThat(exception.getCause()).isNull();
    }

    @Test
    void testConstructorWithMessageAndCause() {
        Throwable cause = new RuntimeException("401 Unauthorized");
        ClaudeAuthException exception = new ClaudeAuthException("Authentication failed", cause);

        assertThat(exception.getMessage()).isEqualTo("Authentication failed");
        assertThat(exception.getCause()).isSameAs(cause);
    }

    @Test
    void testConstructorWithCause() {
        Throwable cause = new RuntimeException("403 Forbidden");
        ClaudeAuthException exception = new ClaudeAuthException(cause);

        assertThat(exception.getCause()).isSameAs(cause);
    }

    @Test
    void testInheritance() {
        ClaudeAuthException exception = new ClaudeAuthException("Test");

        assertThat(exception).isInstanceOf(ClaudeException.class);
        assertThat(exception).isInstanceOf(Exception.class);
    }
}
