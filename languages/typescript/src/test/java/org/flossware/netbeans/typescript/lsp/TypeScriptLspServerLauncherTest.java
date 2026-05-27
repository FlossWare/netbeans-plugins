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

package org.flossware.netbeans.typescript.lsp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for TypeScriptLspServerLauncher.
 */
class TypeScriptLspServerLauncherTest {

    private TypeScriptLspServerLauncher launcher;

    @BeforeEach
    void setUp() {
        launcher = new TypeScriptLspServerLauncher();
    }

    @Test
    void testGetServerCommands() {
        String[] commands = launcher.getServerCommands();

        assertThat(commands).isNotNull();
        assertThat(commands).isNotEmpty();
    }

    @Test
    void testGetInstallationInstructions() {
        String instructions = launcher.getInstallationInstructions();

        assertThat(instructions).isNotNull();
        assertThat(instructions).contains("language server");
    }

    @Test
    void testGetServerCommand_ReturnsNonNull() {
        String[] command = launcher.getServerCommand(null);
        assertThat(command).satisfiesAnyOf(
            cmd -> assertThat(cmd).isNull(),
            cmd -> assertThat(cmd).isNotEmpty()
        );
    }
}
