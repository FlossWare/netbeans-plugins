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

package org.flossware.netbeans.ai.core.logging;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Factory for obtaining Logger instances with consistent naming conventions.
 *
 * <p>This factory ensures that all loggers in the FlossWare NetBeans plugins
 * use a consistent naming pattern and are properly configured through
 * logging.properties.</p>
 *
 * <p>Usage Example:</p>
 * <pre>
 * private static final Logger LOG = LoggerFactory.getLogger(MyClass.class);
 * LOG.info("Initialization started");
 * LOG.warning("Configuration issue detected");
 * LOG.severe("Critical error: " + e.getMessage());
 * </pre>
 *
 * @since 1.0
 */
public final class LoggerFactory {

    private static final Map<String, Logger> LOGGER_CACHE = new HashMap<>();

    /**
     * Private constructor to prevent instantiation.
     */
    private LoggerFactory() {
        throw new AssertionError("LoggerFactory cannot be instantiated");
    }

    /**
     * Gets or creates a logger for the specified class.
     *
     * <p>Logger names are based on the fully qualified class name, which
     * allows for fine-grained configuration via logging.properties.</p>
     *
     * @param clazz The class requesting the logger
     * @return A Logger instance configured for the given class
     * @throws NullPointerException if clazz is null
     */
    public static Logger getLogger(Class<?> clazz) {
        if (clazz == null) {
            throw new NullPointerException("Class cannot be null");
        }

        String loggerName = clazz.getName();
        return LOGGER_CACHE.computeIfAbsent(loggerName, Logger::getLogger);
    }

    /**
     * Gets or creates a logger with the specified name.
     *
     * <p>Direct logger name specification should be used sparingly. Prefer
     * {@link #getLogger(Class)} for class-based logging.</p>
     *
     * @param loggerName The name of the logger
     * @return A Logger instance with the specified name
     * @throws NullPointerException if loggerName is null
     * @throws IllegalArgumentException if loggerName is empty
     */
    public static Logger getLogger(String loggerName) {
        if (loggerName == null) {
            throw new NullPointerException("Logger name cannot be null");
        }
        if (loggerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Logger name cannot be empty");
        }

        return LOGGER_CACHE.computeIfAbsent(loggerName, Logger::getLogger);
    }

    /**
     * Clears the logger cache.
     *
     * <p>This is primarily useful for testing. In production, loggers
     * should be reused to maintain configuration consistency.</p>
     */
    protected static void clearCache() {
        LOGGER_CACHE.clear();
    }
}
