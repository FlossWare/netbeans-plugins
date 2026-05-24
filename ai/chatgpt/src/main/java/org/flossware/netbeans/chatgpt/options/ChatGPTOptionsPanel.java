package org.flossware.netbeans.chatgpt.options;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.prefs.Preferences;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.flossware.netbeans.chatgpt.api.ChatGPTService;
import org.flossware.netbeans.chatgpt.completion.ChatGPTCompletionSettings;
import org.openide.util.NbPreferences;

/**
 * Panel for ChatGPT options
 */
final class ChatGPTOptionsPanel extends JPanel {

    private static final String PREF_API_KEY = "openai.api.key";
    private static final String PREF_MODEL = "openai.model";
    private static final String PREF_MAX_TOKENS = "openai.max.tokens";
    private static final String PREF_TEMPERATURE = "openai.temperature";

    private final ChatGPTOptionsPanelController controller;
    private JPasswordField apiKeyField;
    private JComboBox<String> modelComboBox;
    private JSpinner maxTokensSpinner;
    private JSpinner temperatureSpinner;
    private JCheckBox enableProjectContextCheckBox;

    // Completion settings
    private JCheckBox enableCompletionCheckBox;
    private JCheckBox autoTriggerCheckBox;
    private JSpinner minCharsSpinner;
    private JTextField triggerCharsField;
    private JCheckBox enableCacheCheckBox;

    ChatGPTOptionsPanel(ChatGPTOptionsPanelController controller) {
        this.controller = controller;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // API Key
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        mainPanel.add(new JLabel("API Key:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        apiKeyField = new JPasswordField(40);
        apiKeyField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                controller.changed();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                controller.changed();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                controller.changed();
            }
        });
        mainPanel.add(apiKeyField, gbc);

        // Help text for API key
        gbc.gridx = 1;
        gbc.gridy = 1;
        JLabel helpLabel = new JLabel("<html><small>Get your API key from <a href='https://console.openai.com/'>console.openai.com</a></small></html>");
        mainPanel.add(helpLabel, gbc);

        // Model selection
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        mainPanel.add(new JLabel("Model:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        modelComboBox = new JComboBox<>(new String[]{
            "gpt-4-turbo-preview",
            "gpt-4",
            "gpt-3.5-turbo",
            "gpt-3.5-turbo-16k"
        });
        modelComboBox.addActionListener(e -> controller.changed());
        mainPanel.add(modelComboBox, gbc);

        // Max tokens
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        mainPanel.add(new JLabel("Max Tokens:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        maxTokensSpinner = new JSpinner(new SpinnerNumberModel(4096, 1024, 200000, 1024));
        maxTokensSpinner.addChangeListener(e -> controller.changed());
        mainPanel.add(maxTokensSpinner, gbc);

        // Temperature
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        mainPanel.add(new JLabel("Temperature:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        temperatureSpinner = new JSpinner(new SpinnerNumberModel(1.0, 0.0, 1.0, 0.1));
        temperatureSpinner.addChangeListener(e -> controller.changed());
        mainPanel.add(temperatureSpinner, gbc);

        // Enable project context
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        enableProjectContextCheckBox = new JCheckBox("Enable project context (reads project files)");
        enableProjectContextCheckBox.addActionListener(e -> controller.changed());
        mainPanel.add(enableProjectContextCheckBox, gbc);

        // Separator
        gbc.gridy = 6;
        mainPanel.add(new JSeparator(), gbc);

        // Code Completion Section Header
        gbc.gridy = 7;
        JLabel completionLabel = new JLabel("<html><b>Code Completion Settings</b></html>");
        mainPanel.add(completionLabel, gbc);

        // Enable completion
        gbc.gridy = 8;
        enableCompletionCheckBox = new JCheckBox("Enable AI-powered code completion");
        enableCompletionCheckBox.addActionListener(e -> {
            controller.changed();
            toggleCompletionSettings();
        });
        mainPanel.add(enableCompletionCheckBox, gbc);

        // Auto-trigger
        gbc.gridy = 9;
        autoTriggerCheckBox = new JCheckBox("Auto-trigger on typing (otherwise Ctrl+Space only)");
        autoTriggerCheckBox.addActionListener(e -> controller.changed());
        mainPanel.add(autoTriggerCheckBox, gbc);

        // Minimum characters
        gbc.gridy = 10;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        mainPanel.add(new JLabel("Minimum characters to trigger:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        minCharsSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 10, 1));
        minCharsSpinner.addChangeListener(e -> controller.changed());
        mainPanel.add(minCharsSpinner, gbc);

        // Trigger characters
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.weightx = 0.0;
        mainPanel.add(new JLabel("Auto-trigger on characters:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        triggerCharsField = new JTextField(".");
        triggerCharsField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { controller.changed(); }
            @Override
            public void removeUpdate(DocumentEvent e) { controller.changed(); }
            @Override
            public void changedUpdate(DocumentEvent e) { controller.changed(); }
        });
        mainPanel.add(triggerCharsField, gbc);

        // Enable cache
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 2;
        enableCacheCheckBox = new JCheckBox("Enable completion caching (reduces API calls)");
        enableCacheCheckBox.addActionListener(e -> controller.changed());
        mainPanel.add(enableCacheCheckBox, gbc);

        // Test connection button
        gbc.gridx = 0;
        gbc.gridy = 13;
        gbc.gridwidth = 2;
        JButton testButton = new JButton("Test Connection");
        testButton.addActionListener(e -> testConnection());
        mainPanel.add(testButton, gbc);

        add(mainPanel, BorderLayout.NORTH);
    }

    void load() {
        Preferences prefs = NbPreferences.forModule(ChatGPTOptionsPanel.class);
        apiKeyField.setText(prefs.get(PREF_API_KEY, ""));
        modelComboBox.setSelectedItem(prefs.get(PREF_MODEL, "gpt-4-turbo-preview"));
        maxTokensSpinner.setValue(prefs.getInt(PREF_MAX_TOKENS, 4096));
        temperatureSpinner.setValue(prefs.getDouble(PREF_TEMPERATURE, 1.0));
        enableProjectContextCheckBox.setSelected(prefs.getBoolean("enable.project.context", true));

        // Load completion settings
        enableCompletionCheckBox.setSelected(ChatGPTCompletionSettings.isEnabled());
        autoTriggerCheckBox.setSelected(ChatGPTCompletionSettings.isAutoTriggerEnabled());
        minCharsSpinner.setValue(ChatGPTCompletionSettings.getMinimumCharacters());
        triggerCharsField.setText(ChatGPTCompletionSettings.getTriggerCharacters());
        enableCacheCheckBox.setSelected(ChatGPTCompletionSettings.isCacheEnabled());

        toggleCompletionSettings();
    }

    void store() {
        Preferences prefs = NbPreferences.forModule(ChatGPTOptionsPanel.class);
        prefs.put(PREF_API_KEY, new String(apiKeyField.getPassword()));
        prefs.put(PREF_MODEL, (String) modelComboBox.getSelectedItem());
        prefs.putInt(PREF_MAX_TOKENS, (Integer) maxTokensSpinner.getValue());
        prefs.putDouble(PREF_TEMPERATURE, (Double) temperatureSpinner.getValue());
        prefs.putBoolean("enable.project.context", enableProjectContextCheckBox.isSelected());

        // Store completion settings
        ChatGPTCompletionSettings.setEnabled(enableCompletionCheckBox.isSelected());
        ChatGPTCompletionSettings.setAutoTriggerEnabled(autoTriggerCheckBox.isSelected());
        ChatGPTCompletionSettings.setMinimumCharacters((Integer) minCharsSpinner.getValue());
        ChatGPTCompletionSettings.setTriggerCharacters(triggerCharsField.getText());
        ChatGPTCompletionSettings.setCacheEnabled(enableCacheCheckBox.isSelected());

        // Update the service with new API key
        ChatGPTService.getInstance().getClient().setApiKey(new String(apiKeyField.getPassword()));
    }

    /**
     * Enable/disable completion settings based on main checkbox
     */
    private void toggleCompletionSettings() {
        boolean enabled = enableCompletionCheckBox.isSelected();
        autoTriggerCheckBox.setEnabled(enabled);
        minCharsSpinner.setEnabled(enabled);
        triggerCharsField.setEnabled(enabled);
        enableCacheCheckBox.setEnabled(enabled);
    }

    boolean valid() {
        return apiKeyField.getPassword().length > 0;
    }

    private void testConnection() {
        String apiKey = new String(apiKeyField.getPassword());
        if (apiKey.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter an API key first.",
                    "No API Key",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Testing connection...\n(This will make a small API call)",
                "Test Connection",
                JOptionPane.INFORMATION_MESSAGE);

        // Test the connection
        ChatGPTService service = ChatGPTService.getInstance();
        service.getClient().setApiKey(apiKey);

        service.sendMessageAsync("Hello").thenAccept(response -> {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this,
                        "Connection successful!\nChatGPT responded: " +
                        (response.length() > 100 ? response.substring(0, 100) + "..." : response),
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            });
        }).exceptionally(ex -> {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this,
                        "Connection failed!\nError: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            });
            return null;
        });
    }
}
