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

package org.flossware.netbeans.rust.lexer;

import org.junit.jupiter.api.Test;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for RustLexer.
 */
class RustLexerTest {

    private TokenSequence<RustTokenId> createTokenSequence(String text) {
        TokenHierarchy<?> hierarchy = TokenHierarchy.create(text, RustTokenId.language());
        return hierarchy.tokenSequence(RustTokenId.language());
    }

    @Test
    void testKeywordRecognition() {
        TokenSequence<RustTokenId> ts = createTokenSequence("fn main");
        assertThat(ts.moveNext()).isTrue();
        assertThat(ts.token().id()).isEqualTo(RustTokenId.KEYWORD);
        assertThat(ts.token().text().toString()).isEqualTo("fn");

        assertThat(ts.moveNext()).isTrue();
        assertThat(ts.token().id()).isEqualTo(RustTokenId.WHITESPACE);

        assertThat(ts.moveNext()).isTrue();
        assertThat(ts.token().id()).isEqualTo(RustTokenId.IDENTIFIER);
    }

    @Test
    void testRustKeywords() {
        String[] keywords = {
            "fn", "let", "mut", "const", "static", "if", "else", "match", "loop",
            "while", "for", "return", "break", "continue", "pub", "mod", "use",
            "struct", "enum", "trait", "impl", "where", "type", "unsafe", "async", "await"
        };

        for (String keyword : keywords) {
            TokenSequence<RustTokenId> ts = createTokenSequence(keyword);
            assertThat(ts.moveNext()).isTrue();
            assertThat(ts.token().id())
                .withFailMessage("Keyword '%s' should be recognized as KEYWORD", keyword)
                .isEqualTo(RustTokenId.KEYWORD);
        }
    }

    @Test
    void testStringLiterals() {
        TokenSequence<RustTokenId> ts = createTokenSequence("\"hello world\"");
        assertThat(ts.moveNext()).isTrue();
        assertThat(ts.token().id()).isEqualTo(RustTokenId.STRING);
    }

    @Test
    void testNumbers() {
        String[] numbers = {"123", "0x1A", "0b1010", "3.14", "1e10"};

        for (String num : numbers) {
            TokenSequence<RustTokenId> ts = createTokenSequence(num);
            assertThat(ts.moveNext()).isTrue();
            assertThat(ts.token().id())
                .withFailMessage("Number '%s' should be recognized as NUMBER", num)
                .isEqualTo(RustTokenId.NUMBER);
        }
    }

    @Test
    void testSingleLineComment() {
        TokenSequence<RustTokenId> ts = createTokenSequence("// comment");
        assertThat(ts.moveNext()).isTrue();
        assertThat(ts.token().id()).isEqualTo(RustTokenId.COMMENT);
    }

    @Test
    void testMultiLineComment() {
        TokenSequence<RustTokenId> ts = createTokenSequence("/* comment */");
        assertThat(ts.moveNext()).isTrue();
        assertThat(ts.token().id()).isEqualTo(RustTokenId.COMMENT);
    }

    @Test
    void testOperators() {
        TokenSequence<RustTokenId> ts = createTokenSequence("+ - * / % = < > ! & | ^");
        while (ts.moveNext()) {
            RustTokenId id = ts.token().id();
            if (id != RustTokenId.WHITESPACE) {
                assertThat(id).isEqualTo(RustTokenId.OPERATOR);
            }
        }
    }

    @Test
    void testSeparators() {
        TokenSequence<RustTokenId> ts = createTokenSequence("( ) [ ] { } , : ; .");
        while (ts.moveNext()) {
            RustTokenId id = ts.token().id();
            if (id != RustTokenId.WHITESPACE) {
                assertThat(id).isEqualTo(RustTokenId.SEPARATOR);
            }
        }
    }
}
