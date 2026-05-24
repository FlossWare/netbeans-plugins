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

package org.flossware.netbeans.common.settings;

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

/**
 * Abstract base class for language plugin settings.
 *
 * <p>This class provides common functionality for storing and retrieving
 * user preferences using NetBeans preferences API. It supports:</p>
 * <ul>
 *   <li>String settings with defaults</li>
 *   <li>Boolean settings with defaults</li>
 *   <li>Integer settings with defaults</li>
 *   <li>Language-specific preference modules</li>
 *   <li>Default value management</li>
 * </ul>
 *
 * <p>Subclasses must implement abstract methods to provide language-specific
 * configuration:</p>
 * <ul>
 *   <li>{@link #getPreferenceModule()} - Return module name for NbPreferences</li>
 *   <li>{@link #getDefaultValues()} - Return default setting values</li>
 * </ul>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * public class ErlangSettings extends AbstractSettings {
 *     private static final String PREF_ERLC_PATH = "erlc.path";
 *     private static final String PREF_LSP_SERVER = "lsp.server";
 *
 *     private static final ErlangSettings INSTANCE = new ErlangSettings();
 *
 *     public static ErlangSettings getInstance() {
 *         return INSTANCE;
 *     }
 *
 *     @Override
 *     protected String getPreferenceModule() {
 *         return "org.flossware.netbeans.erlang";
 *     }
 *
 *     @Override
 *     protected Map<String, String> getDefaultValues() {
 *         Map<String, String> defaults = new HashMap<>();
 *         defaults.put(PREF_ERLC_PATH, "erlc");
 *         defaults.put(PREF_LSP_SERVER, "erlang_ls");
 *         return defaults;
 *     }
 *
 *     public String getErlcPath() {
 *         return getSetting(PREF_ERLC_PATH);
 *     }
 *
 *     public void setErlcPath(String path) {
 *         setSetting(PREF_ERLC_PATH, path);
 *     }
 * }
 * }</pre>
 *
 * <p><b>Thread Safety:</b> This class is thread-safe. The underlying
 * Preferences API handles synchronization.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public abstract class AbstractSettings {

    private volatile Preferences preferences;
    private volatile Map<String, String> defaultValues;

    /**
     * Get the NetBeans preferences instance.
     *
     * <p>Lazily initialized and cached.</p>
     *
     * @return The preferences instance
     */
    protected Preferences getPreferences() {
        if (preferences == null) {
            synchronized (this) {
                if (preferences == null) {
                    String module = getPreferenceModule();
                    if (module == null || module.isEmpty()) {
                        throw new IllegalStateException("Preference module cannot be null or empty");
                    }
                    try {
                        Class<?> moduleClass = Class.forName(module);
                        preferences = NbPreferences.forModule(moduleClass);
                    } catch (ClassNotFoundException e) {
                        // Fallback: use this class as module
                        preferences = NbPreferences.forModule(getClass());
                    }
                }
            }
        }
        return preferences;
    }

    /**
     * Get a setting value.
     *
     * <p>Returns the stored value if present, otherwise returns the default
     * value from {@link #getDefaultValues()}.</p>
     *
     * @param key The setting key
     * @return The setting value, or null if not found and no default
     */
    public String getSetting(String key) {
        if (key == null || key.isEmpty()) {
            return null;
        }
        String defaultValue = getDefaultValue(key);
        return getPreferences().get(key, defaultValue);
    }

    /**
     * Get a setting value with an explicit default.
     *
     * @param key The setting key
     * @param defaultValue The default value to use if not found
     * @return The setting value, or defaultValue if not found
     */
    public String getSetting(String key, String defaultValue) {
        if (key == null || key.isEmpty()) {
            return defaultValue;
        }
        return getPreferences().get(key, defaultValue);
    }

    /**
     * Set a setting value.
     *
     * @param key The setting key
     * @param value The setting value
     */
    public void setSetting(String key, String value) {
        if (key == null || key.isEmpty()) {
            return;
        }
        if (value == null) {
            getPreferences().remove(key);
        } else {
            getPreferences().put(key, value);
        }
    }

    /**
     * Get a boolean setting value.
     *
     * @param key The setting key
     * @param defaultValue The default value if not found
     * @return The boolean setting value
     */
    public boolean getBooleanSetting(String key, boolean defaultValue) {
        if (key == null || key.isEmpty()) {
            return defaultValue;
        }
        return getPreferences().getBoolean(key, defaultValue);
    }

    /**
     * Set a boolean setting value.
     *
     * @param key The setting key
     * @param value The boolean value
     */
    public void setBooleanSetting(String key, boolean value) {
        if (key == null || key.isEmpty()) {
            return;
        }
        getPreferences().putBoolean(key, value);
    }

    /**
     * Get an integer setting value.
     *
     * @param key The setting key
     * @param defaultValue The default value if not found
     * @return The integer setting value
     */
    public int getIntSetting(String key, int defaultValue) {
        if (key == null || key.isEmpty()) {
            return defaultValue;
        }
        return getPreferences().getInt(key, defaultValue);
    }

    /**
     * Set an integer setting value.
     *
     * @param key The setting key
     * @param value The integer value
     */
    public void setIntSetting(String key, int value) {
        if (key == null || key.isEmpty()) {
            return;
        }
        getPreferences().putInt(key, value);
    }

    /**
     * Remove a setting.
     *
     * @param key The setting key to remove
     */
    public void removeSetting(String key) {
        if (key != null && !key.isEmpty()) {
            getPreferences().remove(key);
        }
    }

    /**
     * Clear all settings.
     *
     * <p>Removes all stored preferences. Use with caution.</p>
     */
    public void clearAllSettings() {
        try {
            getPreferences().clear();
        } catch (Exception e) {
            // Ignore errors
        }
    }

    /**
     * Get the preference module name.
     *
     * <p>Should be the fully qualified class name of a class in the plugin
     * (e.g., "org.flossware.netbeans.erlang.ErlangPlugin").</p>
     *
     * @return The preference module name
     */
    protected abstract String getPreferenceModule();

    /**
     * Get default values for settings.
     *
     * <p>Returns a map of setting keys to default values. These defaults are
     * used when a setting has not been explicitly set by the user.</p>
     *
     * @return Map of default values, or null/empty if no defaults
     */
    protected abstract Map<String, String> getDefaultValues();

    /**
     * Get the default value for a specific key.
     *
     * @param key The setting key
     * @return The default value, or null if not found
     */
    protected String getDefaultValue(String key) {
        if (defaultValues == null) {
            synchronized (this) {
                if (defaultValues == null) {
                    Map<String, String> defaults = getDefaultValues();
                    defaultValues = defaults != null ? defaults : new HashMap<>();
                }
            }
        }
        return defaultValues.get(key);
    }
}
