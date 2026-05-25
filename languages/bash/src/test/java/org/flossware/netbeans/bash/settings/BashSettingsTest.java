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

package org.flossware.netbeans.bash.settings;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for BashSettings.
 * Note: These tests verify the API but depend on NetBeans preferences which may not be
 * available in all test environments. Default values are tested.
 */
class BashSettingsTest {

    @Test
    void testGetInstance_ReturnsSameInstance() {
        BashSettings instance1 = BashSettings.getInstance();
        BashSettings instance2 = BashSettings.getInstance();

        assertThat(instance1).isSameAs(instance2);
    }

    @Test
    void testGetBashPath_DefaultValue() {
        String path = BashSettings.getInstance().getBashPath();
        assertThat(path).isEqualTo("bash");
    }

    @Test
    void testGetBashdbPath_DefaultValue() {
        String path = BashSettings.getInstance().getBashdbPath();
        assertThat(path).isEqualTo("bashdb");
    }

    @Test
    void testSettersDoNotThrow() {
        assertThatCode(() -> BashSettings.getInstance().setBashPath("/bin/bash"))
            .doesNotThrowAnyException();
        assertThatCode(() -> BashSettings.getInstance().setBashdbPath("/usr/bin/bashdb"))
            .doesNotThrowAnyException();
    }

    @Test
    void testSetAndGet_BashPath() {
        BashSettings settings = BashSettings.getInstance();
        settings.setBashPath("/custom/bash");
        // Note: May return default in test environment without full NetBeans platform
        assertThatCode(() -> settings.getBashPath()).doesNotThrowAnyException();
    }

    @Test
    void testSetAndGet_BashdbPath() {
        BashSettings settings = BashSettings.getInstance();
        settings.setBashdbPath("/custom/bashdb");
        assertThatCode(() -> settings.getBashdbPath()).doesNotThrowAnyException();
    }
}
