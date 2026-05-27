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

package org.flossware.netbeans.go.project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for GoProjectFactory.
 */
class GoProjectFactoryTest {

    private GoProjectFactory factory;

    @BeforeEach
    void setUp() {
        factory = new GoProjectFactory();
    }

    @Test
    void testGetProjectMarkerFiles() {
        String[] markers = factory.getProjectMarkerFiles();

        assertThat(markers).isNotNull();
        assertThat(markers).contains("go.mod", "go.sum");
    }

    @Test
    void testGetFileExtension() {
        assertThat(factory.getFileExtension()).isEqualTo("go");
    }

    @Test
    void testCreateProjectInstance() {
        FileObject mockDir = Mockito.mock(FileObject.class);
        ProjectState mockState = Mockito.mock(ProjectState.class);

        assertThat(factory.createProjectInstance(mockDir, mockState))
            .isInstanceOf(GoProject.class);
    }

    @Test
    void testIsProject_WithGoMod_ReturnsTrue() {
        FileObject mockDir = Mockito.mock(FileObject.class);
        FileObject mockGoMod = Mockito.mock(FileObject.class);

        Mockito.when(mockDir.getFileObject("go.mod")).thenReturn(mockGoMod);

        assertThat(factory.isProject(mockDir)).isTrue();
    }

    @Test
    void testIsProject_WithGoSum_ReturnsTrue() {
        FileObject mockDir = Mockito.mock(FileObject.class);
        FileObject mockGoSum = Mockito.mock(FileObject.class);

        Mockito.when(mockDir.getFileObject("go.sum")).thenReturn(mockGoSum);

        assertThat(factory.isProject(mockDir)).isTrue();
    }

    @Test
    void testIsProject_NoMarkers_ReturnsFalse() {
        FileObject mockDir = Mockito.mock(FileObject.class);

        Mockito.when(mockDir.getFileObject(Mockito.anyString())).thenReturn(null);
        Mockito.when(mockDir.getChildren()).thenReturn(new FileObject[0]);

        assertThat(factory.isProject(mockDir)).isFalse();
    }
}
