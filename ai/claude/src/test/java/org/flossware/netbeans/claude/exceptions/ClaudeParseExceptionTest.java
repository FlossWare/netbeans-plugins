package org.flossware.netbeans.claude.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test coverage for ClaudeParseException.
 */
class ClaudeParseExceptionTest {

    @Test
    void testConstructorWithMessage() {
        ClaudeParseException exception = new ClaudeParseException("Invalid JSON");

        assertThat(exception.getMessage()).isEqualTo("Invalid JSON");
        assertThat(exception.getCause()).isNull();
    }

    @Test
    void testConstructorWithMessageAndCause() {
        Throwable cause = new com.google.gson.JsonSyntaxException("Malformed JSON");
        ClaudeParseException exception = new ClaudeParseException("Parse failed", cause);

        assertThat(exception.getMessage()).isEqualTo("Parse failed");
        assertThat(exception.getCause()).isSameAs(cause);
    }

    @Test
    void testConstructorWithCause() {
        Throwable cause = new com.google.gson.JsonSyntaxException("Missing field");
        ClaudeParseException exception = new ClaudeParseException(cause);

        assertThat(exception.getCause()).isSameAs(cause);
    }

    @Test
    void testInheritance() {
        ClaudeParseException exception = new ClaudeParseException("Test");

        assertThat(exception).isInstanceOf(ClaudeException.class);
        assertThat(exception).isInstanceOf(Exception.class);
    }
}
