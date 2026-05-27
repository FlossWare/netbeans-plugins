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

import org.flossware.netbeans.common.ui.AbstractConsoleTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 * JavaScript Interactive Console (REPL).
 *
 * <p>Provides a JavaScript interactive shell (Node.js) within NetBeans.</p>
 *
 * <p>This class extends {@link AbstractConsoleTopComponent} which provides
 * common REPL functionality including process management, I/O handling,
 * and UI components.</p>
 */
@TopComponent.Description(
    preferredID = "JavaScriptConsoleTopComponent",
    iconBase = "org/flossware/netbeans/javascript/resources/javascript-icon.png",
    persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "output", openAtStartup = false)
@ActionID(category = "Window", id = "org.flossware.netbeans.javascript.ui.JavaScriptConsoleTopComponent")
@ActionReference(path = "Menu/Window", position = 333)
@TopComponent.OpenActionRegistration(
    displayName = "#CTL_JavaScriptConsoleAction",
    preferredID = "JavaScriptConsoleTopComponent"
)
@Messages({
    "CTL_JavaScriptConsoleAction=JavaScript Console",
    "CTL_JavaScriptConsoleTopComponent=JavaScript Console",
    "HINT_JavaScriptConsoleTopComponent=Interactive JavaScript console (REPL)"
})
public final class JavaScriptConsoleTopComponent extends AbstractConsoleTopComponent {

    public JavaScriptConsoleTopComponent() {
        super();
        setName(Bundle.CTL_JavaScriptConsoleTopComponent());
        setToolTipText(Bundle.HINT_JavaScriptConsoleTopComponent());
    }

    @Override
    protected String getReplCommand() {
        return "node";
    }

    @Override
    protected String[] getReplArgs() {
        return new String[]{"-i"};
    }

    @Override
    protected String getConsoleName() {
        return "JavaScript Console";
    }

    @Override
    protected String getIconPath() {
        return "org/flossware/netbeans/javascript/resources/javascript-icon.png";
    }

    @Override
    protected String getStartupMessage() {
        return "Type JavaScript code and press Enter\n\n";
    }
}
