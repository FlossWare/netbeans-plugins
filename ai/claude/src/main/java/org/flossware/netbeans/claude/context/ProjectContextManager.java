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

package org.flossware.netbeans.claude.context;

import java.util.prefs.Preferences;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ui.OpenProjects;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.util.NbPreferences;
import org.openide.windows.TopComponent;

/**
 * Manages project context across the application
 */
public class ProjectContextManager {

    private static ProjectContextManager instance;
    private ProjectContext currentProjectContext;

    private ProjectContextManager() {
    }

    public static synchronized ProjectContextManager getInstance() {
        if (instance == null) {
            instance = new ProjectContextManager();
        }
        return instance;
    }

    /**
     * Get the currently active project
     */
    public Project getActiveProject() {
        // Try to get project from active editor
        TopComponent activeTC = TopComponent.getRegistry().getActivated();
        if (activeTC != null) {
            DataObject dataObject = activeTC.getLookup().lookup(DataObject.class);
            if (dataObject != null) {
                FileObject file = dataObject.getPrimaryFile();
                Project project = org.netbeans.api.project.FileOwnerQuery.getOwner(file);
                if (project != null) {
                    return project;
                }
            }
        }

        // Fall back to main project
        Project[] openProjects = OpenProjects.getDefault().getOpenProjects();
        if (openProjects.length > 0) {
            return OpenProjects.getDefault().getMainProject();
        }

        return null;
    }

    /**
     * Get project context for the active project
     */
    public ProjectContext getActiveProjectContext() {
        Project project = getActiveProject();
        if (project != null) {
            if (currentProjectContext == null || !project.equals(getCurrentProject())) {
                currentProjectContext = new ProjectContext(project);
            }
            return currentProjectContext;
        }
        return null;
    }

    /**
     * Get the current project from context
     */
    private Project getCurrentProject() {
        return currentProjectContext != null ? currentProjectContext.getProjectDirectory().getLookup().lookup(Project.class) : null;
    }

    /**
     * Get the currently active file
     */
    public FileObject getActiveFile() {
        TopComponent activeTC = TopComponent.getRegistry().getActivated();
        if (activeTC != null) {
            DataObject dataObject = activeTC.getLookup().lookup(DataObject.class);
            if (dataObject != null) {
                return dataObject.getPrimaryFile();
            }
        }
        return null;
    }

    /**
     * Check if project context is enabled in preferences
     */
    public boolean isProjectContextEnabled() {
        Preferences prefs = NbPreferences.forModule(ProjectContextManager.class);
        return prefs.getBoolean("enable.project.context", true);
    }

    /**
     * Build context for a query
     */
    public String buildContextForQuery(String query) {
        if (!isProjectContextEnabled()) {
            return query;
        }

        ProjectContext context = getActiveProjectContext();
        FileObject activeFile = getActiveFile();

        if (context != null) {
            return context.buildContextForQuery(query, activeFile);
        }

        return query;
    }

    /**
     * Get project summary
     */
    public String getProjectSummary() {
        ProjectContext context = getActiveProjectContext();
        if (context != null) {
            return context.getProjectSummary();
        }
        return "No active project";
    }
}
