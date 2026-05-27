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

package org.flossware.netbeans.go.lexer;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for GoTokenId.
 */
class GoTokenIdTest {

    @Test
    void testTokenCategories() {
        assertThat(GoTokenId.KEYWORD.primaryCategory()).isEqualTo("keyword");
        assertThat(GoTokenId.IDENTIFIER.primaryCategory()).isEqualTo("identifier");
        assertThat(GoTokenId.STRING.primaryCategory()).isEqualTo("string");
        assertThat(GoTokenId.NUMBER.primaryCategory()).isEqualTo("number");
        assertThat(GoTokenId.OPERATOR.primaryCategory()).isEqualTo("operator");
        assertThat(GoTokenId.SEPARATOR.primaryCategory()).isEqualTo("separator");
        assertThat(GoTokenId.COMMENT.primaryCategory()).isEqualTo("comment");
        assertThat(GoTokenId.WHITESPACE.primaryCategory()).isEqualTo("whitespace");
        assertThat(GoTokenId.ERROR.primaryCategory()).isEqualTo("error");
    }

    @Test
    void testAllTokenTypesPresent() {
        GoTokenId[] tokens = GoTokenId.values();
        assertThat(tokens).hasSize(9);
        assertThat(tokens).contains(
            GoTokenId.KEYWORD,
            GoTokenId.IDENTIFIER,
            GoTokenId.STRING,
            GoTokenId.NUMBER,
            GoTokenId.OPERATOR,
            GoTokenId.SEPARATOR,
            GoTokenId.COMMENT,
            GoTokenId.WHITESPACE,
            GoTokenId.ERROR
        );
    }

    @Test
    void testLanguageNotNull() {
        assertThat(GoTokenId.language()).isNotNull();
    }

    @Test
    void testLanguageIsSingleton() {
        assertThat(GoTokenId.language()).isSameAs(GoTokenId.language());
    }
}
