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

import java.util.HashSet;
import java.util.Set;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

/**
 * Lexer for Erlang syntax highlighting
 */
public class ErlangLexer implements Lexer<ErlangTokenId> {

    private static final Set<String> KEYWORDS = new HashSet<>();

    static {
        // Erlang keywords
        String[] keywords = {
            "after", "and", "andalso", "band", "begin", "bnot", "bor", "bsl",
            "bsr", "bxor", "case", "catch", "cond", "div", "end", "fun", "if",
            "let", "not", "of", "or", "orelse", "receive", "rem", "try", "when",
            "xor", "maybe", "else"
        };
        for (String keyword : keywords) {
            KEYWORDS.add(keyword);
        }
    }

    private final LexerRestartInfo<ErlangTokenId> info;

    public ErlangLexer(LexerRestartInfo<ErlangTokenId> info) {
        this.info = info;
    }

    @Override
    public org.netbeans.api.lexer.Token<ErlangTokenId> nextToken() {
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
                        return info.tokenFactory().createToken(ErlangTokenId.WHITESPACE);
                    }
                }
            }

            // Comments
            if (c == '%') {
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF || c == '\n' || c == '\r') {
                        info.input().backup(1);
                        return info.tokenFactory().createToken(ErlangTokenId.COMMENT);
                    }
                }
            }

            // Variables (start with uppercase or underscore)
            if (Character.isUpperCase(c) || c == '_') {
                StringBuilder sb = new StringBuilder();
                sb.append((char) c);
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF ||
                        (!Character.isLetterOrDigit(c) && c != '_' && c != '@')) {
                        info.input().backup(1);
                        return info.tokenFactory().createToken(ErlangTokenId.VARIABLE);
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
                        return info.tokenFactory().createToken(ErlangTokenId.STRING);
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
                        return info.tokenFactory().createToken(ErlangTokenId.STRING);
                    }
                }
            }

            // Atoms (quoted)
            if (c == '\'') {
                boolean escaped = false;
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF) {
                        return info.tokenFactory().createToken(ErlangTokenId.ATOM);
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
                        return info.tokenFactory().createToken(ErlangTokenId.ATOM);
                    }
                }
            }

            // Numbers
            if (Character.isDigit(c)) {
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF ||
                        (!Character.isDigit(c) && c != '.' && c != 'e' && c != 'E' &&
                         c != '#' && c != '-' && c != '+')) {
                        info.input().backup(1);
                        return info.tokenFactory().createToken(ErlangTokenId.NUMBER);
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
                        (!Character.isLetterOrDigit(c) && c != '_' && c != '@')) {
                        info.input().backup(1);
                        String text = sb.toString();
                        if (KEYWORDS.contains(text)) {
                            return info.tokenFactory().createToken(ErlangTokenId.KEYWORD);
                        }
                        return info.tokenFactory().createToken(ErlangTokenId.ATOM);
                    }
                    sb.append((char) c);
                }
            }

            // Operators and separators
            switch (c) {
                case '+': case '*': case '/': case '=':
                case '<': case '>': case '!': case '&': case '|':
                case '~': case '?': case ':': case '#':
                    return info.tokenFactory().createToken(ErlangTokenId.OPERATOR);
                case '-':
                    // Could be operator or start of -module, -export, etc.
                    int next = info.input().read();
                    if (Character.isLowerCase(next)) {
                        // Module attribute
                        StringBuilder sb = new StringBuilder();
                        sb.append('-');
                        sb.append((char) next);
                        while (true) {
                            c = info.input().read();
                            if (c == org.netbeans.spi.lexer.LexerInput.EOF ||
                                (!Character.isLetterOrDigit(c) && c != '_')) {
                                info.input().backup(1);
                                return info.tokenFactory().createToken(ErlangTokenId.KEYWORD);
                            }
                            sb.append((char) c);
                        }
                    } else {
                        info.input().backup(1);
                        return info.tokenFactory().createToken(ErlangTokenId.OPERATOR);
                    }
                case '(': case ')': case '[': case ']': case '{': case '}':
                case ',': case ';': case '.': case '@':
                    return info.tokenFactory().createToken(ErlangTokenId.SEPARATOR);
                default:
                    return info.tokenFactory().createToken(ErlangTokenId.ERROR);
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
