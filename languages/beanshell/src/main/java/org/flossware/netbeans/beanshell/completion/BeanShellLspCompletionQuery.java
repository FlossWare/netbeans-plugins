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
