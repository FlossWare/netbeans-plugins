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

package org.flossware.netbeans.ruby.lexer;

import java.util.Collection;
import java.util.EnumSet;
import org.netbeans.api.lexer.Language;
import org.netbeans.api.lexer.TokenId;
import org.netbeans.spi.lexer.LanguageHierarchy;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

public enum RubyTokenId implements TokenId {
    KEYWORD("keyword"),
    IDENTIFIER("identifier"),
    STRING("string"),
    NUMBER("number"),
    OPERATOR("operator"),
    SEPARATOR("separator"),
    COMMENT("comment"),
    SYMBOL("literal"),
    GLOBAL_VARIABLE("field"),
    INSTANCE_VARIABLE("field"),
    CLASS_VARIABLE("field"),
    WHITESPACE("whitespace"),
    ERROR("error");

    private final String category;

    RubyTokenId(String category) {
        this.category = category;
    }

    @Override
    public String primaryCategory() {
        return category;
    }

    private static final Language<RubyTokenId> LANGUAGE = new LanguageHierarchy<RubyTokenId>() {
        @Override
        protected Collection<RubyTokenId> createTokenIds() {
            return EnumSet.allOf(RubyTokenId.class);
        }

        @Override
        protected Lexer<RubyTokenId> createLexer(LexerRestartInfo<RubyTokenId> info) {
            return new RubyLexer(info);
        }

        @Override
        protected String mimeType() {
            return "text/x-ruby";
        }
    }.language();

    public static Language<RubyTokenId> language() {
        return LANGUAGE;
    }
}
