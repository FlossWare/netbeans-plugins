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

package org.flossware.netbeans.groovy.completion;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.flossware.netbeans.common.completion.AbstractLspCompletionQuery;
import org.netbeans.api.lsp.Completion;
import org.netbeans.modules.lsp.client.LSPBindings;
import org.openide.filesystems.FileObject;

/**
 * Groovy LSP completion query implementation.
 *
 * <p>Handles completion requests for Groovy code by querying the
 * Groovy language server via LSP.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class GroovyLspCompletionQuery extends AbstractLspCompletionQuery {

    @Override
    protected String getMimeType() {
        return "text/x-groovy";
    }

    @Override
    protected List<Completion> queryLspServer(FileObject file, int line, int column) {
        try {
            LSPBindings bindings = LSPBindings.getBindings(file);
            if (bindings == null) {
                return new ArrayList<>();
            }

            CompletableFuture<List<Completion>> future = bindings.getCompletions(file, line, column);
            List<Completion> completions = future.get(getLspTimeoutSeconds(), TimeUnit.SECONDS);

            return completions != null ? completions : new ArrayList<>();

        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
