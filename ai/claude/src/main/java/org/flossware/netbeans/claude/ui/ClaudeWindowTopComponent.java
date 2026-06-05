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

package org.flossware.netbeans.claude.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import org.flossware.netbeans.claude.api.ClaudeService;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays Claude chat window
 */
@TopComponent.Description(
        preferredID = "ClaudeWindowTopComponent",
        iconBase = "org/flossware/netbeans/claude/resources/claude-icon.png",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "rightSlidingSide", openAtStartup = false)
@ActionID(category = "Window", id = "org.flossware.netbeans.claude.ui.ClaudeWindowTopComponent")
@ActionReference(path = "Menu/Window", position = 333)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_ClaudeWindowAction",
        preferredID = "ClaudeWindowTopComponent"
)
@Messages({
    "CTL_ClaudeWindowAction=Claude",
    "CTL_ClaudeWindowTopComponent=Claude Chat",
    "HINT_ClaudeWindowTopComponent=Claude AI Assistant",
    "BTN_Send=Send",
    "BTN_Clear=Clear",
    "LBL_StatusNotConfigured=⚠ API key not configured. Set it in Tools > Options",
    "LBL_StatusReady=✓ Ready",
    "MSG_Welcome=Welcome to Claude AI Assistant!\nType your questions or requests below.\nYou can ask about code, get explanations, or request help with programming tasks.",
    "MSG_ConfigurationError=API key not configured. Please set your Anthropic API key in Tools > Options.",
    "MSG_Thinking=Thinking...",
    "MSG_ResponseError=Failed to get response: ",
    "MSG_ChatCleared=Chat cleared. Conversation history reset."
})
public final class ClaudeWindowTopComponent extends TopComponent {

    private static ClaudeWindowTopComponent instance;
    private JTextPane chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private JButton clearButton;
    private final ClaudeService claudeService;

    public ClaudeWindowTopComponent() {
        this.claudeService = ClaudeService.getInstance();
        initComponents();
        setName(Bundle.CTL_ClaudeWindowTopComponent());
        setToolTipText(Bundle.HINT_ClaudeWindowTopComponent());
    }

    private void initComponents() {
        setLayout(new BorderLayout(5, 5));

        // Chat display area
        chatArea = new JTextPane();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setPreferredSize(new Dimension(400, 500));

        // Input panel
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputField = new JTextField();
        inputField.setFont(new Font("SansSerif", Font.PLAIN, 12));
        inputField.addActionListener(this::sendMessage);

        sendButton = new JButton(Bundle.BTN_Send());
        sendButton.addActionListener(this::sendMessage);

        clearButton = new JButton(Bundle.BTN_Clear());
        clearButton.addActionListener(e -> clearChat());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(sendButton);
        buttonPanel.add(clearButton);

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.EAST);

        // Status label
        JLabel statusLabel = new JLabel();
        if (!claudeService.isConfigured()) {
            statusLabel.setText(Bundle.LBL_StatusNotConfigured());
            statusLabel.setForeground(Color.RED);
        } else {
            statusLabel.setText(Bundle.LBL_StatusReady());
            statusLabel.setForeground(new Color(0, 128, 0));
        }

        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
        add(statusLabel, BorderLayout.NORTH);

        // Initial welcome message
        appendToChat("System", Bundle.MSG_Welcome(), Color.BLUE);
    }

    private void sendMessage(ActionEvent evt) {
        String message = inputField.getText().trim();
        if (message.isEmpty()) {
            return;
        }

        // Display user message
        appendToChat("You", message, Color.BLACK);
        inputField.setText("");
        sendButton.setEnabled(false);

        // Check if configured
        if (!claudeService.isConfigured()) {
            appendToChat("System",
                    Bundle.MSG_ConfigurationError(),
                    Color.RED);
            sendButton.setEnabled(true);
            return;
        }

        // Send to Claude API
        appendToChat("Claude", Bundle.MSG_Thinking(), Color.GRAY);
        claudeService.sendMessageAsync(message).thenAccept(response -> {
            SwingUtilities.invokeLater(() -> {
                removeLastMessage(); // Remove "Thinking..."
                appendToChat("Claude", response, new Color(75, 0, 130));
                sendButton.setEnabled(true);
                inputField.requestFocus();
            });
        }).exceptionally(ex -> {
            SwingUtilities.invokeLater(() -> {
                removeLastMessage(); // Remove "Thinking..."
                appendToChat("Error", Bundle.MSG_ResponseError() + ex.getMessage(), Color.RED);
                sendButton.setEnabled(true);
                inputField.requestFocus();
            });
            return null;
        });
    }

    private void appendToChat(String sender, String message, Color color) {
        StyledDocument doc = chatArea.getStyledDocument();
        Style style = chatArea.addStyle("Style", null);
        StyleConstants.setForeground(style, color);
        StyleConstants.setBold(style, true);

        Style messageStyle = chatArea.addStyle("MessageStyle", null);
        StyleConstants.setForeground(messageStyle, Color.BLACK);

        try {
            doc.insertString(doc.getLength(), sender + ": ", style);
            doc.insertString(doc.getLength(), message + "\n\n", messageStyle);
        } catch (BadLocationException e) {
            Exceptions.printStackTrace(e);
        }

        // Auto-scroll to bottom
        chatArea.setCaretPosition(doc.getLength());
    }

    private void removeLastMessage() {
        StyledDocument doc = chatArea.getStyledDocument();
        String text = chatArea.getText();
        int lastIndex = text.lastIndexOf("Claude: Thinking...");
        if (lastIndex != -1) {
            try {
                doc.remove(lastIndex, doc.getLength() - lastIndex);
            } catch (BadLocationException e) {
                Exceptions.printStackTrace(e);
            }
        }
    }

    private void clearChat() {
        chatArea.setText("");
        claudeService.clearHistory();
        appendToChat("System", Bundle.MSG_ChatCleared(), Color.BLUE);
    }

    public static synchronized ClaudeWindowTopComponent getDefault() {
        if (instance == null) {
            instance = new ClaudeWindowTopComponent();
        }
        return instance;
    }

    public static synchronized ClaudeWindowTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent("ClaudeWindowTopComponent");
        if (win == null) {
            return getDefault();
        }
        if (win instanceof ClaudeWindowTopComponent) {
            return (ClaudeWindowTopComponent) win;
        }
        return getDefault();
    }

    @Override
    public void componentOpened() {
        // Called when component is opened
    }

    @Override
    public void componentClosed() {
        // Called when component is closed
    }

    @Override
    protected String preferredID() {
        return "ClaudeWindowTopComponent";
    }

    /**
     * Ask Claude about selected code
     */
    public void askAboutCode(String code) {
        String message = "Can you explain this code?\n\n```\n" + code + "\n```";
        appendToChat("You", message, Color.BLACK);
        sendToClaudeAsync(message);
    }

    /**
     * Explain selected code
     */
    public void explainCode(String code) {
        String message = "Please explain what this code does in detail:\n\n```\n" + code + "\n```";
        appendToChat("You", message, Color.BLACK);
        sendToClaudeAsync(message);
    }

    /**
     * Get refactoring suggestions for code
     */
    public void refactorCode(String code) {
        String message = "Please suggest refactorings for this code:\n\n```\n" + code + "\n```";
        appendToChat("You", message, Color.BLACK);
        sendToClaudeAsync(message);
    }

    /**
     * Show project context in the chat window
     */
    public void showProjectContext(String context) {
        appendToChat("System", "Current Project Context:\n\n" + context, Color.BLUE);
    }

    /**
     * Send a message to Claude asynchronously
     */
    private void sendToClaudeAsync(String message) {
        sendButton.setEnabled(false);

        if (!claudeService.isConfigured()) {
            appendToChat("System",
                    Bundle.MSG_ConfigurationError(),
                    Color.RED);
            sendButton.setEnabled(true);
            return;
        }

        appendToChat("Claude", Bundle.MSG_Thinking(), Color.GRAY);
        claudeService.sendMessageAsync(message).thenAccept(response -> {
            SwingUtilities.invokeLater(() -> {
                removeLastMessage();
                appendToChat("Claude", response, new Color(75, 0, 130));
                sendButton.setEnabled(true);
            });
        }).exceptionally(ex -> {
            SwingUtilities.invokeLater(() -> {
                removeLastMessage();
                appendToChat("Error", Bundle.MSG_ResponseError() + ex.getMessage(), Color.RED);
                sendButton.setEnabled(true);
            });
            return null;
        });
    }
}
