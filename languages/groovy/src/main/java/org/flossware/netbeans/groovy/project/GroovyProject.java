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
