/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.claude.completion;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClaudeCompletionItemTest {

    @Test
    void testConstruction() {
        ClaudeCompletionItem item = new ClaudeCompletionItem("text", "prefix", 10, 0);
        assertThat(item).isNotNull();
    }

    @Test
    void testGetSortPriority() {
        ClaudeCompletionItem item = new ClaudeCompletionItem("text", "prefix", 10, 5);
        assertThat(item.getSortPriority()).isEqualTo(5);
    }

    @Test
    void testGetSortText() {
        ClaudeCompletionItem item = new ClaudeCompletionItem("myText", "prefix", 10, 0);
        assertThat(item.getSortText()).isEqualTo("myText");
    }

    @Test
    void testGetInsertPrefix() {
        ClaudeCompletionItem item = new ClaudeCompletionItem("insertMe", "prefix", 10, 0);
        assertThat(item.getInsertPrefix()).isEqualTo("insertMe");
    }

    @Test
    void testDefaultAction_ValidComponent() throws Exception {
        ClaudeCompletionItem item = new ClaudeCompletionItem("newText", "old", 3, 0);
        JTextComponent mockComponent = mock(JTextComponent.class);
        PlainDocument doc = new PlainDocument();
        doc.insertString(0, "old", null);
        when(mockComponent.getDocument()).thenReturn(doc);

        assertThatCode(() -> item.defaultAction(mockComponent)).doesNotThrowAnyException();
    }

    @Test
    void testProcessKeyEvent_NullEvent() {
        ClaudeCompletionItem item = new ClaudeCompletionItem("text", "prefix", 10, 0);
        assertThatCode(() -> item.processKeyEvent(null)).doesNotThrowAnyException();
    }

    @Test
    void testCreateDocumentationTask() {
        ClaudeCompletionItem item = new ClaudeCompletionItem("text", "prefix", 10, 0);
        assertThat(item.createDocumentationTask()).isNotNull();
    }

    @Test
    void testCreateToolTipTask() {
        ClaudeCompletionItem item = new ClaudeCompletionItem("text", "prefix", 10, 0);
        assertThat(item.createToolTipTask()).isNull();
    }

    @Test
    void testInstantSubstitution() {
        ClaudeCompletionItem item = new ClaudeCompletionItem("text", "prefix", 10, 0);
        JTextComponent mockComponent = mock(JTextComponent.class);
        assertThat(item.instantSubstitution(mockComponent)).isFalse();
    }

    @Test
    void testLongText_Truncation() {
        String longText = "This is a very long text that should be truncated when displayed in the completion popup because it exceeds the maximum display length";
        ClaudeCompletionItem item = new ClaudeCompletionItem(longText, "prefix", 10, 0);
        assertThat(item).isNotNull();
    }

    @Test
    void testMultilineText() {
        String multilineText = "line1\nline2\nline3";
        ClaudeCompletionItem item = new ClaudeCompletionItem(multilineText, "prefix", 10, 0);
        assertThat(item).isNotNull();
    }
}
