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

package org.flossware.netbeans.groovy.project;

import org.flossware.netbeans.common.project.AbstractProject;
import org.netbeans.spi.project.ProjectState;
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

    public GroovyProject(FileObject projectDirectory, ProjectState state) {
        super(projectDirectory, state);
    }

    @Override
    protected String getLanguageName() {
        return "Groovy";
    }

    @Override
    protected String getIconPath() {
        return "org/flossware/netbeans/groovy/resources/groovy-icon.png";
    }
}
