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
import javax.swing.text.JTextComponent;
import org.flossware.netbeans.claude.ui.ClaudeWindowTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

/**
 * Action to explain selected code
 */
@ActionID(
        category = "Edit",
        id = "org.flossware.netbeans.claude.actions.ExplainCodeAction"
)
@ActionRegistration(
        displayName = "#CTL_ExplainCodeAction",
        lazy = true
)
@ActionReferences({
    @ActionReference(path = "Editors/text/x-java/Popup", position = 1101),
    @ActionReference(path = "Editors/text/x-python/Popup", position = 1101),
    @ActionReference(path = "Editors/text/javascript/Popup", position = 1101),
    @ActionReference(path = "Editors/Popup", position = 1101)
})
@Messages("CTL_ExplainCodeAction=Explain This Code (Claude)")
public final class ExplainCodeAction extends AbstractAction {

    private final JTextComponent component;

    public ExplainCodeAction(JTextComponent component) {
        super(Bundle.CTL_ExplainCodeAction());
        this.component = component;
        setEnabled(component != null && component.getSelectionStart() != component.getSelectionEnd());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String selectedText = component.getSelectedText();
        if (selectedText == null || selectedText.trim().isEmpty()) {
            return;
        }

        ClaudeWindowTopComponent tc = ClaudeWindowTopComponent.findInstance();
        tc.open();
        tc.requestActive();
        tc.explainCode(selectedText);
    }
}
