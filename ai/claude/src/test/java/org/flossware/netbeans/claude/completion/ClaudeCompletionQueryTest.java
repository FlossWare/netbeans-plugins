/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.claude.completion;

import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClaudeCompletionQueryTest {

    @Test
    void testConstruction() {
        JTextComponent mockComponent = mock(JTextComponent.class);
        assertThatCode(() -> new ClaudeCompletionQuery(mockComponent)).doesNotThrowAnyException();
    }

    @Test
    void testQuery_ShortPrefix() throws Exception {
        JTextComponent mockComponent = mock(JTextComponent.class);
        PlainDocument doc = new PlainDocument();
        doc.insertString(0, "a", null);  // Only 1 char
        when(mockComponent.getDocument()).thenReturn(doc);
        when(mockComponent.getCaretPosition()).thenReturn(1);
        
        CompletionResultSet mockResultSet = mock(CompletionResultSet.class);
        
        ClaudeCompletionQuery query = new ClaudeCompletionQuery(mockComponent);
        assertThatCode(() -> query.query(mockResultSet, doc, 1)).doesNotThrowAnyException();
        
        verify(mockResultSet, atLeastOnce()).finish();
    }

    @Test
    void testQuery_NullDocument() {
        JTextComponent mockComponent = mock(JTextComponent.class);
        when(mockComponent.getDocument()).thenReturn(null);
        
        CompletionResultSet mockResultSet = mock(CompletionResultSet.class);
        
        ClaudeCompletionQuery query = new ClaudeCompletionQuery(mockComponent);
        assertThatCode(() -> query.query(mockResultSet, null, 0)).doesNotThrowAnyException();
    }

    @Test
    void testQuery_LongPrefix() throws Exception {
        JTextComponent mockComponent = mock(JTextComponent.class);
        PlainDocument doc = new PlainDocument();
        doc.insertString(0, "public class Test", null);
        when(mockComponent.getDocument()).thenReturn(doc);
        when(mockComponent.getCaretPosition()).thenReturn(17);
        
        CompletionResultSet mockResultSet = mock(CompletionResultSet.class);
        
        ClaudeCompletionQuery query = new ClaudeCompletionQuery(mockComponent);
        // This will trigger async behavior
        assertThatCode(() -> query.query(mockResultSet, doc, 17)).doesNotThrowAnyException();
        
        // Wait a bit for async processing
        Thread.sleep(100);
    }

    @Test
    void testQuery_WithException() {
        JTextComponent mockComponent = mock(JTextComponent.class);
        when(mockComponent.getDocument()).thenThrow(new RuntimeException("Test exception"));
        
        CompletionResultSet mockResultSet = mock(CompletionResultSet.class);
        PlainDocument doc = new PlainDocument();
        
        ClaudeCompletionQuery query = new ClaudeCompletionQuery(mockComponent);
        assertThatCode(() -> query.query(mockResultSet, doc, 0)).doesNotThrowAnyException();
        
        verify(mockResultSet, atLeastOnce()).finish();
    }

    @Test
    void testQuery_ZeroOffset() {
        JTextComponent mockComponent = mock(JTextComponent.class);
        PlainDocument doc = new PlainDocument();
        when(mockComponent.getDocument()).thenReturn(doc);
        when(mockComponent.getCaretPosition()).thenReturn(0);
        
        CompletionResultSet mockResultSet = mock(CompletionResultSet.class);
        
        ClaudeCompletionQuery query = new ClaudeCompletionQuery(mockComponent);
        assertThatCode(() -> query.query(mockResultSet, doc, 0)).doesNotThrowAnyException();
    }

    @Test
    void testQuery_LargeOffset() throws Exception {
        JTextComponent mockComponent = mock(JTextComponent.class);
        PlainDocument doc = new PlainDocument();
        String largeText = "public class Test {\n    public void method() {\n        System.out.println(\"test\");\n    }\n}";
        doc.insertString(0, largeText, null);
        when(mockComponent.getDocument()).thenReturn(doc);
        when(mockComponent.getCaretPosition()).thenReturn(50);
        
        CompletionResultSet mockResultSet = mock(CompletionResultSet.class);
        
        ClaudeCompletionQuery query = new ClaudeCompletionQuery(mockComponent);
        assertThatCode(() -> query.query(mockResultSet, doc, 50)).doesNotThrowAnyException();
    }

    @Test
    void testQuery_EmptyDocument() {
        JTextComponent mockComponent = mock(JTextComponent.class);
        PlainDocument doc = new PlainDocument();
        when(mockComponent.getDocument()).thenReturn(doc);
        when(mockComponent.getCaretPosition()).thenReturn(0);
        
        CompletionResultSet mockResultSet = mock(CompletionResultSet.class);
        
        ClaudeCompletionQuery query = new ClaudeCompletionQuery(mockComponent);
        assertThatCode(() -> query.query(mockResultSet, doc, 0)).doesNotThrowAnyException();
    }
}
