package org.flossware.netbeans.python.project;

import org.flossware.netbeans.common.project.AbstractProject;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;

/**
 * NetBeans Python Project representation.
 *
 * <p>Recognized by presence of Python project files (setup.py, pyproject.toml, etc.).
 * This class extends {@link AbstractProject} which provides common project
 * functionality including lookup creation, project information, and logical view.</p>
 */
public class PythonProject extends AbstractProject {

    /**
     * Create a new Python project instance.
     *
     * @param projectDir The project directory
     * @param state The project state
     */
    public PythonProject(FileObject projectDir, ProjectState state) {
        super(projectDir, state);
    }

    @Override
    protected String getLanguageName() {
        return "Python";
    }

    @Override
    protected String getIconPath() {
        return "org/flossware/netbeans/python/resources/python-icon.png";
    }
}
