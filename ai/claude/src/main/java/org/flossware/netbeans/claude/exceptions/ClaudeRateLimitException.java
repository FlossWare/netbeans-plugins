package org.flossware.netbeans.claude.exceptions;

/**
 * Thrown when Claude API rate limits are exceeded.
 * Includes retry-after information when available.
 */
public class ClaudeRateLimitException extends ClaudeException {

    private final Integer retryAfterSeconds;

    public ClaudeRateLimitException(String message) {
        super(message);
        this.retryAfterSeconds = null;
    }

    public ClaudeRateLimitException(String message, int retryAfterSeconds) {
        super(message);
        this.retryAfterSeconds = retryAfterSeconds;
    }

    public ClaudeRateLimitException(String message, Throwable cause) {
        super(message, cause);
        this.retryAfterSeconds = null;
    }

    public Integer getRetryAfterSeconds() {
        return retryAfterSeconds;
    }
}
