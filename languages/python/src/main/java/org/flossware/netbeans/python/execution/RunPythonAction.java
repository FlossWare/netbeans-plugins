/*
 * Copyright 2026 FlossWare.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.flossware.netbeans.python.execution;

import java.io.File;
import org.flossware.netbeans.common.execution.AbstractRunAction;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.loaders.DataObject;
import org.openide.util.NbBundle.Messages;

/**
 * Action to run a Python script.
 *
 * <p>Executes the currently selected .py file using the system Python interpreter.</p>
 *
 * <p>This class extends {@link AbstractRunAction} which provides common
 * script execution logic including process management, I/O handling, and
 * error reporting.</p>
 */
@ActionID(
    category = "Python",
    id = "org.flossware.netbeans.python.execution.RunPythonAction"
)
@ActionRegistration(
    displayName = "#CTL_RunPythonAction"
)
@ActionReferences({
    @ActionReference(path = "Loaders/text/x-python/Actions", position = 100),
    @ActionReference(path = "Editors/text/x-python/Popup", position = 100)
})
@Messages("CTL_RunPythonAction=Run Python Script")
public final class RunPythonAction extends AbstractRunAction {

    public RunPythonAction(DataObject context) {
        super(context);
    }

    @Override
    protected String getInterpreterCommand() {
        return "python";
    }

    @Override
    protected String[] getInterpreterArgs(File file) {
        return new String[]{file.getAbsolutePath()};
    }

    @Override
    protected String getFileExtension() {
        return "py";
    }

    @Override
    protected String getLanguageName() {
        return "Python";
    }
}
