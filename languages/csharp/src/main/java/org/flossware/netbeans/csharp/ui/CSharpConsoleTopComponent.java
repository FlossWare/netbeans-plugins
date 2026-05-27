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

import org.flossware.netbeans.common.ui.AbstractConsoleTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 * C# Interactive Console (REPL).
 *
 * <p>Provides a C# interactive shell within NetBeans using dotnet-script.</p>
 *
 * <p>This class extends {@link AbstractConsoleTopComponent} which provides
 * common REPL functionality including process management, I/O handling,
 * and UI components.</p>
 */
@TopComponent.Description(
    preferredID = "CSharpConsoleTopComponent",
    iconBase = "org/flossware/netbeans/csharp/resources/csharp-icon.png",
    persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "output", openAtStartup = false)
@ActionID(category = "Window", id = "org.flossware.netbeans.csharp.ui.CSharpConsoleTopComponent")
@ActionReference(path = "Menu/Window", position = 333)
@TopComponent.OpenActionRegistration(
    displayName = "#CTL_CSharpConsoleAction",
    preferredID = "CSharpConsoleTopComponent"
)
@Messages({
    "CTL_CSharpConsoleAction=C# Console",
    "CTL_CSharpConsoleTopComponent=C# Console",
    "HINT_CSharpConsoleTopComponent=Interactive C# console (REPL)"
})
public final class CSharpConsoleTopComponent extends AbstractConsoleTopComponent {

    public CSharpConsoleTopComponent() {
        super();
        setName(Bundle.CTL_CSharpConsoleTopComponent());
        setToolTipText(Bundle.HINT_CSharpConsoleTopComponent());
    }

    @Override
    protected String getReplCommand() {
        return "dotnet";
    }

    @Override
    protected String[] getReplArgs() {
        return new String[]{"script"};
    }

    @Override
    protected String getConsoleName() {
        return "C# Console";
    }

    @Override
    protected String getIconPath() {
        return "org/flossware/netbeans/csharp/resources/csharp-icon.png";
    }

    @Override
    protected String getStartupMessage() {
        return "Type C# code and press Enter\nRequires dotnet-script: dotnet tool install -g dotnet-script\n\n";
    }
}
