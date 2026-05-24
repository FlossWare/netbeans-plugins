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

package org.flossware.netbeans.common.lsp;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Validator for NetBeans LSP client availability.
 *
 * <p>This class provides runtime validation of the NetBeans LSP client module
 * (org-netbeans-modules-lsp-client). Since this module is not available at
 * compile time via Maven Central, we need to check for its availability at
 * runtime.</p>
 *
 * <p>The LSP client module is available in NetBeans 22.0 and later.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class LspClientValidator {

    private static final String LSP_CLIENT_CLASS = "org.netbeans.modules.lsp.client.LSPBindings";
    private static Boolean lspClientAvailable;

    /**
     * Check if the NetBeans LSP client module is available at runtime.
     *
     * <p>This method attempts to load the LSPBindings class to verify that
     * the LSP client module is present in the NetBeans installation.</p>
     *
     * @return true if LSP client is available, false otherwise
     */
    public static synchronized boolean isLspClientAvailable() {
        if (lspClientAvailable == null) {
            try {
                Class.forName(LSP_CLIENT_CLASS);
                lspClientAvailable = true;
            } catch (ClassNotFoundException e) {
                lspClientAvailable = false;
            }
        }
        return lspClientAvailable;
    }

    /**
     * Validate LSP client availability and log a warning if not available.
     *
     * <p>This method should be called during plugin initialization to inform
     * users if LSP features will not be available.</p>
     *
     * @param logger The logger to use for warning messages
     */
    public static void validateAndWarn(Logger logger) {
        if (!isLspClientAvailable()) {
            if (logger != null) {
                logger.log(Level.WARNING,
                    "NetBeans LSP client module (org-netbeans-modules-lsp-client) is not available. " +
                    "LSP-based code completion and language server features will not work. " +
                    "Please use NetBeans 22.0 or later for full LSP support.");
            }
        }
    }

    /**
     * Reset the cached availability check.
     *
     * <p>This method is primarily for testing purposes.</p>
     */
    static void reset() {
        lspClientAvailable = null;
    }

    private LspClientValidator() {
        // Utility class - no instances
    }
}
