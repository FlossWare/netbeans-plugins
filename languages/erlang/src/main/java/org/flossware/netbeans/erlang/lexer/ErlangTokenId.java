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

package org.flossware.netbeans.erlang.lexer;

import java.util.Collection;
import java.util.EnumSet;
import org.netbeans.api.lexer.Language;
import org.netbeans.api.lexer.TokenId;
import org.netbeans.spi.lexer.LanguageHierarchy;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

public enum ErlangTokenId implements TokenId {
    KEYWORD("keyword"),
    IDENTIFIER("identifier"),
    STRING("string"),
    NUMBER("number"),
    OPERATOR("operator"),
    SEPARATOR("separator"),
    COMMENT("comment"),
    ATOM("literal"),
    VARIABLE("variable"),
    WHITESPACE("whitespace"),
    ERROR("error");

    private final String category;

    ErlangTokenId(String category) {
        this.category = category;
    }

    @Override
    public String primaryCategory() {
        return category;
    }

    private static final Language<ErlangTokenId> LANGUAGE = new LanguageHierarchy<ErlangTokenId>() {
        @Override
        protected Collection<ErlangTokenId> createTokenIds() {
            return EnumSet.allOf(ErlangTokenId.class);
        }

        @Override
        protected Lexer<ErlangTokenId> createLexer(LexerRestartInfo<ErlangTokenId> info) {
            return new ErlangLexer(info);
        }

        @Override
        protected String mimeType() {
            return "text/x-erlang";
        }
    }.language();

    public static Language<ErlangTokenId> language() {
        return LANGUAGE;
    }
}
