package org.flossware.netbeans.claude.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.flossware.netbeans.claude.ui.ClaudeWindowTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

/**
 * Action to open Claude chat window
 */
@ActionID(
        category = "Tools",
        id = "org.flossware.netbeans.claude.actions.OpenClaudeAction"
)
@ActionRegistration(
        displayName = "#CTL_OpenClaudeAction"
)
@ActionReference(path = "Menu/Tools", position = 1000)
@Messages("CTL_OpenClaudeAction=Open Claude Chat")
public final class OpenClaudeAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        ClaudeWindowTopComponent tc = ClaudeWindowTopComponent.findInstance();
        tc.open();
        tc.requestActive();
    }
}
