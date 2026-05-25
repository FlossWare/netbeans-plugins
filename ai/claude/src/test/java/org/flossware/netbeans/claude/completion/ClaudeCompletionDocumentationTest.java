/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.claude.completion;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ClaudeCompletionDocumentationTest {

    @Test
    void testConstruction() {
        ClaudeCompletionDocumentation doc = new ClaudeCompletionDocumentation("text");
        assertThat(doc).isNotNull();
    }

    @Test
    void testConstruction_NullText() {
        assertThatCode(() -> new ClaudeCompletionDocumentation(null)).doesNotThrowAnyException();
    }

    @Test
    void testConstruction_EmptyText() {
        ClaudeCompletionDocumentation doc = new ClaudeCompletionDocumentation("");
        assertThat(doc).isNotNull();
    }

    @Test
    void testGetText_ContainsInput() {
        ClaudeCompletionDocumentation doc = new ClaudeCompletionDocumentation("test text");
        assertThat(doc.getText()).contains("test text");
    }

    @Test
    void testGetText_IsHtml() {
        ClaudeCompletionDocumentation doc = new ClaudeCompletionDocumentation("code");
        String text = doc.getText();
        assertThat(text).startsWith("<html>");
        assertThat(text).endsWith("</html>");
    }

    @Test
    void testGetURL() {
        ClaudeCompletionDocumentation doc = new ClaudeCompletionDocumentation("text");
        assertThat(doc.getURL()).isNull();
    }

    @Test
    void testResolveLink() {
        ClaudeCompletionDocumentation doc = new ClaudeCompletionDocumentation("text");
        assertThat(doc.resolveLink("link")).isNull();
    }

    @Test
    void testGetGotoSourceAction() {
        ClaudeCompletionDocumentation doc = new ClaudeCompletionDocumentation("text");
        assertThat(doc.getGotoSourceAction()).isNull();
    }
}
