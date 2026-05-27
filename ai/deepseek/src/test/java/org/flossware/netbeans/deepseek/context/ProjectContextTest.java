/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.deepseek.context;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectContextTest {

    @Mock
    private Project mockProject;

    @Mock
    private FileObject mockProjectDir;

    private ProjectContext context;

    @BeforeEach
    void setUp() {
        lenient().when(mockProject.getProjectDirectory()).thenReturn(mockProjectDir);
        lenient().when(mockProjectDir.getName()).thenReturn("test-project");
        context = new ProjectContext(mockProject);
    }

    @Test
    void testConstruction() {
        assertThat(context).isNotNull();
    }

    @Test
    void testConstruction_WithNullProject() {
        assertThatCode(() -> new ProjectContext(null)).doesNotThrowAnyException();
    }

    @Test
    void testGetProjectName() {
        String name = context.getProjectName();
        assertThat(name).isNotNull();
    }

    @Test
    void testGetProjectDirectory() {
        FileObject dir = context.getProjectDirectory();
        assertThat(dir).isEqualTo(mockProjectDir);
    }

    @Test
    void testGetProjectSummary() {
        String summary = context.getProjectSummary();
        assertThat(summary).isNotNull();
    }

    @Test
    void testGetProjectSummary_ContainsProjectName() {
        String summary = context.getProjectSummary();
        assertThat(summary).contains("test-project");
    }

    @Test
    void testReadFileContent_NullFile() {
        assertThatCode(() -> context.readFileContent(null)).doesNotThrowAnyException();
    }

    @Test
    void testFindFiles_NullPattern() {
        assertThatCode(() -> context.findFiles(null)).doesNotThrowAnyException();
    }

    @Test
    void testFindFiles_EmptyPattern() {
        assertThatCode(() -> context.findFiles("")).doesNotThrowAnyException();
    }

    @Test
    void testBuildContextForQuery_WithNullFile() {
        assertThatCode(() -> context.buildContextForQuery("test query", null)).doesNotThrowAnyException();
    }
}
