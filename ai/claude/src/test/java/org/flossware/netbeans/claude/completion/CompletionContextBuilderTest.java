/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.claude.completion;

import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompletionContextBuilderTest {

    @Test
    void testConstruction() {
        JTextComponent mockComponent = mock(JTextComponent.class);
        when(mockComponent.getDocument()).thenReturn(new PlainDocument());

        assertThatCode(() -> new CompletionContextBuilder(mockComponent, 0))
            .doesNotThrowAnyException();
    }

    @Test
    void testBuild_BasicContext() {
        JTextComponent mockComponent = mock(JTextComponent.class);
        PlainDocument doc = new PlainDocument();
        when(mockComponent.getDocument()).thenReturn(doc);

        CompletionContextBuilder builder = new CompletionContextBuilder(mockComponent, 0);
        CompletionContextBuilder.CompletionContext context = builder.build();

        assertThat(context).isNotNull();
    }

    @Test
    void testBuild_WithTextContent() throws Exception {
        JTextComponent mockComponent = mock(JTextComponent.class);
        PlainDocument doc = new PlainDocument();
        doc.insertString(0, "public class Test {}", null);
        when(mockComponent.getDocument()).thenReturn(doc);
        when(mockComponent.getCaretPosition()).thenReturn(10);

        CompletionContextBuilder builder = new CompletionContextBuilder(mockComponent, 10);
        CompletionContextBuilder.CompletionContext context = builder.build();

        assertThat(context.getTextBefore()).isNotEmpty();
        assertThat(context.getCaretOffset()).isEqualTo(10);
    }

    @Test
    void testBuild_ImportStatement() throws Exception {
        JTextComponent mockComponent = mock(JTextComponent.class);
        PlainDocument doc = new PlainDocument();
        doc.insertString(0, "import java.util.", null);
        when(mockComponent.getDocument()).thenReturn(doc);
        when(mockComponent.getCaretPosition()).thenReturn(17);

        CompletionContextBuilder builder = new CompletionContextBuilder(mockComponent, 17);
        CompletionContextBuilder.CompletionContext context = builder.build();

        assertThat(context.getContextType()).isEqualTo("import");
    }

    @Test
    void testBuild_NewStatement() throws Exception {
        JTextComponent mockComponent = mock(JTextComponent.class);
        PlainDocument doc = new PlainDocument();
        doc.insertString(0, "new ArrayList", null);
        when(mockComponent.getDocument()).thenReturn(doc);
        when(mockComponent.getCaretPosition()).thenReturn(13);

        CompletionContextBuilder builder = new CompletionContextBuilder(mockComponent, 13);
        CompletionContextBuilder.CompletionContext context = builder.build();

        assertThat(context.getContextType()).isEqualTo("constructor");
    }

    @Test
    void testBuild_MethodCall() throws Exception {
        JTextComponent mockComponent = mock(JTextComponent.class);
        PlainDocument doc = new PlainDocument();
        doc.insertString(0, "list.", null);
        when(mockComponent.getDocument()).thenReturn(doc);
        when(mockComponent.getCaretPosition()).thenReturn(5);

        CompletionContextBuilder builder = new CompletionContextBuilder(mockComponent, 5);
        CompletionContextBuilder.CompletionContext context = builder.build();

        assertThat(context.getContextType()).isEqualTo("method_call");
    }

    @Test
    void testBuild_ParameterContext() throws Exception {
        JTextComponent mockComponent = mock(JTextComponent.class);
        PlainDocument doc = new PlainDocument();
        doc.insertString(0, "method(", null);
        when(mockComponent.getDocument()).thenReturn(doc);
        when(mockComponent.getCaretPosition()).thenReturn(7);

        CompletionContextBuilder builder = new CompletionContextBuilder(mockComponent, 7);
        CompletionContextBuilder.CompletionContext context = builder.build();

        assertThat(context.getContextType()).isEqualTo("parameter");
    }

    @Test
    void testCompletionContext_BuildPrompt() {
        CompletionContextBuilder.CompletionContext context = new CompletionContextBuilder.CompletionContext();
        context.setFileName("Test.java");
        context.setFileExtension("java");
        context.setContextType("method");
        context.setPrefix("test");
        context.setSurroundingContext("public void test");
        context.setCurrentLine("    test");

        String prompt = context.buildPrompt();
        assertThat(prompt).contains("Test.java");
        assertThat(prompt).contains("Java");
        assertThat(prompt).contains("test");
    }

    @Test
    void testCompletionContext_BuildPrompt_Python() {
        CompletionContextBuilder.CompletionContext context = new CompletionContextBuilder.CompletionContext();
        context.setFileName("script.py");
        context.setFileExtension("py");
        context.setContextType("general");
        context.setPrefix("def");
        context.setSurroundingContext("class MyClass:");
        context.setCurrentLine("    def");

        String prompt = context.buildPrompt();
        assertThat(prompt).contains("script.py");
        assertThat(prompt).contains("Python");
    }

    @Test
    void testCompletionContext_BuildPrompt_JavaScript() {
        CompletionContextBuilder.CompletionContext context = new CompletionContextBuilder.CompletionContext();
        context.setFileName("app.js");
        context.setFileExtension("js");

        String prompt = context.buildPrompt();
        assertThat(prompt).contains("JavaScript");
    }

    @Test
    void testCompletionContext_BuildPrompt_TypeScript() {
        CompletionContextBuilder.CompletionContext context = new CompletionContextBuilder.CompletionContext();
        context.setFileName("component.ts");
        context.setFileExtension("ts");

        String prompt = context.buildPrompt();
        assertThat(prompt).contains("TypeScript");
    }

    @Test
    void testCompletionContext_BuildPrompt_HTML() {
        CompletionContextBuilder.CompletionContext context = new CompletionContextBuilder.CompletionContext();
        context.setFileName("index.html");
        context.setFileExtension("html");

        String prompt = context.buildPrompt();
        assertThat(prompt).contains("HTML");
    }

    @Test
    void testCompletionContext_BuildPrompt_CSS() {
        CompletionContextBuilder.CompletionContext context = new CompletionContextBuilder.CompletionContext();
        context.setFileName("style.css");
        context.setFileExtension("css");

        String prompt = context.buildPrompt();
        assertThat(prompt).contains("CSS");
    }

    @Test
    void testCompletionContext_BuildPrompt_XML() {
        CompletionContextBuilder.CompletionContext context = new CompletionContextBuilder.CompletionContext();
        context.setFileName("config.xml");
        context.setFileExtension("xml");

        String prompt = context.buildPrompt();
        assertThat(prompt).contains("XML");
    }

    @Test
    void testCompletionContext_BuildPrompt_UnknownExtension() {
        CompletionContextBuilder.CompletionContext context = new CompletionContextBuilder.CompletionContext();
        context.setFileName("file.xyz");
        context.setFileExtension("xyz");

        String prompt = context.buildPrompt();
        assertThat(prompt).contains("xyz");
    }

    @Test
    void testCompletionContext_BuildPrompt_NullExtension() {
        CompletionContextBuilder.CompletionContext context = new CompletionContextBuilder.CompletionContext();
        context.setFileName("file");
        context.setFileExtension(null);

        String prompt = context.buildPrompt();
        assertThat(prompt).contains("unknown");
    }

    @Test
    void testCompletionContext_Getters() {
        CompletionContextBuilder.CompletionContext context = new CompletionContextBuilder.CompletionContext();
        context.setFileName("Test.java");
        context.setMimeType("text/x-java");
        context.setFileExtension("java");
        context.setTextBefore("before");
        context.setTextAfter("after");
        context.setCurrentLine("line");
        context.setSurroundingContext("context");
        context.setPrefix("pre");
        context.setContextType("type");
        context.setCaretOffset(42);

        assertThat(context.getFileName()).isEqualTo("Test.java");
        assertThat(context.getMimeType()).isEqualTo("text/x-java");
        assertThat(context.getFileExtension()).isEqualTo("java");
        assertThat(context.getTextBefore()).isEqualTo("before");
        assertThat(context.getTextAfter()).isEqualTo("after");
        assertThat(context.getCurrentLine()).isEqualTo("line");
        assertThat(context.getSurroundingContext()).isEqualTo("context");
        assertThat(context.getPrefix()).isEqualTo("pre");
        assertThat(context.getContextType()).isEqualTo("type");
        assertThat(context.getCaretOffset()).isEqualTo(42);
    }
}
