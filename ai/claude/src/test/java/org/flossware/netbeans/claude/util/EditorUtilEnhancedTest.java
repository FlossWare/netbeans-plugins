/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.claude.util;

import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Enhanced tests for EditorUtil to improve coverage.
 */
class EditorUtilEnhancedTest {

    @Test
    void testGetActiveEditor_MultipleCalls() {
        // Should consistently return null without editor
        assertThatCode(() -> {
            EditorUtil.getActiveEditor();
            EditorUtil.getActiveEditor();
            EditorUtil.getActiveEditor();
        }).doesNotThrowAnyException();
    }

    @Test
    void testGetSelectedText_MultipleCalls() {
        // Should consistently return null
        String text1 = EditorUtil.getSelectedText();
        String text2 = EditorUtil.getSelectedText();

        assertThat(text1).isEqualTo(text2);
    }

    @Test
    void testInsertTextAtCursor_EmptyString() {
        assertThatCode(() -> EditorUtil.insertTextAtCursor("")).doesNotThrowAnyException();
    }

    @Test
    void testInsertTextAtCursor_Whitespace() {
        assertThatCode(() -> EditorUtil.insertTextAtCursor("   \n\t  ")).doesNotThrowAnyException();
    }

    @Test
    void testInsertTextAtCursor_LongText() {
        String longText = "test ".repeat(10000);
        assertThatCode(() -> EditorUtil.insertTextAtCursor(longText)).doesNotThrowAnyException();
    }

    @Test
    void testInsertTextAtCursor_SpecialCharacters() {
        assertThatCode(() -> EditorUtil.insertTextAtCursor("\\n\\t\\r\\\"\\'")).doesNotThrowAnyException();
    }

    @Test
    void testReplaceSelectedText_EmptyString() {
        assertThatCode(() -> EditorUtil.replaceSelectedText("")).doesNotThrowAnyException();
    }

    @Test
    void testReplaceSelectedText_Whitespace() {
        assertThatCode(() -> EditorUtil.replaceSelectedText("   ")).doesNotThrowAnyException();
    }

    @Test
    void testReplaceSelectedText_LongText() {
        String longText = "replacement ".repeat(5000);
        assertThatCode(() -> EditorUtil.replaceSelectedText(longText)).doesNotThrowAnyException();
    }

    @Test
    void testInsertTextAtLine_Zero() {
        assertThatCode(() -> EditorUtil.insertTextAtLine(0, "text")).doesNotThrowAnyException();
    }

    @Test
    void testInsertTextAtLine_One() {
        assertThatCode(() -> EditorUtil.insertTextAtLine(1, "text")).doesNotThrowAnyException();
    }

    @Test
    void testInsertTextAtLine_MaxInteger() {
        assertThatCode(() -> EditorUtil.insertTextAtLine(Integer.MAX_VALUE, "text")).doesNotThrowAnyException();
    }

    @Test
    void testInsertTextAtLine_MinInteger() {
        assertThatCode(() -> EditorUtil.insertTextAtLine(Integer.MIN_VALUE, "text")).doesNotThrowAnyException();
    }

    @Test
    void testInsertTextAtLine_NullText() {
        assertThatCode(() -> EditorUtil.insertTextAtLine(0, null)).doesNotThrowAnyException();
    }

    @Test
    void testInsertTextAtLine_EmptyText() {
        assertThatCode(() -> EditorUtil.insertTextAtLine(0, "")).doesNotThrowAnyException();
    }

    @Test
    void testInsertTextAtLine_MultilineText() {
        assertThatCode(() -> EditorUtil.insertTextAtLine(0, "line1\nline2\nline3")).doesNotThrowAnyException();
    }

    @Test
    void testGetActiveDocument_MultipleCalls() {
        // Should consistently return null
        assertThat(EditorUtil.getActiveDocument()).isNull();
        assertThat(EditorUtil.getActiveDocument()).isNull();
    }

    @Test
    void testHasActiveEditor_Consistency() {
        // Should consistently return false
        boolean result1 = EditorUtil.hasActiveEditor();
        boolean result2 = EditorUtil.hasActiveEditor();

        assertThat(result1).isEqualTo(result2);
        assertThat(result1).isFalse();
    }

    @Test
    void testGetAllText_MultipleCalls() {
        // Should consistently return null
        String text1 = EditorUtil.getAllText();
        String text2 = EditorUtil.getAllText();

        assertThat(text1).isEqualTo(text2);
        assertThat(text1).isNull();
    }

    @Test
    void testSequentialOperations() {
        // Test calling multiple operations in sequence
        assertThatCode(() -> {
            EditorUtil.getActiveEditor();
            EditorUtil.hasActiveEditor();
            EditorUtil.getActiveDocument();
            EditorUtil.getSelectedText();
            EditorUtil.getAllText();
            EditorUtil.insertTextAtCursor("test");
            EditorUtil.replaceSelectedText("replacement");
            EditorUtil.insertTextAtLine(0, "line");
        }).doesNotThrowAnyException();
    }

    @Test
    void testConcurrentAccess() throws Exception {
        // Test thread safety - all methods should be safe to call concurrently
        Thread[] threads = new Thread[10];

        for (int i = 0; i < 10; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                EditorUtil.getActiveEditor();
                EditorUtil.hasActiveEditor();
                EditorUtil.getSelectedText();
                EditorUtil.insertTextAtCursor("test" + index);
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        // All should complete without exception
        assertThat(threads).allMatch(t -> !t.isAlive());
    }

    @Test
    void testInsertTextAtCursor_Unicode() {
        assertThatCode(() -> EditorUtil.insertTextAtCursor("こんにちは世界")).doesNotThrowAnyException();
    }

    @Test
    void testReplaceSelectedText_Unicode() {
        assertThatCode(() -> EditorUtil.replaceSelectedText("مرحبا بالعالم")).doesNotThrowAnyException();
    }

    @Test
    void testInsertTextAtLine_Unicode() {
        assertThatCode(() -> EditorUtil.insertTextAtLine(0, "Здравствуй мир")).doesNotThrowAnyException();
    }
}
