package org.flossware.netbeans.gemini.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.flossware.netbeans.gemini.ui.GeminiWindowTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

/**
 * Action to open Gemini chat window
 */
@ActionID(
        category = "Tools",
        id = "org.flossware.netbeans.gemini.actions.OpenGeminiAction"
)
@ActionRegistration(
        displayName = "#CTL_OpenGeminiAction"
)
@ActionReference(path = "Menu/Tools", position = 1000)
@Messages("CTL_OpenGeminiAction=Open Gemini Chat")
public final class OpenGeminiAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        GeminiWindowTopComponent tc = GeminiWindowTopComponent.findInstance();
        tc.open();
        tc.requestActive();
    }
}
