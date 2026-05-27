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

package org.flossware.netbeans.csharp.lexer;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for CSharpTokenId.
 */
class CSharpTokenIdTest {

    @Test
    void testTokenCategories() {
        assertThat(CSharpTokenId.KEYWORD.primaryCategory()).isEqualTo("keyword");
        assertThat(CSharpTokenId.IDENTIFIER.primaryCategory()).isEqualTo("identifier");
        assertThat(CSharpTokenId.STRING.primaryCategory()).isEqualTo("string");
        assertThat(CSharpTokenId.NUMBER.primaryCategory()).isEqualTo("number");
        assertThat(CSharpTokenId.OPERATOR.primaryCategory()).isEqualTo("operator");
        assertThat(CSharpTokenId.SEPARATOR.primaryCategory()).isEqualTo("separator");
        assertThat(CSharpTokenId.COMMENT.primaryCategory()).isEqualTo("comment");
        assertThat(CSharpTokenId.WHITESPACE.primaryCategory()).isEqualTo("whitespace");
        assertThat(CSharpTokenId.ERROR.primaryCategory()).isEqualTo("error");
    }

    @Test
    void testAllTokenTypesPresent() {
        CSharpTokenId[] tokens = CSharpTokenId.values();
        assertThat(tokens).hasSize(9);
    }

    @Test
    void testLanguageNotNull() {
        assertThat(CSharpTokenId.language()).isNotNull();
    }

    @Test
    void testLanguageIsSingleton() {
        assertThat(CSharpTokenId.language()).isSameAs(CSharpTokenId.language());
    }
}
