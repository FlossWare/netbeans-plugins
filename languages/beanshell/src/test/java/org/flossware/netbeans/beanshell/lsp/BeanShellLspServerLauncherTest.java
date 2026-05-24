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

package org.flossware.netbeans.beanshell.lsp;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Tests for BeanShellLspServerLauncher.
 */
class BeanShellLspServerLauncherTest {

    @Test
    void testGetServerCommands() {
        BeanShellLspServerLauncher launcher = new BeanShellLspServerLauncher();
        String[] commands = launcher.getServerCommands();

        // BeanShell does not have a standard LSP server, so should return null
        assertThat(commands).isNull();
    }

    @Test
    void testGetLaunchArgs() {
        BeanShellLspServerLauncher launcher = new BeanShellLspServerLauncher();
        String[] args = launcher.getLaunchArgs("bsh");

        assertThat(args).isNotNull();
        assertThat(args).isEmpty();
    }

    @Test
    void testGetInstallationInstructions() {
        BeanShellLspServerLauncher launcher = new BeanShellLspServerLauncher();
        String instructions = launcher.getInstallationInstructions();

        assertThat(instructions).isNotNull();
        assertThat(instructions).contains("BeanShell");
        assertThat(instructions).contains("Language Server Not Available");
    }
}
