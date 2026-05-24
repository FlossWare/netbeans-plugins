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

package org.flossware.netbeans.zsh.settings;

import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

public class ZshSettings {
    private static final String PREF_ZSH_PATH = "zsh.path";
    private static final String DEFAULT_ZSH_PATH = "zsh";

    private static ZshSettings instance;
    private final Preferences prefs;

    private ZshSettings() {
        prefs = NbPreferences.forModule(ZshSettings.class);
    }

    public static synchronized ZshSettings getInstance() {
        if (instance == null) {
            instance = new ZshSettings();
        }
        return instance;
    }

    public String getZshPath() {
        return prefs.get(PREF_ZSH_PATH, DEFAULT_ZSH_PATH);
    }

    public void setZshPath(String path) {
        prefs.put(PREF_ZSH_PATH, path);
    }
}
