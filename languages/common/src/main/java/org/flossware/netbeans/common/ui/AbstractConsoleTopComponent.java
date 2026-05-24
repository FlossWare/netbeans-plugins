package org.flossware.netbeans.common.ui;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.openide.windows.TopComponent;

/**
 * Abstract base class for language console/REPL top components.
 *
 * <p>This class provides common functionality for interactive language consoles
 * (REPLs) within NetBeans. It handles:</p>
 * <ul>
 *   <li>Process lifecycle (start/stop REPL)</li>
 *   <li>I/O stream handling (input/output)</li>
 *   <li>UI components (text area, scroll pane)</li>
 *   <li>Thread management for output reading</li>
 * </ul>
 *
 * <p>Subclasses must implement abstract methods to provide language-specific
 * configuration:</p>
 * <ul>
 *   <li>{@link #getReplCommand()} - Return REPL executable name</li>
 *   <li>{@link #getReplArgs()} - Return arguments for launching REPL</li>
 *   <li>{@link #getConsoleName()} - Return console window name</li>
 *   <li>{@link #getIconPath()} - Return icon resource path</li>
 * </ul>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * @TopComponent.Description(
 *     preferredID = "ErlangConsoleTopComponent",
 *     iconBase = "org/flossware/netbeans/erlang/resources/erlang-icon.png",
 *     persistenceType = TopComponent.PERSISTENCE_ALWAYS
 * )
 * @TopComponent.Registration(mode = "output", openAtStartup = false)
 * @ActionID(category = "Window", id = "org.flossware.netbeans.erlang.ui.ErlangConsoleTopComponent")
 * @ActionReference(path = "Menu/Window", position = 333)
 * @TopComponent.OpenActionRegistration(
 *     displayName = "#CTL_ErlangConsoleAction",
 *     preferredID = "ErlangConsoleTopComponent"
 * )
 * @Messages({
 *     "CTL_ErlangConsoleAction=Erlang Console",
 *     "CTL_ErlangConsoleTopComponent=Erlang Console",
 *     "HINT_ErlangConsoleTopComponent=Interactive Erlang console (REPL)"
 * })
 * public final class ErlangConsoleTopComponent extends AbstractConsoleTopComponent {
 *
 *     @Override
 *     protected String getReplCommand() {
 *         return "erl";
 *     }
 *
 *     @Override
 *     protected String[] getReplArgs() {
 *         return new String[0]; // erl doesn't need special args
 *     }
 *
 *     @Override
 *     protected String getConsoleName() {
 *         return "Erlang Console";
 *     }
 *
 *     @Override
 *     protected String getIconPath() {
 *         return "org/flossware/netbeans/erlang/resources/erlang-icon.png";
 *     }
 * }
 * }</pre>
 *
 * <p><b>Thread Safety:</b> This class is not thread-safe. It should only be
 * accessed from the EDT, except for the output reading thread which is managed
 * internally.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public abstract class AbstractConsoleTopComponent extends TopComponent {

    private JTextArea consoleArea;
    private Process replProcess;
    private OutputStream replInput;
    private Thread outputThread;

    /**
     * Create a new console top component.
     *
     * <p>Initializes the UI components.</p>
     */
    protected AbstractConsoleTopComponent() {
        initComponents();
    }

    /**
     * Initialize UI components.
     */
    private void initComponents() {
        setLayout(new BorderLayout());

        consoleArea = new JTextArea();
        consoleArea.setEditable(false);
        consoleArea.setFont(new java.awt.Font("Monospaced", 0, 12));

        JScrollPane scrollPane = new JScrollPane(consoleArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Called when the component is opened.
     *
     * <p>Starts the REPL process.</p>
     */
    @Override
    public void componentOpened() {
        startRepl();
    }

    /**
     * Called when the component is closed.
     *
     * <p>Stops the REPL process.</p>
     */
    @Override
    public void componentClosed() {
        stopRepl();
    }

    /**
     * Get the REPL command name.
     *
     * <p>This is the executable name on the PATH (e.g., "python", "erl", "sbcl").</p>
     *
     * @return The REPL command name
     */
    protected abstract String getReplCommand();

    /**
     * Get the arguments for launching the REPL.
     *
     * <p>For example, Python uses "-i" for interactive mode.</p>
     *
     * @return Array of arguments, or empty array if none
     */
    protected abstract String[] getReplArgs();

    /**
     * Get the console name for display.
     *
     * @return The console name (e.g., "Erlang Console", "Python Console")
     */
    protected abstract String getConsoleName();

    /**
     * Get the path to the icon resource.
     *
     * <p>Should be a path relative to the classpath.</p>
     *
     * @return The icon resource path
     */
    protected abstract String getIconPath();

    /**
     * Start the REPL process.
     */
    protected void startRepl() {
        try {
            String[] commandArray = buildCommandArray();
            ProcessBuilder pb = new ProcessBuilder(commandArray);
            pb.redirectErrorStream(true);

            replProcess = pb.start();
            replInput = replProcess.getOutputStream();

            startOutputThread();

            appendToConsole(getConsoleName() + " started\n");
            appendToConsole(getStartupMessage());

        } catch (IOException ex) {
            appendToConsole("Error starting " + getConsoleName() + ": " + ex.getMessage() + "\n");
            appendToConsole("Make sure " + getReplCommand() + " is installed and on your PATH\n");
        }
    }

    /**
     * Stop the REPL process.
     */
    protected void stopRepl() {
        if (replProcess != null && replProcess.isAlive()) {
            replProcess.destroy();
        }
        if (outputThread != null) {
            outputThread.interrupt();
        }
    }

    /**
     * Send a command to the REPL.
     *
     * @param command The command to send
     */
    public void sendCommand(String command) {
        if (replInput != null) {
            try {
                replInput.write((command + "\n").getBytes());
                replInput.flush();
            } catch (IOException ex) {
                appendToConsole("Error sending command: " + ex.getMessage() + "\n");
            }
        }
    }

    /**
     * Build the command array for launching the REPL.
     *
     * @return Command array (command + arguments)
     */
    protected String[] buildCommandArray() {
        String command = getReplCommand();
        String[] args = getReplArgs();

        if (args == null || args.length == 0) {
            return new String[]{command};
        }

        String[] commandArray = new String[args.length + 1];
        commandArray[0] = command;
        System.arraycopy(args, 0, commandArray, 1, args.length);

        return commandArray;
    }

    /**
     * Start the thread that reads output from the REPL.
     */
    protected void startOutputThread() {
        outputThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(replProcess.getInputStream()))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    final String output = line;
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        appendToConsole(output + "\n");
                    });
                }
            } catch (IOException ex) {
                // Process ended
            }
        });
        outputThread.setDaemon(true);
        outputThread.start();
    }

    /**
     * Append text to the console area.
     *
     * @param text The text to append
     */
    protected void appendToConsole(String text) {
        consoleArea.append(text);
        consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
    }

    /**
     * Get the startup message to display when the REPL starts.
     *
     * <p>Default implementation provides a generic message. Subclasses can
     * override for language-specific instructions.</p>
     *
     * @return The startup message
     */
    protected String getStartupMessage() {
        return "Type commands and press Enter\n\n";
    }

    /**
     * Get the console text area.
     *
     * @return The text area
     */
    protected JTextArea getConsoleArea() {
        return consoleArea;
    }

    /**
     * Get the REPL process.
     *
     * @return The process, or null if not started
     */
    protected Process getReplProcess() {
        return replProcess;
    }

    /**
     * Get the REPL input stream.
     *
     * @return The output stream for sending commands, or null if not started
     */
    protected OutputStream getReplInput() {
        return replInput;
    }
}
