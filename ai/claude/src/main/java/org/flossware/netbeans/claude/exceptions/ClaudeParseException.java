package org.flossware.netbeans.claude.exceptions;

/**
 * Thrown when parsing Claude API responses fails.
 * Examples: malformed JSON, unexpected response structure, missing required fields.
 */
public class ClaudeParseException extends ClaudeException {

    public ClaudeParseException(String message) {
        super(message);
    }

    public ClaudeParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClaudeParseException(Throwable cause) {
        super(cause);
    }
}
