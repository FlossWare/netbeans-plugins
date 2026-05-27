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

package org.flossware.netbeans.typescript;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import org.netbeans.junit.NbTestCase;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;

/**
 * Base class for TypeScript plugin integration tests.
 *
 * <p>Integration tests run with full NetBeans Platform runtime and can test
 * UI components, actions, and complete workflows.</p>
 *
 * <p>Test classes extending this base should be named {@code *IT.java} to be
 * picked up by the Maven Failsafe plugin.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public abstract class TypeScriptIntegrationTestBase extends NbTestCase {

    protected File workDir;
    protected FileObject workDirFileObject;

    public TypeScriptIntegrationTestBase(String name) {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        clearWorkDir();
        workDir = getWorkDir();
        workDirFileObject = FileUtil.toFileObject(workDir);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Create a TypeScript file in the work directory.
     *
     * @param name File name (e.g., "test.ts")
     * @param content File content
     * @return FileObject representing the created file
     * @throws IOException if file creation fails
     */
    protected FileObject createTypeScriptFile(String name, String content) throws IOException {
        FileObject file = workDirFileObject.createData(name);
        try (OutputStream os = file.getOutputStream();
             PrintWriter pw = new PrintWriter(os)) {
            pw.print(content);
        }
        return file;
    }

    /**
     * Create a DataObject from a FileObject.
     *
     * @param fileObject The file object
     * @return DataObject for the file
     * @throws Exception if DataObject creation fails
     */
    protected DataObject createDataObject(FileObject fileObject) throws Exception {
        return DataObject.find(fileObject);
    }

    /**
     * Create a simple TypeScript script for testing.
     *
     * @param fileName Name of the file
     * @return FileObject for the created script
     * @throws IOException if file creation fails
     */
    protected FileObject createSimpleTypeScriptScript(String fileName) throws IOException {
        String content = "// Test TypeScript script\n"
                + "console.log('Hello from TypeScript');\n";
        return createTypeScriptFile(fileName, content);
    }

    /**
     * Create a TypeScript project structure.
     *
     * @param projectName Name of the project directory
     * @return FileObject for the project directory
     * @throws IOException if directory creation fails
     */
    protected FileObject createTypeScriptProject(String projectName) throws IOException {
        FileObject projectDir = workDirFileObject.createFolder(projectName);

        // Create tsconfig.json to make it a TypeScript project
        FileObject tsconfig = projectDir.createData("tsconfig.json");
        String tsconfigContent = "{\n"
                + "  \"compilerOptions\": {\n"
                + "    \"target\": \"ES2020\",\n"
                + "    \"module\": \"commonjs\",\n"
                + "    \"strict\": true\n"
                + "  }\n"
                + "}\n";
        try (OutputStream os = tsconfig.getOutputStream();
             PrintWriter pw = new PrintWriter(os)) {
            pw.print(tsconfigContent);
        }

        // Create a TypeScript source file
        FileObject mainTs = projectDir.createData("main.ts");
        String mainContent = "function main(): void {\n"
                + "  console.log('Hello from " + projectName + "');\n"
                + "}\n"
                + "\n"
                + "main();\n";
        try (OutputStream os = mainTs.getOutputStream();
             PrintWriter pw = new PrintWriter(os)) {
            pw.print(mainContent);
        }

        return projectDir;
    }

    /**
     * Wait for a condition to become true (with timeout).
     *
     * @param condition Condition to check
     * @param timeoutMs Timeout in milliseconds
     * @throws InterruptedException if interrupted while waiting
     */
    protected void waitFor(BooleanSupplier condition, long timeoutMs) throws InterruptedException {
        long start = System.currentTimeMillis();
        while (!condition.getAsBoolean()) {
            if (System.currentTimeMillis() - start > timeoutMs) {
                fail("Timeout waiting for condition");
            }
            Thread.sleep(100);
        }
    }

    /**
     * Functional interface for boolean conditions.
     */
    @FunctionalInterface
    protected interface BooleanSupplier {
        boolean getAsBoolean();
    }
}
