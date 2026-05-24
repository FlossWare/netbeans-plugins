package org.flossware.netbeans.mvel.completion;

import org.flossware.netbeans.common.completion.AbstractLspCompletionItem;

/**
 * MVEL LSP completion item implementation.
 *
 * <p>Represents a single completion suggestion for MVEL code,
 * including the text to insert and documentation.</p>
 *
 * <p><b>Note:</b> MVEL does not currently have a widely-adopted LSP server.
 * This class is provided for future compatibility.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class MvelLspCompletionItem extends AbstractLspCompletionItem {

    public MvelLspCompletionItem(String text, int priority) {
        super(text, priority);
    }
}
