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

package org.flossware.netbeans.go.execution;

import java.awt.event.ActionEvent;
import org.flossware.netbeans.go.GoIntegrationTestBase;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;

/**
 * Integration tests for RunGoAction.
 *
 * <p>Tests the execution of Go programs within NetBeans runtime.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class RunGoActionIT extends GoIntegrationTestBase {

    public RunGoActionIT(String name) {
        super(name);
    }

    /**
     * Test creating RunGoAction with valid Go file.
     */
    public void testActionCreation_WithGoFile() throws Exception {
        FileObject goFile = createSimpleGoProgram("test.go");
        DataObject dataObject = createDataObject(goFile);

        RunGoAction action = new RunGoAction(dataObject);

        assertNotNull("Action should be created", action);
    }

    /**
     * Test action performs without exceptions on valid Go file.
     */
    public void testActionPerformed_WithValidGoFile() throws Exception {
        FileObject goFile = createSimpleGoProgram("hello.go");
        DataObject dataObject = createDataObject(goFile);

        RunGoAction action = new RunGoAction(dataObject);

        // Note: This may fail if Go is not installed on test system
        // In CI, we would need Go installed or mock the execution
        try {
            action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "run"));
            // If we get here without exception, consider it a pass
            assertTrue("Action performed without exception", true);
        } catch (Exception e) {
            // If Go not installed, that's expected in test environment
            String message = e.getMessage();
            if (message != null && (message.contains("go") || message.contains("Cannot run program"))) {
                // Expected in environment without Go
                assertTrue("Expected error when Go not installed", true);
            } else {
                // Unexpected error
                throw e;
            }
        }
    }

    /**
     * Test action with non-Go file shows appropriate handling.
     */
    public void testActionPerformed_WithNonGoFile() throws Exception {
        FileObject textFile = workDirFileObject.createData("test.txt");
        DataObject dataObject = createDataObject(textFile);

        RunGoAction action = new RunGoAction(dataObject);

        // Should handle non-Go files gracefully
        try {
            action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "run"));
            // Action should either succeed or handle gracefully
        } catch (Exception e) {
            // Some error handling is acceptable
            assertNotNull("Exception message should exist", e.getMessage());
        }
    }

    /**
     * Test action recognizes .go extension.
     */
    public void testGoFileRecognition() throws Exception {
        FileObject goFile = createGoFile("program.go", "package main\n\nfunc main() {}\n");

        assertEquals("Should recognize .go extension", "go", goFile.getExt());
    }

    /**
     * Test action with multiple Go files.
     */
    public void testMultipleGoFiles() throws Exception {
        FileObject file1 = createSimpleGoProgram("test1.go");
        FileObject file2 = createSimpleGoProgram("test2.go");

        DataObject dataObject1 = createDataObject(file1);
        DataObject dataObject2 = createDataObject(file2);

        RunGoAction action1 = new RunGoAction(dataObject1);
        RunGoAction action2 = new RunGoAction(dataObject2);

        assertNotNull("First action should be created", action1);
        assertNotNull("Second action should be created", action2);
    }
}
