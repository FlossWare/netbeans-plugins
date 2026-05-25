/* Copyright 2026 FlossWare. */
package org.flossware.netbeans.powershell.lexer;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class PowerShellTokenIdTest {
    @Test
    void testAllTokenTypes_Exist() {
        assertThat(PowerShellTokenId.values()).isNotEmpty();
    }

    @Test
    void testTokenCategories() {
        for (PowerShellTokenId tokenId : PowerShellTokenId.values()) {
            assertThat(tokenId.primaryCategory()).isNotNull();
        }
    }

    @Test
    void testTokenNames() {
        for (PowerShellTokenId tokenId : PowerShellTokenId.values()) {
            assertThat(tokenId.name()).isNotNull();
        }
    }

    @Test
    void testValueOf() {
        assertThat(PowerShellTokenId.valueOf("KEYWORD")).isEqualTo(PowerShellTokenId.KEYWORD);
    }
}
