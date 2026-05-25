/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.prolog.lexer;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class PrologTokenIdTest {
    @Test
    void testAllTokenTypes_Exist() {
        assertThat(PrologTokenId.values()).isNotEmpty();
    }

    @Test
    void testTokenCategories() {
        for (PrologTokenId tokenId : PrologTokenId.values()) {
            assertThat(tokenId.primaryCategory()).isNotNull();
        }
    }

    @Test
    void testTokenNames() {
        for (PrologTokenId tokenId : PrologTokenId.values()) {
            assertThat(tokenId.name()).isNotNull();
        }
    }

    @Test
    void testValueOf() {
        assertThat(PrologTokenId.valueOf("KEYWORD")).isEqualTo(PrologTokenId.KEYWORD);
    }
}
