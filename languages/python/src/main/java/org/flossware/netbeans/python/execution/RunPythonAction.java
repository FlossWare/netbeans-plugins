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
