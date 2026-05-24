package org.flossware.netbeans.groovy.project;

import org.flossware.netbeans.common.project.AbstractProject;
import org.openide.filesystems.FileObject;

/**
 * Represents a Groovy project in NetBeans.
 *
 * <p>Provides project-level functionality for Groovy projects including
 * source file management, build configuration, and LSP integration.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class GroovyProject extends AbstractProject {

    public GroovyProject(FileObject projectDirectory) {
        super(projectDirectory);
    }

    @Override
    protected String getProjectTypeName() {
        return "Groovy";
    }

    @Override
    protected String getDisplayName() {
        return getProjectDirectory().getName() + " (Groovy)";
    }
}
