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

package org.flossware.netbeans.csharp.settings;

import java.util.HashMap;
import java.util.Map;
import org.flossware.netbeans.common.settings.AbstractSettings;

/**
 * Settings for C# plugin.
 *
 * <p>Stores user preferences for C# interpreter path, language server, etc.</p>
 *
 * <p>This class extends {@link AbstractSettings} which provides common
 * settings management using NetBeans preferences API.</p>
 */
public class CSharpSettings extends AbstractSettings {

    private static final String PREF_CSHARP_PATH = "csharp.path";
    private static final String PREF_LSP_SERVER = "lsp.server";
    private static final String PREF_DOTNET_AUTO_DETECT = "dotnet.auto.detect";

    private static final String DEFAULT_CSHARP_PATH = "dotnet";
    private static final String DEFAULT_LSP_SERVER = "auto"; // auto, omnisharp

    private static final CSharpSettings INSTANCE = new CSharpSettings();

    /**
     * Get the singleton instance.
     *
     * @return The CSharpSettings instance
     */
    public static CSharpSettings getInstance() {
        return INSTANCE;
    }

    @Override
    protected String getPreferenceModule() {
        return "org.flossware.netbeans.csharp.settings.CSharpSettings";
    }

    @Override
    protected Map<String, String> getDefaultValues() {
        Map<String, String> defaults = new HashMap<>();
        defaults.put(PREF_CSHARP_PATH, DEFAULT_CSHARP_PATH);
        defaults.put(PREF_LSP_SERVER, DEFAULT_LSP_SERVER);
        return defaults;
    }

    /**
     * Get the C# interpreter path.
     *
     * @return The C# interpreter path
     */
    public String getCSharpPath() {
        return getSetting(PREF_CSHARP_PATH);
    }

    /**
     * Set the C# interpreter path.
     *
     * @param path The C# interpreter path
     */
    public void setCSharpPath(String path) {
        setSetting(PREF_CSHARP_PATH, path);
    }

    /**
     * Get the preferred LSP server (auto or omnisharp).
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
     * Check if .NET SDK auto-detection is enabled.
     *
     * @return true if enabled, false otherwise
     */
    public boolean isDotNetAutoDetectEnabled() {
        return getBooleanSetting(PREF_DOTNET_AUTO_DETECT, true);
    }

    /**
     * Enable or disable .NET SDK auto-detection.
     *
     * @param enabled true to enable, false to disable
     */
    public void setDotNetAutoDetectEnabled(boolean enabled) {
        setBooleanSetting(PREF_DOTNET_AUTO_DETECT, enabled);
    }
}
