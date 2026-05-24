package org.flossware.netbeans.python.completion;

import org.flossware.netbeans.common.completion.AbstractLspCompletionProvider;
import org.flossware.netbeans.common.completion.AbstractLspCompletionQuery;
import org.flossware.netbeans.python.lsp.PythonLspServerLauncher;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.spi.editor.completion.CompletionProvider;

/**
 * Python code completion provider using Language Server Protocol.
 *
 * <p>Provides intelligent code completion for Python files by communicating
 * with a Python language server (pyright or pylsp) via LSP.</p>
 *
 * <p>Features provided by LSP server:</p>
 * <ul>
 *   <li>Symbol completion (variables, functions, classes)</li>
 *   <li>Module/package imports</li>
 *   <li>Method/attribute completion</li>
 *   <li>Keyword completion</li>
 *   <li>Context-aware suggestions</li>
 * </ul>
 *
 * <p>This class extends {@link AbstractLspCompletionProvider} which provides
 * common LSP completion logic including auto-trigger support.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
@MimeRegistration(mimeType = "text/x-python", service = CompletionProvider.class)
public class PythonLspCompletionProvider extends AbstractLspCompletionProvider {

    @Override
    protected String getMimeType() {
        return "text/x-python";
    }

    @Override
    protected char[] getAutoTriggerChars() {
        return new char[]{'.'};
    }

    @Override
    protected AbstractLspCompletionQuery createCompletionQuery() {
        return new PythonLspCompletionQuery();
    }

    @Override
    protected boolean isLspServerAvailable() {
        PythonLspServerLauncher launcher = new PythonLspServerLauncher();
        String[] command = launcher.getServerCommand(null);
        return command != null;
    }
}
