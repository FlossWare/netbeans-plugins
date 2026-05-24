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

package org.flossware.netbeans.mvel.ui;

import org.flossware.netbeans.common.ui.AbstractConsoleTopComponent;
import org.flossware.netbeans.mvel.settings.MvelSettings;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;

/**
 * MVEL interactive console window.
 *
 * <p><b>Note:</b> MVEL does not have a standard interactive REPL (Read-Eval-Print Loop)
 * like some other scripting languages. This console is provided for completeness,
 * but requires a custom MVEL interpreter or runner to be configured.</p>
 *
 * <p>MVEL is typically used as:</p>
 * <ul>
 *   <li>An embedded expression evaluator in Java applications</li>
 *   <li>A template engine within frameworks</li>
 *   <li>Part of rule engines (e.g., Drools)</li>
 * </ul>
 *
 * <p>To use this console, configure a custom MVEL interpreter in the settings
 * that provides interactive execution capabilities.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
@TopComponent.Description(
    preferredID = "MvelConsoleTopComponent",
    persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(
    mode = "output",
    openAtStartup = false
)
@TopComponent.OpenActionRegistration(
    displayName = "#CTL_MvelConsoleAction",
    preferredID = "MvelConsoleTopComponent"
)
@NbBundle.Messages({
    "CTL_MvelConsoleAction=MVEL Console",
    "CTL_MvelConsoleTopComponent=MVEL Console",
    "HINT_MvelConsoleTopComponent=Interactive MVEL console"
})
public class MvelConsoleTopComponent extends AbstractConsoleTopComponent {

    public MvelConsoleTopComponent() {
        setName(Bundle.CTL_MvelConsoleTopComponent());
        setToolTipText(Bundle.HINT_MvelConsoleTopComponent());
    }

    @Override
    protected String[] getInterpreterCommand() {
        MvelSettings settings = MvelSettings.getInstance();
        String interpreterPath = settings.getMvelInterpreterPath();

        if (interpreterPath == null || interpreterPath.isEmpty()) {
            // Return null to indicate no interpreter configured
            // AbstractConsoleTopComponent will handle showing an error message
            return null;
        }

        return new String[]{interpreterPath};
    }

    @Override
    protected String getLanguageName() {
        return "MVEL";
    }

    @Override
    protected String getWelcomeMessage() {
        return "MVEL Interactive Console\n" +
               "Note: MVEL does not have a standard interactive interpreter.\n" +
               "A custom interpreter must be configured in Tools > Options > Miscellaneous > MVEL.\n\n" +
               "MVEL is typically used as an embedded expression language in Java applications.\n";
    }
}
