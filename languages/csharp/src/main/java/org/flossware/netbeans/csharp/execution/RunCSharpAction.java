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

package org.flossware.netbeans.csharp.execution;

import java.io.File;
import org.flossware.netbeans.common.execution.AbstractRunAction;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.loaders.DataObject;
import org.openide.util.NbBundle.Messages;

/**
 * Action to run a C# script.
 *
 * <p>Executes the currently selected .cs file using the dotnet CLI.</p>
 *
 * <p>This class extends {@link AbstractRunAction} which provides common
 * script execution logic including process management, I/O handling, and
 * error reporting.</p>
 */
@ActionID(
    category = "CSharp",
    id = "org.flossware.netbeans.csharp.execution.RunCSharpAction"
)
@ActionRegistration(
    displayName = "#CTL_RunCSharpAction"
)
@ActionReferences({
    @ActionReference(path = "Loaders/text/x-csharp/Actions", position = 100),
    @ActionReference(path = "Editors/text/x-csharp/Popup", position = 100)
})
@Messages("CTL_RunCSharpAction=Run C# Script")
public final class RunCSharpAction extends AbstractRunAction {

    public RunCSharpAction(DataObject context) {
        super(context);
    }

    @Override
    protected String getInterpreterCommand() {
        return "dotnet";
    }

    @Override
    protected String[] getInterpreterArgs(File file) {
        return new String[]{"script", file.getAbsolutePath()};
    }

    @Override
    protected String getFileExtension() {
        return "cs";
    }

    @Override
    protected String getLanguageName() {
        return "C#";
    }
}
