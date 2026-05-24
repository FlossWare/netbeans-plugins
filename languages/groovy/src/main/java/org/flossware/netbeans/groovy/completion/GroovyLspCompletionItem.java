package org.flossware.netbeans.groovy.completion;

import org.flossware.netbeans.common.completion.AbstractLspCompletionItem;

/**
 * Groovy LSP completion item implementation.
 *
 * <p>Represents a single code completion suggestion from the Groovy
 * language server.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class GroovyLspCompletionItem extends AbstractLspCompletionItem {

    public GroovyLspCompletionItem(String text, int startOffset, int caretOffset) {
        super(text, startOffset, caretOffset);
    }

    @Override
    protected String getLanguageName() {
        return "Groovy";
    }
}
