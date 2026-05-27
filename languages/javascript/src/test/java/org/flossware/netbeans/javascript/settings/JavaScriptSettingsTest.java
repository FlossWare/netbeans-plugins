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

package org.flossware.netbeans.javascript.settings;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for JavaScriptSettings.
 * Note: These tests verify the API but depend on NetBeans preferences which may not be
 * available in all test environments. Default values are tested.
 */
class JavaScriptSettingsTest {

    @Test
    void testGetJavaScriptPath_ReturnsValue() {
        String path = JavaScriptSettings.getInstance().getJavaScriptPath();
        assertThat(path).isNotNull();
    }

    @Test
    void testGetLspServer_ReturnsValue() {
        String server = JavaScriptSettings.getInstance().getLspServer();
        assertThat(server).isNotNull();
    }

    @Test
    void testIsNodeAutoDetectEnabled_ReturnsValue() {
        assertThatCode(() -> JavaScriptSettings.getInstance().isNodeAutoDetectEnabled()).doesNotThrowAnyException();
    }

    @Test
    void testSettersDoNotThrow() {
        assertThatCode(() -> JavaScriptSettings.getInstance().setJavaScriptPath("/usr/bin/node")).doesNotThrowAnyException();
        assertThatCode(() -> JavaScriptSettings.getInstance().setLspServer("typescript-language-server")).doesNotThrowAnyException();
        assertThatCode(() -> JavaScriptSettings.getInstance().setNodeAutoDetectEnabled(false)).doesNotThrowAnyException();
    }
}
