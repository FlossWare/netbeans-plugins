package org.flossware.netbeans.python.completion;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.flossware.netbeans.common.completion.AbstractLspCompletionQuery;
import org.netbeans.api.lsp.Completion;
import org.netbeans.modules.lsp.client.LSPBindings;
import org.openide.filesystems.FileObject;

/**
 * Asynchronous completion query that retrieves code completions from Python LSP server.
 *
 * <p>This class queries the Python language server (pyright or pylsp) for code
 * completion suggestions at the current cursor position.</p>
 *
 * <p>This class extends {@link AbstractLspCompletionQuery} which provides
 * common LSP query logic including position calculation and result handling.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class PythonLspCompletionQuery extends AbstractLspCompletionQuery {

    @Override
    protected String getMimeType() {
        return "text/x-python";
    }

    @Override
    protected List<Completion> queryLspServer(FileObject file, int line, int column) {
        try {
            // Get LSP bindings for this file
            LSPBindings bindings = LSPBindings.getBindings(file);
            if (bindings == null) {
                return new ArrayList<>();
            }

            // Query for completions
            CompletableFuture<List<Completion>> future = bindings.getCompletions(file, line, column);

            // Wait for response with timeout
            List<Completion> completions = future.get(getLspTimeoutSeconds(), TimeUnit.SECONDS);

            return completions != null ? completions : new ArrayList<>();

        } catch (Exception e) {
            // LSP query failed or timed out
            return new ArrayList<>();
        }
    }
}
