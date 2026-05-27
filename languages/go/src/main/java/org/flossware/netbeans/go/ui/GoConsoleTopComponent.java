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

import org.flossware.netbeans.common.ui.AbstractConsoleTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 * Go Interactive Console (REPL).
 *
 * <p>Provides a Go interactive shell within NetBeans.</p>
 *
 * <p>This class extends {@link AbstractConsoleTopComponent} which provides
 * common REPL functionality including process management, I/O handling,
 * and UI components.</p>
 */
@TopComponent.Description(
    preferredID = "GoConsoleTopComponent",
    iconBase = "org/flossware/netbeans/go/resources/go-icon.png",
    persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "output", openAtStartup = false)
@ActionID(category = "Window", id = "org.flossware.netbeans.go.ui.GoConsoleTopComponent")
@ActionReference(path = "Menu/Window", position = 334)
@TopComponent.OpenActionRegistration(
    displayName = "#CTL_GoConsoleAction",
    preferredID = "GoConsoleTopComponent"
)
@Messages({
    "CTL_GoConsoleAction=Go Console",
    "CTL_GoConsoleTopComponent=Go Console",
    "HINT_GoConsoleTopComponent=Interactive Go console (REPL)"
})
public final class GoConsoleTopComponent extends AbstractConsoleTopComponent {

    public GoConsoleTopComponent() {
        super();
        setName(Bundle.CTL_GoConsoleTopComponent());
        setToolTipText(Bundle.HINT_GoConsoleTopComponent());
    }

    @Override
    protected String getReplCommand() {
        return "gore";
    }

    @Override
    protected String[] getReplArgs() {
        return new String[0];
    }

    @Override
    protected String getConsoleName() {
        return "Go Console";
    }

    @Override
    protected String getIconPath() {
        return "org/flossware/netbeans/go/resources/go-icon.png";
    }

    @Override
    protected String getStartupMessage() {
        return "Type Go code and press Enter\nNOTE: Requires 'gore' REPL: go install github.com/x-motemen/gore/cmd/gore@latest\n\n";
    }
}
