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

package org.flossware.netbeans.ai.core.util;

import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

/**
 * Utility class for managing AI plugin preferences.
 *
 * <p>This class provides common preference management operations that are
 * shared across all AI plugins, reducing code duplication and ensuring
 * consistent behavior.</p>
 */
public final class PreferencesUtil {

    private PreferencesUtil() {
        // Utility class - no instantiation
    }

    /**
     * Get the API key from preferences.
     *
     * @param moduleClass the module class (for preference scope)
     * @param prefKey the preference key
     * @return the API key, or empty string if not set
     */
    public static String getApiKey(Class<?> moduleClass, String prefKey) {
        Preferences prefs = NbPreferences.forModule(moduleClass);
        return prefs.get(prefKey, "");
    }

    /**
     * Set the API key in preferences.
     *
     * @param moduleClass the module class (for preference scope)
     * @param prefKey the preference key
     * @param apiKey the API key to set, or null/empty to remove
     */
    public static void setApiKey(Class<?> moduleClass, String prefKey, String apiKey) {
        Preferences prefs = NbPreferences.forModule(moduleClass);
        if (apiKey == null || apiKey.trim().isEmpty()) {
            prefs.remove(prefKey);
        } else {
            prefs.put(prefKey, apiKey.trim());
        }
    }

    /**
     * Get the model from preferences.
     *
     * @param moduleClass the module class (for preference scope)
     * @param prefKey the preference key
     * @param defaultModel the default model if not set
     * @return the model name
     */
    public static String getModel(Class<?> moduleClass, String prefKey, String defaultModel) {
        Preferences prefs = NbPreferences.forModule(moduleClass);
        return prefs.get(prefKey, defaultModel);
    }

    /**
     * Set the model in preferences.
     *
     * @param moduleClass the module class (for preference scope)
     * @param prefKey the preference key
     * @param model the model name to set
     */
    public static void setModel(Class<?> moduleClass, String prefKey, String model) {
        Preferences prefs = NbPreferences.forModule(moduleClass);
        prefs.put(prefKey, model);
    }

    /**
     * Get the max tokens from preferences.
     *
     * @param moduleClass the module class (for preference scope)
     * @param prefKey the preference key
     * @param defaultMaxTokens the default value if not set
     * @return the max tokens value
     */
    public static int getMaxTokens(Class<?> moduleClass, String prefKey, int defaultMaxTokens) {
        Preferences prefs = NbPreferences.forModule(moduleClass);
        return prefs.getInt(prefKey, defaultMaxTokens);
    }

    /**
     * Set the max tokens in preferences.
     *
     * @param moduleClass the module class (for preference scope)
     * @param prefKey the preference key
     * @param maxTokens the max tokens value to set
     */
    public static void setMaxTokens(Class<?> moduleClass, String prefKey, int maxTokens) {
        Preferences prefs = NbPreferences.forModule(moduleClass);
        prefs.putInt(prefKey, maxTokens);
    }

    /**
     * Get the temperature from preferences.
     *
     * @param moduleClass the module class (for preference scope)
     * @param prefKey the preference key
     * @param defaultTemperature the default value if not set
     * @return the temperature value
     */
    public static double getTemperature(Class<?> moduleClass, String prefKey, double defaultTemperature) {
        Preferences prefs = NbPreferences.forModule(moduleClass);
        return prefs.getDouble(prefKey, defaultTemperature);
    }

    /**
     * Set the temperature in preferences.
     *
     * @param moduleClass the module class (for preference scope)
     * @param prefKey the preference key
     * @param temperature the temperature value to set
     */
    public static void setTemperature(Class<?> moduleClass, String prefKey, double temperature) {
        Preferences prefs = NbPreferences.forModule(moduleClass);
        prefs.putDouble(prefKey, temperature);
    }

    /**
     * Get a boolean preference.
     *
     * @param moduleClass the module class (for preference scope)
     * @param prefKey the preference key
     * @param defaultValue the default value if not set
     * @return the boolean value
     */
    public static boolean getBoolean(Class<?> moduleClass, String prefKey, boolean defaultValue) {
        Preferences prefs = NbPreferences.forModule(moduleClass);
        return prefs.getBoolean(prefKey, defaultValue);
    }

    /**
     * Set a boolean preference.
     *
     * @param moduleClass the module class (for preference scope)
     * @param prefKey the preference key
     * @param value the boolean value to set
     */
    public static void setBoolean(Class<?> moduleClass, String prefKey, boolean value) {
        Preferences prefs = NbPreferences.forModule(moduleClass);
        prefs.putBoolean(prefKey, value);
    }

    /**
     * Get a string preference.
     *
     * @param moduleClass the module class (for preference scope)
     * @param prefKey the preference key
     * @param defaultValue the default value if not set
     * @return the string value
     */
    public static String getString(Class<?> moduleClass, String prefKey, String defaultValue) {
        Preferences prefs = NbPreferences.forModule(moduleClass);
        return prefs.get(prefKey, defaultValue);
    }

    /**
     * Set a string preference.
     *
     * @param moduleClass the module class (for preference scope)
     * @param prefKey the preference key
     * @param value the string value to set
     */
    public static void setString(Class<?> moduleClass, String prefKey, String value) {
        Preferences prefs = NbPreferences.forModule(moduleClass);
        prefs.put(prefKey, value);
    }

    /**
     * Get an integer preference.
     *
     * @param moduleClass the module class (for preference scope)
     * @param prefKey the preference key
     * @param defaultValue the default value if not set
     * @return the integer value
     */
    public static int getInt(Class<?> moduleClass, String prefKey, int defaultValue) {
        Preferences prefs = NbPreferences.forModule(moduleClass);
        return prefs.getInt(prefKey, defaultValue);
    }

    /**
     * Set an integer preference.
     *
     * @param moduleClass the module class (for preference scope)
     * @param prefKey the preference key
     * @param value the integer value to set
     */
    public static void setInt(Class<?> moduleClass, String prefKey, int value) {
        Preferences prefs = NbPreferences.forModule(moduleClass);
        prefs.putInt(prefKey, value);
    }
}
