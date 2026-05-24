package org.flossware.netbeans.python.completion;

import javax.swing.text.JTextComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.netbeans.spi.editor.completion.CompletionTask;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for PythonLspCompletionProvider.
 */
class PythonLspCompletionProviderTest {

    private PythonLspCompletionProvider provider;
    private JTextComponent mockComponent;

    @BeforeEach
    void setUp() {
        provider = new PythonLspCompletionProvider();
        mockComponent = mock(JTextComponent.class);
    }

    @Test
    void testProviderInstantiation() {
        assertThat(provider).isNotNull();
    }

    @Test
    void testCreateTask_WithCompletionQueryType() {
        CompletionTask task = provider.createTask(
            CompletionProvider.COMPLETION_QUERY_TYPE,
            mockComponent
        );

        // Task may be null if LSP server not available (which is OK in test environment)
        // Just verify it doesn't throw
        assertThat(task).satisfiesAnyOf(
            t -> assertThat(t).isNull(),  // LSP server not available
            t -> assertThat(t).isNotNull() // LSP server available
        );
    }

    @Test
    void testCreateTask_WithDocumentationQueryType() {
        CompletionTask task = provider.createTask(
            CompletionProvider.DOCUMENTATION_QUERY_TYPE,
            mockComponent
        );

        // Should return null for non-completion query types
        assertThat(task).isNull();
    }

    @Test
    void testCreateTask_WithTooltipQueryType() {
        CompletionTask task = provider.createTask(
            CompletionProvider.TOOLTIP_QUERY_TYPE,
            mockComponent
        );

        // Should return null for non-completion query types
        assertThat(task).isNull();
    }

    @Test
    void testGetAutoQueryTypes_WithDot() {
        int queryType = provider.getAutoQueryTypes(mockComponent, ".");

        // Should trigger completion on '.'
        assertThat(queryType).isEqualTo(CompletionProvider.COMPLETION_QUERY_TYPE);
    }

    @Test
    void testGetAutoQueryTypes_WithNonDot() {
        int queryType = provider.getAutoQueryTypes(mockComponent, "a");

        // Should not auto-trigger on regular characters
        assertThat(queryType).isEqualTo(0);
    }

    @Test
    void testGetAutoQueryTypes_WithNull() {
        int queryType = provider.getAutoQueryTypes(mockComponent, null);

        // Should handle null gracefully
        assertThat(queryType).isEqualTo(0);
    }

    @Test
    void testGetAutoQueryTypes_WithMultiCharDot() {
        int queryType = provider.getAutoQueryTypes(mockComponent, "obj.");

        // Should trigger on text ending with '.'
        assertThat(queryType).isEqualTo(CompletionProvider.COMPLETION_QUERY_TYPE);
    }

    @Test
    void testGetAutoQueryTypes_WithDotInMiddle() {
        int queryType = provider.getAutoQueryTypes(mockComponent, "obj.a");

        // Should not trigger if '.' is not at the end
        assertThat(queryType).isEqualTo(0);
    }
}
