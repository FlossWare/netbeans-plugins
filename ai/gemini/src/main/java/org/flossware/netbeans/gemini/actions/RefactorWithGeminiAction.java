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
