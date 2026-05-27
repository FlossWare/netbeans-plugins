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

package org.flossware.netbeans.claude.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.flossware.netbeans.claude.documentation.JavadocGenerator;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

/**
 * Action to generate javadoc for selected code
 */
@ActionID(
        category = "Edit",
        id = "org.flossware.netbeans.claude.actions.GenerateJavadocAction"
)
@ActionRegistration(
        displayName = "#CTL_GenerateJavadocAction",
        lazy = true
)
@ActionReferences({
    @ActionReference(path = "Editors/text/x-java/Popup", position = 1104)
})
@Messages("CTL_GenerateJavadocAction=Generate Javadoc (Claude)")
public final class GenerateJavadocAction extends AbstractAction {

    private final JTextComponent component;

    public GenerateJavadocAction(JTextComponent component) {
        super(Bundle.CTL_GenerateJavadocAction());
        this.component = component;
        setEnabled(component != null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String selectedCode = component.getSelectedText();

        if (selectedCode == null || selectedCode.trim().isEmpty()) {
            selectedCode = selectCurrentMethod();
        }

        if (selectedCode == null || selectedCode.trim().isEmpty()) {
            NotifyDescriptor nd = new NotifyDescriptor.Message(
                    "No method code selected or available",
                    NotifyDescriptor.WARNING_MESSAGE
            );
            DialogDisplayer.getDefault().notify(nd);
            return;
        }

        final String finalMethodCode = selectedCode;

        ProgressHandle handle = ProgressHandleFactory.createHandle("Generating javadoc...");
        handle.start();

        JavadocGenerator.getInstance().generateJavadocAsync(finalMethodCode)
                .thenAccept(javadoc -> SwingUtilities.invokeLater(() -> {
                    handle.finish();

                    try {
                        insertJavadoc(javadoc);
                    } catch (BadLocationException ex) {
                        Exceptions.printStackTrace(ex);
                        NotifyDescriptor nd = new NotifyDescriptor.Message(
                                "Failed to insert javadoc: " + ex.getMessage(),
                                NotifyDescriptor.ERROR_MESSAGE
                        );
                        DialogDisplayer.getDefault().notify(nd);
                    }
                }))
                .exceptionally(ex -> {
                    SwingUtilities.invokeLater(() -> {
                        handle.finish();
                        Exceptions.printStackTrace(ex);
                        NotifyDescriptor nd = new NotifyDescriptor.Message(
                                "Failed to generate javadoc: " + ex.getMessage(),
                                NotifyDescriptor.ERROR_MESSAGE
                        );
                        DialogDisplayer.getDefault().notify(nd);
                    });
                    return null;
                });
    }

    private String selectCurrentMethod() {
        try {
            Document doc = component.getDocument();
            int caretPos = component.getCaretPosition();
            String text = doc.getText(0, doc.getLength());

            int methodStart = findMethodStart(text, caretPos);
            int methodEnd = findMethodEnd(text, caretPos);

            if (methodStart >= 0 && methodEnd > methodStart) {
                return text.substring(methodStart, methodEnd);
            }
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }

        return null;
    }

    private int findMethodStart(String text, int pos) {
        int braceCount = 0;
        for (int i = pos; i >= 0; i--) {
            char c = text.charAt(i);
            if (c == '}') {
                braceCount++;
            } else if (c == '{') {
                braceCount--;
                if (braceCount < 0) {
                    for (int j = i; j >= 0; j--) {
                        if (text.charAt(j) == '\n' || j == 0) {
                            return j == 0 ? 0 : j + 1;
                        }
                    }
                }
            }
        }
        return -1;
    }

    private int findMethodEnd(String text, int pos) {
        int braceCount = 0;
        boolean foundFirst = false;

        for (int i = pos; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '{') {
                braceCount++;
                foundFirst = true;
            } else if (c == '}') {
                braceCount--;
                if (foundFirst && braceCount == 0) {
                    return i + 1;
                }
            }
        }
        return text.length();
    }

    private void insertJavadoc(String javadoc) throws BadLocationException {
        Document doc = component.getDocument();
        int caretPos = component.getCaretPosition();

        String text = doc.getText(0, doc.getLength());
        int methodStart = findMethodStart(text, caretPos);

        if (methodStart >= 0) {
            doc.insertString(methodStart, javadoc + "\n", null);
        } else {
            doc.insertString(caretPos, javadoc + "\n", null);
        }
    }
}
