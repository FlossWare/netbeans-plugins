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

package org.flossware.netbeans.typescript.settings;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for TypeScriptSettings.
 * Note: These tests verify the API but depend on NetBeans preferences which may not be
 * available in all test environments. Default values are tested.
 */
class TypeScriptSettingsTest {

    @Test
    void testGetNodePath_ReturnsValue() {
        String path = TypeScriptSettings.getInstance().getNodePath();
        assertThat(path).isNotNull();
    }

    @Test
    void testGetLspServer_ReturnsValue() {
        String server = TypeScriptSettings.getInstance().getLspServer();
        assertThat(server).isNotNull();
    }

    @Test
    void testIsNodeAutoDetectEnabled_ReturnsValue() {
        assertThatCode(() -> TypeScriptSettings.getInstance().isNodeAutoDetectEnabled()).doesNotThrowAnyException();
    }

    @Test
    void testSettersDoNotThrow() {
        assertThatCode(() -> TypeScriptSettings.getInstance().setNodePath("/usr/bin/node")).doesNotThrowAnyException();
        assertThatCode(() -> TypeScriptSettings.getInstance().setLspServer("typescript-language-server")).doesNotThrowAnyException();
        assertThatCode(() -> TypeScriptSettings.getInstance().setNodeAutoDetectEnabled(false)).doesNotThrowAnyException();
    }
}
