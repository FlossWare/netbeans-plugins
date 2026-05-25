/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.claude.completion;

import java.lang.reflect.Method;
import java.util.List;
import javax.swing.text.JTextComponent;
import org.flossware.netbeans.claude.completion.CompletionContextBuilder.CompletionContext;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for ClaudeCompletionQuery's parseCompletions method using reflection.
 */
class ClaudeCompletionQueryParseTest {

    @Test
    @SuppressWarnings("unchecked")
    void testParseCompletions_NullResponse() throws Exception {
        JTextComponent mockComponent = mock(JTextComponent.class);
        ClaudeCompletionQuery query = new ClaudeCompletionQuery(mockComponent);

        CompletionContext context = new CompletionContext();
        context.setPrefix("test");
        context.setCaretOffset(10);

        // Use reflection to call private parseCompletions method
        Method method = ClaudeCompletionQuery.class.getDeclaredMethod(
            "parseCompletions", String.class, CompletionContext.class);
        method.setAccessible(true);

        List<ClaudeCompletionItem> result = (List<ClaudeCompletionItem>)
            method.invoke(query, null, context);

        assertThat(result).isEmpty();
    }

    @Test
    @SuppressWarnings("unchecked")
    void testParseCompletions_EmptyResponse() throws Exception {
        JTextComponent mockComponent = mock(JTextComponent.class);
        ClaudeCompletionQuery query = new ClaudeCompletionQuery(mockComponent);

        CompletionContext context = new CompletionContext();
        context.setPrefix("test");
        context.setCaretOffset(10);

        Method method = ClaudeCompletionQuery.class.getDeclaredMethod(
            "parseCompletions", String.class, CompletionContext.class);
        method.setAccessible(true);

        List<ClaudeCompletionItem> result = (List<ClaudeCompletionItem>)
            method.invoke(query, "", context);

        assertThat(result).isEmpty();
    }

    @Test
    @SuppressWarnings("unchecked")
    void testParseCompletions_WhitespaceResponse() throws Exception {
        JTextComponent mockComponent = mock(JTextComponent.class);
        ClaudeCompletionQuery query = new ClaudeCompletionQuery(mockComponent);

        CompletionContext context = new CompletionContext();
        context.setPrefix("test");
        context.setCaretOffset(10);

        Method method = ClaudeCompletionQuery.class.getDeclaredMethod(
            "parseCompletions", String.class, CompletionContext.class);
        method.setAccessible(true);

        List<ClaudeCompletionItem> result = (List<ClaudeCompletionItem>)
            method.invoke(query, "   \n\t  ", context);

        assertThat(result).isEmpty();
    }

    @Test
    @SuppressWarnings("unchecked")
    void testParseCompletions_PlainText() throws Exception {
        JTextComponent mockComponent = mock(JTextComponent.class);
        ClaudeCompletionQuery query = new ClaudeCompletionQuery(mockComponent);

        CompletionContext context = new CompletionContext();
        context.setPrefix("test");
        context.setCaretOffset(10);

        Method method = ClaudeCompletionQuery.class.getDeclaredMethod(
            "parseCompletions", String.class, CompletionContext.class);
        method.setAccessible(true);

        String response = "public void method() {}";
        List<ClaudeCompletionItem> result = (List<ClaudeCompletionItem>)
            method.invoke(query, response, context);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isNotNull();
    }

    @Test
    @SuppressWarnings("unchecked")
    void testParseCompletions_WithMarkdownCodeBlock() throws Exception {
        JTextComponent mockComponent = mock(JTextComponent.class);
        ClaudeCompletionQuery query = new ClaudeCompletionQuery(mockComponent);

        CompletionContext context = new CompletionContext();
        context.setPrefix("test");
        context.setCaretOffset(10);

        Method method = ClaudeCompletionQuery.class.getDeclaredMethod(
            "parseCompletions", String.class, CompletionContext.class);
        method.setAccessible(true);

        String response = "```java\npublic void method() {}\n```";
        List<ClaudeCompletionItem> result = (List<ClaudeCompletionItem>)
            method.invoke(query, response, context);

        assertThat(result).hasSize(1);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testParseCompletions_WithMarkdownNoLanguage() throws Exception {
        JTextComponent mockComponent = mock(JTextComponent.class);
        ClaudeCompletionQuery query = new ClaudeCompletionQuery(mockComponent);

        CompletionContext context = new CompletionContext();
        context.setPrefix("test");
        context.setCaretOffset(10);

        Method method = ClaudeCompletionQuery.class.getDeclaredMethod(
            "parseCompletions", String.class, CompletionContext.class);
        method.setAccessible(true);

        String response = "```\ncode here\n```";
        List<ClaudeCompletionItem> result = (List<ClaudeCompletionItem>)
            method.invoke(query, response, context);

        assertThat(result).hasSize(1);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testParseCompletions_MarkdownStartNoNewline() throws Exception {
        JTextComponent mockComponent = mock(JTextComponent.class);
        ClaudeCompletionQuery query = new ClaudeCompletionQuery(mockComponent);

        CompletionContext context = new CompletionContext();
        context.setPrefix("test");
        context.setCaretOffset(10);

        Method method = ClaudeCompletionQuery.class.getDeclaredMethod(
            "parseCompletions", String.class, CompletionContext.class);
        method.setAccessible(true);

        // No newline after ```
        String response = "```code```";
        List<ClaudeCompletionItem> result = (List<ClaudeCompletionItem>)
            method.invoke(query, response, context);

        assertThat(result).hasSize(1);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testParseCompletions_MarkdownNoClosing() throws Exception {
        JTextComponent mockComponent = mock(JTextComponent.class);
        ClaudeCompletionQuery query = new ClaudeCompletionQuery(mockComponent);

        CompletionContext context = new CompletionContext();
        context.setPrefix("test");
        context.setCaretOffset(10);

        Method method = ClaudeCompletionQuery.class.getDeclaredMethod(
            "parseCompletions", String.class, CompletionContext.class);
        method.setAccessible(true);

        // Only opening ```
        String response = "```\ncode without closing";
        List<ClaudeCompletionItem> result = (List<ClaudeCompletionItem>)
            method.invoke(query, response, context);

        assertThat(result).hasSize(1);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testParseCompletions_MarkdownMultipleBlocks() throws Exception {
        JTextComponent mockComponent = mock(JTextComponent.class);
        ClaudeCompletionQuery query = new ClaudeCompletionQuery(mockComponent);

        CompletionContext context = new CompletionContext();
        context.setPrefix("test");
        context.setCaretOffset(10);

        Method method = ClaudeCompletionQuery.class.getDeclaredMethod(
            "parseCompletions", String.class, CompletionContext.class);
        method.setAccessible(true);

        String response = "```java\nfirst\n```\nSome text\n```python\nsecond\n```";
        List<ClaudeCompletionItem> result = (List<ClaudeCompletionItem>)
            method.invoke(query, response, context);

        assertThat(result).hasSize(1);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testParseCompletions_StartIndexZero() throws Exception {
        JTextComponent mockComponent = mock(JTextComponent.class);
        ClaudeCompletionQuery query = new ClaudeCompletionQuery(mockComponent);

        CompletionContext context = new CompletionContext();
        context.setPrefix("test");
        context.setCaretOffset(10);

        Method method = ClaudeCompletionQuery.class.getDeclaredMethod(
            "parseCompletions", String.class, CompletionContext.class);
        method.setAccessible(true);

        // Start index will be -1 (no newline found), should not strip
        String response = "```code```";
        List<ClaudeCompletionItem> result = (List<ClaudeCompletionItem>)
            method.invoke(query, response, context);

        assertThat(result).hasSize(1);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testParseCompletions_EndBeforeStart() throws Exception {
        JTextComponent mockComponent = mock(JTextComponent.class);
        ClaudeCompletionQuery query = new ClaudeCompletionQuery(mockComponent);

        CompletionContext context = new CompletionContext();
        context.setPrefix("test");
        context.setCaretOffset(10);

        Method method = ClaudeCompletionQuery.class.getDeclaredMethod(
            "parseCompletions", String.class, CompletionContext.class);
        method.setAccessible(true);

        // Edge case: ``` at beginning and end
        String response = "``````";
        List<ClaudeCompletionItem> result = (List<ClaudeCompletionItem>)
            method.invoke(query, response, context);

        assertThat(result).hasSize(1);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testParseCompletions_LongResponse() throws Exception {
        JTextComponent mockComponent = mock(JTextComponent.class);
        ClaudeCompletionQuery query = new ClaudeCompletionQuery(mockComponent);

        CompletionContext context = new CompletionContext();
        context.setPrefix("test");
        context.setCaretOffset(10);

        Method method = ClaudeCompletionQuery.class.getDeclaredMethod(
            "parseCompletions", String.class, CompletionContext.class);
        method.setAccessible(true);

        String response = "```\n" + "code line\n".repeat(1000) + "```";
        List<ClaudeCompletionItem> result = (List<ClaudeCompletionItem>)
            method.invoke(query, response, context);

        assertThat(result).hasSize(1);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testParseCompletions_SpecialCharacters() throws Exception {
        JTextComponent mockComponent = mock(JTextComponent.class);
        ClaudeCompletionQuery query = new ClaudeCompletionQuery(mockComponent);

        CompletionContext context = new CompletionContext();
        context.setPrefix("test");
        context.setCaretOffset(10);

        Method method = ClaudeCompletionQuery.class.getDeclaredMethod(
            "parseCompletions", String.class, CompletionContext.class);
        method.setAccessible(true);

        String response = "```\nString s = \"test\\n\\t\";\n```";
        List<ClaudeCompletionItem> result = (List<ClaudeCompletionItem>)
            method.invoke(query, response, context);

        assertThat(result).hasSize(1);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testParseCompletions_Unicode() throws Exception {
        JTextComponent mockComponent = mock(JTextComponent.class);
        ClaudeCompletionQuery query = new ClaudeCompletionQuery(mockComponent);

        CompletionContext context = new CompletionContext();
        context.setPrefix("test");
        context.setCaretOffset(10);

        Method method = ClaudeCompletionQuery.class.getDeclaredMethod(
            "parseCompletions", String.class, CompletionContext.class);
        method.setAccessible(true);

        String response = "```\nString msg = \"こんにちは\";\n```";
        List<ClaudeCompletionItem> result = (List<ClaudeCompletionItem>)
            method.invoke(query, response, context);

        assertThat(result).hasSize(1);
    }
}
