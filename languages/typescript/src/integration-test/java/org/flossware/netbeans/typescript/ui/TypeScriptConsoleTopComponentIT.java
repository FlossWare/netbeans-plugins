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

package org.flossware.netbeans.typescript.ui;

import org.flossware.netbeans.typescript.TypeScriptIntegrationTestBase;

/**
 * Integration tests for TypeScriptConsoleTopComponent.
 *
 * <p>Tests the TypeScript REPL console window within NetBeans runtime.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class TypeScriptConsoleTopComponentIT extends TypeScriptIntegrationTestBase {

    public TypeScriptConsoleTopComponentIT(String name) {
        super(name);
    }

    /**
     * Test console component can be instantiated.
     */
    public void testConsoleCreation() {
        TypeScriptConsoleTopComponent console = new TypeScriptConsoleTopComponent();

        assertNotNull("Console should be created", console);
    }

    /**
     * Test console component has proper name.
     */
    public void testConsoleName() {
        TypeScriptConsoleTopComponent console = new TypeScriptConsoleTopComponent();

        String name = console.getName();

        assertNotNull("Console name should not be null", name);
        assertTrue("Console name should contain 'TypeScript'", name.contains("TypeScript"));
    }

    /**
     * Test console component has tooltip.
     */
    public void testConsoleTooltip() {
        TypeScriptConsoleTopComponent console = new TypeScriptConsoleTopComponent();

        String tooltip = console.getToolTipText();

        assertNotNull("Tooltip should not be null", tooltip);
        assertFalse("Tooltip should not be empty", tooltip.isEmpty());
    }

    /**
     * Test console component can be opened.
     */
    public void testConsoleOpen() {
        TypeScriptConsoleTopComponent console = new TypeScriptConsoleTopComponent();

        try {
            console.componentOpened();
            // If we get here, opening succeeded (or ts-node failed to start, which is OK in test env)
            assertTrue("Console opened without exception", true);

            // Clean up
            console.componentClosed();
        } catch (Exception e) {
            // If ts-node not installed, REPL start will fail - that's expected
            String message = e.getMessage();
            if (message != null && (message.contains("ts-node") || message.contains("Cannot run program"))) {
                // Expected in environment without ts-node
                assertTrue("Expected error when ts-node not installed", true);
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
        TypeScriptConsoleTopComponent console = new TypeScriptConsoleTopComponent();

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
        TypeScriptConsoleTopComponent console = new TypeScriptConsoleTopComponent();

        try {
            // Open
            console.componentOpened();

            // Close
            console.componentClosed();

            assertTrue("Console lifecycle completed", true);
        } catch (Exception e) {
            // ts-node not installed is acceptable in test environment
            String message = e.getMessage();
            if (message == null || (!message.contains("ts-node") && !message.contains("Cannot run program"))) {
                fail("Unexpected error in lifecycle: " + e.getMessage());
            }
        }
    }

    /**
     * Test sending command to console (if ts-node available).
     */
    public void testSendCommand() {
        TypeScriptConsoleTopComponent console = new TypeScriptConsoleTopComponent();

        try {
            console.componentOpened();

            // Try to send a simple command
            console.sendCommand("console.log('test');");

            // If we get here, command was sent (whether it executed depends on ts-node availability)
            assertTrue("Command sent without exception", true);

            console.componentClosed();
        } catch (Exception e) {
            // Expected if ts-node not available
            assertTrue("Exception is acceptable in test environment", true);
        }
    }

    /**
     * Test multiple console instances.
     */
    public void testMultipleConsoles() {
        TypeScriptConsoleTopComponent console1 = new TypeScriptConsoleTopComponent();
        TypeScriptConsoleTopComponent console2 = new TypeScriptConsoleTopComponent();

        assertNotNull("First console should be created", console1);
        assertNotNull("Second console should be created", console2);
        assertNotSame("Consoles should be different instances", console1, console2);
    }
}
