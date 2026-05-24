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

package org.flossware.netbeans.common.execution;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;
import org.netbeans.api.extexecution.ExecutionDescriptor;
import org.netbeans.api.extexecution.ExecutionService;
import org.netbeans.api.extexecution.ExternalProcessBuilder;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 * Abstract base class for actions that run/execute language scripts.
 *
 * <p>This class provides common functionality for executing language files
 * (scripts, modules) using an interpreter. It handles:</p>
 * <ul>
 *   <li>File extension validation</li>
 *   <li>Process execution using ExternalProcessBuilder</li>
 *   <li>I/O redirection to NetBeans output window</li>
 *   <li>Working directory setup</li>
 *   <li>Error handling and user feedback</li>
 * </ul>
 *
 * <p>Subclasses must implement abstract methods to provide language-specific
 * configuration:</p>
 * <ul>
 *   <li>{@link #getInterpreterCommand()} - Return interpreter executable name</li>
 *   <li>{@link #getInterpreterArgs(File)} - Return arguments for executing file</li>
 *   <li>{@link #getFileExtension()} - Return file extension to validate</li>
 *   <li>{@link #getLanguageName()} - Return language name for error messages</li>
 * </ul>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * @ActionID(category = "Erlang", id = "org.flossware.netbeans.erlang.execution.RunErlangAction")
 * @ActionRegistration(displayName = "#CTL_RunErlangAction")
 * @ActionReferences({
 *     @ActionReference(path = "Loaders/text/x-erlang/Actions", position = 100)
 * })
 * @Messages("CTL_RunErlangAction=Run Erlang Module")
 * public final class RunErlangAction extends AbstractRunAction {
 *
 *     public RunErlangAction(DataObject context) {
 *         super(context);
 *     }
 *
 *     @Override
 *     protected String getInterpreterCommand() {
 *         return "escript";
 *     }
 *
 *     @Override
 *     protected String[] getInterpreterArgs(File file) {
 *         return new String[]{file.getAbsolutePath()};
 *     }
 *
 *     @Override
 *     protected String getFileExtension() {
 *         return "erl";
 *     }
 *
 *     @Override
 *     protected String getLanguageName() {
 *         return "Erlang";
 *     }
 * }
 * }</pre>
 *
 * <p><b>Thread Safety:</b> This class is thread-safe. Action methods are
 * typically called from the EDT.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public abstract class AbstractRunAction implements ActionListener {

    private final DataObject context;

    /**
     * Create a new run action for the given context.
     *
     * @param context The DataObject context (file being executed)
     */
    protected AbstractRunAction(DataObject context) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null");
        }
        this.context = context;
    }

    /**
     * Called when the action is performed.
     *
     * <p>Validates the file extension and executes the file if valid.</p>
     *
     * @param ev The action event
     */
    @Override
    public void actionPerformed(ActionEvent ev) {
        FileObject fileObject = context.getPrimaryFile();

        if (!isValidFileExtension(fileObject)) {
            showError("Not a " + getLanguageName() + " file");
            return;
        }

        try {
            runScript(fileObject);
        } catch (IOException ex) {
            showError("Error running " + getLanguageName() + " script: " + ex.getMessage());
        }
    }

    /**
     * Execute a script file using the interpreter.
     *
     * @param scriptFile The file to execute
     * @throws IOException If an error occurs during execution
     */
    protected void runScript(FileObject scriptFile) throws IOException {
        File file = FileUtil.toFile(scriptFile);
        if (file == null) {
            throw new IOException("Cannot convert FileObject to File");
        }

        File workingDir = getWorkingDirectory(file);
        String[] commandArray = buildCommandArray(file);

        ExternalProcessBuilder processBuilder = new ExternalProcessBuilder(commandArray[0]);
        for (int i = 1; i < commandArray.length; i++) {
            processBuilder.addArgument(commandArray[i]);
        }
        processBuilder.workingDirectory(workingDir);

        String displayName = getDisplayName(scriptFile);
        ExecutionDescriptor descriptor = createExecutionDescriptor();

        ExecutionService service = ExecutionService.newService(
            processBuilder,
            descriptor,
            displayName
        );

        service.run();
    }

    /**
     * Get the interpreter command name.
     *
     * <p>This is the executable name on the PATH (e.g., "python", "escript", "sbcl").</p>
     *
     * @return The interpreter command name
     */
    protected abstract String getInterpreterCommand();

    /**
     * Get the arguments to pass to the interpreter.
     *
     * <p>Typically includes the file path and any language-specific flags.</p>
     *
     * @param file The file being executed
     * @return Array of arguments
     */
    protected abstract String[] getInterpreterArgs(File file);

    /**
     * Get the file extension for validation.
     *
     * <p>Should be the extension without the dot (e.g., "erl", "py", "lisp").</p>
     *
     * @return The file extension
     */
    protected abstract String getFileExtension();

    /**
     * Get the language name for error messages.
     *
     * @return The language name (e.g., "Erlang", "Python", "Lisp")
     */
    protected abstract String getLanguageName();

    /**
     * Check if the file has a valid extension.
     *
     * @param fileObject The file to check
     * @return true if the extension matches
     */
    protected boolean isValidFileExtension(FileObject fileObject) {
        if (fileObject == null) {
            return false;
        }
        String expectedExt = getFileExtension();
        return expectedExt != null && expectedExt.equals(fileObject.getExt());
    }

    /**
     * Get the working directory for execution.
     *
     * <p>Default implementation returns the file's parent directory.
     * Subclasses can override for custom behavior.</p>
     *
     * @param file The file being executed
     * @return The working directory
     */
    protected File getWorkingDirectory(File file) {
        return file.getParentFile();
    }

    /**
     * Build the command array for execution.
     *
     * @param file The file being executed
     * @return Command array (command + arguments)
     */
    protected String[] buildCommandArray(File file) {
        String command = getInterpreterCommand();
        String[] args = getInterpreterArgs(file);

        String[] commandArray = new String[args.length + 1];
        commandArray[0] = command;
        System.arraycopy(args, 0, commandArray, 1, args.length);

        return commandArray;
    }

    /**
     * Get the display name for the execution window.
     *
     * <p>Default implementation uses "LanguageName - filename".</p>
     *
     * @param scriptFile The file being executed
     * @return Display name
     */
    protected String getDisplayName(FileObject scriptFile) {
        return getLanguageName() + " - " + scriptFile.getName();
    }

    /**
     * Create the execution descriptor for the process.
     *
     * <p>Configures I/O, progress, and window behavior.</p>
     *
     * @return Execution descriptor
     */
    protected ExecutionDescriptor createExecutionDescriptor() {
        return new ExecutionDescriptor()
            .frontWindow(true)
            .frontWindowOnError(true)
            .inputVisible(true)
            .controllable(true)
            .showProgress(true);
    }

    /**
     * Show an error dialog.
     *
     * @param message The error message
     */
    protected void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Get the DataObject context.
     *
     * @return The context
     */
    protected DataObject getContext() {
        return context;
    }
}
