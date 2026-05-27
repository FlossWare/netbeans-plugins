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

package org.flossware.netbeans.javascript.lexer;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for JavaScriptTokenId.
 */
class JavaScriptTokenIdTest {

    @Test
    void testTokenCategories() {
        assertThat(JavaScriptTokenId.KEYWORD.primaryCategory()).isEqualTo("keyword");
        assertThat(JavaScriptTokenId.IDENTIFIER.primaryCategory()).isEqualTo("identifier");
        assertThat(JavaScriptTokenId.STRING.primaryCategory()).isEqualTo("string");
        assertThat(JavaScriptTokenId.NUMBER.primaryCategory()).isEqualTo("number");
        assertThat(JavaScriptTokenId.OPERATOR.primaryCategory()).isEqualTo("operator");
        assertThat(JavaScriptTokenId.SEPARATOR.primaryCategory()).isEqualTo("separator");
        assertThat(JavaScriptTokenId.COMMENT.primaryCategory()).isEqualTo("comment");
        assertThat(JavaScriptTokenId.WHITESPACE.primaryCategory()).isEqualTo("whitespace");
        assertThat(JavaScriptTokenId.ERROR.primaryCategory()).isEqualTo("error");
    }

    @Test
    void testAllTokenTypesPresent() {
        JavaScriptTokenId[] tokens = JavaScriptTokenId.values();
        assertThat(tokens).hasSize(9);
    }

    @Test
    void testLanguageNotNull() {
        assertThat(JavaScriptTokenId.language()).isNotNull();
    }

    @Test
    void testLanguageIsSingleton() {
        assertThat(JavaScriptTokenId.language()).isSameAs(JavaScriptTokenId.language());
    }
}
