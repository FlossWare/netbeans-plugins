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

package org.flossware.netbeans.python.settings;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for PythonSettings.
 * Note: These tests verify the API but depend on NetBeans preferences which may not be
 * available in all test environments. Default values are tested.
 */
class PythonSettingsTest {

    @Test
    void testGetPythonPath_DefaultValue() {
        String path = PythonSettings.getInstance().getPythonPath();
        assertThat(path).isEqualTo("python");
    }

    @Test
    void testGetLspServer_DefaultValue() {
        String server = PythonSettings.getInstance().getLspServer();
        assertThat(server).isEqualTo("auto");
    }

    @Test
    void testIsVenvAutoDetectEnabled_DefaultValue() {
        boolean enabled = PythonSettings.getInstance().isVenvAutoDetectEnabled();
        assertThat(enabled).isTrue();
    }

    @Test
    void testSettersDoNotThrow() {
        assertThatCode(() -> PythonSettings.getInstance().setPythonPath("/usr/bin/python3")).doesNotThrowAnyException();
        assertThatCode(() -> PythonSettings.getInstance().setLspServer("pyright")).doesNotThrowAnyException();
        assertThatCode(() -> PythonSettings.getInstance().setVenvAutoDetectEnabled(false)).doesNotThrowAnyException();
    }
}
