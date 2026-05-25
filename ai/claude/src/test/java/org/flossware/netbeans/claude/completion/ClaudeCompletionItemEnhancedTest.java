/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.claude.completion;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Enhanced tests for ClaudeCompletionItem covering all edge cases.
 */
class ClaudeCompletionItemEnhancedTest {

    @Test
    void testGetSortText_Multiline() {
        String multiline = "line1\nline2\nline3";
        ClaudeCompletionItem item = new ClaudeCompletionItem(multiline, "pre", 10, 0);
        assertThat(item.getSortText().toString()).isEqualTo(multiline);
    }

    @Test
    void testGetSortText_VeryLong() {
        String longText = "a".repeat(1000);
        ClaudeCompletionItem item = new ClaudeCompletionItem(longText, "pre", 10, 0);
        assertThat(item.getSortText().toString()).isEqualTo(longText);
    }

    @Test
    void testGetInsertPrefix_MatchesText() {
        ClaudeCompletionItem item = new ClaudeCompletionItem("insert", "pre", 10, 0);
        assertThat(item.getInsertPrefix()).isEqualTo(item.getSortText());
    }

    @Test
    void testGetSortPriority_Zero() {
        ClaudeCompletionItem item = new ClaudeCompletionItem("text", "pre", 10, 0);
        assertThat(item.getSortPriority()).isEqualTo(0);
    }

    @Test
    void testGetSortPriority_Negative() {
        ClaudeCompletionItem item = new ClaudeCompletionItem("text", "pre", 10, -100);
        assertThat(item.getSortPriority()).isEqualTo(-100);
    }

    @Test
    void testGetSortPriority_VeryHigh() {
        ClaudeCompletionItem item = new ClaudeCompletionItem("text", "pre", 10, Integer.MAX_VALUE);
        assertThat(item.getSortPriority()).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    void testCreateDocumentationTask_NotNull() {
        ClaudeCompletionItem item = new ClaudeCompletionItem("text", "pre", 10, 0);
        assertThat(item.createDocumentationTask()).isNotNull();
    }

    @Test
    void testCreateDocumentationTask_MultipleCreations() {
        ClaudeCompletionItem item = new ClaudeCompletionItem("text", "pre", 10, 0);
        assertThat(item.createDocumentationTask()).isNotNull();
        assertThat(item.createDocumentationTask()).isNotNull();
        assertThat(item.createDocumentationTask()).isNotNull();
    }

    @Test
    void testCreateToolTipTask_ReturnsNull() {
        ClaudeCompletionItem item = new ClaudeCompletionItem("text", "pre", 10, 0);
        assertThat(item.createToolTipTask()).isNull();
    }

    @Test
    void testInstantSubstitution_AlwaysFalse() {
        ClaudeCompletionItem item = new ClaudeCompletionItem("text", "pre", 10, 0);
        JTextComponent mockComponent = mock(JTextComponent.class);
        assertThat(item.instantSubstitution(mockComponent)).isFalse();
    }

    @Test
    void testInstantSubstitution_NullComponent() {
        ClaudeCompletionItem item = new ClaudeCompletionItem("text", "pre", 10, 0);
        assertThat(item.instantSubstitution(null)).isFalse();
    }

    @Test
    void testDefaultAction_EmptyPrefix() throws Exception {
        ClaudeCompletionItem item = new ClaudeCompletionItem("newText", "", 0, 0);
        JTextComponent mockComponent = mock(JTextComponent.class);
        PlainDocument doc = new PlainDocument();
        when(mockComponent.getDocument()).thenReturn(doc);

        assertThatCode(() -> item.defaultAction(mockComponent)).doesNotThrowAnyException();
    }

    @Test
    void testDefaultAction_LongPrefix() throws Exception {
        String longPrefix = "a".repeat(100);
        ClaudeCompletionItem item = new ClaudeCompletionItem("newText", longPrefix, 100, 0);
        JTextComponent mockComponent = mock(JTextComponent.class);
        PlainDocument doc = new PlainDocument();
        doc.insertString(0, longPrefix, null);
        when(mockComponent.getDocument()).thenReturn(doc);

        assertThatCode(() -> item.defaultAction(mockComponent)).doesNotThrowAnyException();
    }

    @Test
    void testDefaultAction_AtStart() throws Exception {
        ClaudeCompletionItem item = new ClaudeCompletionItem("text", "p", 1, 0);
        JTextComponent mockComponent = mock(JTextComponent.class);
        PlainDocument doc = new PlainDocument();
        doc.insertString(0, "p", null);
        when(mockComponent.getDocument()).thenReturn(doc);

        assertThatCode(() -> item.defaultAction(mockComponent)).doesNotThrowAnyException();
    }

    @Test
    void testDefaultAction_AtEnd() throws Exception {
        ClaudeCompletionItem item = new ClaudeCompletionItem("text", "suffix", 20, 0);
        JTextComponent mockComponent = mock(JTextComponent.class);
        PlainDocument doc = new PlainDocument();
        doc.insertString(0, "prefix suffix", null);
        when(mockComponent.getDocument()).thenReturn(doc);

        assertThatCode(() -> item.defaultAction(mockComponent)).doesNotThrowAnyException();
    }

    @Test
    void testRender_WithRealGraphics() {
        ClaudeCompletionItem item = new ClaudeCompletionItem("text", "pre", 10, 0);
        
        // Create a real Graphics object from BufferedImage
        BufferedImage img = new BufferedImage(100, 20, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        Font font = new Font("Monospaced", Font.PLAIN, 12);
        
        assertThatCode(() -> item.render(g, font, Color.BLACK, Color.WHITE, 100, 20, false))
            .doesNotThrowAnyException();
        
        g.dispose();
    }

    @Test
    void testRender_SelectedState() {
        ClaudeCompletionItem item = new ClaudeCompletionItem("text", "pre", 10, 0);
        
        BufferedImage img = new BufferedImage(100, 20, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        Font font = new Font("Monospaced", Font.PLAIN, 12);
        
        // Test with selected=true
        assertThatCode(() -> item.render(g, font, Color.BLACK, Color.WHITE, 100, 20, true))
            .doesNotThrowAnyException();
        
        g.dispose();
    }

    @Test
    void testRender_LongText() {
        String longText = "This is a very long text that exceeds the display limit and should be truncated";
        ClaudeCompletionItem item = new ClaudeCompletionItem(longText, "pre", 10, 0);
        
        BufferedImage img = new BufferedImage(200, 30, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        Font font = new Font("SansSerif", Font.PLAIN, 14);
        
        assertThatCode(() -> item.render(g, font, Color.BLUE, Color.YELLOW, 200, 30, false))
            .doesNotThrowAnyException();
        
        g.dispose();
    }

    @Test
    void testRender_MultilineText() {
        String multiline = "line1\nline2\nline3";
        ClaudeCompletionItem item = new ClaudeCompletionItem(multiline, "pre", 10, 0);
        
        BufferedImage img = new BufferedImage(150, 25, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        Font font = new Font("Dialog", Font.BOLD, 11);
        
        assertThatCode(() -> item.render(g, font, Color.RED, Color.GREEN, 150, 25, true))
            .doesNotThrowAnyException();
        
        g.dispose();
    }

    @Test
    void testRender_DifferentDimensions() {
        ClaudeCompletionItem item = new ClaudeCompletionItem("code", "c", 1, 5);
        
        BufferedImage img = new BufferedImage(500, 50, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        Font font = new Font("Courier", Font.PLAIN, 16);
        
        assertThatCode(() -> item.render(g, font, Color.DARK_GRAY, Color.LIGHT_GRAY, 500, 50, false))
            .doesNotThrowAnyException();
        
        g.dispose();
    }

    @Test
    void testGetPreferredWidth_WithRealGraphics() {
        ClaudeCompletionItem item = new ClaudeCompletionItem("test text", "test", 4, 0);
        
        BufferedImage img = new BufferedImage(100, 20, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        Font font = new Font("Monospaced", Font.PLAIN, 12);
        
        int width = item.getPreferredWidth(g, font);
        assertThat(width).isGreaterThan(0);
        
        g.dispose();
    }

    @Test
    void testGetPreferredWidth_LongText() {
        String longText = "a".repeat(200);
        ClaudeCompletionItem item = new ClaudeCompletionItem(longText, "a", 1, 0);
        
        BufferedImage img = new BufferedImage(100, 20, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        Font font = new Font("SansSerif", Font.PLAIN, 10);
        
        int width = item.getPreferredWidth(g, font);
        assertThat(width).isGreaterThan(0);
        
        g.dispose();
    }

    @Test
    void testGetPreferredWidth_MultilineText() {
        ClaudeCompletionItem item = new ClaudeCompletionItem("line1\nline2\nline3", "l", 1, 0);
        
        BufferedImage img = new BufferedImage(100, 20, BufferedImage.TYPE_INT_RGB);
        Graphics g = img.getGraphics();
        Font font = new Font("Dialog", Font.PLAIN, 12);
        
        int width = item.getPreferredWidth(g, font);
        assertThat(width).isGreaterThan(0);
        
        g.dispose();
    }

    @Test
    void testProcessKeyEvent_NullEvent() {
        ClaudeCompletionItem item = new ClaudeCompletionItem("text", "t", 1, 0);
        assertThatCode(() -> item.processKeyEvent(null)).doesNotThrowAnyException();
    }

    @Test
    void testMultipleOperations() {
        ClaudeCompletionItem item = new ClaudeCompletionItem("complete", "comp", 4, 10);
        
        // Test multiple method calls
        assertThat(item.getSortPriority()).isEqualTo(10);
        assertThat(item.getSortText().toString()).isEqualTo("complete");
        assertThat(item.getInsertPrefix().toString()).isEqualTo("complete");
        assertThat(item.createDocumentationTask()).isNotNull();
        assertThat(item.createToolTipTask()).isNull();
        assertThat(item.instantSubstitution(null)).isFalse();
    }
}
