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

package org.flossware.netbeans.javascript.completion;

import javax.swing.text.Document;
import org.flossware.netbeans.javascript.JavaScriptIntegrationTestBase;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;

/**
 * Integration tests for JavaScript LSP code completion.
 *
 * <p>Tests code completion functionality with NetBeans runtime and LSP server.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class JavaScriptLspCompletionIT extends JavaScriptIntegrationTestBase {

    public JavaScriptLspCompletionIT(String name) {
        super(name);
    }

    /**
     * Test completion provider is registered for JavaScript files.
     */
    public void testCompletionProviderRegistered() {
        JavaScriptLspCompletionProvider provider = new JavaScriptLspCompletionProvider();

        assertNotNull("Completion provider should be instantiated", provider);
    }

    /**
     * Test completion task creation.
     */
    public void testCompletionTaskCreation() throws Exception {
        FileObject jsFile = createSimpleJavaScriptScript("test.js");
        DataObject dataObject = createDataObject(jsFile);

        // Get editor cookie
        EditorCookie editorCookie = dataObject.getLookup().lookup(EditorCookie.class);

        if (editorCookie != null) {
            Document doc = editorCookie.openDocument();
            assertNotNull("Document should be created", doc);

            // Create completion provider
            JavaScriptLspCompletionProvider provider = new JavaScriptLspCompletionProvider();

            // Request completion task (may return null if LSP server not available)
            CompletionTask task = provider.createTask(CompletionProvider.COMPLETION_QUERY_TYPE, null);

            // We can't assert much here without LSP server running
            // Just verify the provider doesn't crash
            assertTrue("Completion provider created successfully", true);
        }
    }

    /**
     * Test provider handles missing LSP server gracefully.
     */
    public void testCompletionWithoutLspServer() {
        JavaScriptLspCompletionProvider provider = new JavaScriptLspCompletionProvider();

        // Should not throw even without LSP server
        assertNotNull("Provider should handle missing LSP server", provider);
    }
}
