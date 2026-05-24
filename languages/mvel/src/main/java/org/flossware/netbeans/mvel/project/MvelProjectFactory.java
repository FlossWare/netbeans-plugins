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

import org.flossware.netbeans.common.project.AbstractProjectFactory;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectFactory;
import org.openide.filesystems.FileObject;
import org.openide.util.lookup.ServiceProvider;

/**
 * Factory for creating MVEL projects.
 *
 * <p>Recognizes directories as MVEL projects based on common indicators:</p>
 * <ul>
 *   <li>Presence of .mvel files</li>
 *   <li>Presence of .mv files</li>
 *   <li>Maven projects with MVEL dependencies (pom.xml)</li>
 *   <li>Gradle projects with MVEL dependencies (build.gradle)</li>
 * </ul>
 *
 * <p><b>Note:</b> MVEL is typically used as an embedded expression language
 * in Java applications, so standalone MVEL projects are uncommon. This factory
 * primarily supports projects that contain MVEL template or expression files.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
@ServiceProvider(service = ProjectFactory.class)
public class MvelProjectFactory extends AbstractProjectFactory {

    @Override
    protected String[] getProjectMarkerFiles() {
        return new String[]{
            "pom.xml",
            "build.gradle"
        };
    }

    @Override
    protected String getFileExtension() {
        return "mvel";
    }

    @Override
    protected Project createProjectInstance(FileObject dir, org.netbeans.spi.project.ProjectState state) {
        return new MvelProject(dir, state);
    }
}
