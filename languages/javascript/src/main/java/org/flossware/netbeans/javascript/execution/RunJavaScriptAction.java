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

package org.flossware.netbeans.javascript.execution;

import java.io.File;
import org.flossware.netbeans.common.execution.AbstractRunAction;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.loaders.DataObject;
import org.openide.util.NbBundle.Messages;

/**
 * Action to run a JavaScript script.
 *
 * <p>Executes the currently selected .js file using Node.js.</p>
 *
 * <p>This class extends {@link AbstractRunAction} which provides common
 * script execution logic including process management, I/O handling, and
 * error reporting.</p>
 */
@ActionID(
    category = "JavaScript",
    id = "org.flossware.netbeans.javascript.execution.RunJavaScriptAction"
)
@ActionRegistration(
    displayName = "#CTL_RunJavaScriptAction"
)
@ActionReferences({
    @ActionReference(path = "Loaders/text/x-javascript/Actions", position = 100),
    @ActionReference(path = "Editors/text/x-javascript/Popup", position = 100)
})
@Messages("CTL_RunJavaScriptAction=Run JavaScript Script")
public final class RunJavaScriptAction extends AbstractRunAction {

    public RunJavaScriptAction(DataObject context) {
        super(context);
    }

    @Override
    protected String getInterpreterCommand() {
        return "node";
    }

    @Override
    protected String[] getInterpreterArgs(File file) {
        return new String[]{file.getAbsolutePath()};
    }

    @Override
    protected String getFileExtension() {
        return "js";
    }

    @Override
    protected String getLanguageName() {
        return "JavaScript";
    }
}
