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

package org.flossware.netbeans.go.settings;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for GoSettings.
 * Note: These tests verify the API but depend on NetBeans preferences which may not be
 * available in all test environments. Default values are tested.
 */
class GoSettingsTest {

    @Test
    void testGetGoPath_ReturnsValue() {
        String path = GoSettings.getInstance().getGoPath();
        assertThat(path).isNotNull();
    }

    @Test
    void testGetLspServer_ReturnsValue() {
        String server = GoSettings.getInstance().getLspServer();
        assertThat(server).isNotNull();
    }

    @Test
    void testIsModuleAutoDetectEnabled_ReturnsValue() {
        assertThatCode(() -> GoSettings.getInstance().isModuleAutoDetectEnabled()).doesNotThrowAnyException();
    }

    @Test
    void testSettersDoNotThrow() {
        assertThatCode(() -> GoSettings.getInstance().setGoPath("/usr/bin/go")).doesNotThrowAnyException();
        assertThatCode(() -> GoSettings.getInstance().setLspServer("gopls")).doesNotThrowAnyException();
        assertThatCode(() -> GoSettings.getInstance().setModuleAutoDetectEnabled(false)).doesNotThrowAnyException();
    }
}
