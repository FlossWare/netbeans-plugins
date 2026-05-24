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

package org.flossware.netbeans.python.settings;

import java.util.HashMap;
import java.util.Map;
import org.flossware.netbeans.common.settings.AbstractSettings;

/**
 * Settings for Python plugin.
 *
 * <p>Stores user preferences for Python interpreter path, language server, etc.</p>
 *
 * <p>This class extends {@link AbstractSettings} which provides common
 * settings management using NetBeans preferences API.</p>
 */
public class PythonSettings extends AbstractSettings {

    private static final String PREF_PYTHON_PATH = "python.path";
    private static final String PREF_LSP_SERVER = "lsp.server";
    private static final String PREF_VENV_AUTO_DETECT = "venv.auto.detect";

    private static final String DEFAULT_PYTHON_PATH = "python";
    private static final String DEFAULT_LSP_SERVER = "auto"; // auto, pyright, pylsp

    private static final PythonSettings INSTANCE = new PythonSettings();

    /**
     * Get the singleton instance.
     *
     * @return The PythonSettings instance
     */
    public static PythonSettings getInstance() {
        return INSTANCE;
    }

    @Override
    protected String getPreferenceModule() {
        return "org.flossware.netbeans.python.settings.PythonSettings";
    }

    @Override
    protected Map<String, String> getDefaultValues() {
        Map<String, String> defaults = new HashMap<>();
        defaults.put(PREF_PYTHON_PATH, DEFAULT_PYTHON_PATH);
        defaults.put(PREF_LSP_SERVER, DEFAULT_LSP_SERVER);
        return defaults;
    }

    /**
     * Get the Python interpreter path.
     *
     * @return The Python interpreter path
     */
    public String getPythonPath() {
        return getSetting(PREF_PYTHON_PATH);
    }

    /**
     * Set the Python interpreter path.
     *
     * @param path The Python interpreter path
     */
    public void setPythonPath(String path) {
        setSetting(PREF_PYTHON_PATH, path);
    }

    /**
     * Get the preferred LSP server (auto, pyright, or pylsp).
     *
     * @return The LSP server preference
     */
    public String getLspServer() {
        return getSetting(PREF_LSP_SERVER);
    }

    /**
     * Set the preferred LSP server.
     *
     * @param server The LSP server preference
     */
    public void setLspServer(String server) {
        setSetting(PREF_LSP_SERVER, server);
    }

    /**
     * Check if virtual environment auto-detection is enabled.
     *
     * @return true if enabled, false otherwise
     */
    public boolean isVenvAutoDetectEnabled() {
        return getBooleanSetting(PREF_VENV_AUTO_DETECT, true);
    }

    /**
     * Enable or disable virtual environment auto-detection.
     *
     * @param enabled true to enable, false to disable
     */
    public void setVenvAutoDetectEnabled(boolean enabled) {
        setBooleanSetting(PREF_VENV_AUTO_DETECT, enabled);
    }
}
