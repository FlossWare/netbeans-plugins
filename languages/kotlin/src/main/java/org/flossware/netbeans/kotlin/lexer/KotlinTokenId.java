/*
 * Copyright 2026 FlossWare.
 */

package org.flossware.netbeans.kotlin.lexer;

import java.util.Collection;
import java.util.EnumSet;
import org.netbeans.api.lexer.Language;
import org.netbeans.api.lexer.TokenId;
import org.netbeans.spi.lexer.LanguageHierarchy;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

public enum KotlinTokenId implements TokenId {
    KEYWORD("keyword"),
    IDENTIFIER("identifier"),
    STRING("string"),
    NUMBER("number"),
    OPERATOR("operator"),
    SEPARATOR("separator"),
    COMMENT("comment"),
    ANNOTATION("literal"),
    WHITESPACE("whitespace"),
    ERROR("error");

    private final String category;

    KotlinTokenId(String category) {
        this.category = category;
    }

    @Override
    public String primaryCategory() {
        return category;
    }

    private static final Language<KotlinTokenId> LANGUAGE = new LanguageHierarchy<KotlinTokenId>() {
        @Override
        protected Collection<KotlinTokenId> createTokenIds() {
            return EnumSet.allOf(KotlinTokenId.class);
        }

        @Override
        protected Lexer<KotlinTokenId> createLexer(LexerRestartInfo<KotlinTokenId> info) {
            return new KotlinLexer(info);
        }

        @Override
        protected String mimeType() {
            return "text/x-kotlin";
        }
    }.language();

    public static Language<KotlinTokenId> language() {
        return LANGUAGE;
    }
}
