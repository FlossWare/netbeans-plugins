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

package org.flossware.netbeans.python.completion;

import java.util.logging.Logger;
import org.flossware.netbeans.common.completion.AbstractLspCompletionProvider;
import org.flossware.netbeans.common.completion.AbstractLspCompletionQuery;
import org.flossware.netbeans.common.lsp.LspClientValidator;
import org.flossware.netbeans.python.lsp.PythonLspServerLauncher;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.spi.editor.completion.CompletionProvider;

/**
 * Python code completion provider using Language Server Protocol.
 *
 * <p>Provides intelligent code completion for Python files by communicating
 * with a Python language server (pyright or pylsp) via LSP.</p>
 *
 * <p>Features provided by LSP server:</p>
 * <ul>
 *   <li>Symbol completion (variables, functions, classes)</li>
 *   <li>Module/package imports</li>
 *   <li>Method/attribute completion</li>
 *   <li>Keyword completion</li>
 *   <li>Context-aware suggestions</li>
 * </ul>
 *
 * <p>This class extends {@link AbstractLspCompletionProvider} which provides
 * common LSP completion logic including auto-trigger support.</p>
 *
 * <p><b>Requirements:</b></p>
 * <ul>
 *   <li>NetBeans 22.0 or later (for LSP client support)</li>
 *   <li>Python LSP server installed (pyright or pylsp)</li>
 * </ul>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
@MimeRegistration(mimeType = "text/x-python", service = CompletionProvider.class)
public class PythonLspCompletionProvider extends AbstractLspCompletionProvider {

    private static final Logger LOGGER = Logger.getLogger(PythonLspCompletionProvider.class.getName());

    static {
        // Validate NetBeans LSP client availability at class load time
        LspClientValidator.validateAndWarn(LOGGER);
    }

    @Override
    protected String getMimeType() {
        return "text/x-python";
    }

    @Override
    protected char[] getAutoTriggerChars() {
        return new char[]{'.'};
    }

    @Override
    protected AbstractLspCompletionQuery createCompletionQuery() {
        return new PythonLspCompletionQuery();
    }

    @Override
    protected boolean isLspServerAvailable() {
        // First check if NetBeans LSP client module is available
        if (!LspClientValidator.isLspClientAvailable()) {
            return false;
        }

        // Then check if Python LSP server is installed
        PythonLspServerLauncher launcher = new PythonLspServerLauncher();
        String[] command = launcher.getServerCommand(null);
        return command != null;
    }
}
