package org.flossware.netbeans.claude.exceptions;

/**
 * Thrown when network-related errors occur while communicating with Claude API.
 * Examples: connection timeout, DNS failure, network unavailable.
 */
public class ClaudeNetworkException extends ClaudeException {

    public ClaudeNetworkException(String message) {
        super(message);
    }

    public ClaudeNetworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClaudeNetworkException(Throwable cause) {
        super(cause);
    }
}
