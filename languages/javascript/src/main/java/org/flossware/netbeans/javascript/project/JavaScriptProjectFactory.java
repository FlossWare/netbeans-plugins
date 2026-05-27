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

package org.flossware.netbeans.javascript.project;

import org.flossware.netbeans.common.project.AbstractProjectFactory;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.util.lookup.ServiceProvider;

/**
 * Factory for recognizing and creating JavaScript projects.
 *
 * <p>A directory is considered a JavaScript project if it contains:</p>
 * <ul>
 *   <li>package.json (npm/yarn)</li>
 *   <li>package-lock.json (npm)</li>
 *   <li>yarn.lock (yarn)</li>
 *   <li>pnpm-lock.yaml (pnpm)</li>
 *   <li>.js files in root (fallback)</li>
 * </ul>
 *
 * <p>This factory extends {@link AbstractProjectFactory} which provides
 * common project recognition logic.</p>
 */
@ServiceProvider(service = ProjectFactory.class)
public class JavaScriptProjectFactory extends AbstractProjectFactory {

    @Override
    protected String[] getProjectMarkerFiles() {
        return new String[]{
            "package.json",
            "package-lock.json",
            "yarn.lock",
            "pnpm-lock.yaml"
        };
    }

    @Override
    protected String getFileExtension() {
        return "js";
    }

    @Override
    protected Project createProjectInstance(FileObject dir, ProjectState state) {
        return new JavaScriptProject(dir, state);
    }
}
