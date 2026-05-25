/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.lisp.lexer;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class LispTokenIdTest {
    @Test
    void testAllTokenTypes_Exist() {
        assertThat(LispTokenId.values()).isNotEmpty();
    }

    @Test
    void testTokenCategories() {
        for (LispTokenId tokenId : LispTokenId.values()) {
            assertThat(tokenId.primaryCategory()).isNotNull();
        }
    }

    @Test
    void testTokenNames() {
        for (LispTokenId tokenId : LispTokenId.values()) {
            assertThat(tokenId.name()).isNotNull();
        }
    }

    @Test
    void testValueOf() {
        assertThat(LispTokenId.valueOf("KEYWORD")).isEqualTo(LispTokenId.KEYWORD);
    }
}
