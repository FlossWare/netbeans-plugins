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

package org.flossware.netbeans.powershell.settings;

import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

public class PowerShellSettings {
    private static final String PREF_POWERSHELL_PATH = "powershell.path";

    private static PowerShellSettings instance;

    private final Preferences prefs;

    private PowerShellSettings() {
        prefs = NbPreferences.forModule(PowerShellSettings.class);
    }

    public static synchronized PowerShellSettings getInstance() {
        if (instance == null) {
            instance = new PowerShellSettings();
        }
        return instance;
    }

    public String getPowerShellPath() {
        return prefs.get(PREF_POWERSHELL_PATH, "pwsh");
    }

    public void setPowerShellPath(String path) {
        prefs.put(PREF_POWERSHELL_PATH, path);
    }
}
