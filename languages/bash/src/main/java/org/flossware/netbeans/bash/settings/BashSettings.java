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

package org.flossware.netbeans.bash.settings;

import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

public class BashSettings {
    private static final String PREF_BASH_PATH = "bash.path";
    private static final String PREF_BASHDB_PATH = "bashdb.path";

    private static BashSettings instance;

    private final Preferences prefs;

    private BashSettings() {
        prefs = NbPreferences.forModule(BashSettings.class);
    }

    public static synchronized BashSettings getInstance() {
        if (instance == null) {
            instance = new BashSettings();
        }
        return instance;
    }

    public String getBashPath() {
        return prefs.get(PREF_BASH_PATH, "bash");
    }

    public void setBashPath(String path) {
        prefs.put(PREF_BASH_PATH, path);
    }

    public String getBashdbPath() {
        return prefs.get(PREF_BASHDB_PATH, "bashdb");
    }

    public void setBashdbPath(String path) {
        prefs.put(PREF_BASHDB_PATH, path);
    }
}
