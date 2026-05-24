package org.flossware.netbeans.beanshell.project;

import org.flossware.netbeans.common.project.AbstractProject;
import org.openide.filesystems.FileObject;

/**
 * Represents a BeanShell project in NetBeans.
 *
 * <p>Provides project-level functionality for BeanShell projects including
 * source file management and build configuration.</p>
 *
 * <p>Note: While BeanShell does not currently have LSP support, this project
 * type still provides basic project management capabilities.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class BeanShellProject extends AbstractProject {

    public BeanShellProject(FileObject projectDirectory) {
        super(projectDirectory);
    }

    @Override
    protected String getProjectTypeName() {
        return "BeanShell";
    }

    @Override
    protected String getDisplayName() {
        return getProjectDirectory().getName() + " (BeanShell)";
    }
}
