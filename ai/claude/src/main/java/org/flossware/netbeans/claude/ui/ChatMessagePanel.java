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
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import org.flossware.netbeans.claude.util.CodeExtractor;
import org.flossware.netbeans.claude.util.CodeExtractor.CodeBlock;

/**
 * Panel for displaying a chat message with code insertion capabilities
 */
public class ChatMessagePanel extends JPanel {

    public ChatMessagePanel(String sender, String message, Color senderColor) {
        setLayout(new BorderLayout(5, 5));
        setOpaque(false);

        // Sender label
        JLabel senderLabel = new JLabel(sender + ":");
        senderLabel.setForeground(senderColor);
        senderLabel.setFont(senderLabel.getFont().deriveFont(Font.BOLD));

        // Message area
        JTextArea messageArea = new JTextArea(message);
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        messageArea.setOpaque(false);

        // Check for code blocks
        List<CodeBlock> codeBlocks = CodeExtractor.extractCodeBlocks(message);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.add(senderLabel, BorderLayout.NORTH);
        contentPanel.add(messageArea, BorderLayout.CENTER);

        // Add code insertion buttons if code blocks found
        if (!codeBlocks.isEmpty() && sender.equals("Claude")) {
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
            buttonPanel.setOpaque(false);

            for (int i = 0; i < codeBlocks.size(); i++) {
                final CodeBlock block = codeBlocks.get(i);
                JButton insertButton = new JButton("Insert Code " + (i + 1) + " (" + block.getLanguage() + ")");
                insertButton.setFont(insertButton.getFont().deriveFont(10f));
                insertButton.addActionListener(e -> CodeInsertDialog.showAndInsert(block));
                buttonPanel.add(insertButton);
            }

            contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        }

        add(contentPanel, BorderLayout.CENTER);
    }
}
