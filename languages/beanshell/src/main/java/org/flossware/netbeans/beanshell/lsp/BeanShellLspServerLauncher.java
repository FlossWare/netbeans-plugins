package org.flossware.netbeans.beanshell.lsp;

import org.flossware.netbeans.common.lsp.AbstractLspServerLauncher;

/**
 * Launches BeanShell Language Server.
 *
 * <p>Currently, BeanShell does not have a standard Language Server Protocol
 * implementation available. This class returns null for server commands to
 * indicate LSP features are not available.</p>
 *
 * <p>Note: While LSP-based code completion is not available, basic BeanShell
 * support including script execution and project management is still provided.</p>
 *
 * <p>This class extends {@link AbstractLspServerLauncher} which provides
 * common LSP server detection and launching logic.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class BeanShellLspServerLauncher extends AbstractLspServerLauncher {

    @Override
    protected String[] getServerCommands() {
        // BeanShell does not currently have a standard LSP server
        return null;
    }

    @Override
    protected String[] getLaunchArgs(String serverCommand) {
        // No LSP server available
        return new String[0];
    }

    @Override
    protected String getInstallationInstructions() {
        StringBuilder sb = new StringBuilder();
        sb.append("BeanShell Language Server Not Available\n\n");
        sb.append("BeanShell does not currently have a standard Language Server Protocol (LSP) implementation.\n");
        sb.append("As a result, LSP-based code completion and analysis features are not available.\n\n");
        sb.append("However, this plugin still provides:\n");
        sb.append("  - Script execution (bsh command)\n");
        sb.append("  - Project management for BeanShell projects\n");
        sb.append("  - Interactive BeanShell console\n\n");
        sb.append("If a BeanShell LSP server becomes available in the future, it can be integrated\n");
        sb.append("by updating this launcher class.\n");
        return sb.toString();
    }
}
