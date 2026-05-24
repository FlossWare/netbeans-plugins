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

package org.flossware.netbeans.gemini.context;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.api.project.SourceGroup;
import org.netbeans.api.project.Sources;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 * Manages project context for Gemini
 */
public class ProjectContext {

    private final Project project;
    private static final int MAX_FILE_SIZE = 100 * 1024; // 100KB
    private static final Set<String> RELEVANT_EXTENSIONS = new HashSet<>(Arrays.asList(
            "java", "py", "js", "ts", "jsx", "tsx", "html", "css", "xml", "json",
            "properties", "yml", "yaml", "md", "txt", "sh", "sql"
    ));

    public ProjectContext(Project project) {
        this.project = project;
    }

    /**
     * Get project name
     */
    public String getProjectName() {
        return ProjectUtils.getInformation(project).getDisplayName();
    }

    /**
     * Get project directory
     */
    public FileObject getProjectDirectory() {
        return project.getProjectDirectory();
    }

    /**
     * Get a summary of the project structure
     */
    public String getProjectSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Project: ").append(getProjectName()).append("\n");
        summary.append("Location: ").append(getProjectDirectory().getPath()).append("\n\n");

        // Get source groups
        Sources sources = ProjectUtils.getSources(project);
        SourceGroup[] sourceGroups = sources.getSourceGroups(Sources.TYPE_GENERIC);

        summary.append("Source Structure:\n");
        for (SourceGroup group : sourceGroups) {
            summary.append("  - ").append(group.getDisplayName())
                    .append(": ").append(group.getRootFolder().getPath()).append("\n");
        }

        // List important files
        summary.append("\nImportant Files:\n");
        List<FileObject> importantFiles = findImportantFiles();
        for (FileObject file : importantFiles) {
            summary.append("  - ").append(file.getNameExt()).append("\n");
        }

        return summary.toString();
    }

    /**
     * Find important project files (pom.xml, package.json, README, etc.)
     */
    private List<FileObject> findImportantFiles() {
        List<FileObject> important = new ArrayList<>();
        FileObject root = getProjectDirectory();

        String[] importantNames = {
            "pom.xml", "build.gradle", "package.json", "setup.py",
            "README.md", "README.txt", "CONTRIBUTING.md",
            "Dockerfile", ".gitignore", "requirements.txt"
        };

        for (String name : importantNames) {
            FileObject file = root.getFileObject(name);
            if (file != null) {
                important.add(file);
            }
        }

        return important;
    }

    /**
     * Read content of a file if it's relevant and not too large
     */
    public String readFileContent(FileObject file) {
        if (file == null || !file.isData()) {
            return null;
        }

        // Check size
        if (file.getSize() > MAX_FILE_SIZE) {
            return null;
        }

        // Check extension
        String ext = file.getExt();
        if (!RELEVANT_EXTENSIONS.contains(ext.toLowerCase())) {
            return null;
        }

        try {
            return file.asText();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Get context for a specific file
     */
    public String getFileContext(FileObject file) {
        StringBuilder context = new StringBuilder();
        context.append("File: ").append(file.getPath()).append("\n");
        context.append("Project: ").append(getProjectName()).append("\n\n");

        String content = readFileContent(file);
        if (content != null) {
            context.append("Content:\n```").append(file.getExt()).append("\n");
            context.append(content);
            context.append("\n```\n");
        }

        return context.toString();
    }

    /**
     * Search for files matching a pattern
     */
    public List<FileObject> findFiles(String pattern) {
        List<FileObject> results = new ArrayList<>();
        FileObject root = getProjectDirectory();

        Enumeration<? extends FileObject> files = root.getChildren(true);
        while (files.hasMoreElements()) {
            FileObject file = files.nextElement();
            if (file.isData() && file.getNameExt().contains(pattern)) {
                results.add(file);
            }
        }

        return results;
    }

    /**
     * Get related files (e.g., tests, interfaces, implementations)
     */
    public List<FileObject> getRelatedFiles(FileObject file) {
        List<FileObject> related = new ArrayList<>();
        String baseName = file.getName();
        String ext = file.getExt();

        // Look for test files
        FileObject parent = file.getParent();
        if (parent != null) {
            // Same directory
            FileObject test = parent.getFileObject(baseName + "Test", ext);
            if (test != null) related.add(test);

            FileObject testCamelCase = parent.getFileObject(baseName + "Tests", ext);
            if (testCamelCase != null) related.add(testCamelCase);

            // Test directory
            FileObject testDir = parent.getParent();
            if (testDir != null) {
                FileObject testFolder = testDir.getFileObject("test");
                if (testFolder != null) {
                    FileObject testFile = testFolder.getFileObject(baseName + "Test", ext);
                    if (testFile != null) related.add(testFile);
                }
            }
        }

        return related;
    }

    /**
     * Build context string for Gemini including relevant project files
     */
    public String buildContextForQuery(String query, FileObject currentFile) {
        StringBuilder context = new StringBuilder();

        context.append("=== PROJECT CONTEXT ===\n\n");
        context.append(getProjectSummary()).append("\n");

        if (currentFile != null) {
            context.append("\n=== CURRENT FILE ===\n");
            context.append(getFileContext(currentFile)).append("\n");

            // Add related files
            List<FileObject> related = getRelatedFiles(currentFile);
            if (!related.isEmpty()) {
                context.append("\n=== RELATED FILES ===\n");
                for (FileObject relatedFile : related) {
                    context.append("\n").append(getFileContext(relatedFile)).append("\n");
                }
            }
        }

        context.append("\n=== USER QUERY ===\n");
        context.append(query).append("\n");

        return context.toString();
    }
}
