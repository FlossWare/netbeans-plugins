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

package org.netbeans.modules.lsp.client;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.netbeans.api.lsp.Completion;
import org.openide.filesystems.FileObject;

/**
 * Stub class for LSPBindings to allow compilation.
 *
 * <p>This class provides minimal method signatures to allow the Python module
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
