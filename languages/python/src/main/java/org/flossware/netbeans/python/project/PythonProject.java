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

package org.flossware.netbeans.python.project;

import org.flossware.netbeans.common.project.AbstractProject;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;

/**
 * NetBeans Python Project representation.
 *
 * <p>Recognized by presence of Python project files (setup.py, pyproject.toml, etc.).
 * This class extends {@link AbstractProject} which provides common project
 * functionality including lookup creation, project information, and logical view.</p>
 */
public class PythonProject extends AbstractProject {

    /**
     * Create a new Python project instance.
     *
     * @param projectDir The project directory
     * @param state The project state
     */
    public PythonProject(FileObject projectDir, ProjectState state) {
        super(projectDir, state);
    }

    @Override
    protected String getLanguageName() {
        return "Python";
    }

    @Override
    protected String getIconPath() {
        return "org/flossware/netbeans/python/resources/python-icon.png";
    }
}
