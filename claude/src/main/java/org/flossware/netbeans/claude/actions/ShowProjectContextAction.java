package org.flossware.netbeans.claude.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.flossware.netbeans.claude.context.ProjectContextManager;
import org.flossware.netbeans.claude.ui.ClaudeWindowTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

/**
 * Action to show project context in Claude window
 */
@ActionID(
        category = "Tools",
        id = "org.flossware.netbeans.claude.actions.ShowProjectContextAction"
)
@ActionRegistration(
        displayName = "#CTL_ShowProjectContextAction"
)
@ActionReference(path = "Menu/Tools", position = 1001)
@Messages("CTL_ShowProjectContextAction=Show Project Context (Claude)")
public final class ShowProjectContextAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        ProjectContextManager contextManager = ProjectContextManager.getInstance();
        String summary = contextManager.getProjectSummary();

        ClaudeWindowTopComponent tc = ClaudeWindowTopComponent.findInstance();
        tc.open();
        tc.requestActive();
        tc.showProjectContext(summary);
    }
}
