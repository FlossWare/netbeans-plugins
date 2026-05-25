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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for BashTokenId enum.
 */
class BashTokenIdTest {

    @Test
    void testAllTokenTypes_Exist() {
        assertThat(BashTokenId.values()).isNotEmpty();
        assertThat(BashTokenId.values()).containsExactlyInAnyOrder(
            BashTokenId.KEYWORD,
            BashTokenId.IDENTIFIER,
            BashTokenId.STRING,
            BashTokenId.NUMBER,
            BashTokenId.OPERATOR,
            BashTokenId.SEPARATOR,
            BashTokenId.COMMENT,
            BashTokenId.WHITESPACE,
            BashTokenId.VARIABLE,
            BashTokenId.ERROR
        );
    }

    @Test
    void testTokenCategories() {
        assertThat(BashTokenId.KEYWORD.primaryCategory()).isEqualTo("keyword");
        assertThat(BashTokenId.IDENTIFIER.primaryCategory()).isEqualTo("identifier");
        assertThat(BashTokenId.STRING.primaryCategory()).isEqualTo("string");
        assertThat(BashTokenId.NUMBER.primaryCategory()).isEqualTo("number");
        assertThat(BashTokenId.OPERATOR.primaryCategory()).isEqualTo("operator");
        assertThat(BashTokenId.SEPARATOR.primaryCategory()).isEqualTo("separator");
        assertThat(BashTokenId.COMMENT.primaryCategory()).isEqualTo("comment");
        assertThat(BashTokenId.WHITESPACE.primaryCategory()).isEqualTo("whitespace");
        assertThat(BashTokenId.VARIABLE.primaryCategory()).isEqualTo("variable");
        assertThat(BashTokenId.ERROR.primaryCategory()).isEqualTo("error");
    }

    @Test
    void testTokenNames() {
        assertThat(BashTokenId.KEYWORD.name()).isEqualTo("KEYWORD");
        assertThat(BashTokenId.VARIABLE.name()).isEqualTo("VARIABLE");
        assertThat(BashTokenId.ERROR.name()).isEqualTo("ERROR");
    }

    @Test
    void testValueOf() {
        assertThat(BashTokenId.valueOf("KEYWORD")).isEqualTo(BashTokenId.KEYWORD);
        assertThat(BashTokenId.valueOf("VARIABLE")).isEqualTo(BashTokenId.VARIABLE);
    }
}
