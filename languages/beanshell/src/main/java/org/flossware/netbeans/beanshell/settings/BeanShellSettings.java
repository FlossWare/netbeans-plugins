package org.flossware.netbeans.beanshell.settings;

import org.flossware.netbeans.common.settings.AbstractSettings;

/**
 * Settings for BeanShell language support.
 *
 * <p>Manages configuration for:</p>
 * <ul>
 *   <li>BeanShell interpreter path</li>
 *   <li>Additional runtime options</li>
 * </ul>
 *
 * <p>Note: LSP server settings are not included as BeanShell does not
 * currently have a standard LSP implementation.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class BeanShellSettings extends AbstractSettings {

    private static final String PREF_BEANSHELL_PATH = "beanshellPath";

    private static BeanShellSettings instance;

    private BeanShellSettings() {
        super("org/flossware/netbeans/beanshell");
    }

    /**
     * Get the singleton instance.
     */
    public static synchronized BeanShellSettings getInstance() {
        if (instance == null) {
            instance = new BeanShellSettings();
        }
        return instance;
    }

    /**
     * Get the BeanShell interpreter path.
     */
    public String getBeanShellPath() {
        return get(PREF_BEANSHELL_PATH, "bsh");
    }

    /**
     * Set the BeanShell interpreter path.
     */
    public void setBeanShellPath(String path) {
        put(PREF_BEANSHELL_PATH, path);
    }
}
