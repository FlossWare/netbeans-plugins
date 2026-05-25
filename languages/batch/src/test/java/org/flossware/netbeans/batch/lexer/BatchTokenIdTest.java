/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.batch.lexer;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class BatchTokenIdTest {
    @Test
    void testAllTokenTypes_Exist() {
        assertThat(BatchTokenId.values()).isNotEmpty();
    }

    @Test
    void testTokenCategories() {
        for (BatchTokenId tokenId : BatchTokenId.values()) {
            assertThat(tokenId.primaryCategory()).isNotNull();
        }
    }

    @Test
    void testTokenNames() {
        for (BatchTokenId tokenId : BatchTokenId.values()) {
            assertThat(tokenId.name()).isNotNull();
        }
    }

    @Test
    void testValueOf() {
        assertThat(BatchTokenId.valueOf("KEYWORD")).isEqualTo(BatchTokenId.KEYWORD);
    }
}
