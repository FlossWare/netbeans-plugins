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

package org.flossware.netbeans.rust.settings;

import java.util.HashMap;
import java.util.Map;
import org.flossware.netbeans.common.settings.AbstractSettings;

/**
 * Settings for Rust plugin.
 *
 * <p>Stores user preferences for Rust compiler path, language server, etc.</p>
 *
 * <p>This class extends {@link AbstractSettings} which provides common
 * settings management using NetBeans preferences API.</p>
 */
public class RustSettings extends AbstractSettings {

    private static final String PREF_RUSTC_PATH = "rust.rustc.path";
    private static final String PREF_LSP_SERVER = "lsp.server";
    private static final String PREF_CARGO_AUTO_DETECT = "cargo.auto.detect";

    private static final String DEFAULT_RUSTC_PATH = "rustc";
    private static final String DEFAULT_LSP_SERVER = "rust-analyzer";

    private static final RustSettings INSTANCE = new RustSettings();

    /**
     * Get the singleton instance.
     *
     * @return The RustSettings instance
     */
    public static RustSettings getInstance() {
        return INSTANCE;
    }

    @Override
    protected String getPreferenceModule() {
        return "org.flossware.netbeans.rust.settings.RustSettings";
    }

    @Override
    protected Map<String, String> getDefaultValues() {
        Map<String, String> defaults = new HashMap<>();
        defaults.put(PREF_RUSTC_PATH, DEFAULT_RUSTC_PATH);
        defaults.put(PREF_LSP_SERVER, DEFAULT_LSP_SERVER);
        return defaults;
    }

    /**
     * Get the Rust compiler path.
     *
     * @return The Rust compiler path
     */
    public String getRustcPath() {
        return getSetting(PREF_RUSTC_PATH);
    }

    /**
     * Set the Rust compiler path.
     *
     * @param path The Rust compiler path
     */
    public void setRustcPath(String path) {
        setSetting(PREF_RUSTC_PATH, path);
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
     * Check if Cargo project auto-detection is enabled.
     *
     * @return true if enabled, false otherwise
     */
    public boolean isCargoAutoDetectEnabled() {
        return getBooleanSetting(PREF_CARGO_AUTO_DETECT, true);
    }

    /**
     * Enable or disable Cargo project auto-detection.
     *
     * @param enabled true to enable, false to disable
     */
    public void setCargoAutoDetectEnabled(boolean enabled) {
        setBooleanSetting(PREF_CARGO_AUTO_DETECT, enabled);
    }
}
