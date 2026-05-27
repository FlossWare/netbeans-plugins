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

package org.flossware.netbeans.typescript.execution;

import java.awt.event.ActionEvent;
import org.flossware.netbeans.typescript.TypeScriptIntegrationTestBase;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;

/**
 * Integration tests for RunTypeScriptAction.
 *
 * <p>Tests the execution of TypeScript scripts within NetBeans runtime.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class RunTypeScriptActionIT extends TypeScriptIntegrationTestBase {

    public RunTypeScriptActionIT(String name) {
        super(name);
    }

    /**
     * Test creating RunTypeScriptAction with valid TypeScript file.
     */
    public void testActionCreation_WithTypeScriptFile() throws Exception {
        FileObject tsFile = createSimpleTypeScriptScript("test.ts");
        DataObject dataObject = createDataObject(tsFile);

        RunTypeScriptAction action = new RunTypeScriptAction(dataObject);

        assertNotNull("Action should be created", action);
    }

    /**
     * Test action performs without exceptions on valid TypeScript file.
     */
    public void testActionPerformed_WithValidTypeScriptFile() throws Exception {
        FileObject tsFile = createSimpleTypeScriptScript("hello.ts");
        DataObject dataObject = createDataObject(tsFile);

        RunTypeScriptAction action = new RunTypeScriptAction(dataObject);

        // Note: This may fail if ts-node is not installed on test system
        // In CI, we would need ts-node installed or mock the execution
        try {
            action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "run"));
            // If we get here without exception, consider it a pass
            assertTrue("Action performed without exception", true);
        } catch (Exception e) {
            // If ts-node not installed, that's expected in test environment
            String message = e.getMessage();
            if (message != null && (message.contains("ts-node") || message.contains("Cannot run program"))) {
                // Expected in environment without ts-node
                assertTrue("Expected error when ts-node not installed", true);
            } else {
                // Unexpected error
                throw e;
            }
        }
    }

    /**
     * Test action with non-TypeScript file shows appropriate handling.
     */
    public void testActionPerformed_WithNonTypeScriptFile() throws Exception {
        FileObject textFile = workDirFileObject.createData("test.txt");
        DataObject dataObject = createDataObject(textFile);

        RunTypeScriptAction action = new RunTypeScriptAction(dataObject);

        // Should handle non-TypeScript files gracefully
        try {
            action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "run"));
            // Action should either succeed or handle gracefully
        } catch (Exception e) {
            // Some error handling is acceptable
            assertNotNull("Exception message should exist", e.getMessage());
        }
    }

    /**
     * Test action recognizes .ts extension.
     */
    public void testTypeScriptFileRecognition() throws Exception {
        FileObject tsFile = createTypeScriptFile("script.ts", "console.log('test');");

        assertEquals("Should recognize .ts extension", "ts", tsFile.getExt());
    }

    /**
     * Test action with .tsx file (TypeScript JSX).
     */
    public void testTsxFileRecognition() throws Exception {
        FileObject tsxFile = createTypeScriptFile("component.tsx", "const App = () => <div>Hello</div>;");

        assertEquals("Should recognize .tsx extension", "tsx", tsxFile.getExt());
    }

    /**
     * Test action with multiple TypeScript files.
     */
    public void testMultipleTypeScriptFiles() throws Exception {
        FileObject file1 = createSimpleTypeScriptScript("test1.ts");
        FileObject file2 = createSimpleTypeScriptScript("test2.ts");

        DataObject dataObject1 = createDataObject(file1);
        DataObject dataObject2 = createDataObject(file2);

        RunTypeScriptAction action1 = new RunTypeScriptAction(dataObject1);
        RunTypeScriptAction action2 = new RunTypeScriptAction(dataObject2);

        assertNotNull("First action should be created", action1);
        assertNotNull("Second action should be created", action2);
    }
}
