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
