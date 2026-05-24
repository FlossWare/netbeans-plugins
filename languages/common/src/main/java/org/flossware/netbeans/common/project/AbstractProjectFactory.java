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

package org.flossware.netbeans.common.project;

import java.io.IOException;
import org.netbeans.api.project.Project;
import org.netbeans.spi.project.ProjectFactory;
import org.netbeans.spi.project.ProjectState;
import org.openide.filesystems.FileObject;

/**
 * Abstract base class for language-specific project factories.
 *
 * <p>This class implements the Template Method pattern to provide a common
 * project recognition and creation strategy. A directory is considered a project if:</p>
 * <ul>
 *   <li>It contains one or more marker files (e.g., rebar.config, mix.exs), OR</li>
 *   <li>It contains files with the language's primary file extension</li>
 * </ul>
 *
 * <p>Subclasses must implement abstract methods to provide language-specific
 * configuration:</p>
 * <ul>
 *   <li>{@link #getProjectMarkerFiles()} - Return marker files that identify projects</li>
 *   <li>{@link #getFileExtension()} - Return the primary file extension</li>
 *   <li>{@link #createProjectInstance(FileObject, ProjectState)} - Create the project instance</li>
 * </ul>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * @ServiceProvider(service = ProjectFactory.class)
 * public class ErlangProjectFactory extends AbstractProjectFactory {
 *     @Override
 *     protected String[] getProjectMarkerFiles() {
 *         return new String[]{"rebar.config", "rebar3.config"};
 *     }
 *
 *     @Override
 *     protected String getFileExtension() {
 *         return "erl";
 *     }
 *
 *     @Override
 *     protected Project createProjectInstance(FileObject dir, ProjectState state) {
 *         return new ErlangProject(dir, state);
 *     }
 * }
 * }</pre>
 *
 * <p><b>Thread Safety:</b> This class is thread-safe. The {@link #isProject(FileObject)}
 * and {@link #loadProject(FileObject, ProjectState)} methods can be called concurrently.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public abstract class AbstractProjectFactory implements ProjectFactory {

    /**
     * Determine if a directory is a project for this language.
     *
     * <p>Checks for marker files first, then falls back to scanning for
     * files with the language's file extension.</p>
     *
     * @param projectDirectory The directory to check
     * @return true if this directory is a project for this language
     */
    @Override
    public boolean isProject(FileObject projectDirectory) {
        if (projectDirectory == null || !projectDirectory.isFolder()) {
            return false;
        }

        // Check for project marker files
        String[] markerFiles = getProjectMarkerFiles();
        if (markerFiles != null) {
            for (String markerFile : markerFiles) {
                if (markerFile != null && projectDirectory.getFileObject(markerFile) != null) {
                    return true;
                }
            }
        }

        // Fall back to checking for files with the language extension
        String extension = getFileExtension();
        if (extension != null && !extension.isEmpty()) {
            return hasFilesWithExtension(projectDirectory, extension);
        }

        return false;
    }

    /**
     * Load a project from a directory.
     *
     * @param dir The project directory
     * @param state The project state
     * @return The loaded project, or null if not a valid project
     * @throws IOException If an error occurs loading the project
     */
    @Override
    public Project loadProject(FileObject dir, ProjectState state) throws IOException {
        if (isProject(dir)) {
            return createProjectInstance(dir, state);
        }
        return null;
    }

    /**
     * Save project metadata.
     *
     * <p>Default implementation does nothing, as most language projects
     * don't have special project files to save. Subclasses can override
     * if needed.</p>
     *
     * @param project The project to save
     * @throws IOException If an error occurs saving the project
     */
    @Override
    public void saveProject(Project project) throws IOException {
        // Most language projects don't have special project files to save
        // Configuration is in language-specific files (e.g., rebar.config, mix.exs)
    }

    /**
     * Get the marker files that identify a project.
     *
     * <p>These are files whose presence in a directory indicates it is a project
     * (e.g., "rebar.config", "mix.exs", "setup.py").</p>
     *
     * @return Array of marker file names, or null/empty if no marker files
     */
    protected abstract String[] getProjectMarkerFiles();

    /**
     * Get the primary file extension for this language.
     *
     * <p>Used as a fallback to identify projects when marker files are absent.
     * Should be the extension without the dot (e.g., "erl", "ex", "py").</p>
     *
     * @return The file extension, or null if not applicable
     */
    protected abstract String getFileExtension();

    /**
     * Create a project instance for this language.
     *
     * @param dir The project directory
     * @param state The project state
     * @return The created project instance
     */
    protected abstract Project createProjectInstance(FileObject dir, ProjectState state);

    /**
     * Check if a directory contains files with the given extension.
     *
     * <p>Only checks direct children, not subdirectories. This is a weak
     * indicator but helps recognize simple projects.</p>
     *
     * @param dir The directory to check
     * @param extension The file extension (without dot)
     * @return true if at least one file with the extension exists
     */
    protected boolean hasFilesWithExtension(FileObject dir, String extension) {
        if (dir == null || !dir.isFolder() || extension == null) {
            return false;
        }

        FileObject[] children = dir.getChildren();
        if (children == null) {
            return false;
        }

        for (FileObject child : children) {
            if (child != null && extension.equals(child.getExt())) {
                return true;
            }
        }

        return false;
    }
}
