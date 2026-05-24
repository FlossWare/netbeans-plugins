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

package org.flossware.netbeans.mvel.lsp;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Tests for MvelLspServerLauncher.
 */
class MvelLspServerLauncherTest {

    @Test
    void testGetServerCommands() {
        MvelLspServerLauncher launcher = new MvelLspServerLauncher();
        String[] commands = launcher.getServerCommands();

        assertThat(commands).isNotNull();
        // MVEL doesn't have a standard LSP server, so array should be empty
        assertThat(commands).isEmpty();
    }

    @Test
    void testGetLaunchArgs() {
        MvelLspServerLauncher launcher = new MvelLspServerLauncher();
        String[] args = launcher.getLaunchArgs("mvel-language-server");

        assertThat(args).isNotNull();
        assertThat(args).isEmpty();
    }

    @Test
    void testGetInstallationInstructions() {
        MvelLspServerLauncher launcher = new MvelLspServerLauncher();
        String instructions = launcher.getInstallationInstructions();

        assertThat(instructions).isNotNull();
        assertThat(instructions).contains("MVEL");
        assertThat(instructions).contains("Language Server");
        assertThat(instructions).contains("embedded");
    }
}
