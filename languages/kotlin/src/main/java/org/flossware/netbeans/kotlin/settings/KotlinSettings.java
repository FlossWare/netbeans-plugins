/*
 * Copyright 2026 FlossWare.
 */

package org.flossware.netbeans.kotlin.settings;

import java.util.prefs.Preferences;
import org.openide.util.NbPreferences;

public class KotlinSettings {
    private static final String PREF_KOTLINC_PATH = "kotlinc.path";
    private static final String DEFAULT_KOTLINC_PATH = "kotlinc";

    private static KotlinSettings instance;
    private final Preferences prefs;

    private KotlinSettings() {
        prefs = NbPreferences.forModule(KotlinSettings.class);
    }

    public static synchronized KotlinSettings getInstance() {
        if (instance == null) {
            instance = new KotlinSettings();
        }
        return instance;
    }

    public String getKotlincPath() {
        return prefs.get(PREF_KOTLINC_PATH, DEFAULT_KOTLINC_PATH);
    }

    public void setKotlincPath(String path) {
        prefs.put(PREF_KOTLINC_PATH, path);
    }
}
