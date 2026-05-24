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

package org.flossware.netbeans.python.lexer;

import java.util.HashSet;
import java.util.Set;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

/**
 * Lexer for Python syntax highlighting
 */
public class PythonLexer implements Lexer<PythonTokenId> {

    private static final Set<String> KEYWORDS = new HashSet<>();

    static {
        // Python keywords
        String[] keywords = {
            "and", "as", "assert", "async", "await", "break", "class", "continue",
            "def", "del", "elif", "else", "except", "False", "finally", "for",
            "from", "global", "if", "import", "in", "is", "lambda", "None",
            "nonlocal", "not", "or", "pass", "raise", "return", "True", "try",
            "while", "with", "yield"
        };
        for (String keyword : keywords) {
            KEYWORDS.add(keyword);
        }
    }

    private final LexerRestartInfo<PythonTokenId> info;

    public PythonLexer(LexerRestartInfo<PythonTokenId> info) {
        this.info = info;
    }

    @Override
    public org.netbeans.api.lexer.Token<PythonTokenId> nextToken() {
        while (true) {
            int c = info.input().read();

            if (c == LexerRestartInfo.EOI) {
                return null;
            }

            // Whitespace
            if (Character.isWhitespace(c)) {
                while (true) {
                    c = info.input().read();
                    if (c == LexerRestartInfo.EOI || !Character.isWhitespace(c)) {
                        info.input().backup(1);
                        return info.tokenFactory().createToken(PythonTokenId.WHITESPACE);
                    }
                }
            }

            // Comments
            if (c == '#') {
                while (true) {
                    c = info.input().read();
                    if (c == LexerRestartInfo.EOI || c == '\n' || c == '\r') {
                        info.input().backup(1);
                        return info.tokenFactory().createToken(PythonTokenId.COMMENT);
                    }
                }
            }

            // Strings
            if (c == '"' || c == '\'') {
                int quote = c;
                boolean tripleQuote = false;

                // Check for triple quotes
                c = info.input().read();
                if (c == quote) {
                    c = info.input().read();
                    if (c == quote) {
                        tripleQuote = true;
                    } else {
                        info.input().backup(2);
                        return info.tokenFactory().createToken(PythonTokenId.STRING);
                    }
                } else {
                    info.input().backup(1);
                }

                boolean escaped = false;
                while (true) {
                    c = info.input().read();
                    if (c == LexerRestartInfo.EOI) {
                        return info.tokenFactory().createToken(PythonTokenId.STRING);
                    }
                    if (escaped) {
                        escaped = false;
                        continue;
                    }
                    if (c == '\\') {
                        escaped = true;
                        continue;
                    }
                    if (c == quote) {
                        if (tripleQuote) {
                            c = info.input().read();
                            if (c == quote) {
                                c = info.input().read();
                                if (c == quote) {
                                    return info.tokenFactory().createToken(PythonTokenId.STRING);
                                }
                                info.input().backup(1);
                            } else {
                                info.input().backup(1);
                            }
                        } else {
                            return info.tokenFactory().createToken(PythonTokenId.STRING);
                        }
                    }
                }
            }

            // Numbers
            if (Character.isDigit(c)) {
                while (true) {
                    c = info.input().read();
                    if (c == LexerRestartInfo.EOI ||
                        (!Character.isDigit(c) && c != '.' && c != 'e' && c != 'E' &&
                         c != 'x' && c != 'X' && c != 'b' && c != 'B' && c != 'o' && c != 'O')) {
                        info.input().backup(1);
                        return info.tokenFactory().createToken(PythonTokenId.NUMBER);
                    }
                }
            }

            // Identifiers and keywords
            if (Character.isJavaIdentifierStart(c)) {
                StringBuilder sb = new StringBuilder();
                sb.append((char) c);
                while (true) {
                    c = info.input().read();
                    if (c == LexerRestartInfo.EOI || !Character.isJavaIdentifierPart(c)) {
                        info.input().backup(1);
                        String text = sb.toString();
                        if (KEYWORDS.contains(text)) {
                            return info.tokenFactory().createToken(PythonTokenId.KEYWORD);
                        }
                        return info.tokenFactory().createToken(PythonTokenId.IDENTIFIER);
                    }
                    sb.append((char) c);
                }
            }

            // Operators and separators
            switch (c) {
                case '+': case '-': case '*': case '/': case '%': case '=':
                case '<': case '>': case '!': case '&': case '|': case '^':
                case '~': case '@':
                    return info.tokenFactory().createToken(PythonTokenId.OPERATOR);
                case '(': case ')': case '[': case ']': case '{': case '}':
                case ',': case ':': case ';': case '.':
                    return info.tokenFactory().createToken(PythonTokenId.SEPARATOR);
                default:
                    return info.tokenFactory().createToken(PythonTokenId.ERROR);
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
