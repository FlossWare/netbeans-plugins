/*
 * Copyright 2026 FlossWare.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.flossware.netbeans.common.lsp;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Validates that NetBeans LSP client module is available at runtime.
 *
 * <p>The NetBeans LSP client module (org.netbeans.modules.lsp.client) is not
 * available in Maven Central but is provided at runtime by NetBeans 22.0+.
 * This validator checks for its availability and provides clear error messages
 * if it's missing.</p>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * if (!LspClientValidator.isLspClientAvailable()) {
 *     logger.warning("LSP features disabled: " +
 *         LspClientValidator.getErrorMessage());
 *     return null;
 * }
 * }</pre>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class LspClientValidator {

    private static final Logger LOGGER = Logger.getLogger(LspClientValidator.class.getName());
    private static final String LSP_BINDINGS_CLASS = "org.netbeans.modules.lsp.client.LSPBindings";
    private static final String MINIMUM_NETBEANS_VERSION = "22.0";

    private static Boolean lspClientAvailable = null;
    private static String errorMessage = null;

    /**
     * Check if NetBeans LSP client module is available.
     *
     * <p>This method caches the result after the first check for performance.</p>
     *
     * @return true if LSP client is available, false otherwise
     */
    public static synchronized boolean isLspClientAvailable() {
        if (lspClientAvailable != null) {
            return lspClientAvailable;
        }

        try {
            // Try to load the LSPBindings class
            Class<?> lspBindingsClass = Class.forName(LSP_BINDINGS_CLASS);

            // Verify it's not our stub class by checking for a real method
            try {
                lspBindingsClass.getMethod("getBindings",
                    Class.forName("org.openide.filesystems.FileObject"));
                lspClientAvailable = true;
                errorMessage = null;
                LOGGER.log(Level.INFO, "NetBeans LSP client module is available");
            } catch (NoSuchMethodException e) {
                lspClientAvailable = false;
                errorMessage = buildStubClassErrorMessage();
                LOGGER.log(Level.WARNING, errorMessage);
            }

        } catch (ClassNotFoundException e) {
            lspClientAvailable = false;
            errorMessage = buildMissingModuleErrorMessage();
            LOGGER.log(Level.WARNING, errorMessage);
        }

        return lspClientAvailable;
    }

    /**
     * Get error message explaining why LSP client is not available.
     *
     * <p>Returns null if LSP client is available.</p>
     *
     * @return Error message or null
     */
    public static String getErrorMessage() {
        isLspClientAvailable(); // Ensure check has been performed
        return errorMessage;
    }

    /**
     * Get the minimum NetBeans version required for LSP support.
     *
     * @return Minimum NetBeans version (e.g., "22.0")
     */
    public static String getMinimumNetBeansVersion() {
        return MINIMUM_NETBEANS_VERSION;
    }

    /**
     * Validate LSP client availability and log warning if unavailable.
     *
     * <p>Convenience method that checks availability and logs a warning
     * with the error message if LSP client is not available.</p>
     *
     * @param logger The logger to use for warnings
     * @return true if LSP client is available, false otherwise
     */
    public static boolean validateAndWarn(Logger logger) {
        if (!isLspClientAvailable()) {
            logger.log(Level.WARNING, getErrorMessage());
            return false;
        }
        return true;
    }

    /**
     * Reset the cached validation state.
     *
     * <p>For testing purposes only. Not normally needed in production.</p>
     */
    static synchronized void resetCache() {
        lspClientAvailable = null;
        errorMessage = null;
    }

    /**
     * Build error message for when LSP client module is missing.
     */
    private static String buildMissingModuleErrorMessage() {
        return String.format(
            "NetBeans LSP client module not found. " +
            "Language support features require NetBeans %s or later. " +
            "Current NetBeans installation does not include " +
            "org.netbeans.modules.lsp.client module. " +
            "Please upgrade to NetBeans %s or later for LSP-based language support.",
            MINIMUM_NETBEANS_VERSION, MINIMUM_NETBEANS_VERSION
        );
    }

    /**
     * Build error message for when stub class is being used.
     */
    private static String buildStubClassErrorMessage() {
        return String.format(
            "LSP client stub class detected. " +
            "The compilation stub for org.netbeans.modules.lsp.client.LSPBindings " +
            "is being used instead of the real NetBeans implementation. " +
            "This plugin requires NetBeans %s or later with LSP client support. " +
            "If running in NetBeans %s+, this indicates a module loading issue.",
            MINIMUM_NETBEANS_VERSION, MINIMUM_NETBEANS_VERSION
        );
    }

    // Private constructor to prevent instantiation
    private LspClientValidator() {
        throw new UnsupportedOperationException("Utility class");
    }
}
