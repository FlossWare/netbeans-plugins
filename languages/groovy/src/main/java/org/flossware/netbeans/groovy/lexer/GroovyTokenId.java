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

package org.flossware.netbeans.groovy.lexer;

import org.netbeans.api.lexer.Language;
import org.netbeans.api.lexer.TokenId;

/**
 * Token types for Groovy syntax highlighting
 */
public enum GroovyTokenId implements TokenId {

    KEYWORD("keyword"),
    IDENTIFIER("identifier"),
    STRING("string"),
    NUMBER("number"),
    OPERATOR("operator"),
    SEPARATOR("separator"),
    COMMENT("comment"),
    WHITESPACE("whitespace"),
    ERROR("error");

    private final String category;

    GroovyTokenId(String category) {
        this.category = category;
    }

    @Override
    public String primaryCategory() {
        return category;
    }

    private static final Language<GroovyTokenId> language =
            new GroovyLanguageHierarchy().language();

    public static Language<GroovyTokenId> language() {
        return language;
    }
}
