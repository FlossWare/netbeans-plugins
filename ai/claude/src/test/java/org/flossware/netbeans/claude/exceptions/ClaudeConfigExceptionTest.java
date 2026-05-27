package org.flossware.netbeans.claude.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test coverage for ClaudeConfigException.
 */
class ClaudeConfigExceptionTest {

    @Test
    void testConstructorWithMessage() {
        ClaudeConfigException exception = new ClaudeConfigException("Missing API key");

        assertThat(exception.getMessage()).isEqualTo("Missing API key");
        assertThat(exception.getCause()).isNull();
    }

    @Test
    void testConstructorWithMessageAndCause() {
        Throwable cause = new java.io.FileNotFoundException("config.properties");
        ClaudeConfigException exception = new ClaudeConfigException("Configuration not found", cause);

        assertThat(exception.getMessage()).isEqualTo("Configuration not found");
        assertThat(exception.getCause()).isSameAs(cause);
    }

    @Test
    void testConstructorWithCause() {
        Throwable cause = new IllegalArgumentException("Invalid setting");
        ClaudeConfigException exception = new ClaudeConfigException(cause);

        assertThat(exception.getCause()).isSameAs(cause);
    }

    @Test
    void testInheritance() {
        ClaudeConfigException exception = new ClaudeConfigException("Test");

        assertThat(exception).isInstanceOf(ClaudeException.class);
        assertThat(exception).isInstanceOf(Exception.class);
    }
}
