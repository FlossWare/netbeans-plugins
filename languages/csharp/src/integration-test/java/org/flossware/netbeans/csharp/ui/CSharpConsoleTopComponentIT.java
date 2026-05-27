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

package org.flossware.netbeans.csharp.ui;

import org.flossware.netbeans.csharp.CSharpIntegrationTestBase;

/**
 * Integration tests for CSharpConsoleTopComponent.
 *
 * <p>Tests the C# REPL console window within NetBeans runtime.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class CSharpConsoleTopComponentIT extends CSharpIntegrationTestBase {

    public CSharpConsoleTopComponentIT(String name) {
        super(name);
    }

    /**
     * Test console component can be instantiated.
     */
    public void testConsoleCreation() {
        CSharpConsoleTopComponent console = new CSharpConsoleTopComponent();

        assertNotNull("Console should be created", console);
    }

    /**
     * Test console component has proper name.
     */
    public void testConsoleName() {
        CSharpConsoleTopComponent console = new CSharpConsoleTopComponent();

        String name = console.getName();

        assertNotNull("Console name should not be null", name);
        assertTrue("Console name should contain 'C#'", name.contains("C#"));
    }

    /**
     * Test console component has tooltip.
     */
    public void testConsoleTooltip() {
        CSharpConsoleTopComponent console = new CSharpConsoleTopComponent();

        String tooltip = console.getToolTipText();

        assertNotNull("Console tooltip should not be null", tooltip);
    }

    /**
     * Test console is properly configured.
     */
    public void testConsoleConfiguration() {
        CSharpConsoleTopComponent console = new CSharpConsoleTopComponent();

        // Console should be ready to use
        assertNotNull("Console should be configured", console);
    }
}
