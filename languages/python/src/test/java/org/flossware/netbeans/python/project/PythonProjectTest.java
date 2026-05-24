package org.flossware.netbeans.python.project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.util.Lookup;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for PythonProject.
 */
class PythonProjectTest {

    private PythonProject project;
    private FileObject mockProjectDir;
    private ProjectState mockState;

    @BeforeEach
    void setUp() {
        mockProjectDir = mock(FileObject.class);
        mockState = mock(ProjectState.class);
        when(mockProjectDir.getName()).thenReturn("test-python-project");

        project = new PythonProject(mockProjectDir, mockState);
    }

    @Test
    void testGetProjectDirectory() {
        FileObject result = project.getProjectDirectory();

        assertThat(result).isEqualTo(mockProjectDir);
    }

    @Test
    void testGetLookup() {
        Lookup lookup = project.getLookup();

        assertThat(lookup).isNotNull();
    }

    @Test
    void testGetLookup_ReturnsSameInstance() {
        Lookup lookup1 = project.getLookup();
        Lookup lookup2 = project.getLookup();

        assertThat(lookup1).isSameAs(lookup2);
    }

    @Test
    void testGetLookup_ContainsProject() {
        Lookup lookup = project.getLookup();

        PythonProject foundProject = lookup.lookup(PythonProject.class);

        assertThat(foundProject).isEqualTo(project);
    }
}
