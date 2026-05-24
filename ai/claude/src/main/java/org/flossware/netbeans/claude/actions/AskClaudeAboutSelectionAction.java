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
import javax.swing.JEditorPane;
import javax.swing.text.JTextComponent;
import org.flossware.netbeans.claude.ui.ClaudeWindowTopComponent;
import org.netbeans.api.editor.EditorRegistry;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

/**
 * Action to ask Claude about selected code in editor
 */
@ActionID(
        category = "Edit",
        id = "org.flossware.netbeans.claude.actions.AskClaudeAboutSelectionAction"
)
@ActionRegistration(
        displayName = "#CTL_AskClaudeAboutSelectionAction",
        lazy = true
)
@ActionReferences({
    @ActionReference(path = "Editors/text/x-java/Popup", position = 1100),
    @ActionReference(path = "Editors/text/x-python/Popup", position = 1100),
    @ActionReference(path = "Editors/text/javascript/Popup", position = 1100),
    @ActionReference(path = "Editors/text/x-typescript/Popup", position = 1100),
    @ActionReference(path = "Editors/text/html/Popup", position = 1100),
    @ActionReference(path = "Editors/text/xml/Popup", position = 1100),
    @ActionReference(path = "Editors/Popup", position = 1100)
})
@Messages("CTL_AskClaudeAboutSelectionAction=Ask Claude About This Code")
public final class AskClaudeAboutSelectionAction extends AbstractAction {

    private final JTextComponent component;

    public AskClaudeAboutSelectionAction(JTextComponent component) {
        super(Bundle.CTL_AskClaudeAboutSelectionAction());
        this.component = component;
        setEnabled(component != null && component.getSelectionStart() != component.getSelectionEnd());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String selectedText = component.getSelectedText();
        if (selectedText == null || selectedText.trim().isEmpty()) {
            return;
        }

        // Open Claude window and send the code
        ClaudeWindowTopComponent tc = ClaudeWindowTopComponent.findInstance();
        tc.open();
        tc.requestActive();
        tc.askAboutCode(selectedText);
    }
}
