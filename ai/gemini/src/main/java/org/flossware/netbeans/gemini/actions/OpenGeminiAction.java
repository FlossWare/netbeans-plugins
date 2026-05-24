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
