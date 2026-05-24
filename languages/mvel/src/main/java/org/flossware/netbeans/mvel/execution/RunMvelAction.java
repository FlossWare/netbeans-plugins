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

package org.flossware.netbeans.mvel.execution;

import org.flossware.netbeans.common.execution.AbstractRunAction;
import org.flossware.netbeans.mvel.settings.MvelSettings;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.util.NbBundle;

/**
 * Action to run MVEL scripts.
 *
 * <p><b>Note:</b> MVEL is primarily an embedded expression language and does not
 * have a widely-used standalone interpreter. This action is provided for
 * completeness, but executing MVEL files typically requires:</p>
 * <ul>
 *   <li>A custom Java runner that embeds the MVEL runtime</li>
 *   <li>Integration with a framework that supports MVEL (e.g., Drools)</li>
 *   <li>A project-specific execution environment</li>
 * </ul>
 *
 * <p>The default implementation attempts to use a configured MVEL interpreter
 * if one exists, but users will typically need to configure a custom execution
 * environment for their specific use case.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
@ActionID(
    category = "Run",
    id = "org.flossware.netbeans.mvel.execution.RunMvelAction"
)
@ActionRegistration(
    displayName = "#CTL_RunMvelAction"
)
@ActionReference(
    path = "Loaders/text/x-mvel/Actions",
    position = 100
)
@NbBundle.Messages("CTL_RunMvelAction=Run MVEL Script")
public class RunMvelAction extends AbstractRunAction {

    @Override
    protected String[] getCommand(FileObject file) {
        MvelSettings settings = MvelSettings.getInstance();
        String mvelPath = settings.getMvelInterpreterPath();

        if (mvelPath == null || mvelPath.isEmpty()) {
            // No interpreter configured - show helpful message
            // This will be caught and displayed by AbstractRunAction
            throw new IllegalStateException(
                "MVEL interpreter not configured.\n\n" +
                "MVEL is typically used as an embedded expression language in Java applications.\n" +
                "To execute MVEL files, configure a custom interpreter or runner in:\n" +
                "Tools > Options > Miscellaneous > MVEL\n\n" +
                "Alternatively, integrate MVEL execution into your build system or\n" +
                "use it within a framework that supports MVEL (e.g., Drools)."
            );
        }

        return new String[]{mvelPath, file.getPath()};
    }

    @Override
    protected String getLanguageName() {
        return "MVEL";
    }

    @Override
    protected String getFileExtension() {
        return "mvel";
    }
}
