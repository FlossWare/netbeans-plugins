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

package org.flossware.netbeans.python.lsp;

import org.flossware.netbeans.common.lsp.AbstractLspServerLauncher;

/**
 * Launches Python Language Server (pyright or pylsp).
 *
 * <p>Detects which language server is installed on the system and provides
 * the appropriate command to start it. Pyright is preferred over pylsp due
 * to better performance and accuracy.</p>
 *
 * <p>Supported language servers:</p>
 * <ul>
 *   <li><b>pyright-langserver</b> - Microsoft's Python language server (recommended)</li>
 *   <li><b>pylsp</b> - Python Language Server (community-maintained)</li>
 * </ul>
 *
 * <p>This class extends {@link AbstractLspServerLauncher} which provides
 * common LSP server detection and launching logic.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class PythonLspServerLauncher extends AbstractLspServerLauncher {

    @Override
    protected String[] getServerCommands() {
        // pyright is preferred over pylsp (faster and more accurate)
        return new String[]{"pyright-langserver", "pylsp"};
    }

    @Override
    protected String[] getLaunchArgs(String serverCommand) {
        // pyright-langserver requires --stdio, pylsp doesn't need arguments
        if ("pyright-langserver".equals(serverCommand)) {
            return new String[]{"--stdio"};
        }
        return new String[0];
    }

    @Override
    protected String getInstallationInstructions() {
        StringBuilder sb = new StringBuilder();
        sb.append("No Python language server found.\n\n");
        sb.append("Install one of the following:\n\n");
        sb.append("Pyright (recommended):\n");
        sb.append("  npm install -g pyright\n\n");
        sb.append("Python LSP Server:\n");
        sb.append("  pip install python-lsp-server\n");
        return sb.toString();
    }
}
