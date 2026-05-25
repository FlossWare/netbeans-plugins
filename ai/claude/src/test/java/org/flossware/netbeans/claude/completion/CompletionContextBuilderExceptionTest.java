/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.claude.completion;

import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import org.flossware.netbeans.claude.completion.CompletionContextBuilder.CompletionContext;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for CompletionContextBuilder exception handling paths.
 */
class CompletionContextBuilderExceptionTest {

    @Test
    void testBuild_DocumentThrowsBadLocationException() throws Exception {
        JTextComponent mockComponent = mock(JTextComponent.class);
        PlainDocument mockDoc = mock(PlainDocument.class);

        when(mockComponent.getDocument()).thenReturn(mockDoc);
        when(mockComponent.getCaretPosition()).thenReturn(10);

        // Make document throw BadLocationException
        when(mockDoc.getText(anyInt(), anyInt()))
            .thenThrow(new BadLocationException("test", 0));
        when(mockDoc.getLength()).thenReturn(100);

        CompletionContextBuilder builder = new CompletionContextBuilder(mockComponent, 10);
        CompletionContext context = builder.build();

        // Should still return a context, with empty fields
        assertThat(context).isNotNull();
    }

    @Test
    void testBuild_VeryLargeCaretOffset() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        String text = "test";
        doc.insertString(0, text, null);
        component.setDocument(doc);

        // Use offset way beyond document length
        CompletionContextBuilder builder = new CompletionContextBuilder(component, 10000);
        CompletionContext context = builder.build();

        assertThat(context).isNotNull();
    }

    @Test
    void testBuild_NegativeCaretOffset() {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        component.setDocument(doc);

        // Negative offset
        CompletionContextBuilder builder = new CompletionContextBuilder(component, -10);
        CompletionContext context = builder.build();

        assertThat(context).isNotNull();
    }

    @Test
    void testExtractPrefix_SingleCharAtStart() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        doc.insertString(0, "a", null);
        component.setDocument(doc);
        component.setCaretPosition(1);

        CompletionContextBuilder builder = new CompletionContextBuilder(component, 1);
        CompletionContext context = builder.build();

        // Should extract "a" as prefix
        assertThat(context.getPrefix()).isEqualTo("a");
    }

    @Test
    void testExtractPrefix_DotAtStart() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        doc.insertString(0, ".test", null);
        component.setDocument(doc);
        component.setCaretPosition(5);

        CompletionContextBuilder builder = new CompletionContextBuilder(component, 5);
        CompletionContext context = builder.build();

        // Should extract ".test" including the dot
        assertThat(context.getPrefix()).isEqualTo(".test");
    }

    @Test
    void testExtractPrefix_OnlyDots() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        doc.insertString(0, "...", null);
        component.setDocument(doc);
        component.setCaretPosition(3);

        CompletionContextBuilder builder = new CompletionContextBuilder(component, 3);
        CompletionContext context = builder.build();

        assertThat(context.getPrefix()).contains(".");
    }

    @Test
    void testExtractPrefix_NumbersAndLetters() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        doc.insertString(0, "test123variable", null);
        component.setDocument(doc);
        component.setCaretPosition(15);

        CompletionContextBuilder builder = new CompletionContextBuilder(component, 15);
        CompletionContext context = builder.build();

        assertThat(context.getPrefix()).isEqualTo("test123variable");
    }

    @Test
    void testExtractPrefix_UnderscoreInIdentifier() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        doc.insertString(0, "my_variable_name", null);
        component.setDocument(doc);
        component.setCaretPosition(16);

        CompletionContextBuilder builder = new CompletionContextBuilder(component, 16);
        CompletionContext context = builder.build();

        assertThat(context.getPrefix()).isEqualTo("my_variable_name");
    }

    @Test
    void testExtractPrefix_DollarSign() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        doc.insertString(0, "$varname", null);
        component.setDocument(doc);
        component.setCaretPosition(8);

        CompletionContextBuilder builder = new CompletionContextBuilder(component, 8);
        CompletionContext context = builder.build();

        assertThat(context.getPrefix()).isEqualTo("$varname");
    }

    @Test
    void testExtractSurroundingContext_ExactlyAtStart() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        doc.insertString(0, "start", null);
        component.setDocument(doc);
        component.setCaretPosition(0);

        CompletionContextBuilder builder = new CompletionContextBuilder(component, 0);
        CompletionContext context = builder.build();

        // At position 0, surrounding context should be empty
        assertThat(context.getSurroundingContext()).isEmpty();
    }

    @Test
    void testExtractSurroundingContext_Exactly1000CharsBack() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();

        // Create exactly 1000 characters
        String text = "x".repeat(1000);
        doc.insertString(0, text, null);
        component.setDocument(doc);
        component.setCaretPosition(1000);

        CompletionContextBuilder builder = new CompletionContextBuilder(component, 1000);
        CompletionContext context = builder.build();

        // Should get all 1000 chars
        assertThat(context.getSurroundingContext().length()).isEqualTo(1000);
    }

    @Test
    void testExtractSurroundingContext_MoreThan1000CharsBack() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();

        // Create more than 1000 characters
        String text = "x".repeat(2000);
        doc.insertString(0, text, null);
        component.setDocument(doc);
        component.setCaretPosition(2000);

        CompletionContextBuilder builder = new CompletionContextBuilder(component, 2000);
        CompletionContext context = builder.build();

        // Should get only last 1000 chars
        assertThat(context.getSurroundingContext().length()).isEqualTo(1000);
    }

    @Test
    void testDetermineContextType_ExtendsAtVeryStart() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        doc.insertString(0, "extends Base", null);
        component.setDocument(doc);
        component.setCaretPosition(12);

        CompletionContextBuilder builder = new CompletionContextBuilder(component, 12);
        CompletionContext context = builder.build();

        assertThat(context.getContextType()).isEqualTo("type");
    }

    @Test
    void testDetermineContextType_ImplementsAtVeryStart() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        doc.insertString(0, "implements Interface", null);
        component.setDocument(doc);
        component.setCaretPosition(20);

        CompletionContextBuilder builder = new CompletionContextBuilder(component, 20);
        CompletionContext context = builder.build();

        assertThat(context.getContextType()).isEqualTo("type");
    }

    @Test
    void testDetermineContextType_NewAtVeryStart() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        doc.insertString(0, "new Object", null);
        component.setDocument(doc);
        component.setCaretPosition(10);

        CompletionContextBuilder builder = new CompletionContextBuilder(component, 10);
        CompletionContext context = builder.build();

        assertThat(context.getContextType()).isEqualTo("constructor");
    }

    @Test
    void testDetermineContextType_ImportAtVeryStart() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        doc.insertString(0, "import java.util.List", null);
        component.setDocument(doc);
        component.setCaretPosition(21);

        CompletionContextBuilder builder = new CompletionContextBuilder(component, 21);
        CompletionContext context = builder.build();

        assertThat(context.getContextType()).isEqualTo("import");
    }

    @Test
    void testDetermineContextType_ParenthesisAtVeryStart() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        doc.insertString(0, "(param", null);
        component.setDocument(doc);
        component.setCaretPosition(6);

        CompletionContextBuilder builder = new CompletionContextBuilder(component, 6);
        CompletionContext context = builder.build();

        assertThat(context.getContextType()).isEqualTo("parameter");
    }

    @Test
    void testDetermineContextType_DotInPrefix() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        doc.insertString(0, "obj.meth", null);
        component.setDocument(doc);
        component.setCaretPosition(8);

        CompletionContextBuilder builder = new CompletionContextBuilder(component, 8);
        CompletionContext context = builder.build();

        assertThat(context.getContextType()).isEqualTo("method_call");
    }

    @Test
    void testDetermineContextType_OnlyGeneral() throws Exception {
        JTextComponent component = new JTextArea();
        PlainDocument doc = new PlainDocument();
        doc.insertString(0, "someVariable", null);
        component.setDocument(doc);
        component.setCaretPosition(12);

        CompletionContextBuilder builder = new CompletionContextBuilder(component, 12);
        CompletionContext context = builder.build();

        assertThat(context.getContextType()).isEqualTo("general");
    }

    @Test
    void testBuild_WithMockDocumentThrowing() throws Exception {
        JTextComponent mockComponent = mock(JTextComponent.class);
        PlainDocument mockDoc = mock(PlainDocument.class);

        when(mockComponent.getDocument()).thenReturn(mockDoc);
        when(mockComponent.getCaretPosition()).thenReturn(5);

        // First call succeeds, subsequent calls fail
        when(mockDoc.getText(eq(0), eq(5)))
            .thenThrow(new BadLocationException("test", 0));
        when(mockDoc.getText(eq(5), anyInt()))
            .thenThrow(new BadLocationException("test", 5));
        when(mockDoc.getLength()).thenReturn(10);

        CompletionContextBuilder builder = new CompletionContextBuilder(mockComponent, 5);
        CompletionContext context = builder.build();

        // Should handle exceptions gracefully
        assertThat(context).isNotNull();
    }
}
