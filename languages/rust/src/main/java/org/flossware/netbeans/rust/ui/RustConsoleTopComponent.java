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

package org.flossware.netbeans.rust.ui;

import org.flossware.netbeans.common.ui.AbstractConsoleTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 * Rust Interactive Console (REPL).
 *
 * <p>Provides a Rust interactive shell within NetBeans.</p>
 *
 * <p>This class extends {@link AbstractConsoleTopComponent} which provides
 * common REPL functionality including process management, I/O handling,
 * and UI components.</p>
 */
@TopComponent.Description(
    preferredID = "RustConsoleTopComponent",
    iconBase = "org/flossware/netbeans/rust/resources/rust-icon.png",
    persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "output", openAtStartup = false)
@ActionID(category = "Window", id = "org.flossware.netbeans.rust.ui.RustConsoleTopComponent")
@ActionReference(path = "Menu/Window", position = 334)
@TopComponent.OpenActionRegistration(
    displayName = "#CTL_RustConsoleAction",
    preferredID = "RustConsoleTopComponent"
)
@Messages({
    "CTL_RustConsoleAction=Rust Console",
    "CTL_RustConsoleTopComponent=Rust Console",
    "HINT_RustConsoleTopComponent=Interactive Rust console (REPL)"
})
public final class RustConsoleTopComponent extends AbstractConsoleTopComponent {

    public RustConsoleTopComponent() {
        super();
        setName(Bundle.CTL_RustConsoleTopComponent());
        setToolTipText(Bundle.HINT_RustConsoleTopComponent());
    }

    @Override
    protected String getReplCommand() {
        return "evcxr";
    }

    @Override
    protected String[] getReplArgs() {
        return new String[0];
    }

    @Override
    protected String getConsoleName() {
        return "Rust Console";
    }

    @Override
    protected String getIconPath() {
        return "org/flossware/netbeans/rust/resources/rust-icon.png";
    }

    @Override
    protected String getStartupMessage() {
        return "Type Rust code and press Enter\n(requires evcxr REPL: cargo install evcxr_repl)\n\n";
    }
}
