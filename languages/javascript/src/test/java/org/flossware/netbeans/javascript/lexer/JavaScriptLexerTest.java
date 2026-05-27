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

package org.flossware.netbeans.javascript.lexer;

import org.junit.jupiter.api.Test;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for JavaScriptLexer.
 */
class JavaScriptLexerTest {

    private TokenSequence<JavaScriptTokenId> createTokenSequence(String text) {
        TokenHierarchy<?> hierarchy = TokenHierarchy.create(text, JavaScriptTokenId.language());
        return hierarchy.tokenSequence(JavaScriptTokenId.language());
    }

    @Test
    void testKeywordRecognition() {
        TokenSequence<JavaScriptTokenId> ts = createTokenSequence("function test");
        assertThat(ts.moveNext()).isTrue();
        assertThat(ts.token().id()).isEqualTo(JavaScriptTokenId.KEYWORD);
        assertThat(ts.token().text().toString()).isEqualTo("function");
    }

    @Test
    void testJavaScriptKeywords() {
        String[] keywords = {
            "function", "var", "let", "const", "if", "else", "return", "for", "while",
            "class", "extends", "import", "export", "async", "await", "try", "catch"
        };

        for (String keyword : keywords) {
            TokenSequence<JavaScriptTokenId> ts = createTokenSequence(keyword);
            assertThat(ts.moveNext()).isTrue();
            assertThat(ts.token().id())
                .withFailMessage("Keyword '%s' should be recognized as KEYWORD", keyword)
                .isEqualTo(JavaScriptTokenId.KEYWORD);
        }
    }

    @Test
    void testStringLiterals() {
        TokenSequence<JavaScriptTokenId> ts = createTokenSequence("\"hello\"");
        assertThat(ts.moveNext()).isTrue();
        assertThat(ts.token().id()).isEqualTo(JavaScriptTokenId.STRING);
    }

    @Test
    void testNumbers() {
        String[] numbers = {"123", "3.14", "1e10", "0x1A"};

        for (String num : numbers) {
            TokenSequence<JavaScriptTokenId> ts = createTokenSequence(num);
            assertThat(ts.moveNext()).isTrue();
            assertThat(ts.token().id())
                .withFailMessage("Number '%s' should be recognized as NUMBER", num)
                .isEqualTo(JavaScriptTokenId.NUMBER);
        }
    }

    @Test
    void testSingleLineComment() {
        TokenSequence<JavaScriptTokenId> ts = createTokenSequence("// comment");
        assertThat(ts.moveNext()).isTrue();
        assertThat(ts.token().id()).isEqualTo(JavaScriptTokenId.COMMENT);
    }

    @Test
    void testMultiLineComment() {
        TokenSequence<JavaScriptTokenId> ts = createTokenSequence("/* comment */");
        assertThat(ts.moveNext()).isTrue();
        assertThat(ts.token().id()).isEqualTo(JavaScriptTokenId.COMMENT);
    }
}
