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
 * Action to explain selected code
 */
@ActionID(
        category = "Edit",
        id = "org.flossware.netbeans.gemini.actions.ExplainCodeAction"
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
@Messages("CTL_ExplainCodeAction=Explain This Code (Gemini)")
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

        GeminiWindowTopComponent tc = GeminiWindowTopComponent.findInstance();
        tc.open();
        tc.requestActive();
        tc.explainCode(selectedText);
    }
}
