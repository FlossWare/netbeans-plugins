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
 * Action to get refactoring suggestions from Claude
 */
@ActionID(
        category = "Edit",
        id = "org.flossware.netbeans.claude.actions.RefactorWithClaudeAction"
)
@ActionRegistration(
        displayName = "#CTL_RefactorWithClaudeAction",
        lazy = true
)
@ActionReferences({
    @ActionReference(path = "Editors/text/x-java/Popup", position = 1102),
    @ActionReference(path = "Editors/text/x-python/Popup", position = 1102),
    @ActionReference(path = "Editors/text/javascript/Popup", position = 1102),
    @ActionReference(path = "Editors/Popup", position = 1102)
})
@Messages("CTL_RefactorWithClaudeAction=Suggest Refactoring (Claude)")
public final class RefactorWithClaudeAction extends AbstractAction {

    private final JTextComponent component;

    public RefactorWithClaudeAction(JTextComponent component) {
        super(Bundle.CTL_RefactorWithClaudeAction());
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
        tc.refactorCode(selectedText);
    }
}
