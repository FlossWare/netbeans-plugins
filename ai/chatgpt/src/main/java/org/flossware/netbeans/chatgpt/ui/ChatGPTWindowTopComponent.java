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

package org.flossware.netbeans.chatgpt.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import org.flossware.netbeans.chatgpt.api.ChatGPTService;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays ChatGPT chat window
 */
@TopComponent.Description(
        preferredID = "ChatGPTWindowTopComponent",
        iconBase = "org/flossware/netbeans/chatgpt/resources/chatgpt-icon.png",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "rightSlidingSide", openAtStartup = false)
@ActionID(category = "Window", id = "org.flossware.netbeans.chatgpt.ui.ChatGPTWindowTopComponent")
@ActionReference(path = "Menu/Window", position = 333)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_ChatGPTWindowAction",
        preferredID = "ChatGPTWindowTopComponent"
)
@Messages({
    "CTL_ChatGPTWindowAction=ChatGPT",
    "CTL_ChatGPTWindowTopComponent=ChatGPT Chat",
    "HINT_ChatGPTWindowTopComponent=ChatGPT AI Assistant"
})
public final class ChatGPTWindowTopComponent extends TopComponent {

    private static ChatGPTWindowTopComponent instance;
    private JTextPane chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private JButton clearButton;
    private final ChatGPTService chatgptService;

    public ChatGPTWindowTopComponent() {
        this.chatgptService = ChatGPTService.getInstance();
        initComponents();
        setName(Bundle.CTL_ChatGPTWindowTopComponent());
        setToolTipText(Bundle.HINT_ChatGPTWindowTopComponent());
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

        sendButton = new JButton("Send");
        sendButton.addActionListener(this::sendMessage);

        clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clearChat());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(sendButton);
        buttonPanel.add(clearButton);

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(buttonPanel, BorderLayout.EAST);

        // Status label
        JLabel statusLabel = new JLabel();
        if (!chatgptService.isConfigured()) {
            statusLabel.setText("⚠ API key not configured. Set it in Tools > Options");
            statusLabel.setForeground(Color.RED);
        } else {
            statusLabel.setText("✓ Ready");
            statusLabel.setForeground(new Color(0, 128, 0));
        }

        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
        add(statusLabel, BorderLayout.NORTH);

        // Initial welcome message
        appendToChat("System", "Welcome to ChatGPT AI Assistant!\n" +
                "Type your questions or requests below.\n" +
                "You can ask about code, get explanations, or request help with programming tasks.\n",
                Color.BLUE);
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
        if (!chatgptService.isConfigured()) {
            appendToChat("System",
                    "API key not configured. Please set your OpenAI API key in Tools > Options.",
                    Color.RED);
            sendButton.setEnabled(true);
            return;
        }

        // Send to ChatGPT API
        appendToChat("ChatGPT", "Thinking...", Color.GRAY);
        chatgptService.sendMessageAsync(message).thenAccept(response -> {
            SwingUtilities.invokeLater(() -> {
                removeLastMessage(); // Remove "Thinking..."
                appendToChat("ChatGPT", response, new Color(75, 0, 130));
                sendButton.setEnabled(true);
                inputField.requestFocus();
            });
        }).exceptionally(ex -> {
            SwingUtilities.invokeLater(() -> {
                removeLastMessage(); // Remove "Thinking..."
                appendToChat("Error", "Failed to get response: " + ex.getMessage(), Color.RED);
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
        int lastIndex = text.lastIndexOf("ChatGPT: Thinking...");
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
        chatgptService.clearHistory();
        appendToChat("System", "Chat cleared. Conversation history reset.", Color.BLUE);
    }

    public static synchronized ChatGPTWindowTopComponent getDefault() {
        if (instance == null) {
            instance = new ChatGPTWindowTopComponent();
        }
        return instance;
    }

    public static synchronized ChatGPTWindowTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent("ChatGPTWindowTopComponent");
        if (win == null) {
            return getDefault();
        }
        if (win instanceof ChatGPTWindowTopComponent) {
            return (ChatGPTWindowTopComponent) win;
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
        return "ChatGPTWindowTopComponent";
    }

    /**
     * Ask ChatGPT about selected code
     */
    public void askAboutCode(String code) {
        String message = "Can you explain this code?\n\n```\n" + code + "\n```";
        appendToChat("You", message, Color.BLACK);
        sendToChatGPTAsync(message);
    }

    /**
     * Explain selected code
     */
    public void explainCode(String code) {
        String message = "Please explain what this code does in detail:\n\n```\n" + code + "\n```";
        appendToChat("You", message, Color.BLACK);
        sendToChatGPTAsync(message);
    }

    /**
     * Get refactoring suggestions for code
     */
    public void refactorCode(String code) {
        String message = "Please suggest refactorings for this code:\n\n```\n" + code + "\n```";
        appendToChat("You", message, Color.BLACK);
        sendToChatGPTAsync(message);
    }

    /**
     * Show project context in the chat window
     */
    public void showProjectContext(String context) {
        appendToChat("System", "Current Project Context:\n\n" + context, Color.BLUE);
    }

    /**
     * Send a message to ChatGPT asynchronously
     */
    private void sendToChatGPTAsync(String message) {
        sendButton.setEnabled(false);

        if (!chatgptService.isConfigured()) {
            appendToChat("System",
                    "API key not configured. Please set your OpenAI API key in Tools > Options.",
                    Color.RED);
            sendButton.setEnabled(true);
            return;
        }

        appendToChat("ChatGPT", "Thinking...", Color.GRAY);
        chatgptService.sendMessageAsync(message).thenAccept(response -> {
            SwingUtilities.invokeLater(() -> {
                removeLastMessage();
                appendToChat("ChatGPT", response, new Color(75, 0, 130));
                sendButton.setEnabled(true);
            });
        }).exceptionally(ex -> {
            SwingUtilities.invokeLater(() -> {
                removeLastMessage();
                appendToChat("Error", "Failed to get response: " + ex.getMessage(), Color.RED);
                sendButton.setEnabled(true);
            });
            return null;
        });
    }
}
