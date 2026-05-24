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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.flossware.netbeans.claude.util.CodeExtractor.CodeBlock;
import org.flossware.netbeans.claude.util.EditorUtil;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

/**
 * Dialog for inserting code into the editor
 */
public class CodeInsertDialog extends JPanel {

    private final CodeBlock codeBlock;
    private JTextArea codeArea;
    private JRadioButton insertAtCursorRadio;
    private JRadioButton replaceSelectionRadio;

    public CodeInsertDialog(CodeBlock codeBlock) {
        this.codeBlock = codeBlock;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Title
        JLabel titleLabel = new JLabel("Insert Code: " + codeBlock.getLanguage());
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
        add(titleLabel, BorderLayout.NORTH);

        // Code preview
        codeArea = new JTextArea(codeBlock.getCode());
        codeArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        codeArea.setEditable(true);
        codeArea.setLineWrap(false);
        JScrollPane scrollPane = new JScrollPane(codeArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(600, 400));
        add(scrollPane, BorderLayout.CENTER);

        // Options panel
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        insertAtCursorRadio = new JRadioButton("Insert at cursor", true);
        replaceSelectionRadio = new JRadioButton("Replace selection");
        replaceSelectionRadio.setEnabled(EditorUtil.getSelectedText() != null);

        ButtonGroup group = new ButtonGroup();
        group.add(insertAtCursorRadio);
        group.add(replaceSelectionRadio);

        optionsPanel.add(insertAtCursorRadio);
        optionsPanel.add(replaceSelectionRadio);
        add(optionsPanel, BorderLayout.SOUTH);
    }

    /**
     * Show the dialog and insert code if confirmed
     */
    public static void showAndInsert(CodeBlock codeBlock) {
        if (!EditorUtil.hasActiveEditor()) {
            NotifyDescriptor nd = new NotifyDescriptor.Message(
                    "No active editor found. Please open a file first.",
                    NotifyDescriptor.WARNING_MESSAGE
            );
            DialogDisplayer.getDefault().notify(nd);
            return;
        }

        CodeInsertDialog panel = new CodeInsertDialog(codeBlock);

        DialogDescriptor dd = new DialogDescriptor(
                panel,
                "Insert Code",
                true,
                DialogDescriptor.OK_CANCEL_OPTION,
                DialogDescriptor.OK_OPTION,
                null
        );

        Object result = DialogDisplayer.getDefault().notify(dd);

        if (result == DialogDescriptor.OK_OPTION) {
            String code = panel.codeArea.getText();
            if (panel.replaceSelectionRadio.isSelected()) {
                EditorUtil.replaceSelectedText(code);
            } else {
                EditorUtil.insertTextAtCursor(code);
            }
        }
    }
}
