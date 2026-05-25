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

package org.flossware.netbeans.bash.lexer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.netbeans.api.lexer.Token;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.spi.lexer.LexerRestartInfo;

import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for BashLexer.
 * Note: These tests verify lexer behavior using NetBeans Lexer API.
 */
@ExtendWith(MockitoExtension.class)
class BashLexerTest {

    @Mock
    private LexerRestartInfo<BashTokenId> mockInfo;

    @BeforeEach
    void setUp() {
        // Lexer tests use real TokenHierarchy for integration
    }

    @Test
    void testKeywordRecognition() throws Exception {
        String text = "if then else fi";
        TokenHierarchy<?> hi = TokenHierarchy.create(text, BashTokenId.language());
        TokenSequence<BashTokenId> ts = hi.tokenSequence(BashTokenId.language());

        assertThat(ts.moveNext()).isTrue();
        assertThat(ts.token().id()).isEqualTo(BashTokenId.KEYWORD);
        assertThat(ts.token().text().toString()).isEqualTo("if");

        ts.moveNext();
        ts.moveNext(); // skip whitespace

        assertThat(ts.token().id()).isEqualTo(BashTokenId.KEYWORD);
        assertThat(ts.token().text().toString()).isEqualTo("then");
    }

    @Test
    void testVariableRecognition() throws Exception {
        String text = "$VAR ${HOME}";
        TokenHierarchy<?> hi = TokenHierarchy.create(text, BashTokenId.language());
        TokenSequence<BashTokenId> ts = hi.tokenSequence(BashTokenId.language());

        assertThat(ts.moveNext()).isTrue();
        assertThat(ts.token().id()).isEqualTo(BashTokenId.VARIABLE);

        ts.moveNext(); // skip whitespace
        ts.moveNext();

        assertThat(ts.token().id()).isEqualTo(BashTokenId.VARIABLE);
    }

    @Test
    void testStringRecognition() throws Exception {
        String text = "\"hello world\" 'single'";
        TokenHierarchy<?> hi = TokenHierarchy.create(text, BashTokenId.language());
        TokenSequence<BashTokenId> ts = hi.tokenSequence(BashTokenId.language());

        assertThat(ts.moveNext()).isTrue();
        assertThat(ts.token().id()).isEqualTo(BashTokenId.STRING);

        ts.moveNext(); // skip whitespace
        ts.moveNext();

        assertThat(ts.token().id()).isEqualTo(BashTokenId.STRING);
    }

    @Test
    void testCommentRecognition() throws Exception {
        String text = "# this is a comment\necho test";
        TokenHierarchy<?> hi = TokenHierarchy.create(text, BashTokenId.language());
        TokenSequence<BashTokenId> ts = hi.tokenSequence(BashTokenId.language());

        assertThat(ts.moveNext()).isTrue();
        assertThat(ts.token().id()).isEqualTo(BashTokenId.COMMENT);
    }

    @Test
    void testNumberRecognition() throws Exception {
        String text = "123 456";
        TokenHierarchy<?> hi = TokenHierarchy.create(text, BashTokenId.language());
        TokenSequence<BashTokenId> ts = hi.tokenSequence(BashTokenId.language());

        assertThat(ts.moveNext()).isTrue();
        assertThat(ts.token().id()).isEqualTo(BashTokenId.NUMBER);
    }

    @Test
    void testIdentifierRecognition() throws Exception {
        String text = "my_function another_id";
        TokenHierarchy<?> hi = TokenHierarchy.create(text, BashTokenId.language());
        TokenSequence<BashTokenId> ts = hi.tokenSequence(BashTokenId.language());

        assertThat(ts.moveNext()).isTrue();
        assertThat(ts.token().id()).isEqualTo(BashTokenId.IDENTIFIER);

        ts.moveNext(); // skip whitespace
        ts.moveNext();

        assertThat(ts.token().id()).isEqualTo(BashTokenId.IDENTIFIER);
    }

    @Test
    void testOperatorRecognition() throws Exception {
        String text = "+ - * / = ==";
        TokenHierarchy<?> hi = TokenHierarchy.create(text, BashTokenId.language());
        TokenSequence<BashTokenId> ts = hi.tokenSequence(BashTokenId.language());

        while (ts.moveNext()) {
            if (ts.token().id() != BashTokenId.WHITESPACE) {
                assertThat(ts.token().id()).isEqualTo(BashTokenId.OPERATOR);
            }
        }
    }

    @Test
    void testWhitespaceRecognition() throws Exception {
        String text = "  \t\n  ";
        TokenHierarchy<?> hi = TokenHierarchy.create(text, BashTokenId.language());
        TokenSequence<BashTokenId> ts = hi.tokenSequence(BashTokenId.language());

        assertThat(ts.moveNext()).isTrue();
        assertThat(ts.token().id()).isEqualTo(BashTokenId.WHITESPACE);
    }

    @Test
    void testEmptyInput() throws Exception {
        String text = "";
        TokenHierarchy<?> hi = TokenHierarchy.create(text, BashTokenId.language());
        TokenSequence<BashTokenId> ts = hi.tokenSequence(BashTokenId.language());

        assertThat(ts.moveNext()).isFalse();
    }
}
