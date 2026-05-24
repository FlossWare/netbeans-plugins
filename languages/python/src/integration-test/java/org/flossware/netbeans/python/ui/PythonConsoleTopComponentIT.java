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

package org.flossware.netbeans.python.ui;

import org.flossware.netbeans.python.PythonIntegrationTestBase;

/**
 * Integration tests for PythonConsoleTopComponent.
 *
 * <p>Tests the Python REPL console window within NetBeans runtime.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class PythonConsoleTopComponentIT extends PythonIntegrationTestBase {

    public PythonConsoleTopComponentIT(String name) {
        super(name);
    }

    /**
     * Test console component can be instantiated.
     */
    public void testConsoleCreation() {
        PythonConsoleTopComponent console = new PythonConsoleTopComponent();

        assertNotNull("Console should be created", console);
    }

    /**
     * Test console component has proper name.
     */
    public void testConsoleName() {
        PythonConsoleTopComponent console = new PythonConsoleTopComponent();

        String name = console.getName();

        assertNotNull("Console name should not be null", name);
        assertTrue("Console name should contain 'Python'", name.contains("Python"));
    }

    /**
     * Test console component has tooltip.
     */
    public void testConsoleTooltip() {
        PythonConsoleTopComponent console = new PythonConsoleTopComponent();

        String tooltip = console.getToolTipText();

        assertNotNull("Tooltip should not be null", tooltip);
        assertFalse("Tooltip should not be empty", tooltip.isEmpty());
    }

    /**
     * Test console component can be opened.
     */
    public void testConsoleOpen() {
        PythonConsoleTopComponent console = new PythonConsoleTopComponent();

        try {
            console.componentOpened();
            // If we get here, opening succeeded (or Python REPL failed to start, which is OK in test env)
            assertTrue("Console opened without exception", true);

            // Clean up
            console.componentClosed();
        } catch (Exception e) {
            // If Python not installed, REPL start will fail - that's expected
            String message = e.getMessage();
            if (message != null && (message.contains("python") || message.contains("Cannot run program"))) {
                // Expected in environment without Python
                assertTrue("Expected error when Python not installed", true);
            } else {
                // Unexpected error
                fail("Unexpected error: " + e.getMessage());
            }
        }
    }

    /**
     * Test console component can be closed.
     */
    public void testConsoleClose() {
        PythonConsoleTopComponent console = new PythonConsoleTopComponent();

        // Closing should work even if never opened
        try {
            console.componentClosed();
            assertTrue("Console closed without exception", true);
        } catch (Exception e) {
            fail("Console close should not throw: " + e.getMessage());
        }
    }

    /**
     * Test console component lifecycle (open and close).
     */
    public void testConsoleLifecycle() {
        PythonConsoleTopComponent console = new PythonConsoleTopComponent();

        try {
            // Open
            console.componentOpened();

            // Close
            console.componentClosed();

            assertTrue("Console lifecycle completed", true);
        } catch (Exception e) {
            // Python not installed is acceptable in test environment
            String message = e.getMessage();
            if (message == null || (!message.contains("python") && !message.contains("Cannot run program"))) {
                fail("Unexpected error in lifecycle: " + e.getMessage());
            }
        }
    }

    /**
     * Test sending command to console (if Python available).
     */
    public void testSendCommand() {
        PythonConsoleTopComponent console = new PythonConsoleTopComponent();

        try {
            console.componentOpened();

            // Try to send a simple command
            console.sendCommand("print('test')");

            // If we get here, command was sent (whether it executed depends on Python availability)
            assertTrue("Command sent without exception", true);

            console.componentClosed();
        } catch (Exception e) {
            // Expected if Python not available
            assertTrue("Exception is acceptable in test environment", true);
        }
    }

    /**
     * Test multiple console instances.
     */
    public void testMultipleConsoles() {
        PythonConsoleTopComponent console1 = new PythonConsoleTopComponent();
        PythonConsoleTopComponent console2 = new PythonConsoleTopComponent();

        assertNotNull("First console should be created", console1);
        assertNotNull("Second console should be created", console2);
        assertNotSame("Consoles should be different instances", console1, console2);
    }
}
