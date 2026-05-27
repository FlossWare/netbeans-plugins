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

package org.flossware.netbeans.javascript.settings;

import java.util.HashMap;
import java.util.Map;
import org.flossware.netbeans.common.settings.AbstractSettings;

/**
 * Settings for JavaScript plugin.
 *
 * <p>Stores user preferences for JavaScript runtime path, language server, etc.</p>
 *
 * <p>This class extends {@link AbstractSettings} which provides common
 * settings management using NetBeans preferences API.</p>
 */
public class JavaScriptSettings extends AbstractSettings {

    private static final String PREF_JAVASCRIPT_PATH = "javascript.path";
    private static final String PREF_LSP_SERVER = "lsp.server";
    private static final String PREF_NODE_AUTO_DETECT = "node.auto.detect";

    private static final String DEFAULT_JAVASCRIPT_PATH = "node";
    private static final String DEFAULT_LSP_SERVER = "auto"; // auto, typescript-language-server, vscode-langservers-extracted

    private static final JavaScriptSettings INSTANCE = new JavaScriptSettings();

    /**
     * Get the singleton instance.
     *
     * @return The JavaScriptSettings instance
     */
    public static JavaScriptSettings getInstance() {
        return INSTANCE;
    }

    @Override
    protected String getPreferenceModule() {
        return "org.flossware.netbeans.javascript.settings.JavaScriptSettings";
    }

    @Override
    protected Map<String, String> getDefaultValues() {
        Map<String, String> defaults = new HashMap<>();
        defaults.put(PREF_JAVASCRIPT_PATH, DEFAULT_JAVASCRIPT_PATH);
        defaults.put(PREF_LSP_SERVER, DEFAULT_LSP_SERVER);
        return defaults;
    }

    /**
     * Get the JavaScript runtime path.
     *
     * @return The JavaScript runtime path
     */
    public String getJavaScriptPath() {
        return getSetting(PREF_JAVASCRIPT_PATH);
    }

    /**
     * Set the JavaScript runtime path.
     *
     * @param path The JavaScript runtime path
     */
    public void setJavaScriptPath(String path) {
        setSetting(PREF_JAVASCRIPT_PATH, path);
    }

    /**
     * Get the preferred LSP server.
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
     * Check if Node.js auto-detection is enabled.
     *
     * @return true if enabled, false otherwise
     */
    public boolean isNodeAutoDetectEnabled() {
        return getBooleanSetting(PREF_NODE_AUTO_DETECT, true);
    }

    /**
     * Enable or disable Node.js auto-detection.
     *
     * @param enabled true to enable, false to disable
     */
    public void setNodeAutoDetectEnabled(boolean enabled) {
        setBooleanSetting(PREF_NODE_AUTO_DETECT, enabled);
    }
}
