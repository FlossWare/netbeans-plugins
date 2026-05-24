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

import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectInformation;
import org.netbeans.spi.project.ProjectState;
import org.netbeans.spi.project.ui.LogicalViewProvider;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.FilterNode;
import org.openide.nodes.Node;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 * Abstract base class for language-specific projects.
 *
 * <p>This class provides common project implementation for NetBeans projects,
 * including lookup creation, project information, and logical view provider.</p>
 *
 * <p>Subclasses must implement abstract methods to provide language-specific
 * configuration:</p>
 * <ul>
 *   <li>{@link #getLanguageName()} - Return the language name for display</li>
 *   <li>{@link #getIconPath()} - Return the path to the icon resource</li>
 * </ul>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * public class ErlangProject extends AbstractProject {
 *     public ErlangProject(FileObject projectDir, ProjectState state) {
 *         super(projectDir, state);
 *     }
 *
 *     @Override
 *     protected String getLanguageName() {
 *         return "Erlang";
 *     }
 *
 *     @Override
 *     protected String getIconPath() {
 *         return "org/flossware/netbeans/erlang/resources/erlang-icon.png";
 *     }
 * }
 * }</pre>
 *
 * <p><b>Thread Safety:</b> This class is thread-safe. The lookup is lazily
 * initialized in a thread-safe manner.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public abstract class AbstractProject implements Project {

    private final FileObject projectDir;
    private final ProjectState state;
    private volatile Lookup lookup;

    /**
     * Create a new project instance.
     *
     * @param projectDir The project directory
     * @param state The project state
     */
    protected AbstractProject(FileObject projectDir, ProjectState state) {
        if (projectDir == null) {
            throw new IllegalArgumentException("Project directory cannot be null");
        }
        if (state == null) {
            throw new IllegalArgumentException("Project state cannot be null");
        }
        this.projectDir = projectDir;
        this.state = state;
    }

    /**
     * Get the project directory.
     *
     * @return The project directory
     */
    @Override
    public FileObject getProjectDirectory() {
        return projectDir;
    }

    /**
     * Get the project lookup.
     *
     * <p>The lookup contains:</p>
     * <ul>
     *   <li>This project instance</li>
     *   <li>Project information (name, icon)</li>
     *   <li>Logical view provider (project tree)</li>
     *   <li>Any additional items from {@link #createAdditionalLookupItems()}</li>
     * </ul>
     *
     * @return The project lookup
     */
    @Override
    public Lookup getLookup() {
        if (lookup == null) {
            synchronized (this) {
                if (lookup == null) {
                    lookup = createLookup();
                }
            }
        }
        return lookup;
    }

    /**
     * Get the language name for display purposes.
     *
     * @return The language name (e.g., "Erlang", "Python", "Lisp")
     */
    protected abstract String getLanguageName();

    /**
     * Get the path to the icon resource.
     *
     * <p>Should be a path relative to the classpath, e.g.,
     * "org/flossware/netbeans/erlang/resources/erlang-icon.png"</p>
     *
     * @return The icon resource path
     */
    protected abstract String getIconPath();

    /**
     * Create the project lookup.
     *
     * <p>Subclasses can override this method to customize the lookup contents,
     * but should typically call {@link #createAdditionalLookupItems()} instead.</p>
     *
     * @return The project lookup
     */
    protected Lookup createLookup() {
        Object[] items = createAdditionalLookupItems();
        Object[] allItems = new Object[items.length + 3];
        allItems[0] = this;
        allItems[1] = new Info();
        allItems[2] = new ProjectLogicalView(this);
        System.arraycopy(items, 0, allItems, 3, items.length);
        return Lookups.fixed(allItems);
    }

    /**
     * Create additional items to include in the project lookup.
     *
     * <p>Subclasses can override this method to add language-specific
     * capabilities to the project (e.g., build system, debugger).</p>
     *
     * @return Array of additional lookup items, or empty array if none
     */
    protected Object[] createAdditionalLookupItems() {
        return new Object[0];
    }

    /**
     * Get the project state.
     *
     * @return The project state
     */
    protected ProjectState getState() {
        return state;
    }

    /**
     * Project information implementation.
     */
    private final class Info implements ProjectInformation {

        @Override
        public String getName() {
            return getProjectDirectory().getName();
        }

        @Override
        public String getDisplayName() {
            return getName();
        }

        @Override
        public Icon getIcon() {
            String iconPath = getIconPath();
            if (iconPath != null && !iconPath.isEmpty()) {
                return new ImageIcon(ImageUtilities.loadImage(iconPath));
            }
            return null;
        }

        @Override
        public Project getProject() {
            return AbstractProject.this;
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener listener) {
            // No properties change
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener listener) {
            // No properties change
        }
    }

    /**
     * Logical view provider implementation.
     */
    private static final class ProjectLogicalView implements LogicalViewProvider {

        private final AbstractProject project;

        public ProjectLogicalView(AbstractProject project) {
            this.project = project;
        }

        @Override
        public Node createLogicalView() {
            FileObject projectDirectory = project.getProjectDirectory();
            DataFolder projectFolder = DataFolder.findFolder(projectDirectory);
            Node nodeOfProjectFolder = projectFolder.getNodeDelegate();
            return new ProjectNode(nodeOfProjectFolder, project);
        }

        @Override
        public Node findPath(Node root, Object target) {
            // Not implemented - NetBeans will use default navigation
            return null;
        }

        /**
         * Custom node for the project in the Projects window.
         */
        private static final class ProjectNode extends FilterNode {

            private final AbstractProject project;

            public ProjectNode(Node node, AbstractProject project) {
                super(node, new FilterNode.Children(node));
                this.project = project;
            }

            @Override
            public String getDisplayName() {
                return project.getProjectDirectory().getName();
            }

            @Override
            public String getHtmlDisplayName() {
                return "<b>" + getDisplayName() + "</b>";
            }
        }
    }
}
