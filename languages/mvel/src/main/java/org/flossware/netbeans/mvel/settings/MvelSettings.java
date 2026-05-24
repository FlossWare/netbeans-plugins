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

package org.flossware.netbeans.mvel.settings;

import java.util.HashMap;
import java.util.Map;
import org.flossware.netbeans.common.settings.AbstractSettings;

/**
 * Settings for MVEL language support.
 *
 * <p>Manages configuration for:</p>
 * <ul>
 *   <li>MVEL interpreter path (if using custom interpreter)</li>
 *   <li>LSP server path (for future LSP support)</li>
 *   <li>Additional runtime options</li>
 * </ul>
 *
 * <p><b>Note:</b> MVEL is typically used as an embedded expression language
 * in Java applications rather than as a standalone interpreted language.
 * Most users will not need to configure a standalone interpreter.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class MvelSettings extends AbstractSettings {

    private static final String PREF_MVEL_INTERPRETER_PATH = "mvelInterpreterPath";
    private static final String PREF_LSP_SERVER_PATH = "lspServerPath";

    private static MvelSettings instance;

    private MvelSettings() {
    }

    /**
     * Get the singleton instance.
     */
    public static synchronized MvelSettings getInstance() {
        if (instance == null) {
            instance = new MvelSettings();
        }
        return instance;
    }

    @Override
    protected String getPreferenceModule() {
        return "org.flossware.netbeans.mvel.settings.MvelSettings";
    }

    @Override
    protected Map<String, String> getDefaultValues() {
        Map<String, String> defaults = new HashMap<>();
        defaults.put(PREF_MVEL_INTERPRETER_PATH, "");
        defaults.put(PREF_LSP_SERVER_PATH, "");
        return defaults;
    }

    /**
     * Get the MVEL interpreter path.
     *
     * <p><b>Note:</b> MVEL does not have a standard standalone interpreter.
     * This setting is provided for users who have created a custom runner
     * or are using a third-party MVEL execution tool.</p>
     *
     * @return The interpreter path, or empty string if not configured
     */
    public String getMvelInterpreterPath() {
        return getSetting(PREF_MVEL_INTERPRETER_PATH);
    }

    /**
     * Set the MVEL interpreter path.
     *
     * @param path The path to the MVEL interpreter or custom runner
     */
    public void setMvelInterpreterPath(String path) {
        setSetting(PREF_MVEL_INTERPRETER_PATH, path);
    }

    /**
     * Get the MVEL LSP server path.
     *
     * <p><b>Note:</b> MVEL does not currently have a widely-adopted LSP server.
     * This setting is provided for future compatibility.</p>
     *
     * @return The LSP server path, or empty string if not configured
     */
    public String getLspServerPath() {
        return getSetting(PREF_LSP_SERVER_PATH);
    }

    /**
     * Set the MVEL LSP server path.
     *
     * @param path The path to the MVEL LSP server
     */
    public void setLspServerPath(String path) {
        setSetting(PREF_LSP_SERVER_PATH, path);
    }
}
