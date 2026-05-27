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

package org.flossware.netbeans.javascript.lexer;

import java.util.HashSet;
import java.util.Set;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

/**
 * Lexer for JavaScript syntax highlighting
 */
public class JavaScriptLexer implements Lexer<JavaScriptTokenId> {

    private static final Set<String> KEYWORDS = new HashSet<>();

    static {
        // JavaScript keywords (ES6+)
        String[] keywords = {
            "async", "await", "break", "case", "catch", "class", "const", "continue",
            "debugger", "default", "delete", "do", "else", "export", "extends",
            "false", "finally", "for", "function", "if", "import", "in",
            "instanceof", "let", "new", "null", "return", "static", "super",
            "switch", "this", "throw", "true", "try", "typeof", "var",
            "void", "while", "with", "yield"
        };
        for (String keyword : keywords) {
            KEYWORDS.add(keyword);
        }
    }

    private final LexerRestartInfo<JavaScriptTokenId> info;

    public JavaScriptLexer(LexerRestartInfo<JavaScriptTokenId> info) {
        this.info = info;
    }

    @Override
    public org.netbeans.api.lexer.Token<JavaScriptTokenId> nextToken() {
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
                        return info.tokenFactory().createToken(JavaScriptTokenId.WHITESPACE);
                    }
                }
            }

            // Comments
            if (c == '/') {
                c = info.input().read();
                if (c == '/') {
                    // Single-line comment
                    while (true) {
                        c = info.input().read();
                        if (c == org.netbeans.spi.lexer.LexerInput.EOF || c == '\n' || c == '\r') {
                            info.input().backup(1);
                            return info.tokenFactory().createToken(JavaScriptTokenId.COMMENT);
                        }
                    }
                } else if (c == '*') {
                    // Multi-line comment
                    boolean lastWasStar = false;
                    while (true) {
                        c = info.input().read();
                        if (c == org.netbeans.spi.lexer.LexerInput.EOF) {
                            return info.tokenFactory().createToken(JavaScriptTokenId.COMMENT);
                        }
                        if (lastWasStar && c == '/') {
                            return info.tokenFactory().createToken(JavaScriptTokenId.COMMENT);
                        }
                        lastWasStar = (c == '*');
                    }
                } else {
                    info.input().backup(1);
                    return info.tokenFactory().createToken(JavaScriptTokenId.OPERATOR);
                }
            }

            // Strings
            if (c == '"' || c == '\'' || c == '`') {
                int quote = c;
                boolean escaped = false;
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF) {
                        return info.tokenFactory().createToken(JavaScriptTokenId.STRING);
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
                        return info.tokenFactory().createToken(JavaScriptTokenId.STRING);
                    }
                }
            }

            // Numbers
            if (Character.isDigit(c)) {
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF ||
                        (!Character.isDigit(c) && c != '.' && c != 'e' && c != 'E' &&
                         c != 'x' && c != 'X' && c != 'b' && c != 'B' && c != 'o' && c != 'O' &&
                         c != 'n')) {
                        info.input().backup(1);
                        return info.tokenFactory().createToken(JavaScriptTokenId.NUMBER);
                    }
                }
            }

            // Identifiers and keywords
            if (Character.isJavaIdentifierStart(c) || c == '$') {
                StringBuilder sb = new StringBuilder();
                sb.append((char) c);
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF || 
                        (!Character.isJavaIdentifierPart(c) && c != '$')) {
                        info.input().backup(1);
                        String text = sb.toString();
                        if (KEYWORDS.contains(text)) {
                            return info.tokenFactory().createToken(JavaScriptTokenId.KEYWORD);
                        }
                        return info.tokenFactory().createToken(JavaScriptTokenId.IDENTIFIER);
                    }
                    sb.append((char) c);
                }
            }

            // Operators and separators
            switch (c) {
                case '+': case '-': case '*': case '%': case '=':
                case '<': case '>': case '!': case '&': case '|': case '^':
                case '~': case '?': case ':':
                    return info.tokenFactory().createToken(JavaScriptTokenId.OPERATOR);
                case '(': case ')': case '[': case ']': case '{': case '}':
                case ',': case ';': case '.':
                    return info.tokenFactory().createToken(JavaScriptTokenId.SEPARATOR);
                default:
                    return info.tokenFactory().createToken(JavaScriptTokenId.ERROR);
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
