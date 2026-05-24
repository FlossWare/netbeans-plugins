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

package org.flossware.netbeans.mvel.debugger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import org.flossware.netbeans.mvel.settings.MvelSettings;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 * Manages MVEL debugging sessions with verbose logging
 * NOTE: MVEL does not have a native debugger, so this provides
 * enhanced logging and step-by-step output
 */
public class MvelDebuggerSession {

    private final File scriptFile;
    private Process debugProcess;

    public MvelDebuggerSession(File scriptFile) {
        this.scriptFile = scriptFile;
    }

    /**
     * Start debug session with verbose output
     */
    public void start() {
        try {
            String mvelJar = MvelSettings.getInstance().getMvelInterpreterPath();
            if (mvelJar == null || mvelJar.isEmpty()) {
                InputOutput io = IOProvider.getDefault().getIO("MVEL Debug Error", false);
                io.select();
                io.getErr().println("MVEL interpreter path not configured");
                io.getErr().println("Please configure in Tools > Options > MVEL");
                return;
            }

            List<String> command = new ArrayList<>();
            command.add("java");
            command.add("-jar");
            command.add(mvelJar);
            command.add(scriptFile.getAbsolutePath());

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(scriptFile.getParentFile());
            pb.redirectErrorStream(true);

            InputOutput io = IOProvider.getDefault().getIO("MVEL Debug: " + scriptFile.getName(), false);
            io.select();

            io.getOut().println("=".repeat(70));
            io.getOut().println("MVEL DEBUG MODE (Verbose Execution)");
            io.getOut().println("=".repeat(70));
            io.getOut().println();
            io.getOut().println("NOTE: MVEL does not have a native debugger.");
            io.getOut().println("This mode shows verbose output and script execution.");
            io.getOut().println();
            io.getOut().println("Script: " + scriptFile.getAbsolutePath());
            io.getOut().println("Command: " + String.join(" ", command));
            io.getOut().println();
            io.getOut().println("Script Content:");
            io.getOut().println("-".repeat(70));

            // Print script content with line numbers
            List<String> lines = Files.readAllLines(scriptFile.toPath());
            for (int i = 0; i < lines.size(); i++) {
                io.getOut().println(String.format("%4d: %s", i + 1, lines.get(i)));
            }

            io.getOut().println("-".repeat(70));
            io.getOut().println();
            io.getOut().println("Execution Output:");
            io.getOut().println("-".repeat(70));

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
                    io.getOut().println("-".repeat(70));
                    io.getOut().println("Script execution completed with exit code: " + exitCode);
                    io.getOut().println("=".repeat(70));
                } catch (InterruptedException e) {
                    io.getErr().println("Execution interrupted");
                }
            }).start();

        } catch (IOException e) {
            InputOutput io = IOProvider.getDefault().getIO("MVEL Debug Error", false);
            io.select();
            io.getErr().println("Failed to start debug session: " + e.getMessage());
            io.getErr().println();
            io.getErr().println("Make sure MVEL JAR is configured in Tools > Options > MVEL");
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
