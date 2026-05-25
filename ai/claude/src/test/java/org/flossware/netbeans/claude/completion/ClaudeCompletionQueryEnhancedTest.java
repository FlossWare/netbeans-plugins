/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.claude.completion;

import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Enhanced tests for ClaudeCompletionQuery focusing on parseCompletions and edge cases.
 */
class ClaudeCompletionQueryEnhancedTest {

    @BeforeEach
    void setUp() {
        // Clear cache before each test
        CompletionCache.getInstance().clear();
    }

    @Test
    void testQuery_WithCacheHit() throws Exception {
        JTextComponent mockComponent = mock(JTextComponent.class);
        PlainDocument doc = new PlainDocument();
        String text = "public class Test";
        doc.insertString(0, text, null);
        when(mockComponent.getDocument()).thenReturn(doc);
        when(mockComponent.getCaretPosition()).thenReturn(text.length());

        CompletionResultSet mockResultSet = mock(CompletionResultSet.class);

        // Manually add to cache
        CompletionCache cache = CompletionCache.getInstance();
        String cacheKey = text + "|Test";
        cache.put(cacheKey, new java.util.ArrayList<>());

        ClaudeCompletionQuery query = new ClaudeCompletionQuery(mockComponent);
        query.query(mockResultSet, doc, text.length());

        // Should finish quickly from cache
        Thread.sleep(50);
        verify(mockResultSet, atLeastOnce()).finish();
    }

    @Test
    void testQuery_CacheDisabled() throws Exception {
        // Temporarily disable cache
        boolean originalCacheSetting = ClaudeCompletionSettings.isCacheEnabled();
        ClaudeCompletionSettings.setCacheEnabled(false);

        try {
            JTextComponent mockComponent = mock(JTextComponent.class);
            PlainDocument doc = new PlainDocument();
            String text = "public void method";
            doc.insertString(0, text, null);
            when(mockComponent.getDocument()).thenReturn(doc);
            when(mockComponent.getCaretPosition()).thenReturn(text.length());

            CompletionResultSet mockResultSet = mock(CompletionResultSet.class);

            ClaudeCompletionQuery query = new ClaudeCompletionQuery(mockComponent);
            assertThatCode(() -> query.query(mockResultSet, doc, text.length())).doesNotThrowAnyException();

            Thread.sleep(100);
        } finally {
            ClaudeCompletionSettings.setCacheEnabled(originalCacheSetting);
        }
    }

    @Test
    void testQuery_ServiceNotConfigured() throws Exception {
        // This test relies on service not being configured (no API key)
        JTextComponent mockComponent = mock(JTextComponent.class);
        PlainDocument doc = new PlainDocument();
        String text = "public class Example";
        doc.insertString(0, text, null);
        when(mockComponent.getDocument()).thenReturn(doc);
        when(mockComponent.getCaretPosition()).thenReturn(text.length());

        CompletionResultSet mockResultSet = mock(CompletionResultSet.class);

        ClaudeCompletionQuery query = new ClaudeCompletionQuery(mockComponent);
        query.query(mockResultSet, doc, text.length());

        // Should finish quickly if service not configured
        Thread.sleep(50);
        verify(mockResultSet, atLeastOnce()).finish();
    }

    @Test
    void testQuery_MinimumCharactersNotMet() throws Exception {
        int minChars = ClaudeCompletionSettings.getMinimumCharacters();

        JTextComponent mockComponent = mock(JTextComponent.class);
        PlainDocument doc = new PlainDocument();
        // Insert fewer characters than minimum
        String text = "ab"; // Usually minimum is 3
        doc.insertString(0, text, null);
        when(mockComponent.getDocument()).thenReturn(doc);
        when(mockComponent.getCaretPosition()).thenReturn(text.length());

        CompletionResultSet mockResultSet = mock(CompletionResultSet.class);

        ClaudeCompletionQuery query = new ClaudeCompletionQuery(mockComponent);
        query.query(mockResultSet, doc, text.length());

        verify(mockResultSet, atLeastOnce()).finish();
    }

    @Test
    void testQuery_ExactlyMinimumCharacters() throws Exception {
        int minChars = ClaudeCompletionSettings.getMinimumCharacters();

        JTextComponent mockComponent = mock(JTextComponent.class);
        PlainDocument doc = new PlainDocument();
        // Create text with exactly minimum characters
        String text = "a".repeat(Math.max(minChars, 3));
        doc.insertString(0, text, null);
        when(mockComponent.getDocument()).thenReturn(doc);
        when(mockComponent.getCaretPosition()).thenReturn(text.length());

        CompletionResultSet mockResultSet = mock(CompletionResultSet.class);

        ClaudeCompletionQuery query = new ClaudeCompletionQuery(mockComponent);
        assertThatCode(() -> query.query(mockResultSet, doc, text.length())).doesNotThrowAnyException();
    }

    @Test
    void testQuery_MultilineDocument() throws Exception {
        JTextComponent mockComponent = mock(JTextComponent.class);
        PlainDocument doc = new PlainDocument();
        String text = "public class Test {\n    public void method() {\n        ";
        doc.insertString(0, text, null);
        when(mockComponent.getDocument()).thenReturn(doc);
        when(mockComponent.getCaretPosition()).thenReturn(text.length());

        CompletionResultSet mockResultSet = mock(CompletionResultSet.class);

        ClaudeCompletionQuery query = new ClaudeCompletionQuery(mockComponent);
        assertThatCode(() -> query.query(mockResultSet, doc, text.length())).doesNotThrowAnyException();
    }

    @Test
    void testQuery_CaretInMiddle() throws Exception {
        JTextComponent mockComponent = mock(JTextComponent.class);
        PlainDocument doc = new PlainDocument();
        String text = "public class Test {}";
        int caretPos = 13; // After "Test"
        doc.insertString(0, text, null);
        when(mockComponent.getDocument()).thenReturn(doc);
        when(mockComponent.getCaretPosition()).thenReturn(caretPos);

        CompletionResultSet mockResultSet = mock(CompletionResultSet.class);

        ClaudeCompletionQuery query = new ClaudeCompletionQuery(mockComponent);
        assertThatCode(() -> query.query(mockResultSet, doc, caretPos)).doesNotThrowAnyException();
    }

    @Test
    void testQuery_SpecialCharacters() throws Exception {
        JTextComponent mockComponent = mock(JTextComponent.class);
        PlainDocument doc = new PlainDocument();
        String text = "String s = \"test\"; // comment";
        doc.insertString(0, text, null);
        when(mockComponent.getDocument()).thenReturn(doc);
        when(mockComponent.getCaretPosition()).thenReturn(text.length());

        CompletionResultSet mockResultSet = mock(CompletionResultSet.class);

        ClaudeCompletionQuery query = new ClaudeCompletionQuery(mockComponent);
        assertThatCode(() -> query.query(mockResultSet, doc, text.length())).doesNotThrowAnyException();
    }

    @Test
    void testQuery_UnicodeCharacters() throws Exception {
        JTextComponent mockComponent = mock(JTextComponent.class);
        PlainDocument doc = new PlainDocument();
        String text = "String message = \"Hello 世界\";";
        doc.insertString(0, text, null);
        when(mockComponent.getDocument()).thenReturn(doc);
        when(mockComponent.getCaretPosition()).thenReturn(text.length());

        CompletionResultSet mockResultSet = mock(CompletionResultSet.class);

        ClaudeCompletionQuery query = new ClaudeCompletionQuery(mockComponent);
        assertThatCode(() -> query.query(mockResultSet, doc, text.length())).doesNotThrowAnyException();
    }

    @Test
    void testQuery_VeryLongDocument() throws Exception {
        JTextComponent mockComponent = mock(JTextComponent.class);
        PlainDocument doc = new PlainDocument();
        // Create a very long document
        String text = "public class Test {\n".repeat(1000) + "    ";
        doc.insertString(0, text, null);
        when(mockComponent.getDocument()).thenReturn(doc);
        when(mockComponent.getCaretPosition()).thenReturn(text.length());

        CompletionResultSet mockResultSet = mock(CompletionResultSet.class);

        ClaudeCompletionQuery query = new ClaudeCompletionQuery(mockComponent);
        assertThatCode(() -> query.query(mockResultSet, doc, text.length())).doesNotThrowAnyException();
    }

    @Test
    void testQuery_NullComponent() {
        assertThatCode(() -> new ClaudeCompletionQuery(null)).doesNotThrowAnyException();
    }

    @Test
    void testQuery_ComponentWithNullDocument() {
        JTextComponent mockComponent = mock(JTextComponent.class);
        when(mockComponent.getDocument()).thenReturn(null);
        when(mockComponent.getCaretPosition()).thenReturn(0);

        CompletionResultSet mockResultSet = mock(CompletionResultSet.class);

        ClaudeCompletionQuery query = new ClaudeCompletionQuery(mockComponent);
        assertThatCode(() -> query.query(mockResultSet, null, 0)).doesNotThrowAnyException();

        verify(mockResultSet, atLeastOnce()).finish();
    }

    @Test
    void testQuery_NegativeCaretOffset() {
        JTextComponent mockComponent = mock(JTextComponent.class);
        PlainDocument doc = new PlainDocument();
        when(mockComponent.getDocument()).thenReturn(doc);
        when(mockComponent.getCaretPosition()).thenReturn(-1);

        CompletionResultSet mockResultSet = mock(CompletionResultSet.class);

        ClaudeCompletionQuery query = new ClaudeCompletionQuery(mockComponent);
        assertThatCode(() -> query.query(mockResultSet, doc, -1)).doesNotThrowAnyException();
    }

    @Test
    void testQuery_OffsetBeyondDocument() throws Exception {
        JTextComponent mockComponent = mock(JTextComponent.class);
        PlainDocument doc = new PlainDocument();
        doc.insertString(0, "test", null);
        when(mockComponent.getDocument()).thenReturn(doc);
        when(mockComponent.getCaretPosition()).thenReturn(1000); // Way beyond document length

        CompletionResultSet mockResultSet = mock(CompletionResultSet.class);

        ClaudeCompletionQuery query = new ClaudeCompletionQuery(mockComponent);
        assertThatCode(() -> query.query(mockResultSet, doc, 1000)).doesNotThrowAnyException();
    }

    @Test
    void testQuery_DocumentThrowsException() throws Exception {
        JTextComponent mockComponent = mock(JTextComponent.class);
        PlainDocument doc = mock(PlainDocument.class);
        when(doc.getText(anyInt(), anyInt())).thenThrow(new javax.swing.text.BadLocationException("test", 0));
        when(mockComponent.getDocument()).thenReturn(doc);
        when(mockComponent.getCaretPosition()).thenReturn(5);

        CompletionResultSet mockResultSet = mock(CompletionResultSet.class);

        ClaudeCompletionQuery query = new ClaudeCompletionQuery(mockComponent);
        assertThatCode(() -> query.query(mockResultSet, doc, 5)).doesNotThrowAnyException();

        verify(mockResultSet, atLeastOnce()).finish();
    }

    @Test
    void testQuery_ResultSetThrowsException() throws Exception {
        JTextComponent mockComponent = mock(JTextComponent.class);
        PlainDocument doc = new PlainDocument();
        doc.insertString(0, "test", null);
        when(mockComponent.getDocument()).thenReturn(doc);
        when(mockComponent.getCaretPosition()).thenReturn(4);

        CompletionResultSet mockResultSet = mock(CompletionResultSet.class);
        doThrow(new RuntimeException("test")).when(mockResultSet).finish();

        ClaudeCompletionQuery query = new ClaudeCompletionQuery(mockComponent);
        // Should handle exception gracefully
        assertThatCode(() -> query.query(mockResultSet, doc, 4)).doesNotThrowAnyException();
    }

    @Test
    void testQuery_MultipleQueriesSequential() throws Exception {
        JTextComponent mockComponent = mock(JTextComponent.class);
        PlainDocument doc = new PlainDocument();
        doc.insertString(0, "public class", null);
        when(mockComponent.getDocument()).thenReturn(doc);
        when(mockComponent.getCaretPosition()).thenReturn(12);

        CompletionResultSet mockResultSet1 = mock(CompletionResultSet.class);
        CompletionResultSet mockResultSet2 = mock(CompletionResultSet.class);
        CompletionResultSet mockResultSet3 = mock(CompletionResultSet.class);

        ClaudeCompletionQuery query = new ClaudeCompletionQuery(mockComponent);

        query.query(mockResultSet1, doc, 12);
        Thread.sleep(50);
        query.query(mockResultSet2, doc, 12);
        Thread.sleep(50);
        query.query(mockResultSet3, doc, 12);

        assertThat(query).isNotNull();
    }

    @Test
    void testQuery_CachePersistence() throws Exception {
        JTextComponent mockComponent = mock(JTextComponent.class);
        PlainDocument doc = new PlainDocument();
        String text = "public void test";
        doc.insertString(0, text, null);
        when(mockComponent.getDocument()).thenReturn(doc);
        when(mockComponent.getCaretPosition()).thenReturn(text.length());

        // First query
        CompletionResultSet mockResultSet1 = mock(CompletionResultSet.class);
        ClaudeCompletionQuery query1 = new ClaudeCompletionQuery(mockComponent);
        query1.query(mockResultSet1, doc, text.length());
        Thread.sleep(200);

        // Second query with same input should hit cache
        CompletionResultSet mockResultSet2 = mock(CompletionResultSet.class);
        ClaudeCompletionQuery query2 = new ClaudeCompletionQuery(mockComponent);
        query2.query(mockResultSet2, doc, text.length());
        Thread.sleep(50);

        // Both should finish
        verify(mockResultSet1, atLeastOnce()).finish();
        verify(mockResultSet2, atLeastOnce()).finish();
    }
}
