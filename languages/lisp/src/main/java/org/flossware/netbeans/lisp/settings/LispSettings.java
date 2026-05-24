/*
 * Copyright 2026 FlossWare.
 */

package org.flossware.netbeans.lisp.settings;

import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

public class LispSettings {
    private static final String PREF_SBCL_PATH = "sbcl.path";
    private static final String DEFAULT_SBCL_PATH = "sbcl";

    private static LispSettings instance;
    private final Preferences prefs;

    private LispSettings() {
        prefs = NbPreferences.forModule(LispSettings.class);
    }

    public static synchronized LispSettings getInstance() {
        if (instance == null) {
            instance = new LispSettings();
        }
        return instance;
    }

    public String getSbclPath() {
        return prefs.get(PREF_SBCL_PATH, DEFAULT_SBCL_PATH);
    }

    public void setSbclPath(String path) {
        prefs.put(PREF_SBCL_PATH, path);
    }
}
