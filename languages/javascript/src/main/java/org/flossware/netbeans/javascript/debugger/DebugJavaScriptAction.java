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

package org.flossware.netbeans.javascript.debugger;

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
 * Action to debug JavaScript scripts
 */
@ActionID(
        category = "Debug",
        id = "org.flossware.netbeans.javascript.debugger.DebugJavaScriptAction"
)
@ActionRegistration(
        displayName = "#CTL_DebugJavaScriptAction"
)
@ActionReference(path = "Loaders/text/x-javascript/Actions", position = 150)
@Messages("CTL_DebugJavaScriptAction=Debug JavaScript Script")
public final class DebugJavaScriptAction implements ActionListener {

    private final DataObject context;

    public DebugJavaScriptAction(DataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        FileObject fileObject = context.getPrimaryFile();
        File file = FileUtil.toFile(fileObject);

        if (file != null) {
            JavaScriptDebuggerSession session = new JavaScriptDebuggerSession(file);
            session.start();
        }
    }

    public static DebugJavaScriptAction create(DataObject context) {
        return new DebugJavaScriptAction(context);
    }
}
