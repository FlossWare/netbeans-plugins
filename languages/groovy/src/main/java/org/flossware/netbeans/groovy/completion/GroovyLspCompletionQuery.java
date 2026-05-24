package org.flossware.netbeans.groovy.completion;

import org.flossware.netbeans.common.completion.AbstractLspCompletionQuery;

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
    protected String getLanguageName() {
        return "Groovy";
    }

    @Override
    protected String getMimeType() {
        return "text/x-groovy";
    }
}
