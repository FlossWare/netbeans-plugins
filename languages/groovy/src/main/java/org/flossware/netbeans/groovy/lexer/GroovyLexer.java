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

import java.util.HashSet;
import java.util.Set;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

/**
 * Lexer for Groovy syntax highlighting
 */
public class GroovyLexer implements Lexer<GroovyTokenId> {

    private static final Set<String> KEYWORDS = new HashSet<>();

    static {
        // Groovy/Java keywords
        String[] keywords = {
            "abstract", "as", "assert", "boolean", "break", "byte", "case", "catch",
            "char", "class", "const", "continue", "def", "default", "do", "double",
            "else", "enum", "extends", "false", "final", "finally", "float", "for",
            "goto", "if", "implements", "import", "in", "instanceof", "int", "interface",
            "long", "native", "new", "null", "package", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super", "switch", "synchronized",
            "this", "throw", "throws", "trait", "transient", "true", "try", "void",
            "volatile", "while"
        };
        for (String keyword : keywords) {
            KEYWORDS.add(keyword);
        }
    }

    private final LexerRestartInfo<GroovyTokenId> info;

    public GroovyLexer(LexerRestartInfo<GroovyTokenId> info) {
        this.info = info;
    }

    @Override
    public org.netbeans.api.lexer.Token<GroovyTokenId> nextToken() {
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
                        return info.tokenFactory().createToken(GroovyTokenId.WHITESPACE);
                    }
                }
            }

            // Single-line comments
            if (c == '/') {
                c = info.input().read();
                if (c == '/') {
                    while (true) {
                        c = info.input().read();
                        if (c == LexerRestartInfo.EOI || c == '\n' || c == '\r') {
                            info.input().backup(1);
                            return info.tokenFactory().createToken(GroovyTokenId.COMMENT);
                        }
                    }
                } else if (c == '*') {
                    // Multi-line comment
                    while (true) {
                        c = info.input().read();
                        if (c == LexerRestartInfo.EOI) {
                            return info.tokenFactory().createToken(GroovyTokenId.COMMENT);
                        }
                        if (c == '*') {
                            c = info.input().read();
                            if (c == '/') {
                                return info.tokenFactory().createToken(GroovyTokenId.COMMENT);
                            }
                            info.input().backup(1);
                        }
                    }
                } else {
                    info.input().backup(1);
                    return info.tokenFactory().createToken(GroovyTokenId.OPERATOR);
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
                        return info.tokenFactory().createToken(GroovyTokenId.STRING);
                    }
                } else {
                    info.input().backup(1);
                }

                boolean escaped = false;
                while (true) {
                    c = info.input().read();
                    if (c == LexerRestartInfo.EOI) {
                        return info.tokenFactory().createToken(GroovyTokenId.STRING);
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
                                    return info.tokenFactory().createToken(GroovyTokenId.STRING);
                                }
                                info.input().backup(1);
                            } else {
                                info.input().backup(1);
                            }
                        } else {
                            return info.tokenFactory().createToken(GroovyTokenId.STRING);
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
                         c != 'x' && c != 'X' && c != 'L' && c != 'l' && c != 'f' && c != 'F' &&
                         c != 'd' && c != 'D' && c != 'G' && c != 'g')) {
                        info.input().backup(1);
                        return info.tokenFactory().createToken(GroovyTokenId.NUMBER);
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
                            return info.tokenFactory().createToken(GroovyTokenId.KEYWORD);
                        }
                        return info.tokenFactory().createToken(GroovyTokenId.IDENTIFIER);
                    }
                    sb.append((char) c);
                }
            }

            // Operators and separators
            switch (c) {
                case '+': case '-': case '*': case '%': case '=':
                case '<': case '>': case '!': case '&': case '|': case '^':
                case '~': case '?': case ':':
                    return info.tokenFactory().createToken(GroovyTokenId.OPERATOR);
                case '(': case ')': case '[': case ']': case '{': case '}':
                case ',': case ';': case '.': case '@':
                    return info.tokenFactory().createToken(GroovyTokenId.SEPARATOR);
                default:
                    return info.tokenFactory().createToken(GroovyTokenId.ERROR);
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
