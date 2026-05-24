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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AbstractSettings.
 */
class AbstractSettingsTest {

    private TestSettings settings;

    /**
     * Concrete implementation for testing.
     */
    private static class TestSettings extends AbstractSettings {
        private Map<String, String> defaults;

        public TestSettings() {
            defaults = new HashMap<>();
            defaults.put("test.key", "default-value");
            defaults.put("test.bool", "true");
            defaults.put("test.int", "42");
        }

        @Override
        protected String getPreferenceModule() {
            return "org.flossware.netbeans.common.settings.AbstractSettingsTest";
        }

        @Override
        protected Map<String, String> getDefaultValues() {
            return defaults;
        }

        public void setDefaults(Map<String, String> defaults) {
            this.defaults = defaults;
        }
    }

    @BeforeEach
    void setUp() {
        settings = new TestSettings();
        settings.clearAllSettings();
    }

    @Test
    void testGetSetting_WithDefault() {
        String result = settings.getSetting("test.key");

        assertEquals("default-value", result);
    }

    @Test
    void testGetSetting_WithExplicitDefault() {
        String result = settings.getSetting("nonexistent.key", "explicit-default");

        assertEquals("explicit-default", result);
    }

    @Test
    void testSetAndGetSetting() {
        settings.setSetting("test.key", "new-value");
        String result = settings.getSetting("test.key");

        assertEquals("new-value", result);
    }

    @Test
    void testSetSetting_NullKey() {
        assertDoesNotThrow(() -> settings.setSetting(null, "value"));
    }

    @Test
    void testSetSetting_EmptyKey() {
        assertDoesNotThrow(() -> settings.setSetting("", "value"));
    }

    @Test
    void testSetSetting_NullValue() {
        settings.setSetting("test.key", "value");
        settings.setSetting("test.key", null);

        // Should revert to default
        assertEquals("default-value", settings.getSetting("test.key"));
    }

    @Test
    void testGetBooleanSetting_Default() {
        boolean result = settings.getBooleanSetting("test.bool", false);

        assertFalse(result); // String "true" is not parsed, default is returned
    }

    @Test
    void testSetAndGetBooleanSetting() {
        settings.setBooleanSetting("test.bool", true);
        boolean result = settings.getBooleanSetting("test.bool", false);

        assertTrue(result);
    }

    @Test
    void testGetIntSetting_Default() {
        int result = settings.getIntSetting("test.int", 0);

        assertEquals(0, result); // String "42" is not parsed, default is returned
    }

    @Test
    void testSetAndGetIntSetting() {
        settings.setIntSetting("test.int", 100);
        int result = settings.getIntSetting("test.int", 0);

        assertEquals(100, result);
    }

    @Test
    void testRemoveSetting() {
        settings.setSetting("test.key", "value");
        settings.removeSetting("test.key");

        // Should revert to default
        assertEquals("default-value", settings.getSetting("test.key"));
    }

    @Test
    void testRemoveSetting_NullKey() {
        assertDoesNotThrow(() -> settings.removeSetting(null));
    }

    @Test
    void testClearAllSettings() {
        settings.setSetting("test.key", "value");
        settings.clearAllSettings();

        // Should revert to default
        assertEquals("default-value", settings.getSetting("test.key"));
    }

    @Test
    void testGetSetting_NullKey() {
        String result = settings.getSetting(null);

        assertNull(result);
    }

    @Test
    void testGetSetting_EmptyKey() {
        String result = settings.getSetting("");

        assertNull(result);
    }

    @Test
    void testGetBooleanSetting_NullKey() {
        boolean result = settings.getBooleanSetting(null, true);

        assertTrue(result);
    }

    @Test
    void testGetIntSetting_NullKey() {
        int result = settings.getIntSetting(null, 99);

        assertEquals(99, result);
    }

    @Test
    void testGetDefaultValues_Null() {
        TestSettings nullDefaultSettings = new TestSettings();
        nullDefaultSettings.setDefaults(null);

        // Should not throw, should return null for unknown keys
        assertDoesNotThrow(() -> nullDefaultSettings.getSetting("unknown.key"));
    }
}
