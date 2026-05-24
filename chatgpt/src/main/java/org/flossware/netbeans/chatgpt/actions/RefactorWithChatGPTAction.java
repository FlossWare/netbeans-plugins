package org.flossware.netbeans.chatgpt.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.text.JTextComponent;
import org.flossware.netbeans.chatgpt.ui.ChatGPTWindowTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

/**
 * Action to get refactoring suggestions from ChatGPT
 */
@ActionID(
        category = "Edit",
        id = "org.flossware.netbeans.chatgpt.actions.RefactorWithChatGPTAction"
)
@ActionRegistration(
        displayName = "#CTL_RefactorWithChatGPTAction",
        lazy = true
)
@ActionReferences({
    @ActionReference(path = "Editors/text/x-java/Popup", position = 1102),
    @ActionReference(path = "Editors/text/x-python/Popup", position = 1102),
    @ActionReference(path = "Editors/text/javascript/Popup", position = 1102),
    @ActionReference(path = "Editors/Popup", position = 1102)
})
@Messages("CTL_RefactorWithChatGPTAction=Suggest Refactoring (ChatGPT)")
public final class RefactorWithChatGPTAction extends AbstractAction {

    private final JTextComponent component;

    public RefactorWithChatGPTAction(JTextComponent component) {
        super(Bundle.CTL_RefactorWithChatGPTAction());
        this.component = component;
        setEnabled(component != null && component.getSelectionStart() != component.getSelectionEnd());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String selectedText = component.getSelectedText();
        if (selectedText == null || selectedText.trim().isEmpty()) {
            return;
        }

        ChatGPTWindowTopComponent tc = ChatGPTWindowTopComponent.findInstance();
        tc.open();
        tc.requestActive();
        tc.refactorCode(selectedText);
    }
}
