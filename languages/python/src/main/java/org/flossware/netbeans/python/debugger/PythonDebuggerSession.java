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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.flossware.netbeans.common.debugger.AbstractDebuggerSession;
import org.flossware.netbeans.python.settings.PythonSettings;
import org.openide.windows.InputOutput;

/**
 * Manages Python debugging sessions using debugpy (Debug Adapter Protocol).
 *
 * <p>This class extends {@link AbstractDebuggerSession} which provides proper
 * resource management including:</p>
 * <ul>
 *   <li>Process lifecycle management</li>
 *   <li>Thread pool for background tasks</li>
 *   <li>Proper I/O stream cleanup</li>
 *   <li>Shutdown hooks</li>
 * </ul>
 *
 * <p><b>Prerequisites:</b></p>
 * <pre>
 * pip install debugpy
 * </pre>
 *
 * @author FlossWare
 * @version 2.0
 * @since 1.0
 */
public class PythonDebuggerSession extends AbstractDebuggerSession {

    private static final int DEBUG_PORT = 5678;

    /**
     * Create a new Python debugger session.
     *
     * @param scriptFile The Python script to debug
     */
    public PythonDebuggerSession(File scriptFile) {
        super(scriptFile, "Python Debugger");
    }

    /**
     * Create the ProcessBuilder for debugpy.
     *
     * @return ProcessBuilder configured for Python debugging
     * @throws IOException If process configuration fails
     */
    @Override
    protected ProcessBuilder createProcessBuilder() throws IOException {
        String pythonPath = PythonSettings.getInstance().getPythonPath();
        if (pythonPath == null || pythonPath.isEmpty()) {
            pythonPath = "python3";
        }

        List<String> command = new ArrayList<>();
        command.add(pythonPath);
        command.add("-m");
        command.add("debugpy");
        command.add("--listen");
        command.add("0.0.0.0:" + DEBUG_PORT);
        command.add("--wait-for-client");
        command.add(getScriptFile().getAbsolutePath());

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(getScriptFile().getParentFile());
        pb.redirectErrorStream(true);

        return pb;
    }

    /**
     * Print Python-specific debugger instructions.
     *
     * @param io The I/O window
     */
    @Override
    protected void printStartupInstructions(InputOutput io) {
        io.getOut().println("Debug port: " + DEBUG_PORT);
        io.getOut().println();
        io.getOut().println("To attach debugger:");
        io.getOut().println("1. Install a DAP (Debug Adapter Protocol) client");
        io.getOut().println("2. Connect to localhost:" + DEBUG_PORT);
        io.getOut().println();
        io.getOut().println("Alternative: Use Python debugger in your IDE");
        io.getOut().println("  Host: localhost");
        io.getOut().println("  Port: " + DEBUG_PORT);
        io.getOut().println();
        io.getOut().println("Waiting for debugger to attach on port " + DEBUG_PORT + "...");
    }

    /**
     * Handle debugpy startup errors.
     *
     * @param io The I/O window
     * @param e The exception
     */
    @Override
    protected void handleStartupError(InputOutput io, IOException e) {
        io.getErr().println();
        io.getErr().println("Make sure debugpy is installed:");
        io.getErr().println("  pip install debugpy");
    }
}
