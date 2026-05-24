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

package org.flossware.netbeans.gemini.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.text.JTextComponent;
import org.flossware.netbeans.gemini.ui.GeminiWindowTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

/**
 * Action to get refactoring suggestions from Gemini
 */
@ActionID(
        category = "Edit",
        id = "org.flossware.netbeans.gemini.actions.RefactorWithGeminiAction"
)
@ActionRegistration(
        displayName = "#CTL_RefactorWithGeminiAction",
        lazy = true
)
@ActionReferences({
    @ActionReference(path = "Editors/text/x-java/Popup", position = 1102),
    @ActionReference(path = "Editors/text/x-python/Popup", position = 1102),
    @ActionReference(path = "Editors/text/javascript/Popup", position = 1102),
    @ActionReference(path = "Editors/Popup", position = 1102)
})
@Messages("CTL_RefactorWithGeminiAction=Suggest Refactoring (Gemini)")
public final class RefactorWithGeminiAction extends AbstractAction {

    private final JTextComponent component;

    public RefactorWithGeminiAction(JTextComponent component) {
        super(Bundle.CTL_RefactorWithGeminiAction());
        this.component = component;
        setEnabled(component != null && component.getSelectionStart() != component.getSelectionEnd());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String selectedText = component.getSelectedText();
        if (selectedText == null || selectedText.trim().isEmpty()) {
            return;
        }

        GeminiWindowTopComponent tc = GeminiWindowTopComponent.findInstance();
        tc.open();
        tc.requestActive();
        tc.refactorCode(selectedText);
    }
}
