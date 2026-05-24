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

package org.flossware.netbeans.beanshell.completion;

import org.flossware.netbeans.common.completion.AbstractLspCompletionQuery;

/**
 * BeanShell LSP completion query implementation.
 *
 * <p>Handles completion requests for BeanShell code by querying the
 * BeanShell language server via LSP.</p>
 *
 * <p>Note: BeanShell does not currently have a standard LSP server,
 * so this query will not be active until one becomes available.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class BeanShellLspCompletionQuery extends AbstractLspCompletionQuery {

    @Override
    protected String getLanguageName() {
        return "BeanShell";
    }

    @Override
    protected String getMimeType() {
        return "text/x-beanshell";
    }
}
