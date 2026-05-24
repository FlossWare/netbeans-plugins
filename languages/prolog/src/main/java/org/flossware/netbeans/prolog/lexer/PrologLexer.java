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

package org.flossware.netbeans.prolog.lexer;

import java.util.HashSet;
import java.util.Set;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

/**
 * Lexer for Prolog syntax highlighting
 */
public class PrologLexer implements Lexer<PrologTokenId> {

    private static final Set<String> KEYWORDS = new HashSet<>();

    static {
        // Prolog keywords and directives
        String[] keywords = {
            "is", "mod", "not", "true", "false", "fail", "cut", "if", "then", "else",
            "dynamic", "multifile", "discontiguous", "module", "use_module",
            "abolish", "assert", "asserta", "assertz", "retract", "retractall",
            "findall", "bagof", "setof", "forall", "once", "ignore",
            "catch", "throw", "halt", "trace", "notrace", "spy", "nospy",
            "listing", "consult", "reconsult", "include"
        };
        for (String keyword : keywords) {
            KEYWORDS.add(keyword);
        }
    }

    private final LexerRestartInfo<PrologTokenId> info;

    public PrologLexer(LexerRestartInfo<PrologTokenId> info) {
        this.info = info;
    }

    @Override
    public org.netbeans.api.lexer.Token<PrologTokenId> nextToken() {
        while (true) {
            int c = info.input().read();

            if (c == org.netbeans.spi.lexer.LexerInput.EOF) {
                return null;
            }

            // Whitespace
            if (Character.isWhitespace(c)) {
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF || !Character.isWhitespace(c)) {
                        info.input().backup(1);
                        return info.tokenFactory().createToken(PrologTokenId.WHITESPACE);
                    }
                }
            }

            // Comments - line comment %
            if (c == '%') {
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF || c == '\n' || c == '\r') {
                        info.input().backup(1);
                        return info.tokenFactory().createToken(PrologTokenId.COMMENT);
                    }
                }
            }

            // Comments - block comment /* */
            if (c == '/') {
                int next = info.input().read();
                if (next == '*') {
                    while (true) {
                        c = info.input().read();
                        if (c == org.netbeans.spi.lexer.LexerInput.EOF) {
                            return info.tokenFactory().createToken(PrologTokenId.COMMENT);
                        }
                        if (c == '*') {
                            c = info.input().read();
                            if (c == '/') {
                                return info.tokenFactory().createToken(PrologTokenId.COMMENT);
                            }
                            info.input().backup(1);
                        }
                    }
                } else {
                    info.input().backup(1);
                    return info.tokenFactory().createToken(PrologTokenId.OPERATOR);
                }
            }

            // Variables (start with uppercase or underscore)
            if (Character.isUpperCase(c) || c == '_') {
                StringBuilder sb = new StringBuilder();
                sb.append((char) c);
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF ||
                        (!Character.isLetterOrDigit(c) && c != '_')) {
                        info.input().backup(1);
                        return info.tokenFactory().createToken(PrologTokenId.VARIABLE);
                    }
                    sb.append((char) c);
                }
            }

            // Strings - double quotes
            if (c == '"') {
                boolean escaped = false;
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF) {
                        return info.tokenFactory().createToken(PrologTokenId.STRING);
                    }
                    if (escaped) {
                        escaped = false;
                        continue;
                    }
                    if (c == '\\') {
                        escaped = true;
                        continue;
                    }
                    if (c == '"') {
                        return info.tokenFactory().createToken(PrologTokenId.STRING);
                    }
                }
            }

            // Atoms (quoted)
            if (c == '\'') {
                boolean escaped = false;
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF) {
                        return info.tokenFactory().createToken(PrologTokenId.ATOM);
                    }
                    if (escaped) {
                        escaped = false;
                        continue;
                    }
                    if (c == '\\') {
                        escaped = true;
                        continue;
                    }
                    if (c == '\'') {
                        return info.tokenFactory().createToken(PrologTokenId.ATOM);
                    }
                }
            }

            // Numbers
            if (Character.isDigit(c)) {
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF ||
                        (!Character.isDigit(c) && c != '.' && c != 'e' && c != 'E' && c != '-' && c != '+')) {
                        info.input().backup(1);
                        return info.tokenFactory().createToken(PrologTokenId.NUMBER);
                    }
                }
            }

            // Atoms and keywords (start with lowercase)
            if (Character.isLowerCase(c)) {
                StringBuilder sb = new StringBuilder();
                sb.append((char) c);
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF ||
                        (!Character.isLetterOrDigit(c) && c != '_')) {
                        info.input().backup(1);
                        String text = sb.toString();
                        if (KEYWORDS.contains(text)) {
                            return info.tokenFactory().createToken(PrologTokenId.KEYWORD);
                        }
                        return info.tokenFactory().createToken(PrologTokenId.ATOM);
                    }
                    sb.append((char) c);
                }
            }

            // Operators and separators
            switch (c) {
                case '+': case '*': case '=':
                case '<': case '>': case '!': case '&': case '|':
                case '~': case '?': case ':': case '#': case '@': case '\\':
                    return info.tokenFactory().createToken(PrologTokenId.OPERATOR);
                case '-':
                    // Could be directive :- or operator
                    int next = info.input().read();
                    if (next == '>') {
                        return info.tokenFactory().createToken(PrologTokenId.OPERATOR);
                    } else {
                        info.input().backup(1);
                        return info.tokenFactory().createToken(PrologTokenId.OPERATOR);
                    }
                case '(': case ')': case '[': case ']': case '{': case '}':
                case ',': case ';': case '.':
                    return info.tokenFactory().createToken(PrologTokenId.SEPARATOR);
                default:
                    return info.tokenFactory().createToken(PrologTokenId.ERROR);
            }
        }
    }

    @Override
    public Object state() {
        return null;
    }

    @Override
    public void release() {
    }
}
