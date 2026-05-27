package org.flossware.netbeans.claude.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test coverage for ClaudeNetworkException.
 */
class ClaudeNetworkExceptionTest {

    @Test
    void testConstructorWithMessage() {
        ClaudeNetworkException exception = new ClaudeNetworkException("Connection timeout");

        assertThat(exception.getMessage()).isEqualTo("Connection timeout");
        assertThat(exception.getCause()).isNull();
    }

    @Test
    void testConstructorWithMessageAndCause() {
        Throwable cause = new java.net.SocketTimeoutException("Timeout");
        ClaudeNetworkException exception = new ClaudeNetworkException("Connection timeout", cause);

        assertThat(exception.getMessage()).isEqualTo("Connection timeout");
        assertThat(exception.getCause()).isSameAs(cause);
    }

    @Test
    void testConstructorWithCause() {
        Throwable cause = new java.net.UnknownHostException("DNS failure");
        ClaudeNetworkException exception = new ClaudeNetworkException(cause);

        assertThat(exception.getCause()).isSameAs(cause);
    }

    @Test
    void testInheritance() {
        ClaudeNetworkException exception = new ClaudeNetworkException("Test");

        assertThat(exception).isInstanceOf(ClaudeException.class);
        assertThat(exception).isInstanceOf(Exception.class);
    }
}
