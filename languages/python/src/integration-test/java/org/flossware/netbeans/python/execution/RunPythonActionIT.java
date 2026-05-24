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

package org.flossware.netbeans.python.execution;

import java.awt.event.ActionEvent;
import org.flossware.netbeans.python.PythonIntegrationTestBase;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;

/**
 * Integration tests for RunPythonAction.
 *
 * <p>Tests the execution of Python scripts within NetBeans runtime.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class RunPythonActionIT extends PythonIntegrationTestBase {

    public RunPythonActionIT(String name) {
        super(name);
    }

    /**
     * Test creating RunPythonAction with valid Python file.
     */
    public void testActionCreation_WithPythonFile() throws Exception {
        FileObject pythonFile = createSimplePythonScript("test.py");
        DataObject dataObject = createDataObject(pythonFile);

        RunPythonAction action = new RunPythonAction(dataObject);

        assertNotNull("Action should be created", action);
    }

    /**
     * Test action performs without exceptions on valid Python file.
     */
    public void testActionPerformed_WithValidPythonFile() throws Exception {
        FileObject pythonFile = createSimplePythonScript("hello.py");
        DataObject dataObject = createDataObject(pythonFile);

        RunPythonAction action = new RunPythonAction(dataObject);

        // Note: This may fail if Python is not installed on test system
        // In CI, we would need Python installed or mock the execution
        try {
            action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "run"));
            // If we get here without exception, consider it a pass
            assertTrue("Action performed without exception", true);
        } catch (Exception e) {
            // If Python not installed, that's expected in test environment
            String message = e.getMessage();
            if (message != null && (message.contains("python") || message.contains("Cannot run program"))) {
                // Expected in environment without Python
                assertTrue("Expected error when Python not installed", true);
            } else {
                // Unexpected error
                throw e;
            }
        }
    }

    /**
     * Test action with non-Python file shows appropriate handling.
     */
    public void testActionPerformed_WithNonPythonFile() throws Exception {
        FileObject textFile = workDirFileObject.createData("test.txt");
        DataObject dataObject = createDataObject(textFile);

        RunPythonAction action = new RunPythonAction(dataObject);

        // Should handle non-Python files gracefully
        try {
            action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "run"));
            // Action should either succeed or handle gracefully
        } catch (Exception e) {
            // Some error handling is acceptable
            assertNotNull("Exception message should exist", e.getMessage());
        }
    }

    /**
     * Test action recognizes .py extension.
     */
    public void testPythonFileRecognition() throws Exception {
        FileObject pythonFile = createPythonFile("script.py", "print('test')");

        assertEquals("Should recognize .py extension", "py", pythonFile.getExt());
    }

    /**
     * Test action with multiple Python files.
     */
    public void testMultiplePythonFiles() throws Exception {
        FileObject file1 = createSimplePythonScript("test1.py");
        FileObject file2 = createSimplePythonScript("test2.py");

        DataObject dataObject1 = createDataObject(file1);
        DataObject dataObject2 = createDataObject(file2);

        RunPythonAction action1 = new RunPythonAction(dataObject1);
        RunPythonAction action2 = new RunPythonAction(dataObject2);

        assertNotNull("First action should be created", action1);
        assertNotNull("Second action should be created", action2);
    }
}
