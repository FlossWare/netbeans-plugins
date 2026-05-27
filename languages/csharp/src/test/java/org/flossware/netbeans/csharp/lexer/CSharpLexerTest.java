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

package org.flossware.netbeans.csharp.lexer;

import org.junit.jupiter.api.Test;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for CSharpLexer.
 */
class CSharpLexerTest {

    private TokenSequence<CSharpTokenId> createTokenSequence(String text) {
        TokenHierarchy<?> hierarchy = TokenHierarchy.create(text, CSharpTokenId.language());
        return hierarchy.tokenSequence(CSharpTokenId.language());
    }

    @Test
    void testKeywordRecognition() {
        TokenSequence<CSharpTokenId> ts = createTokenSequence("class Program");
        assertThat(ts.moveNext()).isTrue();
        assertThat(ts.token().id()).isEqualTo(CSharpTokenId.KEYWORD);
        assertThat(ts.token().text().toString()).isEqualTo("class");
    }

    @Test
    void testCSharpKeywords() {
        String[] keywords = {
            "class", "public", "private", "protected", "void", "int", "string", "if",
            "else", "return", "namespace", "using", "static", "readonly", "const",
            "interface", "abstract", "virtual", "override", "async", "await"
        };

        for (String keyword : keywords) {
            TokenSequence<CSharpTokenId> ts = createTokenSequence(keyword);
            assertThat(ts.moveNext()).isTrue();
            assertThat(ts.token().id())
                .withFailMessage("Keyword '%s' should be recognized as KEYWORD", keyword)
                .isEqualTo(CSharpTokenId.KEYWORD);
        }
    }

    @Test
    void testStringLiterals() {
        TokenSequence<CSharpTokenId> ts = createTokenSequence("\"hello\"");
        assertThat(ts.moveNext()).isTrue();
        assertThat(ts.token().id()).isEqualTo(CSharpTokenId.STRING);
    }

    @Test
    void testNumbers() {
        String[] numbers = {"123", "3.14", "1e10", "0x1A"};

        for (String num : numbers) {
            TokenSequence<CSharpTokenId> ts = createTokenSequence(num);
            assertThat(ts.moveNext()).isTrue();
            assertThat(ts.token().id())
                .withFailMessage("Number '%s' should be recognized as NUMBER", num)
                .isEqualTo(CSharpTokenId.NUMBER);
        }
    }

    @Test
    void testSingleLineComment() {
        TokenSequence<CSharpTokenId> ts = createTokenSequence("// comment");
        assertThat(ts.moveNext()).isTrue();
        assertThat(ts.token().id()).isEqualTo(CSharpTokenId.COMMENT);
    }

    @Test
    void testMultiLineComment() {
        TokenSequence<CSharpTokenId> ts = createTokenSequence("/* comment */");
        assertThat(ts.moveNext()).isTrue();
        assertThat(ts.token().id()).isEqualTo(CSharpTokenId.COMMENT);
    }
}
