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
