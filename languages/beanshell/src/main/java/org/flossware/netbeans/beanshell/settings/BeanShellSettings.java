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

package org.flossware.netbeans.beanshell.settings;

import java.util.HashMap;
import java.util.Map;
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

    @Override
    protected String getPreferenceModule() {
        return "org.flossware.netbeans.beanshell.settings.BeanShellSettings";
    }

    @Override
    protected Map<String, String> getDefaultValues() {
        Map<String, String> defaults = new HashMap<>();
        defaults.put(PREF_BEANSHELL_PATH, "bsh");
        return defaults;
    }

    /**
     * Get the BeanShell interpreter path.
     */
    public String getBeanShellPath() {
        return getSetting(PREF_BEANSHELL_PATH);
    }

    /**
     * Set the BeanShell interpreter path.
     */
    public void setBeanShellPath(String path) {
        setSetting(PREF_BEANSHELL_PATH, path);
    }
}
