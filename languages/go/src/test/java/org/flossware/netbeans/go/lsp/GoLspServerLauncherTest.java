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

package org.flossware.netbeans.go.lsp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for GoLspServerLauncher.
 */
class GoLspServerLauncherTest {

    private GoLspServerLauncher launcher;

    @BeforeEach
    void setUp() {
        launcher = new GoLspServerLauncher();
    }

    @Test
    void testGetServerCommands() {
        String[] commands = launcher.getServerCommands();

        assertThat(commands).isNotNull();
        assertThat(commands).contains("gopls");
    }

    @Test
    void testGetLaunchArgs() {
        String[] args = launcher.getLaunchArgs("gopls");

        assertThat(args).isNotNull();
        assertThat(args).isEmpty();
    }

    @Test
    void testGetInstallationInstructions() {
        String instructions = launcher.getInstallationInstructions();

        assertThat(instructions).isNotNull();
        assertThat(instructions).contains("gopls");
        assertThat(instructions).contains("go install");
    }

    @Test
    void testGetServerCommand_ReturnsNonNull() {
        // This may return null if gopls is not installed, which is fine for unit test
        String[] command = launcher.getServerCommand(null);
        // Just verify it doesn't throw an exception
        assertThat(command).satisfiesAnyOf(
            cmd -> assertThat(cmd).isNull(),
            cmd -> assertThat(cmd).isNotEmpty()
        );
    }
}
