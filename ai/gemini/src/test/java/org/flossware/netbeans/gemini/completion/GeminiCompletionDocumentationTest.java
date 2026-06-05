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

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class GeminiCompletionDocumentationTest {

    @Test
    void testConstruction() {
        GeminiCompletionDocumentation doc = new GeminiCompletionDocumentation("text");
        assertThat(doc).isNotNull();
    }

    @Test
    void testConstruction_NullText() {
        assertThatCode(() -> new GeminiCompletionDocumentation(null)).doesNotThrowAnyException();
    }

    @Test
    void testConstruction_EmptyText() {
        GeminiCompletionDocumentation doc = new GeminiCompletionDocumentation("");
        assertThat(doc).isNotNull();
    }

    @Test
    void testGetText_ContainsInput() {
        GeminiCompletionDocumentation doc = new GeminiCompletionDocumentation("test text");
        assertThat(doc.getText()).contains("test text");
    }

    @Test
    void testGetText_IsHtml() {
        GeminiCompletionDocumentation doc = new GeminiCompletionDocumentation("code");
        String text = doc.getText();
        assertThat(text).startsWith("<html>");
        assertThat(text).endsWith("</html>");
    }

    @Test
    void testGetText_EscapesHtmlChars() {
        GeminiCompletionDocumentation doc = new GeminiCompletionDocumentation("List<String>");
        String text = doc.getText();
        assertThat(text).contains("&lt;");
        assertThat(text).contains("&gt;");
    }

    @Test
    void testGetText_EscapesAmpersand() {
        GeminiCompletionDocumentation doc = new GeminiCompletionDocumentation("foo & bar");
        String text = doc.getText();
        assertThat(text).contains("&amp;");
    }

    @Test
    void testGetText_ConvertsNewlines() {
        GeminiCompletionDocumentation doc = new GeminiCompletionDocumentation("line1\nline2");
        String text = doc.getText();
        assertThat(text).contains("<br/>");
    }

    @Test
    void testGetText_ContainsGeminiLabel() {
        GeminiCompletionDocumentation doc = new GeminiCompletionDocumentation("code");
        String text = doc.getText();
        assertThat(text).contains("Gemini AI Suggestion");
    }

    @Test
    void testGetURL() {
        GeminiCompletionDocumentation doc = new GeminiCompletionDocumentation("text");
        assertThat(doc.getURL()).isNull();
    }

    @Test
    void testResolveLink() {
        GeminiCompletionDocumentation doc = new GeminiCompletionDocumentation("text");
        assertThat(doc.resolveLink("link")).isNull();
    }

    @Test
    void testGetGotoSourceAction() {
        GeminiCompletionDocumentation doc = new GeminiCompletionDocumentation("text");
        assertThat(doc.getGotoSourceAction()).isNull();
    }

    @Test
    void testCancel_DoesNotThrow() {
        GeminiCompletionDocumentation doc = new GeminiCompletionDocumentation("text");
        assertThatCode(() -> doc.cancel()).doesNotThrowAnyException();
    }
}
