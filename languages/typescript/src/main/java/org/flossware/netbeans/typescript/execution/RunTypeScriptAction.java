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

package org.flossware.netbeans.typescript.execution;

import java.io.File;
import org.flossware.netbeans.common.execution.AbstractRunAction;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.loaders.DataObject;
import org.openide.util.NbBundle.Messages;

/**
 * Action to run a TypeScript script.
 *
 * <p>Executes the currently selected .ts file using ts-node or compiles and runs with node.</p>
 *
 * <p>This class extends {@link AbstractRunAction} which provides common
 * script execution logic including process management, I/O handling, and
 * error reporting.</p>
 */
@ActionID(
    category = "TypeScript",
    id = "org.flossware.netbeans.typescript.execution.RunTypeScriptAction"
)
@ActionRegistration(
    displayName = "#CTL_RunTypeScriptAction"
)
@ActionReferences({
    @ActionReference(path = "Loaders/text/x-typescript/Actions", position = 100),
    @ActionReference(path = "Editors/text/x-typescript/Popup", position = 100)
})
@Messages("CTL_RunTypeScriptAction=Run TypeScript Script")
public final class RunTypeScriptAction extends AbstractRunAction {

    public RunTypeScriptAction(DataObject context) {
        super(context);
    }

    @Override
    protected String getInterpreterCommand() {
        // Try ts-node first, fall back to tsc + node
        return "ts-node";
    }

    @Override
    protected String[] getInterpreterArgs(File file) {
        return new String[]{file.getAbsolutePath()};
    }

    @Override
    protected String getFileExtension() {
        return "ts";
    }

    @Override
    protected String getLanguageName() {
        return "TypeScript";
    }
}
