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

package org.flossware.netbeans.javascript.lsp;

import org.flossware.netbeans.common.lsp.AbstractLspServerLauncher;

/**
 * Launches JavaScript Language Server.
 *
 * <p>Detects which language server is installed on the system and provides
 * the appropriate command to start it. typescript-language-server is preferred
 * as it supports both TypeScript and JavaScript with excellent compatibility.</p>
 *
 * <p>Supported language servers:</p>
 * <ul>
 *   <li><b>typescript-language-server</b> - TypeScript/JavaScript language server (recommended)</li>
 *   <li><b>vscode-html-language-server</b> - VS Code HTML/JS language server</li>
 * </ul>
 *
 * <p>This class extends {@link AbstractLspServerLauncher} which provides
 * common LSP server detection and launching logic.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class JavaScriptLspServerLauncher extends AbstractLspServerLauncher {

    @Override
    protected String[] getServerCommands() {
        // typescript-language-server is preferred (supports both JS and TS)
        return new String[]{"typescript-language-server", "vscode-html-language-server"};
    }

    @Override
    protected String[] getLaunchArgs(String serverCommand) {
        // typescript-language-server requires --stdio
        if ("typescript-language-server".equals(serverCommand)) {
            return new String[]{"--stdio"};
        }
        // vscode-html-language-server requires --stdio
        if ("vscode-html-language-server".equals(serverCommand)) {
            return new String[]{"--stdio"};
        }
        return new String[0];
    }

    @Override
    protected String getInstallationInstructions() {
        StringBuilder sb = new StringBuilder();
        sb.append("No JavaScript language server found.\n\n");
        sb.append("Install one of the following:\n\n");
        sb.append("TypeScript Language Server (recommended):\n");
        sb.append("  npm install -g typescript-language-server typescript\n\n");
        sb.append("VS Code Language Servers:\n");
        sb.append("  npm install -g vscode-langservers-extracted\n");
        return sb.toString();
    }
}
