package org.flossware.netbeans.groovy.lsp;

import org.flossware.netbeans.common.lsp.AbstractLspServerLauncher;

/**
 * Launches Groovy Language Server.
 *
 * <p>Detects which language server is installed on the system and provides
 * the appropriate command to start it.</p>
 *
 * <p>Supported language servers:</p>
 * <ul>
 *   <li><b>groovy-language-server</b> - Groovy LSP server (recommended)</li>
 * </ul>
 *
 * <p>This class extends {@link AbstractLspServerLauncher} which provides
 * common LSP server detection and launching logic.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class GroovyLspServerLauncher extends AbstractLspServerLauncher {

    @Override
    protected String[] getServerCommands() {
        return new String[]{"groovy-language-server"};
    }

    @Override
    protected String[] getLaunchArgs(String serverCommand) {
        // groovy-language-server typically doesn't need special arguments
        return new String[0];
    }

    @Override
    protected String getInstallationInstructions() {
        StringBuilder sb = new StringBuilder();
        sb.append("No Groovy language server found.\n\n");
        sb.append("Install groovy-language-server:\n");
        sb.append("  npm install -g groovy-language-server\n\n");
        sb.append("Or build from source:\n");
        sb.append("  git clone https://github.com/GroovyLanguageServer/groovy-language-server\n");
        sb.append("  cd groovy-language-server\n");
        sb.append("  ./gradlew build\n");
        return sb.toString();
    }
}
