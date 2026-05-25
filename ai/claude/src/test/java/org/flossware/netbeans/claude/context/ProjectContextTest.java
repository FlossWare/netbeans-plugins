/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.claude.context;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectContextTest {

    private Project mockProject;
    private FileObject mockProjectDir;
    
    @BeforeEach
    void setUp() {
        mockProject = mock(Project.class, withSettings().lenient());
        mockProjectDir = mock(FileObject.class, withSettings().lenient());
        when(mockProject.getProjectDirectory()).thenReturn(mockProjectDir);
    }

    @Test
    void testConstruction() {
        assertThatCode(() -> new ProjectContext(mockProject)).doesNotThrowAnyException();
    }

    @Test
    void testGetProjectDirectory() {
        ProjectContext context = new ProjectContext(mockProject);
        assertThat(context.getProjectDirectory()).isEqualTo(mockProjectDir);
    }

    @Test
    void testGetProjectDirectory_Called() {
        ProjectContext context = new ProjectContext(mockProject);
        context.getProjectDirectory();
        verify(mockProject, atLeastOnce()).getProjectDirectory();
    }

    @Test
    void testGetProjectSummary_NoSources() {
        ProjectContext context = new ProjectContext(mockProject);
        when(mockProjectDir.getChildren()).thenReturn(new FileObject[0]);
        
        String summary = context.getProjectSummary();
        assertThat(summary).isNotNull();
    }

    @Test
    void testReadFileContent_NullFile() {
        ProjectContext context = new ProjectContext(mockProject);
        String content = context.readFileContent(null);
        assertThat(content).isNull();
    }

    @Test
    void testReadFileContent_NotDataFile() {
        ProjectContext context = new ProjectContext(mockProject);
        FileObject mockFile = mock(FileObject.class);
        when(mockFile.isData()).thenReturn(false);
        
        String content = context.readFileContent(mockFile);
        assertThat(content).isNull();
    }

    @Test
    void testReadFileContent_TooLarge() {
        ProjectContext context = new ProjectContext(mockProject);
        FileObject mockFile = mock(FileObject.class);
        when(mockFile.isData()).thenReturn(true);
        when(mockFile.getSize()).thenReturn(300000L); // > 200KB
        
        String content = context.readFileContent(mockFile);
        assertThat(content).isNull();
    }

    @Test
    void testReadFileContent_IrrelevantExtension() {
        ProjectContext context = new ProjectContext(mockProject);
        FileObject mockFile = mock(FileObject.class);
        when(mockFile.isData()).thenReturn(true);
        when(mockFile.getSize()).thenReturn(1000L);
        when(mockFile.getExt()).thenReturn("exe");
        
        String content = context.readFileContent(mockFile);
        assertThat(content).isNull();
    }

    @Test
    void testGetRelatedFiles_NullFile() {
        ProjectContext context = new ProjectContext(mockProject);
        assertThatCode(() -> context.getRelatedFiles(null)).doesNotThrowAnyException();
    }

    @Test
    void testGetRelatedFiles_NullParent() {
        ProjectContext context = new ProjectContext(mockProject);
        FileObject mockFile = mock(FileObject.class);
        when(mockFile.getParent()).thenReturn(null);
        
        assertThatCode(() -> context.getRelatedFiles(mockFile)).doesNotThrowAnyException();
    }

    @Test
    void testBuildContextForQuery_NullFile() {
        ProjectContext context = new ProjectContext(mockProject);
        String contextStr = context.buildContextForQuery("test query", null);
        assertThat(contextStr).contains("test query");
    }

    @Test
    void testBuildContextForQuery_EmptyQuery() {
        ProjectContext context = new ProjectContext(mockProject);
        FileObject mockFile = mock(FileObject.class);
        when(mockFile.getNameExt()).thenReturn("Test.java");
        
        String contextStr = context.buildContextForQuery("", mockFile);
        assertThat(contextStr).isNotNull();
    }

    @Test
    void testBuildContextForQuery_NullQuery() {
        ProjectContext context = new ProjectContext(mockProject);
        FileObject mockFile = mock(FileObject.class);
        when(mockFile.getNameExt()).thenReturn("Test.java");
        
        String contextStr = context.buildContextForQuery(null, mockFile);
        assertThat(contextStr).isNotNull();
    }
}
