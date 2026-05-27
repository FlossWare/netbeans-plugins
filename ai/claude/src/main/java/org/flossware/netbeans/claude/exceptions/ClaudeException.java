package org.flossware.netbeans.claude.exceptions;

/**
 * Base exception for all Claude-related errors.
 * Provides a common hierarchy for structured error handling.
 */
public class ClaudeException extends Exception {

    public ClaudeException(String message) {
        super(message);
    }

    public ClaudeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClaudeException(Throwable cause) {
        super(cause);
    }
}
