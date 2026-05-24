package org.flossware.netbeans.chatgpt.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.flossware.netbeans.chatgpt.context.ProjectContextManager;
import org.flossware.netbeans.chatgpt.ui.ChatGPTWindowTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

/**
 * Action to show project context in ChatGPT window
 */
@ActionID(
        category = "Tools",
        id = "org.flossware.netbeans.chatgpt.actions.ShowProjectContextAction"
)
@ActionRegistration(
        displayName = "#CTL_ShowProjectContextAction"
)
@ActionReference(path = "Menu/Tools", position = 1001)
@Messages("CTL_ShowProjectContextAction=Show Project Context (ChatGPT)")
public final class ShowProjectContextAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        ProjectContextManager contextManager = ProjectContextManager.getInstance();
        String summary = contextManager.getProjectSummary();

        ChatGPTWindowTopComponent tc = ChatGPTWindowTopComponent.findInstance();
        tc.open();
        tc.requestActive();
        tc.showProjectContext(summary);
    }
}
