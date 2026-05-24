package org.flossware.netbeans.common.project;

import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AbstractProjectFactory.
 */
class AbstractProjectFactoryTest {

    private TestProjectFactory factory;
    private FileObject mockProjectDir;
    private FileObject mockMarkerFile;
    private FileObject mockSourceFile;
    private ProjectState mockState;

    /**
     * Concrete implementation for testing.
     */
    private static class TestProjectFactory extends AbstractProjectFactory {
        private String[] markerFiles = new String[]{"test.config"};
        private String fileExtension = "test";
        private Project projectInstance;

        @Override
        protected String[] getProjectMarkerFiles() {
            return markerFiles;
        }

        @Override
        protected String getFileExtension() {
            return fileExtension;
        }

        @Override
        protected Project createProjectInstance(FileObject dir, ProjectState state) {
            return projectInstance;
        }

        public void setMarkerFiles(String[] markerFiles) {
            this.markerFiles = markerFiles;
        }

        public void setFileExtension(String fileExtension) {
            this.fileExtension = fileExtension;
        }

        public void setProjectInstance(Project projectInstance) {
            this.projectInstance = projectInstance;
        }
    }

    @BeforeEach
    void setUp() {
        factory = new TestProjectFactory();
        mockProjectDir = mock(FileObject.class);
        mockMarkerFile = mock(FileObject.class);
        mockSourceFile = mock(FileObject.class);
        mockState = mock(ProjectState.class);

        when(mockProjectDir.isFolder()).thenReturn(true);
        when(mockSourceFile.getExt()).thenReturn("test");
    }

    @Test
    void testIsProject_WithMarkerFile() {
        when(mockProjectDir.getFileObject("test.config")).thenReturn(mockMarkerFile);

        assertTrue(factory.isProject(mockProjectDir));
    }

    @Test
    void testIsProject_WithSourceFile() {
        when(mockProjectDir.getFileObject("test.config")).thenReturn(null);
        when(mockProjectDir.getChildren()).thenReturn(new FileObject[]{mockSourceFile});

        assertTrue(factory.isProject(mockProjectDir));
    }

    @Test
    void testIsProject_NoMarkerOrSource() {
        when(mockProjectDir.getFileObject("test.config")).thenReturn(null);
        when(mockProjectDir.getChildren()).thenReturn(new FileObject[0]);

        assertFalse(factory.isProject(mockProjectDir));
    }

    @Test
    void testIsProject_NullDirectory() {
        assertFalse(factory.isProject(null));
    }

    @Test
    void testIsProject_NotAFolder() {
        when(mockProjectDir.isFolder()).thenReturn(false);

        assertFalse(factory.isProject(mockProjectDir));
    }

    @Test
    void testIsProject_NullMarkerFiles() {
        factory.setMarkerFiles(null);
        when(mockProjectDir.getChildren()).thenReturn(new FileObject[]{mockSourceFile});

        assertTrue(factory.isProject(mockProjectDir));
    }

    @Test
    void testIsProject_NullExtension() {
        factory.setFileExtension(null);
        when(mockProjectDir.getFileObject("test.config")).thenReturn(null);
        when(mockProjectDir.getChildren()).thenReturn(new FileObject[]{mockSourceFile});

        assertFalse(factory.isProject(mockProjectDir));
    }

    @Test
    void testLoadProject_ValidProject() throws IOException {
        when(mockProjectDir.getFileObject("test.config")).thenReturn(mockMarkerFile);
        Project mockProject = mock(Project.class);
        factory.setProjectInstance(mockProject);

        Project result = factory.loadProject(mockProjectDir, mockState);

        assertNotNull(result);
        assertEquals(mockProject, result);
    }

    @Test
    void testLoadProject_InvalidProject() throws IOException {
        when(mockProjectDir.getFileObject("test.config")).thenReturn(null);
        when(mockProjectDir.getChildren()).thenReturn(new FileObject[0]);

        Project result = factory.loadProject(mockProjectDir, mockState);

        assertNull(result);
    }

    @Test
    void testSaveProject_DoesNotThrow() {
        Project mockProject = mock(Project.class);

        assertDoesNotThrow(() -> factory.saveProject(mockProject));
    }

    @Test
    void testHasFilesWithExtension_WithMatchingFile() {
        when(mockProjectDir.getChildren()).thenReturn(new FileObject[]{mockSourceFile});

        assertTrue(factory.hasFilesWithExtension(mockProjectDir, "test"));
    }

    @Test
    void testHasFilesWithExtension_NoMatchingFile() {
        when(mockSourceFile.getExt()).thenReturn("other");
        when(mockProjectDir.getChildren()).thenReturn(new FileObject[]{mockSourceFile});

        assertFalse(factory.hasFilesWithExtension(mockProjectDir, "test"));
    }

    @Test
    void testHasFilesWithExtension_NullDirectory() {
        assertFalse(factory.hasFilesWithExtension(null, "test"));
    }

    @Test
    void testHasFilesWithExtension_NullExtension() {
        assertFalse(factory.hasFilesWithExtension(mockProjectDir, null));
    }

    @Test
    void testHasFilesWithExtension_NullChildren() {
        when(mockProjectDir.getChildren()).thenReturn(null);

        assertFalse(factory.hasFilesWithExtension(mockProjectDir, "test"));
    }
}
