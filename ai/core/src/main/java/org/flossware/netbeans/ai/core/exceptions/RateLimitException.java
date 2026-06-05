/*
 * Copyright 2026 FlossWare.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.flossware.netbeans.ai.core.exceptions;

/**
 * Exception thrown when AI rate limit is exceeded.
 *
 * <p>This exception indicates that the API rate limit has been exceeded
 * (HTTP 429 response). It may include a retry-after value indicating
 * how long to wait before retrying.</p>
 */
public class RateLimitException extends AIException {

    private final Integer retryAfterSeconds;

    /**
     * Creates a new rate limit exception with the specified message.
     *
     * @param message the detail message
     */
    public RateLimitException(String message) {
        super(message);
        this.retryAfterSeconds = null;
    }

    /**
     * Creates a new rate limit exception with the specified message and retry-after value.
     *
     * @param message the detail message
     * @param retryAfterSeconds the number of seconds to wait before retrying, or null if not specified
     */
    public RateLimitException(String message, Integer retryAfterSeconds) {
        super(message);
        this.retryAfterSeconds = retryAfterSeconds;
    }

    /**
     * Creates a new rate limit exception with the specified message and cause.
     *
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public RateLimitException(String message, Throwable cause) {
        super(message, cause);
        this.retryAfterSeconds = null;
    }

    /**
     * Creates a new rate limit exception with message, cause, and retry-after value.
     *
     * @param message the detail message
     * @param cause the cause of this exception
     * @param retryAfterSeconds the number of seconds to wait before retrying, or null if not specified
     */
    public RateLimitException(String message, Throwable cause, Integer retryAfterSeconds) {
        super(message, cause);
        this.retryAfterSeconds = retryAfterSeconds;
    }

    /**
     * Gets the number of seconds to wait before retrying.
     *
     * @return the retry-after value in seconds, or null if not specified
     */
    public Integer getRetryAfterSeconds() {
        return retryAfterSeconds;
    }
}
