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

package org.flossware.netbeans.erlang.debugger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import org.flossware.netbeans.erlang.settings.ErlangSettings;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 * Manages Erlang debugging sessions
 */
public class ErlangDebuggerSession {

    private final File scriptFile;
    private Process debugProcess;

    public ErlangDebuggerSession(File scriptFile) {
        this.scriptFile = scriptFile;
    }

    /**
     * Start debug session
     */
    public void start() {
        try {
            String erlPath = ErlangSettings.getInstance().getErlPath();
            if (erlPath == null || erlPath.isEmpty()) {
                erlPath = "erl";
            }

            List<String> command = new ArrayList<>();
            command.add(erlPath);
            command.add("-noshell");
            command.add("-s");
            command.add("debugger");
            command.add("start");

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(scriptFile.getParentFile());
            pb.redirectErrorStream(true);

            InputOutput io = IOProvider.getDefault().getIO("Erlang Debugger: " + scriptFile.getName(), false);
            io.select();

            io.getOut().println("=".repeat(70));
            io.getOut().println("ERLANG DEBUGGER");
            io.getOut().println("=".repeat(70));
            io.getOut().println();
            io.getOut().println("Script: " + scriptFile.getAbsolutePath());
            io.getOut().println("Command: " + String.join(" ", command));
            io.getOut().println();
            io.getOut().println("NOTE: Erlang Debugger GUI will open in a separate window.");
            io.getOut().println("Load your module and set breakpoints in the GUI.");
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
            io.getOut().println("Debug Session:");
            io.getOut().println("-".repeat(70));
            io.getOut().println("Starting Erlang debugger GUI...");

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
                    io.getOut().println("Debug session ended with exit code: " + exitCode);
                    io.getOut().println("=".repeat(70));
                } catch (InterruptedException e) {
                    io.getErr().println("Debug session interrupted");
                }
            }).start();

        } catch (IOException e) {
            InputOutput io = IOProvider.getDefault().getIO("Erlang Debugger Error", false);
            io.select();
            io.getErr().println("Failed to start debugger: " + e.getMessage());
            io.getErr().println();
            io.getErr().println("Make sure Erlang/OTP is installed and 'erl' is in PATH");
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
