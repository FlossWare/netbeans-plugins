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

package org.flossware.netbeans.ruby.settings;

import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

public class RubySettings {
    private static final String PREF_RUBY_PATH = "ruby.path";
    private static final String DEFAULT_RUBY_PATH = "ruby";

    private static RubySettings instance;
    private final Preferences prefs;

    private RubySettings() {
        prefs = NbPreferences.forModule(RubySettings.class);
    }

    public static synchronized RubySettings getInstance() {
        if (instance == null) {
            instance = new RubySettings();
        }
        return instance;
    }

    public String getRubyPath() {
        return prefs.get(PREF_RUBY_PATH, DEFAULT_RUBY_PATH);
    }

    public void setRubyPath(String path) {
        prefs.put(PREF_RUBY_PATH, path);
    }
}
