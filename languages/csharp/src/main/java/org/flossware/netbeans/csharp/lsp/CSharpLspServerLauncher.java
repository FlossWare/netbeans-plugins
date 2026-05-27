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

package org.flossware.netbeans.csharp.lsp;

import org.flossware.netbeans.common.lsp.AbstractLspServerLauncher;

/**
 * Launches C# Language Server (OmniSharp).
 *
 * <p>Detects which language server is installed on the system and provides
 * the appropriate command to start it. OmniSharp is the primary language
 * server for C#.</p>
 *
 * <p>Supported language servers:</p>
 * <ul>
 *   <li><b>omnisharp</b> - OmniSharp C# language server (recommended)</li>
 *   <li><b>OmniSharp</b> - Alternative OmniSharp executable name</li>
 * </ul>
 *
 * <p>This class extends {@link AbstractLspServerLauncher} which provides
 * common LSP server detection and launching logic.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class CSharpLspServerLauncher extends AbstractLspServerLauncher {

    @Override
    protected String[] getServerCommands() {
        return new String[]{"omnisharp", "OmniSharp"};
    }

    @Override
    protected String[] getLaunchArgs(String serverCommand) {
        // OmniSharp requires --languageserver flag for LSP mode
        return new String[]{"--languageserver"};
    }

    @Override
    protected String getInstallationInstructions() {
        StringBuilder sb = new StringBuilder();
        sb.append("No C# language server found.\n\n");
        sb.append("Install OmniSharp:\n\n");
        sb.append("Via .NET tool:\n");
        sb.append("  dotnet tool install -g omnisharp\n\n");
        sb.append("Or download from:\n");
        sb.append("  https://github.com/OmniSharp/omnisharp-roslyn/releases\n");
        return sb.toString();
    }
}
