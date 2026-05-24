/*
 * Copyright 2026 FlossWare.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
    protected boolean isProjectDirectory(FileObject dir) {
        if (dir == null || !dir.isFolder()) {
            return false;
        }

        // Check for Gradle with Groovy
        FileObject buildGradle = dir.getFileObject("build.gradle");
        if (buildGradle != null) {
            return true;
        }

        // Check for Maven with pom.xml
        FileObject pomXml = dir.getFileObject("pom.xml");
        if (pomXml != null) {
            return true;
        }

        // Check for any .groovy files
        return hasGroovyFiles(dir);
    }

    @Override
    protected Project createProject(FileObject dir) {
        return new GroovyProject(dir);
    }

    @Override
    protected String getProjectTypeName() {
        return "Groovy";
    }

    /**
     * Check if directory contains any .groovy files.
     */
    private boolean hasGroovyFiles(FileObject dir) {
        if (dir == null || !dir.isFolder()) {
            return false;
        }

        for (FileObject child : dir.getChildren()) {
            if (child.isData() && "groovy".equals(child.getExt())) {
                return true;
            }
            if (child.isFolder() && !"build".equals(child.getName())) {
                if (hasGroovyFiles(child)) {
                    return true;
                }
            }
        }
        return false;
    }
}
