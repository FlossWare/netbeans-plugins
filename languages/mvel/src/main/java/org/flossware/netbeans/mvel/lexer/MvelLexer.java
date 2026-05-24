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

package org.flossware.netbeans.mvel.lexer;

import java.util.HashSet;
import java.util.Set;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

/**
 * Lexer for MVEL syntax highlighting
 */
public class MvelLexer implements Lexer<MvelTokenId> {

    private static final Set<String> KEYWORDS = new HashSet<>();

    static {
        // MVEL keywords
        String[] keywords = {
            "if", "else", "foreach", "while", "do", "for", "in", "def", "import",
            "return", "new", "instanceof", "isdef", "contains", "soundslike",
            "strsim", "is", "this", "assert", "true", "false", "null", "nil",
            "empty", "function", "var", "with", "assert", "switch", "case",
            "default", "break", "continue", "throw", "try", "catch", "finally"
        };
        for (String keyword : keywords) {
            KEYWORDS.add(keyword);
        }
    }

    private final LexerRestartInfo<MvelTokenId> info;

    public MvelLexer(LexerRestartInfo<MvelTokenId> info) {
        this.info = info;
    }

    @Override
    public org.netbeans.api.lexer.Token<MvelTokenId> nextToken() {
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
                        return info.tokenFactory().createToken(MvelTokenId.WHITESPACE);
                    }
                }
            }

            // Single-line comments
            if (c == '/') {
                c = info.input().read();
                if (c == '/') {
                    while (true) {
                        c = info.input().read();
                        if (c == org.netbeans.spi.lexer.LexerInput.EOF || c == '\n' || c == '\r') {
                            info.input().backup(1);
                            return info.tokenFactory().createToken(MvelTokenId.COMMENT);
                        }
                    }
                } else if (c == '*') {
                    // Multi-line comment
                    while (true) {
                        c = info.input().read();
                        if (c == org.netbeans.spi.lexer.LexerInput.EOF) {
                            return info.tokenFactory().createToken(MvelTokenId.COMMENT);
                        }
                        if (c == '*') {
                            c = info.input().read();
                            if (c == '/') {
                                return info.tokenFactory().createToken(MvelTokenId.COMMENT);
                            }
                            info.input().backup(1);
                        }
                    }
                } else {
                    info.input().backup(1);
                    return info.tokenFactory().createToken(MvelTokenId.OPERATOR);
                }
            }

            // Strings
            if (c == '"' || c == '\'') {
                int quote = c;
                boolean escaped = false;
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF) {
                        return info.tokenFactory().createToken(MvelTokenId.STRING);
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
                        return info.tokenFactory().createToken(MvelTokenId.STRING);
                    }
                }
            }

            // Numbers
            if (Character.isDigit(c)) {
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF ||
                        (!Character.isDigit(c) && c != '.' && c != 'e' && c != 'E' &&
                         c != 'x' && c != 'X' && c != 'L' && c != 'l' && c != 'f' && c != 'F' &&
                         c != 'd' && c != 'D')) {
                        info.input().backup(1);
                        return info.tokenFactory().createToken(MvelTokenId.NUMBER);
                    }
                }
            }

            // Identifiers and keywords
            if (Character.isJavaIdentifierStart(c)) {
                StringBuilder sb = new StringBuilder();
                sb.append((char) c);
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF || !Character.isJavaIdentifierPart(c)) {
                        info.input().backup(1);
                        String text = sb.toString();
                        if (KEYWORDS.contains(text)) {
                            return info.tokenFactory().createToken(MvelTokenId.KEYWORD);
                        }
                        return info.tokenFactory().createToken(MvelTokenId.IDENTIFIER);
                    }
                    sb.append((char) c);
                }
            }

            // Operators and separators
            switch (c) {
                case '+': case '-': case '*': case '%': case '=':
                case '<': case '>': case '!': case '&': case '|': case '^':
                case '~': case '?': case ':':
                    return info.tokenFactory().createToken(MvelTokenId.OPERATOR);
                case '(': case ')': case '[': case ']': case '{': case '}':
                case ',': case ';': case '.': case '@': case '$':
                    return info.tokenFactory().createToken(MvelTokenId.SEPARATOR);
                default:
                    return info.tokenFactory().createToken(MvelTokenId.ERROR);
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
