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
 * Base exception for all AI-related errors.
 *
 * <p>This is the root of the AI exception hierarchy. All AI plugins should
 * throw subclasses of this exception to enable consistent error handling
 * across different AI providers.</p>
 */
public class AIException extends Exception {

    /**
     * Creates a new AI exception with the specified message.
     *
     * @param message the detail message
     */
    public AIException(String message) {
        super(message);
    }

    /**
     * Creates a new AI exception with the specified message and cause.
     *
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public AIException(String message, Throwable cause) {
        super(message, cause);
    }
}
