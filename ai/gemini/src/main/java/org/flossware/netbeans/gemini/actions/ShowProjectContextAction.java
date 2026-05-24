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
import org.flossware.netbeans.gemini.context.ProjectContextManager;
import org.flossware.netbeans.gemini.ui.GeminiWindowTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

/**
 * Action to show project context in Gemini window
 */
@ActionID(
        category = "Tools",
        id = "org.flossware.netbeans.gemini.actions.ShowProjectContextAction"
)
@ActionRegistration(
        displayName = "#CTL_ShowProjectContextAction"
)
@ActionReference(path = "Menu/Tools", position = 1001)
@Messages("CTL_ShowProjectContextAction=Show Project Context (Gemini)")
public final class ShowProjectContextAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        ProjectContextManager contextManager = ProjectContextManager.getInstance();
        String summary = contextManager.getProjectSummary();

        GeminiWindowTopComponent tc = GeminiWindowTopComponent.findInstance();
        tc.open();
        tc.requestActive();
        tc.showProjectContext(summary);
    }
}
