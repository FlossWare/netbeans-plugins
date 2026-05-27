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

package org.flossware.netbeans.common.debugger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 * Abstract base class for debugger sessions with proper resource management.
 *
 * <p>This class provides common functionality for debugger sessions including:</p>
 * <ul>
 *   <li>Process lifecycle management with graceful shutdown</li>
 *   <li>Thread pool for background tasks (output streaming, process waiting)</li>
 *   <li>Proper I/O stream cleanup with try-with-resources</li>
 *   <li>Shutdown hooks for cleanup on VM exit</li>
 *   <li>Timeout-based forced termination</li>
 * </ul>
 *
 * <p><b>Resource Safety:</b></p>
 * <ul>
 *   <li>All streams are properly closed using try-with-resources</li>
 *   <li>ExecutorService is shutdown with timeout</li>
 *   <li>Process is destroyed gracefully, then forcibly if needed</li>
 *   <li>Shutdown hook ensures cleanup even on abnormal termination</li>
 * </ul>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * public class PythonDebuggerSession extends AbstractDebuggerSession {
 *     public PythonDebuggerSession(File scriptFile) {
 *         super(scriptFile, "Python Debugger");
 *     }
 *
 *     @Override
 *     protected ProcessBuilder createProcessBuilder() {
 *         ProcessBuilder pb = new ProcessBuilder("python3", "-m", "debugpy",
 *             "--listen", "0.0.0.0:5678", "--wait-for-client",
 *             getScriptFile().getAbsolutePath());
 *         pb.directory(getScriptFile().getParentFile());
 *         pb.redirectErrorStream(true);
 *         return pb;
 *     }
 *
 *     @Override
 *     protected void printStartupInstructions(InputOutput io) {
 *         io.getOut().println("Debug port: 5678");
 *         io.getOut().println("To attach: connect DAP client to localhost:5678");
 *     }
 * }
 * }</pre>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public abstract class AbstractDebuggerSession {

    private final File scriptFile;
    private final String debuggerName;
    private Process debugProcess;
    private ExecutorService executorService;
    private InputOutput io;
    private Thread shutdownHook;

    private static final int SHUTDOWN_TIMEOUT_SECONDS = 5;

    /**
     * Create a new debugger session.
     *
     * @param scriptFile The script file to debug
     * @param debuggerName The name of the debugger (for display)
     */
    protected AbstractDebuggerSession(File scriptFile, String debuggerName) {
        if (scriptFile == null) {
            throw new IllegalArgumentException("scriptFile cannot be null");
        }
        if (debuggerName == null || debuggerName.trim().isEmpty()) {
            throw new IllegalArgumentException("debuggerName cannot be null or empty");
        }
        this.scriptFile = scriptFile;
        this.debuggerName = debuggerName;
    }

    /**
     * Create the ProcessBuilder for the debugger.
     *
     * <p>Subclasses must implement this to provide language-specific debug commands.</p>
     *
     * @return ProcessBuilder configured for debugging
     * @throws IOException If process configuration fails
     */
    protected abstract ProcessBuilder createProcessBuilder() throws IOException;

    /**
     * Print language-specific startup instructions to the I/O window.
     *
     * <p>Subclasses can override to provide custom instructions.</p>
     *
     * @param io The I/O window
     */
    protected void printStartupInstructions(InputOutput io) {
        // Default: no additional instructions
    }

    /**
     * Get the script file being debugged.
     *
     * @return The script file
     */
    protected File getScriptFile() {
        return scriptFile;
    }

    /**
     * Get the debugger name.
     *
     * @return The debugger name
     */
    protected String getDebuggerName() {
        return debuggerName;
    }

    /**
     * Start the debug session.
     *
     * <p>This method:</p>
     * <ol>
     *   <li>Creates the process using createProcessBuilder()</li>
     *   <li>Sets up I/O window</li>
     *   <li>Creates executor service for background tasks</li>
     *   <li>Registers shutdown hook</li>
     *   <li>Starts output streaming and process waiting threads</li>
     * </ol>
     */
    public void start() {
        try {
            ProcessBuilder pb = createProcessBuilder();

            io = IOProvider.getDefault().getIO(debuggerName + ": " + scriptFile.getName(), false);
            io.select();

            io.getOut().println("Starting " + debuggerName + "...");
            io.getOut().println("File: " + scriptFile.getAbsolutePath());
            io.getOut().println();

            // Print language-specific instructions
            printStartupInstructions(io);

            io.getOut().println();
            io.getOut().println("Starting debugger...");
            io.getOut().println();

            debugProcess = pb.start();

            // Create executor service for background tasks
            executorService = Executors.newFixedThreadPool(2);

            // Register shutdown hook for cleanup
            shutdownHook = new Thread(this::cleanup);
            Runtime.getRuntime().addShutdownHook(shutdownHook);

            // Stream output using executor (proper resource management)
            executorService.submit(() -> {
                try (InputStream inputStream = debugProcess.getInputStream();
                     InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                     BufferedReader reader = new BufferedReader(inputStreamReader)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        io.getOut().println(line);
                    }
                } catch (IOException e) {
                    io.getErr().println("Error reading process output: " + e.getMessage());
                }
            });

            // Wait for process using executor
            executorService.submit(() -> {
                try {
                    int exitCode = debugProcess.waitFor();
                    io.getOut().println();
                    io.getOut().println("Debug session ended with exit code: " + exitCode);
                    cleanup();
                } catch (InterruptedException e) {
                    io.getErr().println("Debug session interrupted");
                    Thread.currentThread().interrupt();
                }
            });

        } catch (IOException e) {
            InputOutput errorIO = IOProvider.getDefault().getIO(debuggerName + " Error", false);
            errorIO.select();
            errorIO.getErr().println("Failed to start debugger: " + e.getMessage());
            handleStartupError(errorIO, e);
        }
    }

    /**
     * Handle startup errors.
     *
     * <p>Subclasses can override to provide custom error messages.</p>
     *
     * @param io The I/O window
     * @param e The exception
     */
    protected void handleStartupError(InputOutput io, IOException e) {
        // Default: no additional error handling
    }

    /**
     * Stop the debug session.
     *
     * <p>Calls cleanup() to release all resources.</p>
     */
    public void stop() {
        cleanup();
    }

    /**
     * Clean up all resources.
     *
     * <p>This method is called from:</p>
     * <ul>
     *   <li>stop() - manual shutdown</li>
     *   <li>shutdown hook - VM exit</li>
     *   <li>process termination - normal completion</li>
     * </ul>
     *
     * <p>Cleanup steps:</p>
     * <ol>
     *   <li>Destroy process (graceful, then forceful if needed)</li>
     *   <li>Shutdown executor service with timeout</li>
     *   <li>Close I/O streams</li>
     *   <li>Remove shutdown hook if registered</li>
     * </ol>
     */
    private void cleanup() {
        // Destroy process
        if (debugProcess != null && debugProcess.isAlive()) {
            debugProcess.destroy();
            try {
                // Wait for process to terminate gracefully
                if (!debugProcess.waitFor(SHUTDOWN_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                    // Force termination if timeout exceeded
                    debugProcess.destroyForcibly();
                }
            } catch (InterruptedException e) {
                debugProcess.destroyForcibly();
                Thread.currentThread().interrupt();
            }
        }

        // Shutdown executor service
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(SHUTDOWN_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }

        // Close I/O
        if (io != null) {
            io.closeInputOutput();
        }

        // Remove shutdown hook if still registered
        if (shutdownHook != null) {
            try {
                Runtime.getRuntime().removeShutdownHook(shutdownHook);
            } catch (IllegalStateException e) {
                // Already shutting down, ignore
            }
            shutdownHook = null;
        }
    }

    /**
     * Check if the debug session is still active.
     *
     * @return true if the process is alive
     */
    public boolean isActive() {
        return debugProcess != null && debugProcess.isAlive();
    }
}
