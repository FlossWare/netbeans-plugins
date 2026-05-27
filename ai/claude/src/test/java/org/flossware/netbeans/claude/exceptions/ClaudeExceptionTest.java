package org.flossware.netbeans.claude.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test coverage for ClaudeException.
 */
class ClaudeExceptionTest {

    @Test
    void testConstructorWithMessage() {
        ClaudeException exception = new ClaudeException("Test error");

        assertThat(exception.getMessage()).isEqualTo("Test error");
        assertThat(exception.getCause()).isNull();
    }

    @Test
    void testConstructorWithMessageAndCause() {
        Throwable cause = new RuntimeException("Root cause");
        ClaudeException exception = new ClaudeException("Test error", cause);

        assertThat(exception.getMessage()).isEqualTo("Test error");
        assertThat(exception.getCause()).isSameAs(cause);
    }

    @Test
    void testConstructorWithCause() {
        Throwable cause = new RuntimeException("Root cause");
        ClaudeException exception = new ClaudeException(cause);

        assertThat(exception.getCause()).isSameAs(cause);
        assertThat(exception.getMessage()).contains("RuntimeException");
    }

    @Test
    void testInheritance() {
        ClaudeException exception = new ClaudeException("Test");

        assertThat(exception).isInstanceOf(Exception.class);
    }
}
