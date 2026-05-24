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

package org.flossware.netbeans.mvel.settings;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Tests for MvelSettings.
 */
class MvelSettingsTest {

    @Test
    void testGetInstance() {
        MvelSettings settings = MvelSettings.getInstance();
        assertThat(settings).isNotNull();
        assertThat(settings).isSameAs(MvelSettings.getInstance());
    }

    @Test
    void testDefaultMvelInterpreterPath() {
        MvelSettings settings = MvelSettings.getInstance();
        String path = settings.getMvelInterpreterPath();
        // MVEL doesn't have a standard interpreter, default is empty
        assertThat(path).isEmpty();
    }

    @Test
    void testDefaultLspServerPath() {
        MvelSettings settings = MvelSettings.getInstance();
        String path = settings.getLspServerPath();
        // MVEL doesn't have a standard LSP server, default is empty
        assertThat(path).isEmpty();
    }

    @Test
    void testSetMvelInterpreterPath() {
        MvelSettings settings = MvelSettings.getInstance();
        settings.setMvelInterpreterPath("/usr/local/bin/mvel-runner");
        assertThat(settings.getMvelInterpreterPath()).isEqualTo("/usr/local/bin/mvel-runner");
    }

    @Test
    void testSetLspServerPath() {
        MvelSettings settings = MvelSettings.getInstance();
        settings.setLspServerPath("/usr/local/bin/mvel-lsp");
        assertThat(settings.getLspServerPath()).isEqualTo("/usr/local/bin/mvel-lsp");
    }
}
