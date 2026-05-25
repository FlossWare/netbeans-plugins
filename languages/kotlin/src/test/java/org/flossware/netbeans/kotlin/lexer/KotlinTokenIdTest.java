/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.kotlin.lexer;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class KotlinTokenIdTest {
    @Test
    void testAllTokenTypes_Exist() {
        assertThat(KotlinTokenId.values()).isNotEmpty();
    }

    @Test
    void testTokenCategories() {
        for (KotlinTokenId tokenId : KotlinTokenId.values()) {
            assertThat(tokenId.primaryCategory()).isNotNull();
        }
    }

    @Test
    void testTokenNames() {
        for (KotlinTokenId tokenId : KotlinTokenId.values()) {
            assertThat(tokenId.name()).isNotNull();
        }
    }

    @Test
    void testValueOf() {
        assertThat(KotlinTokenId.valueOf("KEYWORD")).isEqualTo(KotlinTokenId.KEYWORD);
    }
}
