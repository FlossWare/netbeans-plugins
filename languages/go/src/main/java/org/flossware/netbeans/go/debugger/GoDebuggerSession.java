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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.flossware.netbeans.go.settings.GoSettings;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 * Manages Go debugging sessions using Delve (Debug Adapter Protocol)
 */
public class GoDebuggerSession {

    private final File scriptFile;
    private Process debugProcess;
    private static final int DEBUG_PORT = 2345;

    public GoDebuggerSession(File scriptFile) {
        this.scriptFile = scriptFile;
    }

    /**
     * Start debug session
     */
    public void start() {
        try {
            String goPath = GoSettings.getInstance().getGoPath();
            if (goPath == null || goPath.isEmpty()) {
                goPath = "dlv";
            } else {
                goPath = "dlv";
            }

            List<String> command = new ArrayList<>();
            command.add(goPath);
            command.add("debug");
            command.add("--headless");
            command.add("--listen=:" + DEBUG_PORT);
            command.add("--api-version=2");
            command.add(scriptFile.getAbsolutePath());

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(scriptFile.getParentFile());
            pb.redirectErrorStream(true);

            InputOutput io = IOProvider.getDefault().getIO("Go Debugger: " + scriptFile.getName(), false);
            io.select();

            io.getOut().println("Starting Go debugger...");
            io.getOut().println("Command: " + String.join(" ", command));
            io.getOut().println("Debug port: " + DEBUG_PORT);
            io.getOut().println();
            io.getOut().println("NOTE: Delve must be installed:");
            io.getOut().println("  go install github.com/go-delve/delve/cmd/dlv@latest");
            io.getOut().println();
            io.getOut().println("To attach debugger:");
            io.getOut().println("1. Install a DAP (Debug Adapter Protocol) client");
            io.getOut().println("2. Connect to localhost:" + DEBUG_PORT);
            io.getOut().println();
            io.getOut().println("Alternative: Use Delve CLI commands");
            io.getOut().println("  dlv connect localhost:" + DEBUG_PORT);
            io.getOut().println();
            io.getOut().println("Listening on port " + DEBUG_PORT + "...");
            io.getOut().println();

            debugProcess = pb.start();

            // Stream output
            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(debugProcess.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        io.getOut().println(line);
                    }
                } catch (IOException e) {
                    io.getErr().println("Error reading process output: " + e.getMessage());
                }
            }).start();

            // Wait for process
            new Thread(() -> {
                try {
                    int exitCode = debugProcess.waitFor();
                    io.getOut().println();
                    io.getOut().println("Debug session ended with exit code: " + exitCode);
                } catch (InterruptedException e) {
                    io.getErr().println("Debug session interrupted");
                }
            }).start();

        } catch (IOException e) {
            InputOutput io = IOProvider.getDefault().getIO("Go Debugger Error", false);
            io.select();
            io.getErr().println("Failed to start debugger: " + e.getMessage());
            io.getErr().println();
            io.getErr().println("Make sure Delve is installed:");
            io.getErr().println("  go install github.com/go-delve/delve/cmd/dlv@latest");
        }
    }

    /**
     * Stop debug session
     */
    public void stop() {
        if (debugProcess != null && debugProcess.isAlive()) {
            debugProcess.destroy();
        }
    }
}
