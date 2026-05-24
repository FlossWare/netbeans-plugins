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

import org.flossware.netbeans.common.completion.AbstractLspCompletionItem;
import org.netbeans.api.lsp.Completion;

/**
 * NetBeans completion item backed by LSP completion data for Python.
 *
 * <p>Represents a single code completion suggestion from the Python language server,
 * adapted to NetBeans' CompletionItem interface.</p>
 *
 * <p>This class extends {@link AbstractLspCompletionItem} which provides
 * common completion item rendering and behavior.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class PythonLspCompletionItem extends AbstractLspCompletionItem {

    /**
     * Create a completion item from LSP completion data.
     *
     * @param lspCompletion The LSP completion object
     * @param caretOffset The caret offset where completion was invoked
     */
    public PythonLspCompletionItem(Completion lspCompletion, int caretOffset) {
        super(lspCompletion, caretOffset);
    }
}
