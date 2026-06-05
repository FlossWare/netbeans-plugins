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

package org.flossware.netbeans.chatgpt.completion;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ChatGPTCompletionDocumentationTest {

    @Test
    void testConstruction() {
        ChatGPTCompletionDocumentation doc = new ChatGPTCompletionDocumentation("text");
        assertThat(doc).isNotNull();
    }

    @Test
    void testConstruction_NullText() {
        assertThatCode(() -> new ChatGPTCompletionDocumentation(null)).doesNotThrowAnyException();
    }

    @Test
    void testConstruction_EmptyText() {
        ChatGPTCompletionDocumentation doc = new ChatGPTCompletionDocumentation("");
        assertThat(doc).isNotNull();
    }

    @Test
    void testGetText_ContainsInput() {
        ChatGPTCompletionDocumentation doc = new ChatGPTCompletionDocumentation("test text");
        assertThat(doc.getText()).contains("test text");
    }

    @Test
    void testGetText_IsHtml() {
        ChatGPTCompletionDocumentation doc = new ChatGPTCompletionDocumentation("code");
        String text = doc.getText();
        assertThat(text).startsWith("<html>");
        assertThat(text).endsWith("</html>");
    }

    @Test
    void testGetText_EscapesHtmlChars() {
        ChatGPTCompletionDocumentation doc = new ChatGPTCompletionDocumentation("List<String>");
        String text = doc.getText();
        assertThat(text).contains("&lt;");
        assertThat(text).contains("&gt;");
    }

    @Test
    void testGetText_EscapesAmpersand() {
        ChatGPTCompletionDocumentation doc = new ChatGPTCompletionDocumentation("foo & bar");
        String text = doc.getText();
        assertThat(text).contains("&amp;");
    }

    @Test
    void testGetText_ConvertsNewlines() {
        ChatGPTCompletionDocumentation doc = new ChatGPTCompletionDocumentation("line1\nline2");
        String text = doc.getText();
        assertThat(text).contains("<br/>");
    }

    @Test
    void testGetText_ContainsChatGPTLabel() {
        ChatGPTCompletionDocumentation doc = new ChatGPTCompletionDocumentation("code");
        String text = doc.getText();
        assertThat(text).contains("ChatGPT AI Suggestion");
    }

    @Test
    void testGetURL() {
        ChatGPTCompletionDocumentation doc = new ChatGPTCompletionDocumentation("text");
        assertThat(doc.getURL()).isNull();
    }

    @Test
    void testResolveLink() {
        ChatGPTCompletionDocumentation doc = new ChatGPTCompletionDocumentation("text");
        assertThat(doc.resolveLink("link")).isNull();
    }

    @Test
    void testGetGotoSourceAction() {
        ChatGPTCompletionDocumentation doc = new ChatGPTCompletionDocumentation("text");
        assertThat(doc.getGotoSourceAction()).isNull();
    }

    @Test
    void testCancel_DoesNotThrow() {
        ChatGPTCompletionDocumentation doc = new ChatGPTCompletionDocumentation("text");
        assertThatCode(() -> doc.cancel()).doesNotThrowAnyException();
    }
}
