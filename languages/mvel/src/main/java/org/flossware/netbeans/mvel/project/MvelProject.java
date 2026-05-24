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

package org.flossware.netbeans.mvel.project;

import org.flossware.netbeans.common.project.AbstractProject;
import org.netbeans.spi.project.ProjectState;
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

    public MvelProject(FileObject projectDirectory, ProjectState state) {
        super(projectDirectory, state);
    }

    @Override
    protected String getLanguageName() {
        return "MVEL";
    }

    @Override
    protected String getIconPath() {
        return "org/flossware/netbeans/mvel/resources/mvel-icon.png";
    }
}
