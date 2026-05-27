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

import org.flossware.netbeans.common.completion.AbstractLspCompletionItem;
import org.netbeans.api.lsp.Completion;

/**
 * NetBeans completion item backed by LSP completion data for Go.
 *
 * <p>Represents a single code completion suggestion from the Go language server,
 * adapted to NetBeans' CompletionItem interface.</p>
 *
 * <p>This class extends {@link AbstractLspCompletionItem} which provides
 * common completion item rendering and behavior.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class GoLspCompletionItem extends AbstractLspCompletionItem {

    /**
     * Create a completion item from LSP completion data.
     *
     * @param lspCompletion The LSP completion object
     * @param caretOffset The caret offset where completion was invoked
     */
    public GoLspCompletionItem(Completion lspCompletion, int caretOffset) {
        super(lspCompletion, caretOffset);
    }
}
