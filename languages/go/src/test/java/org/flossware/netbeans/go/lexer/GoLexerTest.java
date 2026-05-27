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

package org.flossware.netbeans.go.lexer;

import org.junit.jupiter.api.Test;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for GoLexer.
 */
class GoLexerTest {

    private TokenSequence<GoTokenId> createTokenSequence(String text) {
        TokenHierarchy<?> hierarchy = TokenHierarchy.create(text, GoTokenId.language());
        return hierarchy.tokenSequence(GoTokenId.language());
    }

    @Test
    void testKeywordRecognition() {
        TokenSequence<GoTokenId> ts = createTokenSequence("package main");
        assertThat(ts.moveNext()).isTrue();
        assertThat(ts.token().id()).isEqualTo(GoTokenId.KEYWORD);
        assertThat(ts.token().text().toString()).isEqualTo("package");

        assertThat(ts.moveNext()).isTrue();
        assertThat(ts.token().id()).isEqualTo(GoTokenId.WHITESPACE);

        assertThat(ts.moveNext()).isTrue();
        assertThat(ts.token().id()).isEqualTo(GoTokenId.IDENTIFIER);
        assertThat(ts.token().text().toString()).isEqualTo("main");
    }

    @Test
    void testAllGoKeywords() {
        String[] keywords = {
            "break", "case", "chan", "const", "continue", "default", "defer", "else",
            "fallthrough", "for", "func", "go", "goto", "if", "import", "interface",
            "map", "package", "range", "return", "select", "struct", "switch", "type", "var"
        };

        for (String keyword : keywords) {
            TokenSequence<GoTokenId> ts = createTokenSequence(keyword);
            assertThat(ts.moveNext()).isTrue();
            assertThat(ts.token().id())
                .withFailMessage("Keyword '%s' should be recognized as KEYWORD", keyword)
                .isEqualTo(GoTokenId.KEYWORD);
        }
    }

    @Test
    void testStringLiterals_DoubleQuotes() {
        TokenSequence<GoTokenId> ts = createTokenSequence("\"hello world\"");
        assertThat(ts.moveNext()).isTrue();
        assertThat(ts.token().id()).isEqualTo(GoTokenId.STRING);
        assertThat(ts.token().text().toString()).isEqualTo("\"hello world\"");
    }

    @Test
    void testStringLiterals_RawString() {
        TokenSequence<GoTokenId> ts = createTokenSequence("`raw string`");
        assertThat(ts.moveNext()).isTrue();
        assertThat(ts.token().id()).isEqualTo(GoTokenId.STRING);
    }

    @Test
    void testStringLiterals_Rune() {
        TokenSequence<GoTokenId> ts = createTokenSequence("'a'");
        assertThat(ts.moveNext()).isTrue();
        assertThat(ts.token().id()).isEqualTo(GoTokenId.STRING);
    }

    @Test
    void testNumbers() {
        String[] numbers = {"123", "0x1A", "0b1010", "3.14", "1e10"};

        for (String num : numbers) {
            TokenSequence<GoTokenId> ts = createTokenSequence(num);
            assertThat(ts.moveNext()).isTrue();
            assertThat(ts.token().id())
                .withFailMessage("Number '%s' should be recognized as NUMBER", num)
                .isEqualTo(GoTokenId.NUMBER);
        }
    }

    @Test
    void testSingleLineComment() {
        TokenSequence<GoTokenId> ts = createTokenSequence("// comment");
        assertThat(ts.moveNext()).isTrue();
        assertThat(ts.token().id()).isEqualTo(GoTokenId.COMMENT);
    }

    @Test
    void testMultiLineComment() {
        TokenSequence<GoTokenId> ts = createTokenSequence("/* comment */");
        assertThat(ts.moveNext()).isTrue();
        assertThat(ts.token().id()).isEqualTo(GoTokenId.COMMENT);
    }

    @Test
    void testOperators() {
        TokenSequence<GoTokenId> ts = createTokenSequence("+ - * / % = < > ! & | ^");
        while (ts.moveNext()) {
            GoTokenId id = ts.token().id();
            if (id != GoTokenId.WHITESPACE) {
                assertThat(id).isEqualTo(GoTokenId.OPERATOR);
            }
        }
    }

    @Test
    void testSeparators() {
        TokenSequence<GoTokenId> ts = createTokenSequence("( ) [ ] { } , : ; .");
        while (ts.moveNext()) {
            GoTokenId id = ts.token().id();
            if (id != GoTokenId.WHITESPACE) {
                assertThat(id).isEqualTo(GoTokenId.SEPARATOR);
            }
        }
    }

    @Test
    void testIdentifiers() {
        String[] identifiers = {"myVar", "MyFunc", "_private", "var123"};

        for (String id : identifiers) {
            TokenSequence<GoTokenId> ts = createTokenSequence(id);
            assertThat(ts.moveNext()).isTrue();
            assertThat(ts.token().id()).isEqualTo(GoTokenId.IDENTIFIER);
        }
    }

    @Test
    void testWhitespace() {
        TokenSequence<GoTokenId> ts = createTokenSequence("   \t\n");
        assertThat(ts.moveNext()).isTrue();
        assertThat(ts.token().id()).isEqualTo(GoTokenId.WHITESPACE);
    }

    @Test
    void testCompleteGoProgram() {
        String code = "package main\n\nfunc main() {\n    fmt.Println(\"Hello\")\n}";
        TokenSequence<GoTokenId> ts = createTokenSequence(code);

        assertThat(ts.moveNext()).isTrue();
        assertThat(ts.token().id()).isEqualTo(GoTokenId.KEYWORD); // package

        assertThat(ts.moveNext()).isTrue();
        assertThat(ts.token().id()).isEqualTo(GoTokenId.WHITESPACE);

        assertThat(ts.moveNext()).isTrue();
        assertThat(ts.token().id()).isEqualTo(GoTokenId.IDENTIFIER); // main
    }
}
