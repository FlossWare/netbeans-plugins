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

package org.flossware.netbeans.rust.project;

import org.flossware.netbeans.common.project.AbstractProject;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;

/**
 * NetBeans Rust Project representation.
 *
 * <p>Recognized by presence of Rust project files (Cargo.toml, etc.).
 * This class extends {@link AbstractProject} which provides common project
 * functionality including lookup creation, project information, and logical view.</p>
 */
public class RustProject extends AbstractProject {

    /**
     * Create a new Rust project instance.
     *
     * @param projectDir The project directory
     * @param state The project state
     */
    public RustProject(FileObject projectDir, ProjectState state) {
        super(projectDir, state);
    }

    @Override
    protected String getLanguageName() {
        return "Rust";
    }

    @Override
    protected String getIconPath() {
        return "org/flossware/netbeans/rust/resources/rust-icon.png";
    }
}
