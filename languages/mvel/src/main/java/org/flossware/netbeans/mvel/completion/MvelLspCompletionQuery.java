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
