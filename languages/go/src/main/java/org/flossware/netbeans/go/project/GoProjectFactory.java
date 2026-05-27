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

package org.flossware.netbeans.go.project;

import org.flossware.netbeans.common.project.AbstractProjectFactory;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;
import org.openide.util.lookup.ServiceProvider;

/**
 * Factory for recognizing and creating Go projects.
 *
 * <p>A directory is considered a Go project if it contains:</p>
 * <ul>
 *   <li>go.mod (Go modules)</li>
 *   <li>go.sum (Go modules checksums)</li>
 *   <li>.go files in root (fallback)</li>
 * </ul>
 *
 * <p>This factory extends {@link AbstractProjectFactory} which provides
 * common project recognition logic.</p>
 */
@ServiceProvider(service = ProjectFactory.class)
public class GoProjectFactory extends AbstractProjectFactory {

    @Override
    protected String[] getProjectMarkerFiles() {
        return new String[]{
            "go.mod",
            "go.sum"
        };
    }

    @Override
    protected String getFileExtension() {
        return "go";
    }

    @Override
    protected Project createProjectInstance(FileObject dir, ProjectState state) {
        return new GoProject(dir, state);
    }
}
