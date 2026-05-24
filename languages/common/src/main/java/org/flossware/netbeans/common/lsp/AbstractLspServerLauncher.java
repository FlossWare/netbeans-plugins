/*
 * Copyright 2026 FlossWare.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.flossware.netbeans.common.lsp;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.project.Project;
import org.openide.filesystems.FileObject;
import org.openide.util.Utilities;

/**
 * Abstract base class for launching Language Server Protocol (LSP) servers.
 *
 * <p>This class provides common functionality for detecting and launching
 * LSP servers for different languages. It tries each server command in
 * priority order and returns the first available one.</p>
 *
 * <p>Subclasses must implement abstract methods to provide language-specific
 * configuration:</p>
 * <ul>
 *   <li>{@link #getServerCommands()} - Return server commands in priority order</li>
 *   <li>{@link #getLaunchArgs(String)} - Return arguments for launching a specific server</li>
 *   <li>{@link #getInstallationInstructions()} - Return help text for installing servers</li>
 * </ul>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * public class ErlangLspServerLauncher extends AbstractLspServerLauncher {
 *     @Override
 *     protected String[] getServerCommands() {
 *         return new String[]{"erlang_ls", "elixir-ls"};
 *     }
 *
 *     @Override
 *     protected String[] getLaunchArgs(String serverCommand) {
 *         if ("erlang_ls".equals(serverCommand)) {
 *             return new String[]{"--transport", "stdio"};
 *         }
 *         return new String[0];
 *     }
 *
 *     @Override
 *     protected String getInstallationInstructions() {
 *         return "Install erlang_ls: https://erlang-ls.github.io/";
 *     }
 * }
 * }</pre>
 *
 * <p><b>Thread Safety:</b> This class is thread-safe. All methods can be
 * called concurrently.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public abstract class AbstractLspServerLauncher {

    private final Logger logger;

    /**
     * Create a new LSP server launcher.
     */
    protected AbstractLspServerLauncher() {
        this.logger = Logger.getLogger(getClass().getName());
    }

    /**
     * Get the command to launch the LSP server for a project.
     *
     * <p>Tries each server command in priority order and returns the first
     * available one. Returns null if no server is found.</p>
     *
     * @param project The current project (may be null)
     * @return Command array to start the LSP server, or null if no server found
     */
    public String[] getServerCommand(Project project) {
        String[] serverCommands = getServerCommands();
        if (serverCommands == null || serverCommands.length == 0) {
            logger.log(Level.WARNING, "No LSP server commands defined");
            return null;
        }

        for (String command : serverCommands) {
            if (command != null && isCommandAvailable(command)) {
                logger.log(Level.INFO, "Using LSP server: {0}", command);
                return buildCommandArray(command);
            }
        }

        logger.log(Level.WARNING, "No LSP server found. {0}",
            getInstallationInstructions());
        return null;
    }

    /**
     * Get the working directory for the LSP server.
     *
     * <p>Returns the project directory if available, otherwise null.</p>
     *
     * @param project The current project (may be null)
     * @return Working directory as File, or null if not applicable
     */
    public FileObject getWorkingDirectory(Project project) {
        if (project != null) {
            return project.getProjectDirectory();
        }
        return null;
    }

    /**
     * Get the server command names in priority order.
     *
     * <p>The first available command will be used. For example,
     * Python might return ["pyright-langserver", "pylsp"].</p>
     *
     * @return Array of server command names, or null/empty if none
     */
    protected abstract String[] getServerCommands();

    /**
     * Get the launch arguments for a specific server command.
     *
     * <p>Different servers may require different arguments. For example,
     * pyright needs "--stdio" while pylsp doesn't.</p>
     *
     * @param serverCommand The server command being launched
     * @return Array of arguments to pass to the command, or empty array
     */
    protected abstract String[] getLaunchArgs(String serverCommand);

    /**
     * Get installation instructions for missing language servers.
     *
     * <p>Should provide user-friendly instructions for installing at least
     * one LSP server for this language.</p>
     *
     * @return Installation instructions as a string
     */
    protected abstract String getInstallationInstructions();

    /**
     * Check if a command is available on the system PATH.
     *
     * <p>Uses "which" on Unix-like systems and "where" on Windows.</p>
     *
     * @param command The command to check
     * @return true if the command is found on PATH, false otherwise
     */
    protected boolean isCommandAvailable(String command) {
        if (command == null || command.isEmpty()) {
            return false;
        }

        try {
            String testCommand = Utilities.isWindows() ? "where" : "which";
            Process process = new ProcessBuilder(testCommand, command)
                .redirectErrorStream(true)
                .start();

            int exitCode = process.waitFor();
            return exitCode == 0;

        } catch (IOException | InterruptedException e) {
            logger.log(Level.FINE, "Error checking for command: " + command, e);
            return false;
        }
    }

    /**
     * Build the full command array including the command and its arguments.
     *
     * @param serverCommand The server command
     * @return Complete command array
     */
    private String[] buildCommandArray(String serverCommand) {
        String[] args = getLaunchArgs(serverCommand);
        if (args == null || args.length == 0) {
            return new String[]{serverCommand};
        }

        String[] commandArray = new String[args.length + 1];
        commandArray[0] = serverCommand;
        System.arraycopy(args, 0, commandArray, 1, args.length);
        return commandArray;
    }

    /**
     * Get the logger for this launcher.
     *
     * @return The logger instance
     */
    protected Logger getLogger() {
        return logger;
    }
}
