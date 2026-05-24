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

package org.flossware.netbeans.mvel.completion;

import org.flossware.netbeans.common.completion.AbstractLspCompletionQuery;

/**
 * MVEL LSP completion query implementation.
 *
 * <p>Handles completion requests for MVEL code by querying the
 * MVEL language server via LSP (when available).</p>
 *
 * <p><b>Note:</b> MVEL does not currently have a widely-adopted LSP server.
 * This class is provided for future compatibility.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class MvelLspCompletionQuery extends AbstractLspCompletionQuery {

    @Override
    protected String getLanguageName() {
        return "MVEL";
    }

    @Override
    protected String getMimeType() {
        return "text/x-mvel";
    }
}
