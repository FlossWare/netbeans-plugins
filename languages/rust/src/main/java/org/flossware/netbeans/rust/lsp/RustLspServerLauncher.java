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

package org.flossware.netbeans.rust.lsp;

import org.flossware.netbeans.common.lsp.AbstractLspServerLauncher;

/**
 * Launches Rust Language Server (rust-analyzer).
 *
 * <p>Detects which language server is installed on the system and provides
 * the appropriate command to start it. rust-analyzer is the recommended
 * LSP server for Rust.</p>
 *
 * <p>Supported language servers:</p>
 * <ul>
 *   <li><b>rust-analyzer</b> - Official Rust language server (recommended)</li>
 * </ul>
 *
 * <p>This class extends {@link AbstractLspServerLauncher} which provides
 * common LSP server detection and launching logic.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class RustLspServerLauncher extends AbstractLspServerLauncher {

    @Override
    protected String[] getServerCommands() {
        return new String[]{"rust-analyzer"};
    }

    @Override
    protected String[] getLaunchArgs(String serverCommand) {
        // rust-analyzer doesn't need special arguments for stdio mode
        return new String[0];
    }

    @Override
    protected String getInstallationInstructions() {
        StringBuilder sb = new StringBuilder();
        sb.append("No Rust language server found.\n\n");
        sb.append("Install rust-analyzer:\n\n");
        sb.append("Using rustup:\n");
        sb.append("  rustup component add rust-analyzer\n\n");
        sb.append("Or download from:\n");
        sb.append("  https://rust-analyzer.github.io/manual.html#installation\n");
        return sb.toString();
    }
}
