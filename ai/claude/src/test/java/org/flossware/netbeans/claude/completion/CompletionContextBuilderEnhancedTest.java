/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.claude.completion;

import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import javax.swing.JTextArea;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Enhanced tests for CompletionContextBuilder focusing on edge cases in private methods.
 */
class CompletionContextBuilderEnhancedTest {

    @Test
    void testBuild_EmptyDocument() {
        JTextComponent mockComponent = mock(JTextComponent.class);
        PlainDocument doc = new PlainDocument();
        when(mockComponent.getDocument()).thenReturn(doc);
        when(mockComponent.getCaretPosition()).thenReturn(0);

        CompletionContextBuilder builder = new CompletionContextBuilder(mockComponent, 0);
        CompletionContextBuilder.CompletionContext context = builder.build();

        assertThat(context).isNotNull();
        assertThat(context.getTextBefore()).isEmpty();
        assertThat(context.getTextAfter()).isEmpty();
        assertThat(context.getPrefix()).isEmpty();
    }

    @Test
    void testBuild_ExtendsKeyword() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        String text = "class MyClass extends Base";
        doc.insertString(0, text, null);
        component.setDocument(doc);
        component.setCaretPosition(text.length());

        CompletionContextBuilder builder = new CompletionContextBuilder(component, text.length());
        CompletionContextBuilder.CompletionContext context = builder.build();

        assertThat(context.getContextType()).isEqualTo("type");
    }

    @Test
    void testBuild_ImplementsKeyword() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        String text = "class MyClass implements Interface";
        doc.insertString(0, text, null);
        component.setDocument(doc);
        component.setCaretPosition(text.length());

        CompletionContextBuilder builder = new CompletionContextBuilder(component, text.length());
        CompletionContextBuilder.CompletionContext context = builder.build();

        assertThat(context.getContextType()).isEqualTo("type");
    }

    @Test
    void testExtractPrefix_WithDot() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        String text = "object.method";
        doc.insertString(0, text, null);
        component.setDocument(doc);
        component.setCaretPosition(text.length());

        CompletionContextBuilder builder = new CompletionContextBuilder(component, text.length());
        CompletionContextBuilder.CompletionContext context = builder.build();

        assertThat(context.getPrefix()).isEqualTo("object.method");
        assertThat(context.getContextType()).isEqualTo("method_call");
    }

    @Test
    void testExtractPrefix_MultipleDots() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        String text = "object.property.method";
        doc.insertString(0, text, null);
        component.setDocument(doc);
        component.setCaretPosition(text.length());

        CompletionContextBuilder builder = new CompletionContextBuilder(component, text.length());
        CompletionContextBuilder.CompletionContext context = builder.build();

        assertThat(context.getPrefix()).contains(".");
        assertThat(context.getContextType()).isEqualTo("method_call");
    }

    @Test
    void testExtractPrefix_AtStartOfLine() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        String text = "test";
        doc.insertString(0, text, null);
        component.setDocument(doc);
        component.setCaretPosition(text.length());

        CompletionContextBuilder builder = new CompletionContextBuilder(component, text.length());
        CompletionContextBuilder.CompletionContext context = builder.build();

        assertThat(context.getPrefix()).isEqualTo("test");
    }

    @Test
    void testExtractPrefix_AfterSpace() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        String text = "public test";
        doc.insertString(0, text, null);
        component.setDocument(doc);
        component.setCaretPosition(text.length());

        CompletionContextBuilder builder = new CompletionContextBuilder(component, text.length());
        CompletionContextBuilder.CompletionContext context = builder.build();

        assertThat(context.getPrefix()).isEqualTo("test");
    }

    @Test
    void testExtractPrefix_AfterSymbol() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        String text = "(test";
        doc.insertString(0, text, null);
        component.setDocument(doc);
        component.setCaretPosition(text.length());

        CompletionContextBuilder builder = new CompletionContextBuilder(component, text.length());
        CompletionContextBuilder.CompletionContext context = builder.build();

        assertThat(context.getPrefix()).isEqualTo("test");
    }

    @Test
    void testExtractPrefix_EmptyPrefix() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        String text = "public ";
        doc.insertString(0, text, null);
        component.setDocument(doc);
        component.setCaretPosition(text.length());

        CompletionContextBuilder builder = new CompletionContextBuilder(component, text.length());
        CompletionContextBuilder.CompletionContext context = builder.build();

        assertThat(context.getPrefix()).isEmpty();
    }

    @Test
    void testExtractSurroundingContext_LongDocument() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        // Create document longer than 1000 chars
        String longText = "public class Test {\n".repeat(100);
        doc.insertString(0, longText, null);
        component.setDocument(doc);
        component.setCaretPosition(longText.length());

        CompletionContextBuilder builder = new CompletionContextBuilder(component, longText.length());
        CompletionContextBuilder.CompletionContext context = builder.build();

        // Should get last 1000 chars
        assertThat(context.getSurroundingContext()).isNotEmpty();
        assertThat(context.getSurroundingContext().length()).isLessThanOrEqualTo(1000);
    }

    @Test
    void testExtractSurroundingContext_ShortDocument() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        String text = "short text";
        doc.insertString(0, text, null);
        component.setDocument(doc);
        component.setCaretPosition(text.length());

        CompletionContextBuilder builder = new CompletionContextBuilder(component, text.length());
        CompletionContextBuilder.CompletionContext context = builder.build();

        assertThat(context.getSurroundingContext()).isEqualTo(text);
    }

    @Test
    void testExtractSurroundingContext_AtStart() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        String text = "start";
        doc.insertString(0, text, null);
        component.setDocument(doc);
        component.setCaretPosition(0);

        CompletionContextBuilder builder = new CompletionContextBuilder(component, 0);
        CompletionContextBuilder.CompletionContext context = builder.build();

        assertThat(context.getSurroundingContext()).isEmpty();
    }

    @Test
    void testExtractCurrentLine_MultilineDocument() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        String text = "line1\nline2\nline3";
        doc.insertString(0, text, null);
        component.setDocument(doc);
        // Position at start of line2
        component.setCaretPosition(6);

        CompletionContextBuilder builder = new CompletionContextBuilder(component, 6);
        CompletionContextBuilder.CompletionContext context = builder.build();

        assertThat(context.getCurrentLine()).contains("line2");
    }

    @Test
    void testExtractCurrentLine_AtLineEnd() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        String text = "first line\nsecond";
        doc.insertString(0, text, null);
        component.setDocument(doc);
        component.setCaretPosition(10); // End of first line

        CompletionContextBuilder builder = new CompletionContextBuilder(component, 10);
        CompletionContextBuilder.CompletionContext context = builder.build();

        assertThat(context.getCurrentLine()).isNotEmpty();
    }

    @Test
    void testDetermineContextType_NewKeyword() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        String text = "Object obj = new ";
        doc.insertString(0, text, null);
        component.setDocument(doc);
        component.setCaretPosition(text.length());

        CompletionContextBuilder builder = new CompletionContextBuilder(component, text.length());
        CompletionContextBuilder.CompletionContext context = builder.build();

        assertThat(context.getContextType()).isEqualTo("constructor");
    }

    @Test
    void testDetermineContextType_ImportKeyword() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        String text = "import java.util.";
        doc.insertString(0, text, null);
        component.setDocument(doc);
        component.setCaretPosition(text.length());

        CompletionContextBuilder builder = new CompletionContextBuilder(component, text.length());
        CompletionContextBuilder.CompletionContext context = builder.build();

        assertThat(context.getContextType()).isEqualTo("import");
    }

    @Test
    void testDetermineContextType_Parameter() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        String text = "method(param1, ";
        doc.insertString(0, text, null);
        component.setDocument(doc);
        component.setCaretPosition(text.length());

        CompletionContextBuilder builder = new CompletionContextBuilder(component, text.length());
        CompletionContextBuilder.CompletionContext context = builder.build();

        assertThat(context.getContextType()).isEqualTo("parameter");
    }

    @Test
    void testDetermineContextType_General() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        String text = "public void method() {\n    ";
        doc.insertString(0, text, null);
        component.setDocument(doc);
        component.setCaretPosition(text.length());

        CompletionContextBuilder builder = new CompletionContextBuilder(component, text.length());
        CompletionContextBuilder.CompletionContext context = builder.build();

        assertThat(context.getContextType()).isEqualTo("general");
    }

    @Test
    void testBuild_NullStreamDescriptionProperty() {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        // Don't set StreamDescriptionProperty - will be null
        component.setDocument(doc);
        component.setCaretPosition(0);

        CompletionContextBuilder builder = new CompletionContextBuilder(component, 0);
        CompletionContextBuilder.CompletionContext context = builder.build();

        assertThat(context).isNotNull();
        assertThat(context.getFileName()).isNull();
        assertThat(context.getMimeType()).isNull();
        assertThat(context.getFileExtension()).isNull();
    }

    @Test
    void testBuild_CaretAtEnd() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        String text = "complete text";
        doc.insertString(0, text, null);
        component.setDocument(doc);
        component.setCaretPosition(text.length());

        CompletionContextBuilder builder = new CompletionContextBuilder(component, text.length());
        CompletionContextBuilder.CompletionContext context = builder.build();

        assertThat(context.getTextBefore()).isEqualTo(text);
        assertThat(context.getTextAfter()).isEmpty();
    }

    @Test
    void testBuild_CaretInMiddle() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        String text = "before|after";
        doc.insertString(0, text, null);
        component.setDocument(doc);
        int caretPos = 7; // After "before|"
        component.setCaretPosition(caretPos);

        CompletionContextBuilder builder = new CompletionContextBuilder(component, caretPos);
        CompletionContextBuilder.CompletionContext context = builder.build();

        assertThat(context.getTextBefore()).startsWith("before");
        assertThat(context.getTextAfter()).contains("after");
    }

    @Test
    void testBuild_SpecialCharactersInText() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        String text = "String s = \"test\\n\\t\"; // comment";
        doc.insertString(0, text, null);
        component.setDocument(doc);
        component.setCaretPosition(text.length());

        CompletionContextBuilder builder = new CompletionContextBuilder(component, text.length());
        CompletionContextBuilder.CompletionContext context = builder.build();

        assertThat(context.getTextBefore()).contains("\\n");
        assertThat(context.getTextBefore()).contains("//");
    }

    @Test
    void testBuild_UnicodeText() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        String text = "String msg = \"こんにちは\";";
        doc.insertString(0, text, null);
        component.setDocument(doc);
        component.setCaretPosition(text.length());

        CompletionContextBuilder builder = new CompletionContextBuilder(component, text.length());
        CompletionContextBuilder.CompletionContext context = builder.build();

        assertThat(context.getTextBefore()).contains("こんにちは");
    }

    @Test
    void testCompletionContext_BuildPrompt_WithNulls() {
        CompletionContextBuilder.CompletionContext context = new CompletionContextBuilder.CompletionContext();
        // Leave everything null
        String prompt = context.buildPrompt();

        assertThat(prompt).isNotNull();
        assertThat(prompt).contains("unknown"); // For null extension
    }

    @Test
    void testCompletionContext_BuildPrompt_EmptyStrings() {
        CompletionContextBuilder.CompletionContext context = new CompletionContextBuilder.CompletionContext();
        context.setFileName("");
        context.setFileExtension("");
        context.setContextType("");
        context.setPrefix("");
        context.setSurroundingContext("");
        context.setCurrentLine("");

        String prompt = context.buildPrompt();

        assertThat(prompt).isNotNull();
        assertThat(prompt).contains("code completion");
    }

    @Test
    void testCompletionContext_BuildPrompt_LongContext() {
        CompletionContextBuilder.CompletionContext context = new CompletionContextBuilder.CompletionContext();
        context.setFileName("LongFile.java");
        context.setFileExtension("java");
        context.setSurroundingContext("context ".repeat(200)); // Very long context

        String prompt = context.buildPrompt();

        assertThat(prompt).isNotNull();
        assertThat(prompt.length()).isGreaterThan(1000);
    }

    @Test
    void testCompletionContext_BuildPrompt_SpecialCharacters() {
        CompletionContextBuilder.CompletionContext context = new CompletionContextBuilder.CompletionContext();
        context.setFileName("Test<T>.java");
        context.setFileExtension("java");
        context.setPrefix("test<>");
        context.setCurrentLine("    List<String> test<>");

        String prompt = context.buildPrompt();

        assertThat(prompt).contains("<");
        assertThat(prompt).contains(">");
    }
}
