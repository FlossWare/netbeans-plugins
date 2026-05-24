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
