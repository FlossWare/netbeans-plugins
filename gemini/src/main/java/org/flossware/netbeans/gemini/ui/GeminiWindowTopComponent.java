package org.flossware.netbeans.gemini.ui;

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
import org.flossware.netbeans.gemini.api.GeminiService;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Top component which displays Gemini chat window
 */
@TopComponent.Description(
        preferredID = "GeminiWindowTopComponent",
        iconBase = "org/flossware/netbeans/gemini/resources/gemini-icon.png",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "rightSlidingSide", openAtStartup = false)
@ActionID(category = "Window", id = "org.flossware.netbeans.gemini.ui.GeminiWindowTopComponent")
@ActionReference(path = "Menu/Window", position = 333)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_GeminiWindowAction",
        preferredID = "GeminiWindowTopComponent"
)
@Messages({
    "CTL_GeminiWindowAction=Gemini",
    "CTL_GeminiWindowTopComponent=Gemini Chat",
    "HINT_GeminiWindowTopComponent=Gemini AI Assistant"
})
public final class GeminiWindowTopComponent extends TopComponent {

    private static GeminiWindowTopComponent instance;
    private JTextPane chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private JButton clearButton;
    private final GeminiService geminiService;

    public GeminiWindowTopComponent() {
        this.geminiService = GeminiService.getInstance();
        initComponents();
        setName(Bundle.CTL_GeminiWindowTopComponent());
        setToolTipText(Bundle.HINT_GeminiWindowTopComponent());
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
        if (!geminiService.isConfigured()) {
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
        appendToChat("System", "Welcome to Gemini AI Assistant!\n" +
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
        if (!geminiService.isConfigured()) {
            appendToChat("System",
                    "API key not configured. Please set your Google API key in Tools > Options.",
                    Color.RED);
            sendButton.setEnabled(true);
            return;
        }

        // Send to Gemini API
        appendToChat("Gemini", "Thinking...", Color.GRAY);
        geminiService.sendMessageAsync(message).thenAccept(response -> {
            SwingUtilities.invokeLater(() -> {
                removeLastMessage(); // Remove "Thinking..."
                appendToChat("Gemini", response, new Color(75, 0, 130));
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
            e.printStackTrace();
        }

        // Auto-scroll to bottom
        chatArea.setCaretPosition(doc.getLength());
    }

    private void removeLastMessage() {
        StyledDocument doc = chatArea.getStyledDocument();
        String text = chatArea.getText();
        int lastIndex = text.lastIndexOf("Gemini: Thinking...");
        if (lastIndex != -1) {
            try {
                doc.remove(lastIndex, doc.getLength() - lastIndex);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    private void clearChat() {
        chatArea.setText("");
        geminiService.clearHistory();
        appendToChat("System", "Chat cleared. Conversation history reset.", Color.BLUE);
    }

    public static synchronized GeminiWindowTopComponent getDefault() {
        if (instance == null) {
            instance = new GeminiWindowTopComponent();
        }
        return instance;
    }

    public static synchronized GeminiWindowTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent("GeminiWindowTopComponent");
        if (win == null) {
            return getDefault();
        }
        if (win instanceof GeminiWindowTopComponent) {
            return (GeminiWindowTopComponent) win;
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
        return "GeminiWindowTopComponent";
    }

    /**
     * Ask Gemini about selected code
     */
    public void askAboutCode(String code) {
        String message = "Can you explain this code?\n\n```\n" + code + "\n```";
        appendToChat("You", message, Color.BLACK);
        sendToGeminiAsync(message);
    }

    /**
     * Explain selected code
     */
    public void explainCode(String code) {
        String message = "Please explain what this code does in detail:\n\n```\n" + code + "\n```";
        appendToChat("You", message, Color.BLACK);
        sendToGeminiAsync(message);
    }

    /**
     * Get refactoring suggestions for code
     */
    public void refactorCode(String code) {
        String message = "Please suggest refactorings for this code:\n\n```\n" + code + "\n```";
        appendToChat("You", message, Color.BLACK);
        sendToGeminiAsync(message);
    }

    /**
     * Show project context in the chat window
     */
    public void showProjectContext(String context) {
        appendToChat("System", "Current Project Context:\n\n" + context, Color.BLUE);
    }

    /**
     * Send a message to Gemini asynchronously
     */
    private void sendToGeminiAsync(String message) {
        sendButton.setEnabled(false);

        if (!geminiService.isConfigured()) {
            appendToChat("System",
                    "API key not configured. Please set your Google API key in Tools > Options.",
                    Color.RED);
            sendButton.setEnabled(true);
            return;
        }

        appendToChat("Gemini", "Thinking...", Color.GRAY);
        geminiService.sendMessageAsync(message).thenAccept(response -> {
            SwingUtilities.invokeLater(() -> {
                removeLastMessage();
                appendToChat("Gemini", response, new Color(75, 0, 130));
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
