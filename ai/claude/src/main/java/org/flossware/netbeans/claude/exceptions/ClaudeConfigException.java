package org.flossware.netbeans.claude.exceptions;

/**
 * Thrown when configuration-related errors occur.
 * Examples: missing API key, invalid settings, configuration file not found.
 */
public class ClaudeConfigException extends ClaudeException {

    public ClaudeConfigException(String message) {
        super(message);
    }

    public ClaudeConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClaudeConfigException(Throwable cause) {
        super(cause);
    }
}
