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

import org.flossware.netbeans.common.project.AbstractProjectFactory;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectFactory;
import org.openide.filesystems.FileObject;
import org.openide.util.lookup.ServiceProvider;

/**
 * Factory for creating Groovy projects.
 *
 * <p>Recognizes directories as Groovy projects based on common indicators:</p>
 * <ul>
 *   <li>build.gradle (Gradle Groovy project)</li>
 *   <li>pom.xml with Groovy dependencies (Maven Groovy project)</li>
 *   <li>Presence of .groovy files</li>
 * </ul>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
@ServiceProvider(service = ProjectFactory.class)
public class GroovyProjectFactory extends AbstractProjectFactory {

    @Override
    protected String[] getProjectMarkerFiles() {
        return new String[]{
            "build.gradle",
            "pom.xml"
        };
    }

    @Override
    protected String getFileExtension() {
        return "groovy";
    }

    @Override
    protected Project createProjectInstance(FileObject dir, org.netbeans.spi.project.ProjectState state) {
        return new GroovyProject(dir, state);
    }
}
