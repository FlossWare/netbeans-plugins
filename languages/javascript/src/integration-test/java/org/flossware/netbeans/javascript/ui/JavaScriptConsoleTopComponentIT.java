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

package org.flossware.netbeans.javascript.ui;

import org.flossware.netbeans.javascript.JavaScriptIntegrationTestBase;

/**
 * Integration tests for JavaScriptConsoleTopComponent.
 *
 * <p>Tests the JavaScript REPL console window within NetBeans runtime.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class JavaScriptConsoleTopComponentIT extends JavaScriptIntegrationTestBase {

    public JavaScriptConsoleTopComponentIT(String name) {
        super(name);
    }

    /**
     * Test console component can be instantiated.
     */
    public void testConsoleCreation() {
        JavaScriptConsoleTopComponent console = new JavaScriptConsoleTopComponent();

        assertNotNull("Console should be created", console);
    }

    /**
     * Test console component has proper name.
     */
    public void testConsoleName() {
        JavaScriptConsoleTopComponent console = new JavaScriptConsoleTopComponent();

        String name = console.getName();

        assertNotNull("Console name should not be null", name);
        assertTrue("Console name should contain 'JavaScript'", name.contains("JavaScript"));
    }

    /**
     * Test console component has tooltip.
     */
    public void testConsoleTooltip() {
        JavaScriptConsoleTopComponent console = new JavaScriptConsoleTopComponent();

        String tooltip = console.getToolTipText();

        assertNotNull("Tooltip should not be null", tooltip);
    }

    /**
     * Test console open/close lifecycle.
     */
    public void testConsoleLifecycle() {
        JavaScriptConsoleTopComponent console = new JavaScriptConsoleTopComponent();

        // Open the console
        console.open();

        assertTrue("Console should be opened", console.isOpened());

        // Close the console
        console.close();

        assertFalse("Console should be closed", !console.isOpened());
    }
}
