package org.flossware.netbeans.mvel.project;

import org.flossware.netbeans.common.project.AbstractProject;
import org.openide.filesystems.FileObject;

/**
 * Represents an MVEL project in NetBeans.
 *
 * <p>Provides project-level functionality for MVEL projects including
 * source file management and build configuration.</p>
 *
 * <p><b>Note:</b> MVEL is typically used as an embedded expression language
 * in Java applications. Standalone MVEL projects are uncommon, but this class
 * supports projects that contain MVEL template or expression files.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class MvelProject extends AbstractProject {

    public MvelProject(FileObject projectDirectory) {
        super(projectDirectory);
    }

    @Override
    protected String getProjectTypeName() {
        return "MVEL";
    }

    @Override
    protected String getDisplayName() {
        return getProjectDirectory().getName() + " (MVEL)";
    }
}
