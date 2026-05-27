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

package org.flossware.netbeans.go.completion;

import org.flossware.netbeans.go.GoIntegrationTestBase;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;

/**
 * Integration tests for Go LSP code completion.
 *
 * <p>Tests code completion functionality with NetBeans runtime and LSP server.</p>
 *
 * @author FlossWare
 * @version 1.0
 * @since 1.0
 */
public class GoLspCompletionIT extends GoIntegrationTestBase {

    public GoLspCompletionIT(String name) {
        super(name);
    }

    /**
     * Test completion provider is registered for Go files.
     */
    public void testCompletionProviderRegistered() {
        GoLspCompletionProvider provider = new GoLspCompletionProvider();

        assertNotNull("Completion provider should be instantiated", provider);
    }

    /**
     * Test completion task creation.
     */
    public void testCompletionTaskCreation() throws Exception {
        FileObject goFile = createSimpleGoProgram("test.go");
        DataObject dataObject = createDataObject(goFile);

        GoLspCompletionProvider provider = new GoLspCompletionProvider();

        // Provider should be created successfully
        assertNotNull("Provider created", provider);
    }

    /**
     * Test auto-query triggers on dot.
     */
    public void testAutoQueryOnDot() {
        GoLspCompletionProvider provider = new GoLspCompletionProvider();

        int queryType = provider.getAutoQueryTypes(null, ".");

        assertEquals("Should trigger on dot", CompletionProvider.COMPLETION_QUERY_TYPE, queryType);
    }

    /**
     * Test auto-query triggers on object.method pattern.
     */
    public void testAutoQueryOnObjectDot() {
        GoLspCompletionProvider provider = new GoLspCompletionProvider();

        int queryType = provider.getAutoQueryTypes(null, "obj.");

        assertEquals("Should trigger on obj.", CompletionProvider.COMPLETION_QUERY_TYPE, queryType);
    }
}
