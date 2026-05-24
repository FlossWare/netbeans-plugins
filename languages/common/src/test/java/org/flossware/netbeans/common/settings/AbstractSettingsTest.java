/*
 * Copyright 2026 FlossWare.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
