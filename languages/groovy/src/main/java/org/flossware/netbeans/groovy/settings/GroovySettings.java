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

package org.flossware.netbeans.groovy.settings;

import org.flossware.netbeans.common.settings.AbstractSettings;

/**
 * Settings for Groovy language support.
 *
 * <p>Manages configuration for:</p>
 * <ul>
 *   <li>Groovy interpreter path</li>
 *   <li>LSP server path</li>
 *   <li>Additional runtime options</li>
 * </ul>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class GroovySettings extends AbstractSettings {

    private static final String PREF_GROOVY_PATH = "groovyPath";
    private static final String PREF_LSP_SERVER_PATH = "lspServerPath";

    private static GroovySettings instance;

    private GroovySettings() {
        super("org/flossware/netbeans/groovy");
    }

    /**
     * Get the singleton instance.
     */
    public static synchronized GroovySettings getInstance() {
        if (instance == null) {
            instance = new GroovySettings();
        }
        return instance;
    }

    /**
     * Get the Groovy interpreter path.
     */
    public String getGroovyPath() {
        return get(PREF_GROOVY_PATH, "groovy");
    }

    /**
     * Set the Groovy interpreter path.
     */
    public void setGroovyPath(String path) {
        put(PREF_GROOVY_PATH, path);
    }

    /**
     * Get the Groovy LSP server path.
     */
    public String getLspServerPath() {
        return get(PREF_LSP_SERVER_PATH, "groovy-language-server");
    }

    /**
     * Set the Groovy LSP server path.
     */
    public void setLspServerPath(String path) {
        put(PREF_LSP_SERVER_PATH, path);
    }
}
