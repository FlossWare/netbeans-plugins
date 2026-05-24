/*
 * Copyright 2026 FlossWare.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
