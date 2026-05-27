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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.netbeans.spi.lexer.LanguageHierarchy;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

/**
 * Language hierarchy for Go
 */
public class GoLanguageHierarchy extends LanguageHierarchy<GoTokenId> {

    private static List<GoTokenId> tokens;
    private static Map<Integer, GoTokenId> idToToken;

    private static void init() {
        tokens = Arrays.asList(GoTokenId.values());
        idToToken = new HashMap<>();
        for (GoTokenId token : tokens) {
            idToToken.put(token.ordinal(), token);
        }
    }

    static synchronized GoTokenId getToken(int id) {
        if (idToToken == null) {
            init();
        }
        return idToToken.get(id);
    }

    @Override
    protected synchronized Collection<GoTokenId> createTokenIds() {
        if (tokens == null) {
            init();
        }
        return tokens;
    }

    @Override
    protected synchronized Lexer<GoTokenId> createLexer(LexerRestartInfo<GoTokenId> info) {
        return new GoLexer(info);
    }

    @Override
    protected String mimeType() {
        return "text/x-go";
    }
}
