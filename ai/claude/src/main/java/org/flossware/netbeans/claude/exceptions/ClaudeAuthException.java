package org.flossware.netbeans.claude.exceptions;

/**
 * Thrown when authentication fails with Claude API.
 * Examples: invalid API key, expired credentials, insufficient permissions.
 */
public class ClaudeAuthException extends ClaudeException {

    public ClaudeAuthException(String message) {
        super(message);
    }

    public ClaudeAuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClaudeAuthException(Throwable cause) {
        super(cause);
    }
}
