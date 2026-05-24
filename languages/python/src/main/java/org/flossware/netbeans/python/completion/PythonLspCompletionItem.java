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
