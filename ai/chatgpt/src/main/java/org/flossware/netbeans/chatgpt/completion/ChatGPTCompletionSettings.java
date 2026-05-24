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

package org.flossware.netbeans.chatgpt.completion;

import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

/**
 * Settings for ChatGPT code completion
 */
public class ChatGPTCompletionSettings {

    private static final String PREF_COMPLETION_ENABLED = "completion.enabled";
    private static final String PREF_AUTO_TRIGGER_ENABLED = "completion.auto.trigger";
    private static final String PREF_TRIGGER_CHARS = "completion.trigger.chars";
    private static final String PREF_MIN_CHARS = "completion.min.chars";
    private static final String PREF_DELAY_MS = "completion.delay.ms";
    private static final String PREF_CACHE_ENABLED = "completion.cache.enabled";
    private static final String PREF_CACHE_TTL = "completion.cache.ttl";

    private static final String DEFAULT_TRIGGER_CHARS = ".";
    private static final int DEFAULT_MIN_CHARS = 3;
    private static final int DEFAULT_DELAY_MS = 500;
    private static final int DEFAULT_CACHE_TTL = 300; // 5 minutes

    private static Preferences getPreferences() {
        return NbPreferences.forModule(ChatGPTCompletionSettings.class);
    }

    public static boolean isEnabled() {
        return getPreferences().getBoolean(PREF_COMPLETION_ENABLED, true);
    }

    public static void setEnabled(boolean enabled) {
        getPreferences().putBoolean(PREF_COMPLETION_ENABLED, enabled);
    }

    public static boolean isAutoTriggerEnabled() {
        return getPreferences().getBoolean(PREF_AUTO_TRIGGER_ENABLED, false);
    }

    public static void setAutoTriggerEnabled(boolean enabled) {
        getPreferences().putBoolean(PREF_AUTO_TRIGGER_ENABLED, enabled);
    }

    public static String getTriggerCharacters() {
        return getPreferences().get(PREF_TRIGGER_CHARS, DEFAULT_TRIGGER_CHARS);
    }

    public static void setTriggerCharacters(String chars) {
        getPreferences().put(PREF_TRIGGER_CHARS, chars);
    }

    public static int getMinimumCharacters() {
        return getPreferences().getInt(PREF_MIN_CHARS, DEFAULT_MIN_CHARS);
    }

    public static void setMinimumCharacters(int min) {
        getPreferences().putInt(PREF_MIN_CHARS, min);
    }

    public static int getDelayMilliseconds() {
        return getPreferences().getInt(PREF_DELAY_MS, DEFAULT_DELAY_MS);
    }

    public static void setDelayMilliseconds(int delay) {
        getPreferences().putInt(PREF_DELAY_MS, delay);
    }

    public static boolean isCacheEnabled() {
        return getPreferences().getBoolean(PREF_CACHE_ENABLED, true);
    }

    public static void setCacheEnabled(boolean enabled) {
        getPreferences().putBoolean(PREF_CACHE_ENABLED, enabled);
    }

    public static int getCacheTTLSeconds() {
        return getPreferences().getInt(PREF_CACHE_TTL, DEFAULT_CACHE_TTL);
    }

    public static void setCacheTTLSeconds(int ttl) {
        getPreferences().putInt(PREF_CACHE_TTL, ttl);
    }
}
