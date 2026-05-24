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
