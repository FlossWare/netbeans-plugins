/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.ruby.lexer;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class RubyTokenIdTest {
    @Test
    void testAllTokenTypes_Exist() {
        assertThat(RubyTokenId.values()).isNotEmpty();
    }

    @Test
    void testTokenCategories() {
        for (RubyTokenId tokenId : RubyTokenId.values()) {
            assertThat(tokenId.primaryCategory()).isNotNull();
        }
    }

    @Test
    void testTokenNames() {
        for (RubyTokenId tokenId : RubyTokenId.values()) {
            assertThat(tokenId.name()).isNotNull();
        }
    }

    @Test
    void testValueOf() {
        assertThat(RubyTokenId.valueOf("KEYWORD")).isEqualTo(RubyTokenId.KEYWORD);
    }
}
