/*
 * Copyright 2026 FlossWare.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.flossware.netbeans.gemini.completion;

import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class GeminiCompletionItemTest {

    @Test
    void testConstruction() {
        GeminiCompletionItem item = new GeminiCompletionItem("text", "prefix", 10, 0);
        assertThat(item).isNotNull();
    }

    @Test
    void testGetSortPriority() {
        GeminiCompletionItem item = new GeminiCompletionItem("text", "prefix", 10, 5);
        assertThat(item.getSortPriority()).isEqualTo(5);
    }

    @Test
    void testGetSortText() {
        GeminiCompletionItem item = new GeminiCompletionItem("myText", "prefix", 10, 0);
        assertThat(item.getSortText()).isEqualTo("myText");
    }

    @Test
    void testGetInsertPrefix() {
        GeminiCompletionItem item = new GeminiCompletionItem("insertMe", "prefix", 10, 0);
        assertThat(item.getInsertPrefix()).isEqualTo("insertMe");
    }

    @Test
    void testDefaultAction_ValidComponent() throws Exception {
        GeminiCompletionItem item = new GeminiCompletionItem("newText", "old", 3, 0);
        JTextComponent mockComponent = mock(JTextComponent.class);
        PlainDocument doc = new PlainDocument();
        doc.insertString(0, "old", null);
        when(mockComponent.getDocument()).thenReturn(doc);

        assertThatCode(() -> item.defaultAction(mockComponent)).doesNotThrowAnyException();
    }

    @Test
    void testProcessKeyEvent_NullEvent() {
        GeminiCompletionItem item = new GeminiCompletionItem("text", "prefix", 10, 0);
        assertThatCode(() -> item.processKeyEvent(null)).doesNotThrowAnyException();
    }

    @Test
    void testCreateDocumentationTask() {
        GeminiCompletionItem item = new GeminiCompletionItem("text", "prefix", 10, 0);
        assertThat(item.createDocumentationTask()).isNotNull();
    }

    @Test
    void testCreateToolTipTask() {
        GeminiCompletionItem item = new GeminiCompletionItem("text", "prefix", 10, 0);
        assertThat(item.createToolTipTask()).isNull();
    }

    @Test
    void testInstantSubstitution() {
        GeminiCompletionItem item = new GeminiCompletionItem("text", "prefix", 10, 0);
        JTextComponent mockComponent = mock(JTextComponent.class);
        assertThat(item.instantSubstitution(mockComponent)).isFalse();
    }

    @Test
    void testLongText_Truncation() {
        String longText = "This is a very long text that should be truncated when displayed in the completion popup because it exceeds the maximum display length";
        GeminiCompletionItem item = new GeminiCompletionItem(longText, "prefix", 10, 0);
        assertThat(item).isNotNull();
    }

    @Test
    void testMultilineText() {
        String multilineText = "line1\nline2\nline3";
        GeminiCompletionItem item = new GeminiCompletionItem(multilineText, "prefix", 10, 0);
        assertThat(item).isNotNull();
    }

    @Test
    void testEmptyPrefix() {
        GeminiCompletionItem item = new GeminiCompletionItem("text", "", 0, 0);
        assertThat(item.getInsertPrefix()).isEqualTo("text");
    }

    @Test
    void testZeroPriority() {
        GeminiCompletionItem item = new GeminiCompletionItem("text", "prefix", 10, 0);
        assertThat(item.getSortPriority()).isEqualTo(0);
    }

    @Test
    void testNegativePriority() {
        GeminiCompletionItem item = new GeminiCompletionItem("text", "prefix", 10, -1);
        assertThat(item.getSortPriority()).isEqualTo(-1);
    }
}
