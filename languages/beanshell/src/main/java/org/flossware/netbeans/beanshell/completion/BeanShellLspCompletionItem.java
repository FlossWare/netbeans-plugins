package org.flossware.netbeans.beanshell.completion;

import org.flossware.netbeans.common.completion.AbstractLspCompletionItem;

/**
 * BeanShell LSP completion item implementation.
 *
 * <p>Represents a single code completion suggestion from the BeanShell
 * language server.</p>
 *
 * <p>Note: BeanShell does not currently have a standard LSP server,
 * so this class will not be used until one becomes available.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class BeanShellLspCompletionItem extends AbstractLspCompletionItem {

    public BeanShellLspCompletionItem(String text, int startOffset, int caretOffset) {
        super(text, startOffset, caretOffset);
    }

    @Override
    protected String getLanguageName() {
        return "BeanShell";
    }
}
