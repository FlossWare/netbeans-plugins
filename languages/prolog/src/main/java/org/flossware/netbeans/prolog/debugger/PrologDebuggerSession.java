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

package org.flossware.netbeans.prolog.debugger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import org.flossware.netbeans.prolog.settings.PrologSettings;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 * Manages Prolog debugging sessions using SWI-Prolog
 */
public class PrologDebuggerSession {

    private final File scriptFile;
    private Process debugProcess;

    public PrologDebuggerSession(File scriptFile) {
        this.scriptFile = scriptFile;
    }

    /**
     * Start debug session
     */
    public void start() {
        try {
            String swiplPath = PrologSettings.getInstance().getSwiplPath();
            if (swiplPath == null || swiplPath.isEmpty()) {
                swiplPath = "swipl";
            }

            List<String> command = new ArrayList<>();
            command.add(swiplPath);
            command.add("-g");
            command.add("trace");
            command.add("-t");
            command.add("halt");
            command.add(scriptFile.getAbsolutePath());

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(scriptFile.getParentFile());
            pb.redirectErrorStream(true);

            InputOutput io = IOProvider.getDefault().getIO("Prolog Debugger: " + scriptFile.getName(), false);
            io.select();

            io.getOut().println("=".repeat(70));
            io.getOut().println("PROLOG DEBUGGER (SWI-Prolog trace mode)");
            io.getOut().println("=".repeat(70));
            io.getOut().println();
            io.getOut().println("Script: " + scriptFile.getAbsolutePath());
            io.getOut().println("Command: " + String.join(" ", command));
            io.getOut().println();
            io.getOut().println("Debug commands:");
            io.getOut().println("  c     - Creep (single step)");
            io.getOut().println("  l     - Leap (continue to next breakpoint)");
            io.getOut().println("  s     - Skip (skip current goal)");
            io.getOut().println("  r     - Retry current goal");
            io.getOut().println("  a     - Abort execution");
            io.getOut().println("  b     - Break to Prolog toplevel");
            io.getOut().println("  +     - Enable spy point");
            io.getOut().println("  -     - Disable spy point");
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
            InputOutput io = IOProvider.getDefault().getIO("Prolog Debugger Error", false);
            io.select();
            io.getErr().println("Failed to start debugger: " + e.getMessage());
            io.getErr().println();
            io.getErr().println("Make sure SWI-Prolog is installed and 'swipl' is in PATH");
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
