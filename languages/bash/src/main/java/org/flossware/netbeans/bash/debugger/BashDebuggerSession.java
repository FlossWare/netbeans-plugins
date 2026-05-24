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

package org.flossware.netbeans.bash.debugger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import org.flossware.netbeans.bash.settings.BashSettings;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 * Manages Bash debugging sessions using bashdb
 */
public class BashDebuggerSession {

    private final File scriptFile;
    private Process debugProcess;

    public BashDebuggerSession(File scriptFile) {
        this.scriptFile = scriptFile;
    }

    /**
     * Start debug session
     */
    public void start() {
        try {
            String bashdbPath = BashSettings.getInstance().getBashdbPath();
            if (bashdbPath == null || bashdbPath.isEmpty()) {
                bashdbPath = "bashdb";
            }

            List<String> command = new ArrayList<>();
            command.add(bashdbPath);
            command.add(scriptFile.getAbsolutePath());

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(scriptFile.getParentFile());
            pb.redirectErrorStream(true);

            InputOutput io = IOProvider.getDefault().getIO("Bash Debugger: " + scriptFile.getName(), false);
            io.select();

            io.getOut().println("=".repeat(70));
            io.getOut().println("BASH DEBUGGER (bashdb)");
            io.getOut().println("=".repeat(70));
            io.getOut().println();
            io.getOut().println("Script: " + scriptFile.getAbsolutePath());
            io.getOut().println("Command: " + String.join(" ", command));
            io.getOut().println();
            io.getOut().println("NOTE: bashdb must be installed for debugging.");
            io.getOut().println("Install with: sudo apt-get install bashdb (Debian/Ubuntu)");
            io.getOut().println("            or sudo yum install bashdb (RHEL/CentOS/Fedora)");
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
            InputOutput io = IOProvider.getDefault().getIO("Bash Debugger Error", false);
            io.select();
            io.getErr().println("Failed to start debugger: " + e.getMessage());
            io.getErr().println();
            io.getErr().println("Make sure bashdb is installed:");
            io.getErr().println("  sudo apt-get install bashdb (Debian/Ubuntu)");
            io.getErr().println("  sudo yum install bashdb (RHEL/CentOS/Fedora)");
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
