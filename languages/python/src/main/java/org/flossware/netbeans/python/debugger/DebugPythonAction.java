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

package org.flossware.netbeans.python.debugger;

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
 * Action to debug Python scripts
 */
@ActionID(
        category = "Debug",
        id = "org.flossware.netbeans.python.debugger.DebugPythonAction"
)
@ActionRegistration(
        displayName = "#CTL_DebugPythonAction"
)
@ActionReference(path = "Loaders/text/x-python/Actions", position = 150)
@Messages("CTL_DebugPythonAction=Debug Python Script")
public final class DebugPythonAction implements ActionListener {

    private final DataObject context;

    public DebugPythonAction(DataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        FileObject fileObject = context.getPrimaryFile();
        File file = FileUtil.toFile(fileObject);

        if (file != null) {
            PythonDebuggerSession session = new PythonDebuggerSession(file);
            session.start();
        }
    }

    public static DebugPythonAction create(DataObject context) {
        return new DebugPythonAction(context);
    }
}
