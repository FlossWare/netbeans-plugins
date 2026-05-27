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

package org.flossware.netbeans.csharp.settings;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for CSharpSettings.
 * Note: These tests verify the API but depend on NetBeans preferences which may not be
 * available in all test environments. Default values are tested.
 */
class CSharpSettingsTest {

    @Test
    void testGetCSharpPath_ReturnsValue() {
        String path = CSharpSettings.getInstance().getCSharpPath();
        assertThat(path).isNotNull();
    }

    @Test
    void testGetLspServer_ReturnsValue() {
        String server = CSharpSettings.getInstance().getLspServer();
        assertThat(server).isNotNull();
    }

    @Test
    void testIsDotNetAutoDetectEnabled_ReturnsValue() {
        assertThatCode(() -> CSharpSettings.getInstance().isDotNetAutoDetectEnabled()).doesNotThrowAnyException();
    }

    @Test
    void testSettersDoNotThrow() {
        assertThatCode(() -> CSharpSettings.getInstance().setCSharpPath("/usr/bin/dotnet")).doesNotThrowAnyException();
        assertThatCode(() -> CSharpSettings.getInstance().setLspServer("omnisharp")).doesNotThrowAnyException();
        assertThatCode(() -> CSharpSettings.getInstance().setDotNetAutoDetectEnabled(false)).doesNotThrowAnyException();
    }
}
