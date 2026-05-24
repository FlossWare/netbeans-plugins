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
