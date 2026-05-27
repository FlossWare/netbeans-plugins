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

package org.flossware.netbeans.go.ui;

import org.flossware.netbeans.go.GoIntegrationTestBase;

/**
 * Integration tests for GoConsoleTopComponent.
 *
 * <p>Tests the Go console UI component in NetBeans runtime.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class GoConsoleTopComponentIT extends GoIntegrationTestBase {

    public GoConsoleTopComponentIT(String name) {
        super(name);
    }

    /**
     * Test console component creation.
     */
    public void testConsoleCreation() {
        GoConsoleTopComponent console = new GoConsoleTopComponent();

        assertNotNull("Console should be created", console);
        assertNotNull("Console should have name", console.getName());
    }

    /**
     * Test console name is set correctly.
     */
    public void testConsoleName() {
        GoConsoleTopComponent console = new GoConsoleTopComponent();

        String name = console.getName();

        assertNotNull("Name should not be null", name);
        assertTrue("Name should contain 'Go'", name.contains("Go"));
    }

    /**
     * Test console tooltip is set.
     */
    public void testConsoleTooltip() {
        GoConsoleTopComponent console = new GoConsoleTopComponent();

        String tooltip = console.getToolTipText();

        assertNotNull("Tooltip should not be null", tooltip);
    }

    /**
     * Test console can be instantiated multiple times.
     */
    public void testMultipleConsoleInstances() {
        GoConsoleTopComponent console1 = new GoConsoleTopComponent();
        GoConsoleTopComponent console2 = new GoConsoleTopComponent();

        assertNotNull("First console created", console1);
        assertNotNull("Second console created", console2);
        assertNotSame("Should be different instances", console1, console2);
    }
}
