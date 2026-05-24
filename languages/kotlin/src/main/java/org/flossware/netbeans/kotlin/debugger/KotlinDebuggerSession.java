/*
 * Copyright 2026 FlossWare.
 */

package org.flossware.netbeans.kotlin.debugger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import org.flossware.netbeans.kotlin.settings.KotlinSettings;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

public class KotlinDebuggerSession {
    private final File scriptFile;
    private Process debugProcess;

    public KotlinDebuggerSession(File scriptFile) {
        this.scriptFile = scriptFile;
    }

    public void start() {
        try {
            String kotlincPath = KotlinSettings.getInstance().getKotlincPath();
            if (kotlincPath == null || kotlincPath.isEmpty()) {
                kotlincPath = "kotlinc";
            }

            List<String> command = new ArrayList<>();
            command.add(kotlincPath);
            command.add("-script");
            command.add(scriptFile.getAbsolutePath());

            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(scriptFile.getParentFile());
            pb.redirectErrorStream(true);

            InputOutput io = IOProvider.getDefault().getIO("Kotlin: " + scriptFile.getName(), false);
            io.select();

            io.getOut().println("=".repeat(70));
            io.getOut().println("KOTLIN SCRIPT");
            io.getOut().println("=".repeat(70));
            io.getOut().println();
            io.getOut().println("Script: " + scriptFile.getAbsolutePath());
            io.getOut().println();
            io.getOut().println("Script Content:");
            io.getOut().println("-".repeat(70));

            List<String> lines = Files.readAllLines(scriptFile.toPath());
            for (int i = 0; i < lines.size(); i++) {
                io.getOut().println(String.format("%4d: %s", i + 1, lines.get(i)));
            }

            io.getOut().println("-".repeat(70));
            io.getOut().println();

            debugProcess = pb.start();

            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(debugProcess.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        io.getOut().println(line);
                    }
                } catch (IOException e) {
                    io.getErr().println("Error: " + e.getMessage());
                }
            }).start();

            new Thread(() -> {
                try {
                    int exitCode = debugProcess.waitFor();
                    io.getOut().println();
                    io.getOut().println("Ended with exit code: " + exitCode);
                } catch (InterruptedException e) {
                    io.getErr().println("Interrupted");
                }
            }).start();

        } catch (IOException e) {
            InputOutput io = IOProvider.getDefault().getIO("Kotlin Error", false);
            io.select();
            io.getErr().println("Failed to start: " + e.getMessage());
            io.getErr().println("Make sure Kotlin compiler is installed");
        }
    }

    public void stop() {
        if (debugProcess != null && debugProcess.isAlive()) {
            debugProcess.destroy();
        }
    }
}
