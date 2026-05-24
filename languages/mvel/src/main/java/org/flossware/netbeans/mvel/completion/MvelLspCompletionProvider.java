package org.flossware.netbeans.mvel.completion;

import java.util.logging.Logger;
import org.flossware.netbeans.common.completion.AbstractLspCompletionProvider;
import org.flossware.netbeans.common.completion.AbstractLspCompletionQuery;
import org.flossware.netbeans.common.lsp.LspClientValidator;
import org.flossware.netbeans.mvel.lsp.MvelLspServerLauncher;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.spi.editor.completion.CompletionProvider;

/**
 * MVEL code completion provider using Language Server Protocol.
 *
 * <p><b>Note:</b> MVEL does not currently have a widely-adopted LSP server.
 * This provider is included for consistency with other language modules and
 * for future compatibility if an LSP server becomes available.</p>
 *
 * <p>MVEL (MVFLEX Expression Language) features that would benefit from completion:</p>
 * <ul>
 *   <li>Property access (object.property)</li>
 *   <li>Method invocation</li>
 *   <li>Variable references</li>
 *   <li>Built-in functions</li>
 *   <li>Control structures (if, foreach, etc.)</li>
 * </ul>
 *
 * <p>This class extends {@link AbstractLspCompletionProvider} which provides
 * common LSP completion logic including auto-trigger support.</p>
 *
 * <p><b>Requirements:</b></p>
 * <ul>
 *   <li>NetBeans 22.0 or later (for LSP client support)</li>
 *   <li>MVEL language server (when available)</li>
 * </ul>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
@MimeRegistration(mimeType = "text/x-mvel", service = CompletionProvider.class)
public class MvelLspCompletionProvider extends AbstractLspCompletionProvider {

    private static final Logger LOGGER = Logger.getLogger(MvelLspCompletionProvider.class.getName());

    static {
        // Validate NetBeans LSP client availability at class load time
        LspClientValidator.validateAndWarn(LOGGER);
    }

    @Override
    protected String getMimeType() {
        return "text/x-mvel";
    }

    @Override
    protected char[] getAutoTriggerChars() {
        // MVEL uses '.' for property access
        return new char[]{'.'};
    }

    @Override
    protected AbstractLspCompletionQuery createCompletionQuery() {
        return new MvelLspCompletionQuery();
    }

    @Override
    protected boolean isLspServerAvailable() {
        // First check if NetBeans LSP client module is available
        if (!LspClientValidator.isLspClientAvailable()) {
            return false;
        }

        // Then check if MVEL LSP server is installed (currently none exists)
        MvelLspServerLauncher launcher = new MvelLspServerLauncher();
        String[] command = launcher.getServerCommand(null);
        return command != null;
    }
}
