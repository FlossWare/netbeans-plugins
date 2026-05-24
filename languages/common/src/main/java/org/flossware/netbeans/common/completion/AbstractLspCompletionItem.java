package org.flossware.netbeans.common.completion;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.lsp.Completion;
import org.netbeans.spi.editor.completion.CompletionItem;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.CompletionUtilities;
import org.openide.util.Exceptions;

/**
 * NetBeans completion item backed by LSP completion data.
 *
 * <p>This class adapts LSP completion suggestions to NetBeans' CompletionItem
 * interface. It is a fully concrete implementation that can be used as-is or
 * extended for language-specific customization.</p>
 *
 * <p><b>Features:</b></p>
 * <ul>
 *   <li>Inserts completion text on selection</li>
 *   <li>Removes partial word before insertion</li>
 *   <li>Renders with label and detail</li>
 *   <li>Supports LSP sort order</li>
 *   <li>Handles insert text vs. label</li>
 * </ul>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * Completion lspCompletion = ... // from LSP server
 * CompletionItem item = new AbstractLspCompletionItem(lspCompletion, caretOffset);
 * resultSet.addItem(item);
 * }</pre>
 *
 * <p>Subclasses can override methods to customize behavior:</p>
 * <ul>
 *   <li>{@link #getIcon()} - Provide language-specific icons based on completion kind</li>
 *   <li>{@link #findWordStart(Document, int)} - Customize word boundary detection</li>
 *   <li>{@link #getSortPriority()} - Adjust sort priority</li>
 * </ul>
 *
 * <p><b>Thread Safety:</b> This class is not thread-safe. Each instance should
 * be used from a single thread (the EDT for UI operations).</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class AbstractLspCompletionItem implements CompletionItem {

    private final Completion lspCompletion;
    private final int caretOffset;

    /**
     * Create a completion item from LSP completion data.
     *
     * @param lspCompletion The LSP completion object
     * @param caretOffset The caret offset where completion was invoked
     */
    public AbstractLspCompletionItem(Completion lspCompletion, int caretOffset) {
        if (lspCompletion == null) {
            throw new IllegalArgumentException("LSP completion cannot be null");
        }
        this.lspCompletion = lspCompletion;
        this.caretOffset = caretOffset;
    }

    /**
     * Called when the user selects this completion item.
     *
     * <p>Removes the partial word at the cursor and inserts the completion text.</p>
     *
     * @param component The text component
     */
    @Override
    public void defaultAction(JTextComponent component) {
        try {
            Document doc = component.getDocument();
            String insertText = lspCompletion.getInsertText();
            if (insertText == null || insertText.isEmpty()) {
                insertText = lspCompletion.getLabel();
            }

            int wordStart = findWordStart(doc, caretOffset);
            doc.remove(wordStart, caretOffset - wordStart);
            doc.insertString(wordStart, insertText, null);

        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     * Process key event.
     *
     * <p>Default implementation does nothing. Subclasses can override to
     * handle special keys.</p>
     *
     * @param evt The key event
     */
    @Override
    public void processKeyEvent(KeyEvent evt) {
        // Default processing - do nothing
    }

    /**
     * Get the preferred width of this item when rendered.
     *
     * @param g Graphics context
     * @param defaultFont Default font
     * @return Preferred width in pixels
     */
    @Override
    public int getPreferredWidth(Graphics g, Font defaultFont) {
        String label = lspCompletion.getLabel();
        String detail = getDetailNow();
        return CompletionUtilities.getPreferredWidth(label, detail, g, defaultFont);
    }

    /**
     * Render this completion item in the completion popup.
     *
     * @param g Graphics context
     * @param defaultFont Default font
     * @param defaultColor Default color
     * @param backgroundColor Background color
     * @param width Width to render within
     * @param height Height to render within
     * @param selected Whether this item is selected
     */
    @Override
    public void render(Graphics g, Font defaultFont, Color defaultColor,
                      Color backgroundColor, int width, int height, boolean selected) {
        String label = lspCompletion.getLabel();
        String detail = getDetailNow();
        CompletionUtilities.renderHtml(
            getIcon(),
            label,
            detail,
            g,
            defaultFont,
            defaultColor,
            width,
            height,
            selected
        );
    }

    /**
     * Get completion text for sorting.
     *
     * <p>Uses LSP sortText if available, otherwise falls back to label.</p>
     *
     * @return The text to use for sorting
     */
    @Override
    public CharSequence getSortText() {
        String sortText = lspCompletion.getSortText();
        return sortText != null ? sortText : lspCompletion.getLabel();
    }

    /**
     * Get insert prefix for filtering.
     *
     * @return The prefix to match against typed text
     */
    @Override
    public CharSequence getInsertPrefix() {
        return lspCompletion.getLabel();
    }

    /**
     * Get the detail text from the completion, handling the async nature.
     *
     * @return The detail text, or null if not available yet
     */
    protected String getDetailNow() {
        if (lspCompletion.getDetail() != null) {
            return lspCompletion.getDetail().getNow(null);
        }
        return null;
    }

    /**
     * Create documentation task for this item.
     *
     * <p>Default implementation returns null. Subclasses can override to
     * provide LSP documentation.</p>
     *
     * @return Documentation task or null
     */
    @Override
    public CompletionTask createDocumentationTask() {
        return null;
    }

    /**
     * Create tooltip task for this item.
     *
     * <p>Default implementation returns null.</p>
     *
     * @return Tooltip task or null
     */
    @Override
    public CompletionTask createToolTipTask() {
        return null;
    }

    /**
     * Should this item be instant substitution (auto-complete).
     *
     * <p>Default implementation returns false to show the completion popup.</p>
     *
     * @param component The text component
     * @return true if instant substitution
     */
    @Override
    public boolean instantSubstitution(JTextComponent component) {
        return false;
    }

    /**
     * Get sort priority (lower = higher priority).
     *
     * <p>Default implementation returns 0. Subclasses can override to adjust
     * priority based on completion kind or other factors.</p>
     *
     * @return Sort priority
     */
    @Override
    public int getSortPriority() {
        return 0;
    }

    /**
     * Get icon for this completion item.
     *
     * <p>Default implementation returns null. Subclasses can override to
     * provide icons based on LSP CompletionItemKind.</p>
     *
     * @return Icon or null
     */
    protected ImageIcon getIcon() {
        return null;
    }

    /**
     * Find the start of the word being completed.
     *
     * <p>Moves backward from the offset while characters are identifier parts
     * or dots. Subclasses can override for language-specific word boundaries.</p>
     *
     * @param doc The document
     * @param offset Current caret offset
     * @return Start offset of the word
     */
    protected int findWordStart(Document doc, int offset) {
        try {
            String text = doc.getText(0, offset);
            int wordStart = offset;

            while (wordStart > 0) {
                char ch = text.charAt(wordStart - 1);
                if (!Character.isJavaIdentifierPart(ch) && ch != '.') {
                    break;
                }
                wordStart--;
            }

            return wordStart;

        } catch (BadLocationException ex) {
            return offset;
        }
    }

    /**
     * Get the underlying LSP completion object.
     *
     * @return The LSP completion
     */
    protected Completion getLspCompletion() {
        return lspCompletion;
    }

    /**
     * Get the caret offset where completion was invoked.
     *
     * @return The caret offset
     */
    protected int getCaretOffset() {
        return caretOffset;
    }
}
