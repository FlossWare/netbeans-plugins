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
 * Exception thrown when AI authentication fails.
 *
 * <p>This exception indicates that the API key is invalid, expired, or
 * the authentication request failed (HTTP 401/403 responses).</p>
 */
public class AuthException extends AIException {

    /**
     * Creates a new authentication exception with the specified message.
     *
     * @param message the detail message
     */
    public AuthException(String message) {
        super(message);
    }

    /**
     * Creates a new authentication exception with the specified message and cause.
     *
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }
}
