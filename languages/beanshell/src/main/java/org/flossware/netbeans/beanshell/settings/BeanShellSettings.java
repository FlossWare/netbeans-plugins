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

package org.flossware.netbeans.beanshell.settings;

import org.flossware.netbeans.common.settings.AbstractSettings;

/**
 * Settings for BeanShell language support.
 *
 * <p>Manages configuration for:</p>
 * <ul>
 *   <li>BeanShell interpreter path</li>
 *   <li>Additional runtime options</li>
 * </ul>
 *
 * <p>Note: LSP server settings are not included as BeanShell does not
 * currently have a standard LSP implementation.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class BeanShellSettings extends AbstractSettings {

    private static final String PREF_BEANSHELL_PATH = "beanshellPath";

    private static BeanShellSettings instance;

    private BeanShellSettings() {
        super("org/flossware/netbeans/beanshell");
    }

    /**
     * Get the singleton instance.
     */
    public static synchronized BeanShellSettings getInstance() {
        if (instance == null) {
            instance = new BeanShellSettings();
        }
        return instance;
    }

    /**
     * Get the BeanShell interpreter path.
     */
    public String getBeanShellPath() {
        return get(PREF_BEANSHELL_PATH, "bsh");
    }

    /**
     * Set the BeanShell interpreter path.
     */
    public void setBeanShellPath(String path) {
        put(PREF_BEANSHELL_PATH, path);
    }
}
