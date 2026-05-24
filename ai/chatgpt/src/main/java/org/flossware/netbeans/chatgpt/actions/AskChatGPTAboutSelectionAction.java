package org.flossware.netbeans.chatgpt.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JEditorPane;
import javax.swing.text.JTextComponent;
import org.flossware.netbeans.chatgpt.ui.ChatGPTWindowTopComponent;
import org.netbeans.api.editor.EditorRegistry;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

/**
 * Action to ask ChatGPT about selected code in editor
 */
@ActionID(
        category = "Edit",
        id = "org.flossware.netbeans.chatgpt.actions.AskChatGPTAboutSelectionAction"
)
@ActionRegistration(
        displayName = "#CTL_AskChatGPTAboutSelectionAction",
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
@Messages("CTL_AskChatGPTAboutSelectionAction=Ask ChatGPT About This Code")
public final class AskChatGPTAboutSelectionAction extends AbstractAction {

    private final JTextComponent component;

    public AskChatGPTAboutSelectionAction(JTextComponent component) {
        super(Bundle.CTL_AskChatGPTAboutSelectionAction());
        this.component = component;
        setEnabled(component != null && component.getSelectionStart() != component.getSelectionEnd());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String selectedText = component.getSelectedText();
        if (selectedText == null || selectedText.trim().isEmpty()) {
            return;
        }

        // Open ChatGPT window and send the code
        ChatGPTWindowTopComponent tc = ChatGPTWindowTopComponent.findInstance();
        tc.open();
        tc.requestActive();
        tc.askAboutCode(selectedText);
    }
}
