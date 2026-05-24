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

package org.flossware.netbeans.python.project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for PythonProjectFactory.
 */
class PythonProjectFactoryTest {

    private PythonProjectFactory factory;
    private FileObject mockDirectory;

    @BeforeEach
    void setUp() {
        factory = new PythonProjectFactory();
        mockDirectory = mock(FileObject.class);
    }

    @Test
    void testIsProject_WithSetupPy() {
        when(mockDirectory.getFileObject("setup.py")).thenReturn(mock(FileObject.class));

        boolean result = factory.isProject(mockDirectory);

        assertThat(result).isTrue();
    }

    @Test
    void testIsProject_WithPyprojectToml() {
        when(mockDirectory.getFileObject("setup.py")).thenReturn(null);
        when(mockDirectory.getFileObject("pyproject.toml")).thenReturn(mock(FileObject.class));

        boolean result = factory.isProject(mockDirectory);

        assertThat(result).isTrue();
    }

    @Test
    void testIsProject_WithRequirementsTxt() {
        when(mockDirectory.getFileObject("setup.py")).thenReturn(null);
        when(mockDirectory.getFileObject("pyproject.toml")).thenReturn(null);
        when(mockDirectory.getFileObject("requirements.txt")).thenReturn(mock(FileObject.class));

        boolean result = factory.isProject(mockDirectory);

        assertThat(result).isTrue();
    }

    @Test
    void testIsProject_WithPipfile() {
        when(mockDirectory.getFileObject("setup.py")).thenReturn(null);
        when(mockDirectory.getFileObject("pyproject.toml")).thenReturn(null);
        when(mockDirectory.getFileObject("requirements.txt")).thenReturn(null);
        when(mockDirectory.getFileObject("Pipfile")).thenReturn(mock(FileObject.class));

        boolean result = factory.isProject(mockDirectory);

        assertThat(result).isTrue();
    }

    @Test
    void testIsProject_WithPoetryLock() {
        when(mockDirectory.getFileObject("setup.py")).thenReturn(null);
        when(mockDirectory.getFileObject("pyproject.toml")).thenReturn(null);
        when(mockDirectory.getFileObject("requirements.txt")).thenReturn(null);
        when(mockDirectory.getFileObject("Pipfile")).thenReturn(null);
        when(mockDirectory.getFileObject("poetry.lock")).thenReturn(mock(FileObject.class));

        boolean result = factory.isProject(mockDirectory);

        assertThat(result).isTrue();
    }

    @Test
    void testIsProject_WithPythonFiles() {
        when(mockDirectory.getFileObject("setup.py")).thenReturn(null);
        when(mockDirectory.getFileObject("pyproject.toml")).thenReturn(null);
        when(mockDirectory.getFileObject("requirements.txt")).thenReturn(null);
        when(mockDirectory.getFileObject("Pipfile")).thenReturn(null);
        when(mockDirectory.getFileObject("poetry.lock")).thenReturn(null);

        FileObject pythonFile = mock(FileObject.class);
        when(pythonFile.getExt()).thenReturn("py");
        when(mockDirectory.getChildren()).thenReturn(new FileObject[]{pythonFile});

        boolean result = factory.isProject(mockDirectory);

        assertThat(result).isTrue();
    }

    @Test
    void testIsProject_WithNoPythonIndicators() {
        when(mockDirectory.getFileObject("setup.py")).thenReturn(null);
        when(mockDirectory.getFileObject("pyproject.toml")).thenReturn(null);
        when(mockDirectory.getFileObject("requirements.txt")).thenReturn(null);
        when(mockDirectory.getFileObject("Pipfile")).thenReturn(null);
        when(mockDirectory.getFileObject("poetry.lock")).thenReturn(null);

        FileObject javaFile = mock(FileObject.class);
        when(javaFile.getExt()).thenReturn("java");
        when(mockDirectory.getChildren()).thenReturn(new FileObject[]{javaFile});

        boolean result = factory.isProject(mockDirectory);

        assertThat(result).isFalse();
    }

    @Test
    void testLoadProject_WithPythonProject() throws Exception {
        when(mockDirectory.getFileObject("setup.py")).thenReturn(mock(FileObject.class));

        Project project = factory.loadProject(mockDirectory, null);

        assertThat(project).isNotNull();
        assertThat(project).isInstanceOf(PythonProject.class);
    }

    @Test
    void testLoadProject_WithNonPythonProject() throws Exception {
        when(mockDirectory.getFileObject("setup.py")).thenReturn(null);
        when(mockDirectory.getFileObject("pyproject.toml")).thenReturn(null);
        when(mockDirectory.getFileObject("requirements.txt")).thenReturn(null);
        when(mockDirectory.getFileObject("Pipfile")).thenReturn(null);
        when(mockDirectory.getFileObject("poetry.lock")).thenReturn(null);
        when(mockDirectory.getChildren()).thenReturn(new FileObject[0]);

        Project project = factory.loadProject(mockDirectory, null);

        assertThat(project).isNull();
    }

    @Test
    void testSaveProject_DoesNotThrow() {
        Project mockProject = mock(Project.class);

        assertThatCode(() -> factory.saveProject(mockProject))
            .doesNotThrowAnyException();
    }
}
