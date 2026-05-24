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

package org.flossware.netbeans.python.lsp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.netbeans.api.project.Project;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for PythonLspServerLauncher.
 */
class PythonLspServerLauncherTest {

    private PythonLspServerLauncher launcher;

    @BeforeEach
    void setUp() {
        launcher = new PythonLspServerLauncher();
    }

    @Test
    void testGetServerCommand_ReturnsCommand() {
        String[] command = launcher.getServerCommand(null);

        // Should return either pyright, pylsp, or null (if neither installed)
        // We can't assert specific value as it depends on system setup
        // But we can verify it doesn't throw
        assertThat(command).satisfiesAnyOf(
            cmd -> assertThat(cmd).isNull(),
            cmd -> assertThat(cmd).contains("pyright-langserver"),
            cmd -> assertThat(cmd).contains("pylsp")
        );
    }

    @Test
    void testGetServerCommand_WithProject() {
        Project mockProject = mock(Project.class);
        String[] command = launcher.getServerCommand(mockProject);

        // Should work with or without project
        assertThat(command).satisfiesAnyOf(
            cmd -> assertThat(cmd).isNull(),
            cmd -> assertThat(cmd).isNotEmpty()
        );
    }

    @Test
    void testGetWorkingDirectory_WithNullProject() {
        assertThat(launcher.getWorkingDirectory(null)).isNull();
    }

    @Test
    void testGetWorkingDirectory_WithProject() {
        Project mockProject = mock(Project.class);
        org.openide.filesystems.FileObject mockFileObject = mock(org.openide.filesystems.FileObject.class);

        when(mockProject.getProjectDirectory()).thenReturn(mockFileObject);

        org.openide.filesystems.FileObject workingDir = launcher.getWorkingDirectory(mockProject);

        assertThat(workingDir).isNotNull();
        assertThat(workingDir).isEqualTo(mockFileObject);
    }
}
