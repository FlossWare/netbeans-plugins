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

package org.netbeans.modules.lsp.client;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.netbeans.api.lsp.Completion;
import org.openide.filesystems.FileObject;

/**
 * Stub class for LSPBindings to allow compilation.
 *
 * <p>This class provides minimal method signatures to allow the Go module
 * to compile. At runtime in NetBeans, the real LSPBindings from
 * org-netbeans-modules-lsp-client will be used instead.</p>
 *
 * <p><b>DO NOT USE THIS CLASS DIRECTLY.</b> This is only for compilation purposes.</p>
 */
public class LSPBindings {

    /**
     * Get LSP bindings for a file.
     *
     * @param file The file object
     * @return LSP bindings or null
     */
    public static LSPBindings getBindings(FileObject file) {
        throw new UnsupportedOperationException(
            "This is a stub class for compilation only. " +
            "Real implementation should be provided by NetBeans at runtime."
        );
    }

    /**
     * Get code completions at a specific position.
     *
     * @param file The file being edited
     * @param line Line number (0-based)
     * @param column Column number (0-based)
     * @return Future containing list of completions
     */
    public CompletableFuture<List<Completion>> getCompletions(FileObject file, int line, int column) {
        throw new UnsupportedOperationException(
            "This is a stub class for compilation only. " +
            "Real implementation should be provided by NetBeans at runtime."
        );
    }
}
