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

package org.flossware.netbeans.mvel.lsp;

import org.flossware.netbeans.common.lsp.AbstractLspServerLauncher;

/**
 * Launches MVEL Language Server.
 *
 * <p><b>Note:</b> MVEL is an expression language typically embedded in Java applications
 * and does not have a widely-adopted standalone Language Server Protocol (LSP) implementation.
 * This launcher is provided for future compatibility if an LSP server becomes available.</p>
 *
 * <p>MVEL (MVFLEX Expression Language) is primarily used as:</p>
 * <ul>
 *   <li>An embedded expression evaluator in Java applications</li>
 *   <li>Template engine for configuration and rule engines</li>
 *   <li>Lightweight scripting within frameworks like Drools</li>
 * </ul>
 *
 * <p>This class extends {@link AbstractLspServerLauncher} which provides
 * common LSP server detection and launching logic.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class MvelLspServerLauncher extends AbstractLspServerLauncher {

    @Override
    protected String[] getServerCommands() {
        // No standard MVEL LSP server exists yet
        // This array is empty to indicate no LSP server is available
        return new String[0];
    }

    @Override
    protected String[] getLaunchArgs(String serverCommand) {
        // Not applicable - no LSP server available
        return new String[0];
    }

    @Override
    protected String getInstallationInstructions() {
        StringBuilder sb = new StringBuilder();
        sb.append("MVEL Language Server Not Available\n\n");
        sb.append("MVEL (MVFLEX Expression Language) is an expression language primarily\n");
        sb.append("designed to be embedded in Java applications. Currently, there is no\n");
        sb.append("widely-adopted standalone Language Server Protocol (LSP) implementation\n");
        sb.append("for MVEL.\n\n");
        sb.append("MVEL is typically used:\n");
        sb.append("  - As an embedded expression evaluator in Java applications\n");
        sb.append("  - In rule engines (e.g., Drools)\n");
        sb.append("  - For template processing and configuration\n\n");
        sb.append("Basic editor support (syntax highlighting, file recognition) is provided\n");
        sb.append("without an LSP server. Advanced features like code completion and\n");
        sb.append("diagnostics would require a custom LSP server implementation.\n\n");
        sb.append("For more information about MVEL, visit:\n");
        sb.append("  https://github.com/mvel/mvel\n");
        return sb.toString();
    }
}
