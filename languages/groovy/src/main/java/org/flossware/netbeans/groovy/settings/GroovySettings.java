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
