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

package org.flossware.netbeans.typescript.lexer;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for TypeScriptTokenId.
 */
class TypeScriptTokenIdTest {

    @Test
    void testTokenCategories() {
        assertThat(TypeScriptTokenId.KEYWORD.primaryCategory()).isEqualTo("keyword");
        assertThat(TypeScriptTokenId.IDENTIFIER.primaryCategory()).isEqualTo("identifier");
        assertThat(TypeScriptTokenId.STRING.primaryCategory()).isEqualTo("string");
        assertThat(TypeScriptTokenId.NUMBER.primaryCategory()).isEqualTo("number");
        assertThat(TypeScriptTokenId.OPERATOR.primaryCategory()).isEqualTo("operator");
        assertThat(TypeScriptTokenId.SEPARATOR.primaryCategory()).isEqualTo("separator");
        assertThat(TypeScriptTokenId.COMMENT.primaryCategory()).isEqualTo("comment");
        assertThat(TypeScriptTokenId.WHITESPACE.primaryCategory()).isEqualTo("whitespace");
        assertThat(TypeScriptTokenId.ERROR.primaryCategory()).isEqualTo("error");
    }

    @Test
    void testAllTokenTypesPresent() {
        TypeScriptTokenId[] tokens = TypeScriptTokenId.values();
        assertThat(tokens).hasSize(9);
    }

    @Test
    void testLanguageNotNull() {
        assertThat(TypeScriptTokenId.language()).isNotNull();
    }

    @Test
    void testLanguageIsSingleton() {
        assertThat(TypeScriptTokenId.language()).isSameAs(TypeScriptTokenId.language());
    }
}
