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

package org.flossware.netbeans.python.ui;

import org.flossware.netbeans.common.ui.AbstractConsoleTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 * Python Interactive Console (REPL).
 *
 * <p>Provides a Python interactive shell within NetBeans.</p>
 *
 * <p>This class extends {@link AbstractConsoleTopComponent} which provides
 * common REPL functionality including process management, I/O handling,
 * and UI components.</p>
 */
@TopComponent.Description(
    preferredID = "PythonConsoleTopComponent",
    iconBase = "org/flossware/netbeans/python/resources/python-icon.png",
    persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "output", openAtStartup = false)
@ActionID(category = "Window", id = "org.flossware.netbeans.python.ui.PythonConsoleTopComponent")
@ActionReference(path = "Menu/Window", position = 333)
@TopComponent.OpenActionRegistration(
    displayName = "#CTL_PythonConsoleAction",
    preferredID = "PythonConsoleTopComponent"
)
@Messages({
    "CTL_PythonConsoleAction=Python Console",
    "CTL_PythonConsoleTopComponent=Python Console",
    "HINT_PythonConsoleTopComponent=Interactive Python console (REPL)"
})
public final class PythonConsoleTopComponent extends AbstractConsoleTopComponent {

    public PythonConsoleTopComponent() {
        super();
        setName(Bundle.CTL_PythonConsoleTopComponent());
        setToolTipText(Bundle.HINT_PythonConsoleTopComponent());
    }

    @Override
    protected String getReplCommand() {
        return "python";
    }

    @Override
    protected String[] getReplArgs() {
        return new String[]{"-i"};
    }

    @Override
    protected String getConsoleName() {
        return "Python Console";
    }

    @Override
    protected String getIconPath() {
        return "org/flossware/netbeans/python/resources/python-icon.png";
    }

    @Override
    protected String getStartupMessage() {
        return "Type Python code and press Enter\n\n";
    }
}
