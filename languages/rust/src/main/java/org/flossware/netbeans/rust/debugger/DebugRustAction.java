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

package org.flossware.netbeans.rust.debugger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

/**
 * Action to debug a Rust program.
 */
@ActionID(
    category = "Debug",
    id = "org.flossware.netbeans.rust.debugger.DebugRustAction"
)
@ActionRegistration(
    displayName = "#CTL_DebugRustAction"
)
@ActionReference(path = "Menu/Debug", position = 100)
@Messages("CTL_DebugRustAction=Debug Rust Program")
public final class DebugRustAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO: Implement Rust debugging support
    }

    public static DebugRustAction create() {
        return new DebugRustAction();
    }
}
