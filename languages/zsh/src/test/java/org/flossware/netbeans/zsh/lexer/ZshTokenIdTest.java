/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.zsh.lexer;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class ZshTokenIdTest {
    @Test
    void testAllTokenTypes_Exist() {
        assertThat(ZshTokenId.values()).isNotEmpty();
    }

    @Test
    void testTokenCategories() {
        for (ZshTokenId tokenId : ZshTokenId.values()) {
            assertThat(tokenId.primaryCategory()).isNotNull();
        }
    }

    @Test
    void testTokenNames() {
        for (ZshTokenId tokenId : ZshTokenId.values()) {
            assertThat(tokenId.name()).isNotNull();
        }
    }

    @Test
    void testValueOf() {
        assertThat(ZshTokenId.valueOf("KEYWORD")).isEqualTo(ZshTokenId.KEYWORD);
    }
}
