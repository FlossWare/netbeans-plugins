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

package org.flossware.netbeans.beanshell.project;

import org.flossware.netbeans.common.project.AbstractProjectFactory;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectFactory;
import org.openide.filesystems.FileObject;
import org.openide.util.lookup.ServiceProvider;

/**
 * Factory for creating BeanShell projects.
 *
 * <p>Recognizes directories as BeanShell projects based on common indicators:</p>
 * <ul>
 *   <li>build.gradle (Gradle BeanShell project)</li>
 *   <li>pom.xml with BeanShell dependencies (Maven BeanShell project)</li>
 *   <li>Presence of .bsh or .beanshell files</li>
 * </ul>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
@ServiceProvider(service = ProjectFactory.class)
public class BeanShellProjectFactory extends AbstractProjectFactory {

    @Override
    protected boolean isProjectDirectory(FileObject dir) {
        if (dir == null || !dir.isFolder()) {
            return false;
        }

        // Check for Gradle with BeanShell
        FileObject buildGradle = dir.getFileObject("build.gradle");
        if (buildGradle != null) {
            return true;
        }

        // Check for Maven with pom.xml
        FileObject pomXml = dir.getFileObject("pom.xml");
        if (pomXml != null) {
            return true;
        }

        // Check for any .bsh or .beanshell files
        return hasBeanShellFiles(dir);
    }

    @Override
    protected Project createProject(FileObject dir) {
        return new BeanShellProject(dir);
    }

    @Override
    protected String getProjectTypeName() {
        return "BeanShell";
    }

    /**
     * Check if directory contains any .bsh or .beanshell files.
     */
    private boolean hasBeanShellFiles(FileObject dir) {
        if (dir == null || !dir.isFolder()) {
            return false;
        }

        for (FileObject child : dir.getChildren()) {
            if (child.isData()) {
                String ext = child.getExt();
                if ("bsh".equals(ext) || "beanshell".equals(ext)) {
                    return true;
                }
            }
            if (child.isFolder() && !"build".equals(child.getName())) {
                if (hasBeanShellFiles(child)) {
                    return true;
                }
            }
        }
        return false;
    }
}
