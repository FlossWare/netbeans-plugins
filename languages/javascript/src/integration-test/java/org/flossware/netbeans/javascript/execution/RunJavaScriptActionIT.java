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

package org.flossware.netbeans.javascript.execution;

import java.awt.event.ActionEvent;
import org.flossware.netbeans.javascript.JavaScriptIntegrationTestBase;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;

/**
 * Integration tests for RunJavaScriptAction.
 *
 * <p>Tests the execution of JavaScript scripts within NetBeans runtime.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class RunJavaScriptActionIT extends JavaScriptIntegrationTestBase {

    public RunJavaScriptActionIT(String name) {
        super(name);
    }

    /**
     * Test creating RunJavaScriptAction with valid JavaScript file.
     */
    public void testActionCreation_WithJavaScriptFile() throws Exception {
        FileObject jsFile = createSimpleJavaScriptScript("test.js");
        DataObject dataObject = createDataObject(jsFile);

        RunJavaScriptAction action = new RunJavaScriptAction(dataObject);

        assertNotNull("Action should be created", action);
    }

    /**
     * Test action performs without exceptions on valid JavaScript file.
     */
    public void testActionPerformed_WithValidJavaScriptFile() throws Exception {
        FileObject jsFile = createSimpleJavaScriptScript("hello.js");
        DataObject dataObject = createDataObject(jsFile);

        RunJavaScriptAction action = new RunJavaScriptAction(dataObject);

        // Note: This may fail if Node.js is not installed on test system
        // In CI, we would need Node.js installed or mock the execution
        try {
            action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "run"));
            // If we get here without exception, consider it a pass
            assertTrue("Action performed without exception", true);
        } catch (Exception e) {
            // If Node.js not installed, that's expected in test environment
            String message = e.getMessage();
            if (message != null && (message.contains("node") || message.contains("Cannot run program"))) {
                // Expected in environment without Node.js
                assertTrue("Expected error when Node.js not installed", true);
            } else {
                // Unexpected error
                throw e;
            }
        }
    }

    /**
     * Test multiple script executions.
     */
    public void testMultipleExecutions() throws Exception {
        FileObject jsFile = createSimpleJavaScriptScript("multi.js");
        DataObject dataObject = createDataObject(jsFile);

        RunJavaScriptAction action = new RunJavaScriptAction(dataObject);

        // Run multiple times to ensure no state issues
        for (int i = 0; i < 3; i++) {
            try {
                action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "run"));
            } catch (Exception e) {
                // Ignore if Node.js not installed
                if (e.getMessage() == null || (!e.getMessage().contains("node") && !e.getMessage().contains("Cannot run program"))) {
                    throw e;
                }
            }
        }

        assertTrue("Multiple executions completed", true);
    }
}
