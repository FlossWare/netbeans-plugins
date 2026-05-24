package org.flossware.netbeans.common.completion;

import java.util.ArrayList;
import java.util.List;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.netbeans.api.editor.document.LineDocument;
import org.netbeans.api.editor.document.LineDocumentUtils;
import org.netbeans.api.lsp.Completion;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;

/**
 * Abstract base class for LSP-based completion queries.
 *
 * <p>This class provides common functionality for querying LSP servers for
 * code completion suggestions. It handles:</p>
 * <ul>
 *   <li>Position calculation (line and column from caret offset)</li>
 *   <li>LSP server communication via LSPBindings</li>
 *   <li>Timeout handling (default 5 seconds)</li>
 *   <li>Error handling and graceful degradation</li>
 *   <li>Conversion from LSP Completion to NetBeans CompletionItem</li>
 * </ul>
 *
 * <p>Subclasses must implement abstract methods to provide language-specific
 * configuration:</p>
 * <ul>
 *   <li>{@link #getMimeType()} - Return the MIME type for LSPBindings lookup</li>
 * </ul>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * public class ErlangLspCompletionQuery extends AbstractLspCompletionQuery {
 *     @Override
 *     protected String getMimeType() {
 *         return "text/x-erlang";
 *     }
 * }
 * }</pre>
 *
 * <p><b>Thread Safety:</b> Each query instance is used for a single completion
 * request and is not shared across threads.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public abstract class AbstractLspCompletionQuery extends AsyncCompletionQuery {

    /**
     * Default timeout for LSP queries in seconds.
     */
    protected static final int DEFAULT_LSP_TIMEOUT_SECONDS = 5;

    /**
     * Query for completions asynchronously.
     *
     * <p>This method:</p>
     * <ol>
     *   <li>Gets the FileObject for the document</li>
     *   <li>Calculates line and column from caret offset</li>
     *   <li>Queries the LSP server for completions</li>
     *   <li>Converts LSP completions to NetBeans CompletionItems</li>
     *   <li>Adds items to the result set</li>
     * </ol>
     *
     * @param resultSet The result set to populate with completion items
     * @param doc The document being edited
     * @param caretOffset The caret position where completion was invoked
     */
    @Override
    protected void query(CompletionResultSet resultSet, Document doc, int caretOffset) {
        try {
            FileObject file = getFileObject(doc);
            if (file == null || !(doc instanceof LineDocument)) {
                return;
            }

            LineDocument lineDoc = (LineDocument) doc;
            int line = LineDocumentUtils.getLineIndex(lineDoc, caretOffset);
            int column = caretOffset - LineDocumentUtils.getLineStart(lineDoc, caretOffset);

            List<Completion> lspCompletions = queryLspServer(file, line, column);

            for (Completion lspCompletion : lspCompletions) {
                resultSet.addItem(createCompletionItem(lspCompletion, caretOffset));
            }

        } catch (Exception e) {
            Exceptions.printStackTrace(e);
        } finally {
            resultSet.finish();
        }
    }

    /**
     * Get the MIME type for this language.
     *
     * <p>Used for LSPBindings lookup. Must match the MIME type registered
     * for this language.</p>
     *
     * @return The MIME type string (e.g., "text/x-python")
     */
    protected abstract String getMimeType();

    /**
     * Get the timeout for LSP queries in seconds.
     *
     * <p>Subclasses can override this to use a different timeout.</p>
     *
     * @return Timeout in seconds
     */
    protected int getLspTimeoutSeconds() {
        return DEFAULT_LSP_TIMEOUT_SECONDS;
    }

    /**
     * Query the LSP server for completions at the given position.
     *
     * <p>Subclasses must implement this to interact with their LSP server.
     * The implementation should use org.netbeans.modules.lsp.client.LSPBindings
     * which is available at runtime but not compile-time in this module.</p>
     *
     * <p>Example implementation:</p>
     * <pre>{@code
     * @Override
     * protected List<Completion> queryLspServer(FileObject file, int line, int column) {
     *     try {
     *         LSPBindings bindings = LSPBindings.getBindings(file);
     *         if (bindings != null) {
     *             CompletableFuture<List<Completion>> future = bindings.getCompletions(file, line, column);
     *             List<Completion> completions = future.get(getLspTimeoutSeconds(), TimeUnit.SECONDS);
     *             return completions != null ? completions : new ArrayList<>();
     *         }
     *     } catch (Exception e) {
     *         // Handle error
     *     }
     *     return new ArrayList<>();
     * }
     * }</pre>
     *
     * @param file The file being edited
     * @param line The line number (0-based)
     * @param column The column number (0-based)
     * @return List of completions from LSP server, or empty list on error
     */
    protected abstract List<Completion> queryLspServer(FileObject file, int line, int column);

    /**
     * Create a completion item from LSP completion data.
     *
     * <p>Subclasses can override this to use a custom completion item implementation.</p>
     *
     * @param lspCompletion The LSP completion object
     * @param caretOffset The caret offset where completion was invoked
     * @return A NetBeans CompletionItem
     */
    protected AbstractLspCompletionItem createCompletionItem(Completion lspCompletion, int caretOffset) {
        return new AbstractLspCompletionItem(lspCompletion, caretOffset);
    }

    /**
     * Get the FileObject for a document.
     *
     * @param doc The document
     * @return FileObject or null if not available
     */
    protected FileObject getFileObject(Document doc) {
        if (doc == null) {
            return null;
        }

        Object sdp = doc.getProperty(Document.StreamDescriptionProperty);
        if (sdp instanceof FileObject) {
            return (FileObject) sdp;
        }

        return null;
    }
}
