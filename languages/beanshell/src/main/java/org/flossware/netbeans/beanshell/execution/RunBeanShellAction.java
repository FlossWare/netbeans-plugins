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

package org.flossware.netbeans.beanshell.execution;

import org.flossware.netbeans.common.execution.AbstractRunAction;
import org.flossware.netbeans.beanshell.settings.BeanShellSettings;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.util.NbBundle;

/**
 * Action to run BeanShell scripts.
 *
 * <p>Executes BeanShell scripts using the configured BeanShell interpreter and
 * displays output in the NetBeans output window.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
@ActionID(
    category = "Run",
    id = "org.flossware.netbeans.beanshell.execution.RunBeanShellAction"
)
@ActionRegistration(
    displayName = "#CTL_RunBeanShellAction"
)
@ActionReference(
    path = "Loaders/text/x-beanshell/Actions",
    position = 100
)
@NbBundle.Messages("CTL_RunBeanShellAction=Run BeanShell Script")
public class RunBeanShellAction extends AbstractRunAction {

    @Override
    protected String[] getCommand(FileObject file) {
        BeanShellSettings settings = BeanShellSettings.getInstance();
        String bshPath = settings.getBeanShellPath();

        if (bshPath == null || bshPath.isEmpty()) {
            bshPath = "bsh"; // Default to PATH
        }

        return new String[]{bshPath, file.getPath()};
    }

    @Override
    protected String getLanguageName() {
        return "BeanShell";
    }

    @Override
    protected String getFileExtension() {
        return "bsh";
    }
}
