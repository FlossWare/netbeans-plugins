package org.flossware.netbeans.python.project;

import org.flossware.netbeans.common.project.AbstractProjectFactory;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.util.lookup.ServiceProvider;

/**
 * Factory for recognizing and creating Python projects.
 *
 * <p>A directory is considered a Python project if it contains:</p>
 * <ul>
 *   <li>setup.py (setuptools)</li>
 *   <li>pyproject.toml (PEP 518)</li>
 *   <li>requirements.txt (pip)</li>
 *   <li>Pipfile (pipenv)</li>
 *   <li>poetry.lock (poetry)</li>
 *   <li>.py files in root (fallback)</li>
 * </ul>
 *
 * <p>This factory extends {@link AbstractProjectFactory} which provides
 * common project recognition logic.</p>
 */
@ServiceProvider(service = ProjectFactory.class)
public class PythonProjectFactory extends AbstractProjectFactory {

    @Override
    protected String[] getProjectMarkerFiles() {
        return new String[]{
            "setup.py",
            "pyproject.toml",
            "requirements.txt",
            "Pipfile",
            "poetry.lock"
        };
    }

    @Override
    protected String getFileExtension() {
        return "py";
    }

    @Override
    protected Project createProjectInstance(FileObject dir, ProjectState state) {
        return new PythonProject(dir, state);
    }
}
