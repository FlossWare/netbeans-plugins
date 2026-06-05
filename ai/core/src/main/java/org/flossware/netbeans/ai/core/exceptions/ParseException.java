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
 * Exception thrown when AI response parsing fails.
 *
 * <p>This exception indicates that the AI provider returned a response
 * that could not be parsed or was in an unexpected format.</p>
 */
public class ParseException extends AIException {

    /**
     * Creates a new parse exception with the specified message.
     *
     * @param message the detail message
     */
    public ParseException(String message) {
        super(message);
    }

    /**
     * Creates a new parse exception with the specified message and cause.
     *
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
