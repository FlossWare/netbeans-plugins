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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.netbeans.spi.lexer.LanguageHierarchy;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

/**
 * Language hierarchy for TypeScript
 */
public class TypeScriptLanguageHierarchy extends LanguageHierarchy<TypeScriptTokenId> {

    private static List<TypeScriptTokenId> tokens;
    private static Map<Integer, TypeScriptTokenId> idToToken;

    private static void init() {
        tokens = Arrays.asList(TypeScriptTokenId.values());
        idToToken = new HashMap<>();
        for (TypeScriptTokenId token : tokens) {
            idToToken.put(token.ordinal(), token);
        }
    }

    static synchronized TypeScriptTokenId getToken(int id) {
        if (idToToken == null) {
            init();
        }
        return idToToken.get(id);
    }

    @Override
    protected synchronized Collection<TypeScriptTokenId> createTokenIds() {
        if (tokens == null) {
            init();
        }
        return tokens;
    }

    @Override
    protected synchronized Lexer<TypeScriptTokenId> createLexer(LexerRestartInfo<TypeScriptTokenId> info) {
        return new TypeScriptLexer(info);
    }

    @Override
    protected String mimeType() {
        return "text/x-typescript";
    }
}
