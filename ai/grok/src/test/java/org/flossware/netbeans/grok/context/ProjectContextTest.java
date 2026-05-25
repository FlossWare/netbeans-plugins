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

package org.flossware.netbeans.grok.context;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.project.Sources;
import org.openide.filesystems.FileObject;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ProjectContext.
 * Note: These tests use mocks since NetBeans APIs are not available in unit test environment.
 */
@ExtendWith(MockitoExtension.class)
class ProjectContextTest {

    @Mock
    private Project mockProject;

    @Mock
    private FileObject mockProjectDir;

    @Mock
    private ProjectInformation mockProjectInfo;

    @Mock
    private Sources mockSources;

    @Mock
    private FileObject mockFile;

    private ProjectContext context;

    @BeforeEach
    void setUp() {
        // Mock ProjectUtils static methods behavior
        when(mockProject.getProjectDirectory()).thenReturn(mockProjectDir);
        when(mockProjectDir.getPath()).thenReturn("/home/user/test-project");

        context = new ProjectContext(mockProject);
    }

    @Test
    void testConstruction() {
        assertThat(context).isNotNull();
    }

    @Test
    void testGetProjectDirectory() {
        FileObject dir = context.getProjectDirectory();
        assertThat(dir).isEqualTo(mockProjectDir);
    }

    @Test
    void testReadFileContent_NullFile() {
        String content = context.readFileContent(null);
        assertThat(content).isNull();
    }

    @Test
    void testReadFileContent_NotDataFile() {
        when(mockFile.isData()).thenReturn(false);

        String content = context.readFileContent(mockFile);
        assertThat(content).isNull();
    }

    @Test
    void testReadFileContent_FileTooLarge() {
        when(mockFile.isData()).thenReturn(true);
        when(mockFile.getSize()).thenReturn(200 * 1024L); // 200KB

        String content = context.readFileContent(mockFile);
        assertThat(content).isNull();
    }

    @Test
    void testReadFileContent_IrrelevantExtension() {
        when(mockFile.isData()).thenReturn(true);
        when(mockFile.getSize()).thenReturn(50 * 1024L);
        when(mockFile.getExt()).thenReturn("exe");

        String content = context.readFileContent(mockFile);
        assertThat(content).isNull();
    }

    @Test
    void testFindFiles_DoesNotThrowWithEmptyResult() {
        // Test that findFiles handles empty results gracefully
        // Note: Mocking getChildren with wildcards is complex, so we test the method doesn't throw
        assertThatCode(() -> context.findFiles("test"))
            .doesNotThrowAnyException();
    }

    @Test
    void testGetRelatedFiles_NullParent() {
        when(mockFile.getParent()).thenReturn(null);

        assertThatCode(() -> context.getRelatedFiles(mockFile))
            .doesNotThrowAnyException();
    }

    @Test
    void testBuildContextForQuery_WithNullFile() {
        String query = "What does this project do?";

        assertThatCode(() -> context.buildContextForQuery(query, null))
            .doesNotThrowAnyException();
    }

    @Test
    void testGetFileContext_ReturnsFormattedString() {
        when(mockFile.getPath()).thenReturn("/test/path/file.java");
        when(mockFile.getExt()).thenReturn("java");
        when(mockFile.isData()).thenReturn(true);
        when(mockFile.getSize()).thenReturn(100L);

        String fileContext = context.getFileContext(mockFile);

        assertThat(fileContext)
            .contains("File:")
            .contains("Project:");
    }
}
