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

package org.flossware.netbeans.prolog.settings;

import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

public class PrologSettings {
    private static final String PREF_SWIPL_PATH = "swipl.path";
    private static final String DEFAULT_SWIPL_PATH = "swipl";

    private static PrologSettings instance;
    private final Preferences prefs;

    private PrologSettings() {
        prefs = NbPreferences.forModule(PrologSettings.class);
    }

    public static synchronized PrologSettings getInstance() {
        if (instance == null) {
            instance = new PrologSettings();
        }
        return instance;
    }

    public String getSwiplPath() {
        return prefs.get(PREF_SWIPL_PATH, DEFAULT_SWIPL_PATH);
    }

    public void setSwiplPath(String path) {
        prefs.put(PREF_SWIPL_PATH, path);
    }
}
