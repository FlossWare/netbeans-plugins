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

package org.flossware.netbeans.batch.settings;

import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

public class BatchSettings {
    private static final String PREF_CMD_PATH = "cmd.path";

    private static BatchSettings instance;

    private final Preferences prefs;

    private BatchSettings() {
        prefs = NbPreferences.forModule(BatchSettings.class);
    }

    public static synchronized BatchSettings getInstance() {
        if (instance == null) {
            instance = new BatchSettings();
        }
        return instance;
    }

    public String getCmdPath() {
        return prefs.get(PREF_CMD_PATH, "cmd.exe");
    }

    public void setCmdPath(String path) {
        prefs.put(PREF_CMD_PATH, path);
    }
}
