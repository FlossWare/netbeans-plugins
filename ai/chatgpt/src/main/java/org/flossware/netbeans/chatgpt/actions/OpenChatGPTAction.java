package org.flossware.netbeans.chatgpt.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.flossware.netbeans.chatgpt.ui.ChatGPTWindowTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

/**
 * Action to open ChatGPT chat window
 */
@ActionID(
        category = "Tools",
        id = "org.flossware.netbeans.chatgpt.actions.OpenChatGPTAction"
)
@ActionRegistration(
        displayName = "#CTL_OpenChatGPTAction"
)
@ActionReference(path = "Menu/Tools", position = 1000)
@Messages("CTL_OpenChatGPTAction=Open ChatGPT Chat")
public final class OpenChatGPTAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        ChatGPTWindowTopComponent tc = ChatGPTWindowTopComponent.findInstance();
        tc.open();
        tc.requestActive();
    }
}
