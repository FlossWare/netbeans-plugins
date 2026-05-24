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

package org.flossware.netbeans.groovy.debugger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.flossware.netbeans.groovy.settings.GroovySettings;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 * Manages Groovy debugging sessions using JDWP
 */
public class GroovyDebuggerSession {

    private final File scriptFile;
    private Process debugProcess;
    private static final int DEBUG_PORT = 5005;

    public GroovyDebuggerSession(File scriptFile) {
        this.scriptFile = scriptFile;
    }

    /**
     * Start debug session
     */
    public void start() {
        try {
            String groovyPath = GroovySettings.getInstance().getGroovyPath();
            if (groovyPath == null || groovyPath.isEmpty()) {
                groovyPath = "groovy";
            }

            List<String> command = new ArrayList<>();
            command.add(groovyPath);

            // Add JDWP debug agent
            command.add("-Dagentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=" + DEBUG_PORT);

            command.add(scriptFile.getAbsolutePath());

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(scriptFile.getParentFile());
            pb.redirectErrorStream(true);

            InputOutput io = IOProvider.getDefault().getIO("Groovy Debugger: " + scriptFile.getName(), false);
            io.select();

            io.getOut().println("Starting Groovy debugger...");
            io.getOut().println("Command: " + String.join(" ", command));
            io.getOut().println("Debug port: " + DEBUG_PORT);
            io.getOut().println();
            io.getOut().println("To attach debugger:");
            io.getOut().println("1. Go to Debug > Attach Debugger");
            io.getOut().println("2. Connector: SocketAttach");
            io.getOut().println("3. Host: localhost");
            io.getOut().println("4. Port: " + DEBUG_PORT);
            io.getOut().println();
            io.getOut().println("Waiting for debugger to attach on port " + DEBUG_PORT + "...");
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
            InputOutput io = IOProvider.getDefault().getIO("Groovy Debugger Error", false);
            io.select();
            io.getErr().println("Failed to start debugger: " + e.getMessage());
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
