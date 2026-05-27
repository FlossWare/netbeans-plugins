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

package org.flossware.netbeans.typescript.lexer;

import org.junit.jupiter.api.Test;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for TypeScriptLexer.
 */
class TypeScriptLexerTest {

    private TokenSequence<TypeScriptTokenId> createTokenSequence(String text) {
        TokenHierarchy<?> hierarchy = TokenHierarchy.create(text, TypeScriptTokenId.language());
        return hierarchy.tokenSequence(TypeScriptTokenId.language());
    }

    @Test
    void testKeywordRecognition() {
        TokenSequence<TypeScriptTokenId> ts = createTokenSequence("function test");
        assertThat(ts.moveNext()).isTrue();
        assertThat(ts.token().id()).isEqualTo(TypeScriptTokenId.KEYWORD);
        assertThat(ts.token().text().toString()).isEqualTo("function");
    }

    @Test
    void testTypeScriptKeywords() {
        String[] keywords = {
            "function", "var", "let", "const", "if", "else", "return", "interface",
            "type", "namespace", "module", "class", "extends", "implements", "public",
            "private", "protected", "readonly", "async", "await"
        };

        for (String keyword : keywords) {
            TokenSequence<TypeScriptTokenId> ts = createTokenSequence(keyword);
            assertThat(ts.moveNext()).isTrue();
            assertThat(ts.token().id())
                .withFailMessage("Keyword '%s' should be recognized as KEYWORD", keyword)
                .isEqualTo(TypeScriptTokenId.KEYWORD);
        }
    }

    @Test
    void testStringLiterals() {
        TokenSequence<TypeScriptTokenId> ts = createTokenSequence("\"hello\"");
        assertThat(ts.moveNext()).isTrue();
        assertThat(ts.token().id()).isEqualTo(TypeScriptTokenId.STRING);
    }

    @Test
    void testNumbers() {
        String[] numbers = {"123", "3.14", "1e10", "0x1A"};

        for (String num : numbers) {
            TokenSequence<TypeScriptTokenId> ts = createTokenSequence(num);
            assertThat(ts.moveNext()).isTrue();
            assertThat(ts.token().id())
                .withFailMessage("Number '%s' should be recognized as NUMBER", num)
                .isEqualTo(TypeScriptTokenId.NUMBER);
        }
    }

    @Test
    void testSingleLineComment() {
        TokenSequence<TypeScriptTokenId> ts = createTokenSequence("// comment");
        assertThat(ts.moveNext()).isTrue();
        assertThat(ts.token().id()).isEqualTo(TypeScriptTokenId.COMMENT);
    }

    @Test
    void testMultiLineComment() {
        TokenSequence<TypeScriptTokenId> ts = createTokenSequence("/* comment */");
        assertThat(ts.moveNext()).isTrue();
        assertThat(ts.token().id()).isEqualTo(TypeScriptTokenId.COMMENT);
    }
}
