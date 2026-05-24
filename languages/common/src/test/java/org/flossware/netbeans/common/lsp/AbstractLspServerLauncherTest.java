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

package org.flossware.netbeans.common.lsp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AbstractLspServerLauncher.
 */
class AbstractLspServerLauncherTest {

    private TestLspServerLauncher launcher;
    private Project mockProject;
    private FileObject mockProjectDir;

    /**
     * Concrete implementation for testing.
     */
    private static class TestLspServerLauncher extends AbstractLspServerLauncher {
        private String[] serverCommands = new String[]{"test-server"};
        private String[] launchArgs = new String[]{"--stdio"};
        private String installInstructions = "Install test-server";
        private boolean commandAvailable = false;

        @Override
        protected String[] getServerCommands() {
            return serverCommands;
        }

        @Override
        protected String[] getLaunchArgs(String serverCommand) {
            if ("test-server".equals(serverCommand)) {
                return launchArgs;
            }
            return new String[0];
        }

        @Override
        protected String getInstallationInstructions() {
            return installInstructions;
        }

        @Override
        protected boolean isCommandAvailable(String command) {
            return commandAvailable;
        }

        public void setServerCommands(String[] serverCommands) {
            this.serverCommands = serverCommands;
        }

        public void setLaunchArgs(String[] launchArgs) {
            this.launchArgs = launchArgs;
        }

        public void setCommandAvailable(boolean commandAvailable) {
            this.commandAvailable = commandAvailable;
        }
    }

    @BeforeEach
    void setUp() {
        launcher = new TestLspServerLauncher();
        mockProject = mock(Project.class);
        mockProjectDir = mock(FileObject.class);

        when(mockProject.getProjectDirectory()).thenReturn(mockProjectDir);
    }

    @Test
    void testGetServerCommand_ServerAvailable() {
        launcher.setCommandAvailable(true);

        String[] result = launcher.getServerCommand(mockProject);

        assertNotNull(result);
        assertEquals(2, result.length);
        assertEquals("test-server", result[0]);
        assertEquals("--stdio", result[1]);
    }

    @Test
    void testGetServerCommand_ServerNotAvailable() {
        launcher.setCommandAvailable(false);

        String[] result = launcher.getServerCommand(mockProject);

        assertNull(result);
    }

    @Test
    void testGetServerCommand_NullServerCommands() {
        launcher.setServerCommands(null);

        String[] result = launcher.getServerCommand(mockProject);

        assertNull(result);
    }

    @Test
    void testGetServerCommand_EmptyServerCommands() {
        launcher.setServerCommands(new String[0]);

        String[] result = launcher.getServerCommand(mockProject);

        assertNull(result);
    }

    @Test
    void testGetServerCommand_NoLaunchArgs() {
        launcher.setCommandAvailable(true);
        launcher.setLaunchArgs(new String[0]);

        String[] result = launcher.getServerCommand(mockProject);

        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("test-server", result[0]);
    }

    @Test
    void testGetServerCommand_NullLaunchArgs() {
        launcher.setCommandAvailable(true);
        launcher.setLaunchArgs(null);

        String[] result = launcher.getServerCommand(mockProject);

        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("test-server", result[0]);
    }

    @Test
    void testGetWorkingDirectory_WithProject() {
        FileObject result = launcher.getWorkingDirectory(mockProject);

        assertNotNull(result);
        assertEquals(mockProjectDir, result);
    }

    @Test
    void testGetWorkingDirectory_NullProject() {
        FileObject result = launcher.getWorkingDirectory(null);

        assertNull(result);
    }

    @Test
    void testGetInstallationInstructions() {
        String result = launcher.getInstallationInstructions();

        assertNotNull(result);
        assertEquals("Install test-server", result);
    }
}
