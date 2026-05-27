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

import java.util.HashSet;
import java.util.Set;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

/**
 * Lexer for TypeScript syntax highlighting
 */
public class TypeScriptLexer implements Lexer<TypeScriptTokenId> {

    private static final Set<String> KEYWORDS = new HashSet<>();

    static {
        // TypeScript keywords (includes JavaScript keywords + TypeScript-specific)
        String[] keywords = {
            "abstract", "any", "as", "async", "await", "boolean", "break", "case",
            "catch", "class", "const", "continue", "debugger", "declare", "default",
            "delete", "do", "else", "enum", "export", "extends", "false", "finally",
            "for", "from", "function", "get", "if", "implements", "import", "in",
            "instanceof", "interface", "is", "keyof", "let", "module", "namespace",
            "never", "new", "null", "number", "of", "package", "private", "protected",
            "public", "readonly", "require", "return", "set", "static", "string",
            "super", "switch", "symbol", "this", "throw", "true", "try", "type",
            "typeof", "undefined", "unique", "unknown", "var", "void", "while",
            "with", "yield"
        };
        for (String keyword : keywords) {
            KEYWORDS.add(keyword);
        }
    }

    private final LexerRestartInfo<TypeScriptTokenId> info;

    public TypeScriptLexer(LexerRestartInfo<TypeScriptTokenId> info) {
        this.info = info;
    }

    @Override
    public org.netbeans.api.lexer.Token<TypeScriptTokenId> nextToken() {
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
                        return info.tokenFactory().createToken(TypeScriptTokenId.WHITESPACE);
                    }
                }
            }

            // Single-line comments
            if (c == '/') {
                int next = info.input().read();
                if (next == '/') {
                    while (true) {
                        c = info.input().read();
                        if (c == org.netbeans.spi.lexer.LexerInput.EOF || c == '\n' || c == '\r') {
                            info.input().backup(1);
                            return info.tokenFactory().createToken(TypeScriptTokenId.COMMENT);
                        }
                    }
                } else if (next == '*') {
                    // Multi-line comments
                    boolean foundStar = false;
                    while (true) {
                        c = info.input().read();
                        if (c == org.netbeans.spi.lexer.LexerInput.EOF) {
                            return info.tokenFactory().createToken(TypeScriptTokenId.COMMENT);
                        }
                        if (foundStar && c == '/') {
                            return info.tokenFactory().createToken(TypeScriptTokenId.COMMENT);
                        }
                        foundStar = (c == '*');
                    }
                } else {
                    info.input().backup(1);
                    return info.tokenFactory().createToken(TypeScriptTokenId.OPERATOR);
                }
            }

            // Strings (single, double, and template literals)
            if (c == '"' || c == '\'' || c == '`') {
                int quote = c;
                boolean escaped = false;
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF) {
                        return info.tokenFactory().createToken(TypeScriptTokenId.STRING);
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
                        return info.tokenFactory().createToken(TypeScriptTokenId.STRING);
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
                         c != '_' && c != 'n')) {
                        info.input().backup(1);
                        return info.tokenFactory().createToken(TypeScriptTokenId.NUMBER);
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
                            return info.tokenFactory().createToken(TypeScriptTokenId.KEYWORD);
                        }
                        return info.tokenFactory().createToken(TypeScriptTokenId.IDENTIFIER);
                    }
                    sb.append((char) c);
                }
            }

            // Operators and separators
            switch (c) {
                case '+': case '-': case '*': case '%': case '=':
                case '<': case '>': case '!': case '&': case '|':
                case '^': case '~': case '?': case ':':
                    return info.tokenFactory().createToken(TypeScriptTokenId.OPERATOR);
                case '(': case ')': case '[': case ']': case '{': case '}':
                case ',': case ';': case '.': case '@':
                    return info.tokenFactory().createToken(TypeScriptTokenId.SEPARATOR);
                default:
                    return info.tokenFactory().createToken(TypeScriptTokenId.ERROR);
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
