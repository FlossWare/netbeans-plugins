/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.claude.util;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Tests for EditorUtil.
 * Note: Tests run without actual NetBeans editor, so most methods return null.
 * This tests the null-safety and method contracts.
 */
class EditorUtilTest {

    @Test
    void testGetActiveEditor_NoEditor() {
        // When no editor is active, should return null without throwing
        assertThatCode(() -> EditorUtil.getActiveEditor()).doesNotThrowAnyException();
    }

    @Test
    void testGetSelectedText_NoEditor() {
        // Should return null when no editor is active
        String selected = EditorUtil.getSelectedText();
        assertThat(selected).isNull();
    }

    @Test
    void testInsertTextAtCursor_NoEditor() {
        // Should not throw when no editor is active
        assertThatCode(() -> EditorUtil.insertTextAtCursor("test")).doesNotThrowAnyException();
    }

    @Test
    void testInsertTextAtCursor_NullText() {
        // Should handle null text without throwing
        assertThatCode(() -> EditorUtil.insertTextAtCursor(null)).doesNotThrowAnyException();
    }

    @Test
    void testReplaceSelectedText_NoEditor() {
        // Should not throw when no editor is active
        assertThatCode(() -> EditorUtil.replaceSelectedText("replacement")).doesNotThrowAnyException();
    }

    @Test
    void testReplaceSelectedText_NullText() {
        // Should handle null replacement without throwing
        assertThatCode(() -> EditorUtil.replaceSelectedText(null)).doesNotThrowAnyException();
    }

    @Test
    void testInsertTextAtLine_NoEditor() {
        // Should not throw when no editor is active
        assertThatCode(() -> EditorUtil.insertTextAtLine(0, "text")).doesNotThrowAnyException();
    }

    @Test
    void testInsertTextAtLine_NegativeLine() {
        // Should handle negative line numbers
        assertThatCode(() -> EditorUtil.insertTextAtLine(-1, "text")).doesNotThrowAnyException();
    }

    @Test
    void testInsertTextAtLine_LargeLine() {
        // Should handle large line numbers
        assertThatCode(() -> EditorUtil.insertTextAtLine(99999, "text")).doesNotThrowAnyException();
    }

    @Test
    void testGetActiveDocument_NoEditor() {
        // Should return null when no editor is active
        assertThat(EditorUtil.getActiveDocument()).isNull();
    }

    @Test
    void testHasActiveEditor_NoEditor() {
        // Should return false when no editor is active
        assertThat(EditorUtil.hasActiveEditor()).isFalse();
    }

    @Test
    void testGetAllText_NoEditor() {
        // Should return null when no editor is active
        assertThat(EditorUtil.getAllText()).isNull();
    }
}
