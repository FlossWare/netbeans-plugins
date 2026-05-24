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

package org.flossware.netbeans.erlang.settings;

import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

public class ErlangSettings {
    private static final String PREF_ERL_PATH = "erl.path";
    private static final String DEFAULT_ERL_PATH = "erl";

    private static ErlangSettings instance;
    private final Preferences prefs;

    private ErlangSettings() {
        prefs = NbPreferences.forModule(ErlangSettings.class);
    }

    public static synchronized ErlangSettings getInstance() {
        if (instance == null) {
            instance = new ErlangSettings();
        }
        return instance;
    }

    public String getErlPath() {
        return prefs.get(PREF_ERL_PATH, DEFAULT_ERL_PATH);
    }

    public void setErlPath(String path) {
        prefs.put(PREF_ERL_PATH, path);
    }
}
