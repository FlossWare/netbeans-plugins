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

package org.flossware.netbeans.go.debugger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.util.NbBundle.Messages;

/**
 * Action to debug Go programs
 */
@ActionID(
        category = "Debug",
        id = "org.flossware.netbeans.go.debugger.DebugGoAction"
)
@ActionRegistration(
        displayName = "#CTL_DebugGoAction"
)
@ActionReference(path = "Loaders/text/x-go/Actions", position = 150)
@Messages("CTL_DebugGoAction=Debug Go Program")
public final class DebugGoAction implements ActionListener {

    private final DataObject context;

    public DebugGoAction(DataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        FileObject fileObject = context.getPrimaryFile();
        File file = FileUtil.toFile(fileObject);

        if (file != null) {
            GoDebuggerSession session = new GoDebuggerSession(file);
            session.start();
        }
    }

    public static DebugGoAction create(DataObject context) {
        return new DebugGoAction(context);
    }
}
