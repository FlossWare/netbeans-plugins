/*
 * Copyright 2026 FlossWare.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
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
