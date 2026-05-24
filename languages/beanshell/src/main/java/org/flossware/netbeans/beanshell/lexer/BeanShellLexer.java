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

package org.flossware.netbeans.beanshell.lexer;

import java.util.HashSet;
import java.util.Set;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

/**
 * Lexer for BeanShell syntax highlighting
 */
public class BeanShellLexer implements Lexer<BeanShellTokenId> {

    private static final Set<String> KEYWORDS = new HashSet<>();

    static {
        // Java/BeanShell keywords
        String[] keywords = {
            "abstract", "assert", "boolean", "break", "byte", "case", "catch",
            "char", "class", "const", "continue", "default", "do", "double",
            "else", "enum", "extends", "false", "final", "finally", "float", "for",
            "goto", "if", "implements", "import", "instanceof", "int", "interface",
            "long", "native", "new", "null", "package", "private", "protected", "public",
            "return", "short", "static", "strictfp", "super", "switch", "synchronized",
            "this", "throw", "throws", "transient", "true", "try", "void",
            "volatile", "while"
        };
        for (String keyword : keywords) {
            KEYWORDS.add(keyword);
        }
    }

    private final LexerRestartInfo<BeanShellTokenId> info;

    public BeanShellLexer(LexerRestartInfo<BeanShellTokenId> info) {
        this.info = info;
    }

    @Override
    public org.netbeans.api.lexer.Token<BeanShellTokenId> nextToken() {
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
                        return info.tokenFactory().createToken(BeanShellTokenId.WHITESPACE);
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
                            return info.tokenFactory().createToken(BeanShellTokenId.COMMENT);
                        }
                    }
                } else if (c == '*') {
                    // Multi-line comment
                    while (true) {
                        c = info.input().read();
                        if (c == org.netbeans.spi.lexer.LexerInput.EOF) {
                            return info.tokenFactory().createToken(BeanShellTokenId.COMMENT);
                        }
                        if (c == '*') {
                            c = info.input().read();
                            if (c == '/') {
                                return info.tokenFactory().createToken(BeanShellTokenId.COMMENT);
                            }
                            info.input().backup(1);
                        }
                    }
                } else {
                    info.input().backup(1);
                    return info.tokenFactory().createToken(BeanShellTokenId.OPERATOR);
                }
            }

            // Strings
            if (c == '"' || c == '\'') {
                int quote = c;
                boolean escaped = false;
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF) {
                        return info.tokenFactory().createToken(BeanShellTokenId.STRING);
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
                        return info.tokenFactory().createToken(BeanShellTokenId.STRING);
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
                        return info.tokenFactory().createToken(BeanShellTokenId.NUMBER);
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
                            return info.tokenFactory().createToken(BeanShellTokenId.KEYWORD);
                        }
                        return info.tokenFactory().createToken(BeanShellTokenId.IDENTIFIER);
                    }
                    sb.append((char) c);
                }
            }

            // Operators and separators
            switch (c) {
                case '+': case '-': case '*': case '%': case '=':
                case '<': case '>': case '!': case '&': case '|': case '^':
                case '~': case '?': case ':':
                    return info.tokenFactory().createToken(BeanShellTokenId.OPERATOR);
                case '(': case ')': case '[': case ']': case '{': case '}':
                case ',': case ';': case '.': case '@':
                    return info.tokenFactory().createToken(BeanShellTokenId.SEPARATOR);
                default:
                    return info.tokenFactory().createToken(BeanShellTokenId.ERROR);
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
