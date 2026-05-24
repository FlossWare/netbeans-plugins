/*
 * Copyright 2026 FlossWare.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
