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

package org.flossware.netbeans.rust.project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for RustProject.
 */
class RustProjectTest {

    private RustProject project;
    private FileObject mockDir;
    private ProjectState mockState;

    @BeforeEach
    void setUp() {
        mockDir = Mockito.mock(FileObject.class);
        mockState = Mockito.mock(ProjectState.class);
        Mockito.when(mockDir.isFolder()).thenReturn(true);
        project = new RustProject(mockDir, mockState);
    }

    @Test
    void testGetLanguageName() {
        assertThat(project.getLanguageName()).isEqualTo("Rust");
    }

    @Test
    void testGetIconPath() {
        assertThat(project.getIconPath()).isEqualTo("org/flossware/netbeans/rust/resources/rust-icon.png");
    }

    @Test
    void testGetProjectDirectory() {
        assertThat(project.getProjectDirectory()).isEqualTo(mockDir);
    }

    @Test
    void testGetLookup() {
        assertThat(project.getLookup()).isNotNull();
    }
}
