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

package org.flossware.netbeans.ai.core.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import org.flossware.netbeans.ai.core.exceptions.AIException;
import org.flossware.netbeans.ai.core.exceptions.AuthException;
import org.flossware.netbeans.ai.core.exceptions.ConfigException;

/**
 * Abstract base class for AI client implementations.
 *
 * <p>Provides common functionality for all AI provider clients, including
 * configuration management, request building, and error handling.</p>
 */
public abstract class BaseAIClient {

    private static final Logger LOGGER = Logger.getLogger(BaseAIClient.class.getName());

    protected final String apiKey;
    protected final Map<String, String> config;

    /**
     * Constructs a BaseAIClient with the specified API key.
     *
     * @param apiKey The API key for authentication
     * @throws ConfigException if the API key is null or empty
     */
    protected BaseAIClient(String apiKey) throws ConfigException {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new ConfigException("API key cannot be null or empty");
        }
        this.apiKey = apiKey.trim();
        this.config = new HashMap<>();
    }

    /**
     * Validates that the API key is set and non-empty.
     *
     * @throws AuthException if the API key is not configured
     */
    protected void validateApiKey() throws AuthException {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new AuthException("API key is not configured");
        }
    }

    /**
     * Gets the configured API key.
     *
     * @return The API key
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Sets a configuration value.
     *
     * @param key The configuration key
     * @param value The configuration value
     */
    public void setConfig(String key, String value) {
        Objects.requireNonNull(key, "Configuration key cannot be null");
        config.put(key, value);
    }

    /**
     * Gets a configuration value.
     *
     * @param key The configuration key
     * @return The configuration value, or null if not set
     */
    public String getConfig(String key) {
        return config.get(key);
    }

    /**
     * Gets the logger for this client.
     *
     * @return The logger instance
     */
    protected Logger getLogger() {
        return LOGGER;
    }
}
