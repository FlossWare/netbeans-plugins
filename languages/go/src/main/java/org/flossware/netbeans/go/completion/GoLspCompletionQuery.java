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

package org.flossware.netbeans.go.completion;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.flossware.netbeans.common.completion.AbstractLspCompletionQuery;
import org.netbeans.api.lsp.Completion;
import org.netbeans.modules.lsp.client.LSPBindings;
import org.openide.filesystems.FileObject;

/**
 * Asynchronous completion query that retrieves code completions from Go LSP server.
 *
 * <p>This class queries the Go language server (gopls) for code
 * completion suggestions at the current cursor position.</p>
 *
 * <p>This class extends {@link AbstractLspCompletionQuery} which provides
 * common LSP query logic including position calculation and result handling.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class GoLspCompletionQuery extends AbstractLspCompletionQuery {

    @Override
    protected String getMimeType() {
        return "text/x-go";
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
