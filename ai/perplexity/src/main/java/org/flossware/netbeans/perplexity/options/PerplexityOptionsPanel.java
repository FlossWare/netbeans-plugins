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

package org.flossware.netbeans.perplexity.options;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.prefs.Preferences;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.flossware.netbeans.perplexity.api.PerplexityService;
import org.openide.util.NbPreferences;

/**
 * Panel for Perplexity options
 */
final class PerplexityOptionsPanel extends JPanel {

    private static final String PREF_API_KEY = "perplexity.api.key";
    private static final String PREF_MODEL = "perplexity.model";
    private static final String PREF_MAX_TOKENS = "perplexity.max.tokens";
    private static final String PREF_TEMPERATURE = "perplexity.temperature";

    private final PerplexityOptionsPanelController controller;
    private JPasswordField apiKeyField;
    private JComboBox<String> modelComboBox;
    private JSpinner maxTokensSpinner;
    private JSpinner temperatureSpinner;

    PerplexityOptionsPanel(PerplexityOptionsPanelController controller) {
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
        JLabel helpLabel = new JLabel("<html><small>Get your API key from <a href='https://console.perplexity.ai/'>console.perplexity.ai</a></small></html>");
        mainPanel.add(helpLabel, gbc);

        // Model selection
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        mainPanel.add(new JLabel("Model:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        modelComboBox = new JComboBox<>(new String[]{
            "perplexity-small-latest",
            "perplexity-medium-latest",
            "perplexity-small-latest",
            "llama-3.1-sonar-huge-128k-online"
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
        maxTokensSpinner = new JSpinner(new SpinnerNumberModel(4096, 1024, 32000, 1024));
        maxTokensSpinner.addChangeListener(e -> controller.changed());
        mainPanel.add(maxTokensSpinner, gbc);

        // Temperature
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        mainPanel.add(new JLabel("Temperature:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        temperatureSpinner = new JSpinner(new SpinnerNumberModel(0.7, 0.0, 1.0, 0.1));
        temperatureSpinner.addChangeListener(e -> controller.changed());
        mainPanel.add(temperatureSpinner, gbc);

        // Test connection button
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        JButton testButton = new JButton("Test Connection");
        testButton.addActionListener(e -> testConnection());
        mainPanel.add(testButton, gbc);

        add(mainPanel, BorderLayout.NORTH);
    }

    void load() {
        Preferences prefs = NbPreferences.forModule(PerplexityOptionsPanel.class);
        apiKeyField.setText(prefs.get(PREF_API_KEY, ""));
        modelComboBox.setSelectedItem(prefs.get(PREF_MODEL, "perplexity-small-latest"));
        maxTokensSpinner.setValue(prefs.getInt(PREF_MAX_TOKENS, 4096));
        temperatureSpinner.setValue(prefs.getDouble(PREF_TEMPERATURE, 0.7));
    }

    void store() {
        Preferences prefs = NbPreferences.forModule(PerplexityOptionsPanel.class);
        prefs.put(PREF_API_KEY, new String(apiKeyField.getPassword()));
        prefs.put(PREF_MODEL, (String) modelComboBox.getSelectedItem());
        prefs.putInt(PREF_MAX_TOKENS, (Integer) maxTokensSpinner.getValue());
        prefs.putDouble(PREF_TEMPERATURE, (Double) temperatureSpinner.getValue());

        // Update the service with new API key
        PerplexityService.getInstance().getClient().setApiKey(new String(apiKeyField.getPassword()));
    }

    boolean valid() {
        return true;
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
        PerplexityService service = PerplexityService.getInstance();
        service.getClient().setApiKey(apiKey);

        service.sendMessageAsync("Hello").thenAccept(response -> {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this,
                        "Connection successful!\nPerplexity responded: " +
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
