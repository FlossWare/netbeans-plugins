package org.flossware.netbeans.mvel.settings;

import org.flossware.netbeans.common.settings.AbstractSettings;

/**
 * Settings for MVEL language support.
 *
 * <p>Manages configuration for:</p>
 * <ul>
 *   <li>MVEL interpreter path (if using custom interpreter)</li>
 *   <li>LSP server path (for future LSP support)</li>
 *   <li>Additional runtime options</li>
 * </ul>
 *
 * <p><b>Note:</b> MVEL is typically used as an embedded expression language
 * in Java applications rather than as a standalone interpreted language.
 * Most users will not need to configure a standalone interpreter.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class MvelSettings extends AbstractSettings {

    private static final String PREF_MVEL_INTERPRETER_PATH = "mvelInterpreterPath";
    private static final String PREF_LSP_SERVER_PATH = "lspServerPath";

    private static MvelSettings instance;

    private MvelSettings() {
        super("org/flossware/netbeans/mvel");
    }

    /**
     * Get the singleton instance.
     */
    public static synchronized MvelSettings getInstance() {
        if (instance == null) {
            instance = new MvelSettings();
        }
        return instance;
    }

    /**
     * Get the MVEL interpreter path.
     *
     * <p><b>Note:</b> MVEL does not have a standard standalone interpreter.
     * This setting is provided for users who have created a custom runner
     * or are using a third-party MVEL execution tool.</p>
     *
     * @return The interpreter path, or empty string if not configured
     */
    public String getMvelInterpreterPath() {
        return get(PREF_MVEL_INTERPRETER_PATH, "");
    }

    /**
     * Set the MVEL interpreter path.
     *
     * @param path The path to the MVEL interpreter or custom runner
     */
    public void setMvelInterpreterPath(String path) {
        put(PREF_MVEL_INTERPRETER_PATH, path);
    }

    /**
     * Get the MVEL LSP server path.
     *
     * <p><b>Note:</b> MVEL does not currently have a widely-adopted LSP server.
     * This setting is provided for future compatibility.</p>
     *
     * @return The LSP server path, or empty string if not configured
     */
    public String getLspServerPath() {
        return get(PREF_LSP_SERVER_PATH, "");
    }

    /**
     * Set the MVEL LSP server path.
     *
     * @param path The path to the MVEL LSP server
     */
    public void setLspServerPath(String path) {
        put(PREF_LSP_SERVER_PATH, path);
    }
}
