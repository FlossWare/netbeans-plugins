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

import org.flossware.netbeans.common.ui.AbstractConsoleTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 * TypeScript Interactive Console (REPL).
 *
 * <p>Provides a TypeScript interactive shell within NetBeans using ts-node.</p>
 *
 * <p>This class extends {@link AbstractConsoleTopComponent} which provides
 * common REPL functionality including process management, I/O handling,
 * and UI components.</p>
 */
@TopComponent.Description(
    preferredID = "TypeScriptConsoleTopComponent",
    iconBase = "org/flossware/netbeans/typescript/resources/typescript-icon.png",
    persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "output", openAtStartup = false)
@ActionID(category = "Window", id = "org.flossware.netbeans.typescript.ui.TypeScriptConsoleTopComponent")
@ActionReference(path = "Menu/Window", position = 334)
@TopComponent.OpenActionRegistration(
    displayName = "#CTL_TypeScriptConsoleAction",
    preferredID = "TypeScriptConsoleTopComponent"
)
@Messages({
    "CTL_TypeScriptConsoleAction=TypeScript Console",
    "CTL_TypeScriptConsoleTopComponent=TypeScript Console",
    "HINT_TypeScriptConsoleTopComponent=Interactive TypeScript console (REPL)"
})
public final class TypeScriptConsoleTopComponent extends AbstractConsoleTopComponent {

    public TypeScriptConsoleTopComponent() {
        super();
        setName(Bundle.CTL_TypeScriptConsoleTopComponent());
        setToolTipText(Bundle.HINT_TypeScriptConsoleTopComponent());
    }

    @Override
    protected String getReplCommand() {
        return "ts-node";
    }

    @Override
    protected String[] getReplArgs() {
        return new String[]{};
    }

    @Override
    protected String getConsoleName() {
        return "TypeScript Console";
    }

    @Override
    protected String getIconPath() {
        return "org/flossware/netbeans/typescript/resources/typescript-icon.png";
    }

    @Override
    protected String getStartupMessage() {
        return "Type TypeScript code and press Enter\n" +
               "NOTE: Requires ts-node to be installed (npm install -g ts-node)\n\n";
    }
}
