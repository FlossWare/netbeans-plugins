/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.erlang.lexer;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ErlangTokenIdTest {
    @Test
    void testAllTokenTypes_Exist() {
        assertThat(ErlangTokenId.values()).isNotEmpty();
    }

    @Test
    void testTokenCategories() {
        for (ErlangTokenId tokenId : ErlangTokenId.values()) {
            assertThat(tokenId.primaryCategory()).isNotNull();
        }
    }

    @Test
    void testTokenNames() {
        for (ErlangTokenId tokenId : ErlangTokenId.values()) {
            assertThat(tokenId.name()).isNotNull();
        }
    }

    @Test
    void testValueOf() {
        assertThat(ErlangTokenId.valueOf("KEYWORD")).isEqualTo(ErlangTokenId.KEYWORD);
    }
}
