/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.lisp.lexer;

import org.junit.jupiter.api.Test;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;
import static org.assertj.core.api.Assertions.*;

class LispLexerTest {
    @Test
    void testKeywordRecognition() throws Exception {
        String text = "if else";
        TokenHierarchy<?> hi = TokenHierarchy.create(text, LispTokenId.language());
        TokenSequence<LispTokenId> ts = hi.tokenSequence(LispTokenId.language());
        assertThat(ts.moveNext()).isTrue();
    }

    @Test
    void testStringRecognition() throws Exception {
        String text = "\"hello\"";
        TokenHierarchy<?> hi = TokenHierarchy.create(text, LispTokenId.language());
        TokenSequence<LispTokenId> ts = hi.tokenSequence(LispTokenId.language());
        assertThat(ts.moveNext()).isTrue();
    }

    @Test
    void testCommentRecognition() throws Exception {
        String text = "# comment";
        TokenHierarchy<?> hi = TokenHierarchy.create(text, LispTokenId.language());
        TokenSequence<LispTokenId> ts = hi.tokenSequence(LispTokenId.language());
        assertThat(ts.moveNext()).isTrue();
    }

    @Test
    void testWhitespaceRecognition() throws Exception {
        String text = "  ";
        TokenHierarchy<?> hi = TokenHierarchy.create(text, LispTokenId.language());
        TokenSequence<LispTokenId> ts = hi.tokenSequence(LispTokenId.language());
        assertThat(ts.moveNext()).isTrue();
    }

    @Test
    void testEmptyInput() throws Exception {
        String text = "";
        TokenHierarchy<?> hi = TokenHierarchy.create(text, LispTokenId.language());
        TokenSequence<LispTokenId> ts = hi.tokenSequence(LispTokenId.language());
        assertThat(ts.moveNext()).isFalse();
    }
}
