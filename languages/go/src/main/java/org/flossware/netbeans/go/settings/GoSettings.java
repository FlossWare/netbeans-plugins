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

package org.flossware.netbeans.go.settings;

import java.util.HashMap;
import java.util.Map;
import org.flossware.netbeans.common.settings.AbstractSettings;

/**
 * Settings for Go plugin.
 *
 * <p>Stores user preferences for Go interpreter path, language server, etc.</p>
 *
 * <p>This class extends {@link AbstractSettings} which provides common
 * settings management using NetBeans preferences API.</p>
 */
public class GoSettings extends AbstractSettings {

    private static final String PREF_GO_PATH = "go.path";
    private static final String PREF_LSP_SERVER = "lsp.server";
    private static final String PREF_MODULE_AUTO_DETECT = "module.auto.detect";

    private static final String DEFAULT_GO_PATH = "go";
    private static final String DEFAULT_LSP_SERVER = "gopls";

    private static final GoSettings INSTANCE = new GoSettings();

    /**
     * Get the singleton instance.
     *
     * @return The GoSettings instance
     */
    public static GoSettings getInstance() {
        return INSTANCE;
    }

    @Override
    protected String getPreferenceModule() {
        return "org.flossware.netbeans.go.settings.GoSettings";
    }

    @Override
    protected Map<String, String> getDefaultValues() {
        Map<String, String> defaults = new HashMap<>();
        defaults.put(PREF_GO_PATH, DEFAULT_GO_PATH);
        defaults.put(PREF_LSP_SERVER, DEFAULT_LSP_SERVER);
        return defaults;
    }

    /**
     * Get the Go interpreter path.
     *
     * @return The Go interpreter path
     */
    public String getGoPath() {
        return getSetting(PREF_GO_PATH);
    }

    /**
     * Set the Go interpreter path.
     *
     * @param path The Go interpreter path
     */
    public void setGoPath(String path) {
        setSetting(PREF_GO_PATH, path);
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
     * Check if Go module auto-detection is enabled.
     *
     * @return true if enabled, false otherwise
     */
    public boolean isModuleAutoDetectEnabled() {
        return getBooleanSetting(PREF_MODULE_AUTO_DETECT, true);
    }

    /**
     * Enable or disable Go module auto-detection.
     *
     * @param enabled true to enable, false to disable
     */
    public void setModuleAutoDetectEnabled(boolean enabled) {
        setBooleanSetting(PREF_MODULE_AUTO_DETECT, enabled);
    }
}
