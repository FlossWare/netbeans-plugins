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

package org.flossware.netbeans.rust.lexer;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for RustTokenId.
 */
class RustTokenIdTest {

    @Test
    void testTokenCategories() {
        assertThat(RustTokenId.KEYWORD.primaryCategory()).isEqualTo("keyword");
        assertThat(RustTokenId.IDENTIFIER.primaryCategory()).isEqualTo("identifier");
        assertThat(RustTokenId.STRING.primaryCategory()).isEqualTo("string");
        assertThat(RustTokenId.NUMBER.primaryCategory()).isEqualTo("number");
        assertThat(RustTokenId.OPERATOR.primaryCategory()).isEqualTo("operator");
        assertThat(RustTokenId.SEPARATOR.primaryCategory()).isEqualTo("separator");
        assertThat(RustTokenId.COMMENT.primaryCategory()).isEqualTo("comment");
        assertThat(RustTokenId.WHITESPACE.primaryCategory()).isEqualTo("whitespace");
        assertThat(RustTokenId.ERROR.primaryCategory()).isEqualTo("error");
    }

    @Test
    void testAllTokenTypesPresent() {
        RustTokenId[] tokens = RustTokenId.values();
        assertThat(tokens).hasSize(9);
        assertThat(tokens).contains(
            RustTokenId.KEYWORD,
            RustTokenId.IDENTIFIER,
            RustTokenId.STRING,
            RustTokenId.NUMBER,
            RustTokenId.OPERATOR,
            RustTokenId.SEPARATOR,
            RustTokenId.COMMENT,
            RustTokenId.WHITESPACE,
            RustTokenId.ERROR
        );
    }

    @Test
    void testLanguageNotNull() {
        assertThat(RustTokenId.language()).isNotNull();
    }

    @Test
    void testLanguageIsSingleton() {
        assertThat(RustTokenId.language()).isSameAs(RustTokenId.language());
    }
}
