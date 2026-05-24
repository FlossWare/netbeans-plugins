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

package org.flossware.netbeans.common.completion;

import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;

/**
 * Abstract base class for LSP-based code completion providers.
 *
 * <p>This class provides common functionality for language completion providers
 * that use Language Server Protocol (LSP) for intelligent code completion.</p>
 *
 * <p>Subclasses must implement abstract methods to provide language-specific
 * configuration:</p>
 * <ul>
 *   <li>{@link #getMimeType()} - Return the MIME type for this language</li>
 *   <li>{@link #getAutoTriggerChars()} - Return characters that trigger auto-completion</li>
 *   <li>{@link #createCompletionQuery()} - Create the language-specific completion query</li>
 *   <li>{@link #isLspServerAvailable()} - Check if an LSP server is available</li>
 * </ul>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * @MimeRegistration(mimeType = "text/x-erlang", service = CompletionProvider.class)
 * public class ErlangLspCompletionProvider extends AbstractLspCompletionProvider {
 *     @Override
 *     protected String getMimeType() {
 *         return "text/x-erlang";
 *     }
 *
 *     @Override
 *     protected char[] getAutoTriggerChars() {
 *         return new char[]{'.', ':'};
 *     }
 *
 *     @Override
 *     protected AbstractLspCompletionQuery createCompletionQuery() {
 *         return new ErlangLspCompletionQuery();
 *     }
 *
 *     @Override
 *     protected boolean isLspServerAvailable() {
 *         String[] cmd = new ErlangLspServerLauncher().getServerCommand(null);
 *         return cmd != null;
 *     }
 * }
 * }</pre>
 *
 * <p><b>Thread Safety:</b> This class is thread-safe. Methods can be called
 * from multiple threads concurrently.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public abstract class AbstractLspCompletionProvider implements CompletionProvider {

    /**
     * Create a completion task for the given query type.
     *
     * <p>Only handles COMPLETION_QUERY_TYPE. Returns null for other query types
     * or if no LSP server is available.</p>
     *
     * @param queryType The type of completion query
     * @param component The text component where completion is requested
     * @return CompletionTask to execute asynchronously, or null if not applicable
     */
    @Override
    public CompletionTask createTask(int queryType, JTextComponent component) {
        if (queryType != CompletionProvider.COMPLETION_QUERY_TYPE) {
            return null;
        }

        if (!isLspServerAvailable()) {
            return null;
        }

        return new AsyncCompletionTask(createCompletionQuery(), component);
    }

    /**
     * Get the auto-query types for this provider.
     *
     * <p>Determines when completion should be automatically triggered based on
     * typed text. Checks if the last character typed is in the auto-trigger
     * character set.</p>
     *
     * @param component The text component
     * @param typedText The text that was just typed
     * @return Query type mask (COMPLETION_QUERY_TYPE or 0 for no auto-query)
     */
    @Override
    public int getAutoQueryTypes(JTextComponent component, String typedText) {
        if (typedText == null || typedText.isEmpty()) {
            return 0;
        }

        char lastChar = typedText.charAt(typedText.length() - 1);
        char[] triggerChars = getAutoTriggerChars();

        if (triggerChars != null) {
            for (char trigger : triggerChars) {
                if (lastChar == trigger) {
                    return CompletionProvider.COMPLETION_QUERY_TYPE;
                }
            }
        }

        return 0;
    }

    /**
     * Get the MIME type for this language.
     *
     * <p>Used for LSP bindings lookup. Examples: "text/x-python", "text/x-erlang"</p>
     *
     * @return The MIME type string
     */
    protected abstract String getMimeType();

    /**
     * Get characters that should trigger auto-completion.
     *
     * <p>For example, Python uses '.', Erlang might use '.' and ':'</p>
     *
     * @return Array of trigger characters, or null/empty for no auto-trigger
     */
    protected abstract char[] getAutoTriggerChars();

    /**
     * Create a completion query instance for this language.
     *
     * @return A new completion query instance
     */
    protected abstract AbstractLspCompletionQuery createCompletionQuery();

    /**
     * Check if an LSP server is available for this language.
     *
     * <p>Should delegate to the language's LSP server launcher to check
     * for server availability.</p>
     *
     * @return true if an LSP server is available
     */
    protected abstract boolean isLspServerAvailable();

    /**
     * Check if the current line matches a specific pattern.
     *
     * <p>Utility method for subclasses to implement context-aware completion
     * triggers (e.g., import statements).</p>
     *
     * @param component The text component
     * @param pattern The pattern to match (e.g., "import ", "from ")
     * @return true if the current line starts with the pattern
     */
    protected boolean currentLineMatches(JTextComponent component, String pattern) {
        if (component == null || pattern == null) {
            return false;
        }

        try {
            Document doc = component.getDocument();
            int caretPos = component.getCaretPosition();
            int lineStart = javax.swing.text.Utilities.getRowStart(component, caretPos);
            int lineEnd = javax.swing.text.Utilities.getRowEnd(component, caretPos);
            String lineText = doc.getText(lineStart, lineEnd - lineStart);
            return lineText.trim().startsWith(pattern);
        } catch (Exception e) {
            return false;
        }
    }
}
