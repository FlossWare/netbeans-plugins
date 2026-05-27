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
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;
import org.flossware.netbeans.claude.debugging.DebugAnalyzer;
import org.flossware.netbeans.claude.debugging.FixSuggestion;
import org.flossware.netbeans.claude.util.EditorUtil;
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
 * Action to analyze errors and provide fix suggestions
 */
@ActionID(
        category = "Debug",
        id = "org.flossware.netbeans.claude.actions.AnalyzeErrorAction"
)
@ActionRegistration(
        displayName = "#CTL_AnalyzeErrorAction",
        lazy = true
)
@ActionReferences({
    @ActionReference(path = "Editors/Popup", position = 1105)
})
@Messages("CTL_AnalyzeErrorAction=Analyze Error (Claude)")
public final class AnalyzeErrorAction extends AbstractAction {

    private final JTextComponent component;

    public AnalyzeErrorAction(JTextComponent component) {
        super(Bundle.CTL_AnalyzeErrorAction());
        this.component = component;
        setEnabled(component != null && component.getSelectionStart() != component.getSelectionEnd());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String errorText = component.getSelectedText();

        if (errorText == null || errorText.trim().isEmpty()) {
            NotifyDescriptor nd = new NotifyDescriptor.Message(
                    "No error text selected. Please select a stack trace or error message.",
                    NotifyDescriptor.WARNING_MESSAGE
            );
            DialogDisplayer.getDefault().notify(nd);
            return;
        }

        String sourceCode = EditorUtil.getAllText();

        ProgressHandle handle = ProgressHandleFactory.createHandle("Analyzing error...");
        handle.start();

        DebugAnalyzer.getInstance().analyzeErrorAsync(errorText, sourceCode)
                .thenAccept(suggestions -> SwingUtilities.invokeLater(() -> {
                    handle.finish();
                    displaySuggestions(suggestions);
                }))
                .exceptionally(ex -> {
                    SwingUtilities.invokeLater(() -> {
                        handle.finish();
                        Exceptions.printStackTrace(ex);
                        NotifyDescriptor nd = new NotifyDescriptor.Message(
                                "Failed to analyze error: " + ex.getMessage(),
                                NotifyDescriptor.ERROR_MESSAGE
                        );
                        DialogDisplayer.getDefault().notify(nd);
                    });
                    return null;
                });
    }

    private void displaySuggestions(List<FixSuggestion> suggestions) {
        if (suggestions.isEmpty()) {
            NotifyDescriptor nd = new NotifyDescriptor.Message(
                    "No fix suggestions found for this error.",
                    NotifyDescriptor.INFORMATION_MESSAGE
            );
            DialogDisplayer.getDefault().notify(nd);
            return;
        }

        StringBuilder message = new StringBuilder();
        message.append("=== ERROR ANALYSIS RESULTS ===\n\n");

        for (int i = 0; i < suggestions.size(); i++) {
            FixSuggestion suggestion = suggestions.get(i);

            message.append(String.format("Suggestion %d (Confidence: %d%%)\n",
                    i + 1, suggestion.getConfidence()));
            message.append(suggestion.getDescription()).append("\n");

            if (suggestion.hasCode()) {
                message.append("\nSuggested fix:\n");
                message.append(suggestion.getFixCode()).append("\n");
            }

            message.append("\n").append("=".repeat(60)).append("\n\n");
        }

        message.append("Review these suggestions and apply manually as needed.");

        NotifyDescriptor nd = new NotifyDescriptor.Message(
                message.toString(),
                NotifyDescriptor.INFORMATION_MESSAGE
        );
        DialogDisplayer.getDefault().notify(nd);
    }
}
