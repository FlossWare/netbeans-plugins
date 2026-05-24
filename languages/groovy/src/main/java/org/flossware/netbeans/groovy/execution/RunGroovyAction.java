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

package org.flossware.netbeans.groovy.execution;

import org.flossware.netbeans.common.execution.AbstractRunAction;
import org.flossware.netbeans.groovy.settings.GroovySettings;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.util.NbBundle;

/**
 * Action to run Groovy scripts.
 *
 * <p>Executes Groovy scripts using the configured Groovy interpreter and
 * displays output in the NetBeans output window.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
@ActionID(
    category = "Run",
    id = "org.flossware.netbeans.groovy.execution.RunGroovyAction"
)
@ActionRegistration(
    displayName = "#CTL_RunGroovyAction"
)
@ActionReference(
    path = "Loaders/text/x-groovy/Actions",
    position = 100
)
@NbBundle.Messages("CTL_RunGroovyAction=Run Groovy Script")
public class RunGroovyAction extends AbstractRunAction {

    @Override
    protected String[] getCommand(FileObject file) {
        GroovySettings settings = GroovySettings.getInstance();
        String groovyPath = settings.getGroovyPath();

        if (groovyPath == null || groovyPath.isEmpty()) {
            groovyPath = "groovy"; // Default to PATH
        }

        return new String[]{groovyPath, file.getPath()};
    }

    @Override
    protected String getLanguageName() {
        return "Groovy";
    }

    @Override
    protected String getFileExtension() {
        return "groovy";
    }
}
