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
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
import javax.swing.*;
import org.flossware.netbeans.chatgpt.util.CodeExtractor;
import org.flossware.netbeans.chatgpt.util.CodeExtractor.CodeBlock;

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
        if (!codeBlocks.isEmpty() && sender.equals("ChatGPT")) {
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
