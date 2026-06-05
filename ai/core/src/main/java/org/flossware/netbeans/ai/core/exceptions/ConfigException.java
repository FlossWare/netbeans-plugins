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
 * Exception thrown when AI configuration is invalid or missing.
 *
 * <p>This exception indicates that required configuration (such as API key)
 * is not set or is invalid.</p>
 */
public class ConfigException extends AIException {

    /**
     * Creates a new configuration exception with the specified message.
     *
     * @param message the detail message
     */
    public ConfigException(String message) {
        super(message);
    }

    /**
     * Creates a new configuration exception with the specified message and cause.
     *
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
