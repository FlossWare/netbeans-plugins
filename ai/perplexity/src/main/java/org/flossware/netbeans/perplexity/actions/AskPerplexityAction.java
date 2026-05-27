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

package org.flossware.netbeans.perplexity.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.text.JTextComponent;
import org.flossware.netbeans.perplexity.ui.PerplexityWindowTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

/**
 * Action to ask Perplexity AI about selected code
 */
@ActionID(
        category = "Edit",
        id = "org.flossware.netbeans.perplexity.actions.AskPerplexityAction"
)
@ActionRegistration(
        displayName = "#CTL_AskPerplexityAction"
)
@ActionReferences({
    @ActionReference(path = "Editors/text/x-java/Popup", position = 1400),
    @ActionReference(path = "Editors/text/x-python/Popup", position = 1400),
    @ActionReference(path = "Editors/text/javascript/Popup", position = 1400),
    @ActionReference(path = "Editors/text/x-typescript/Popup", position = 1400)
})
@Messages("CTL_AskPerplexityAction=Ask Perplexity AI")
public final class AskPerplexityAction implements ActionListener {

    private final JTextComponent context;

    public AskPerplexityAction(JTextComponent context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        String selectedText = context.getSelectedText();
        if (selectedText == null || selectedText.trim().isEmpty()) {
            return;
        }

        // Open Perplexity window and ask about the code
        PerplexityWindowTopComponent window = PerplexityWindowTopComponent.findInstance();
        window.open();
        window.requestActive();
        window.askAboutCode(selectedText);
    }
}
