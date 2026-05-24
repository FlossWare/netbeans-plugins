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

package org.flossware.netbeans.batch.lexer;

import java.util.HashSet;
import java.util.Set;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

/**
 * Lexer for Windows Batch syntax highlighting
 */
public class BatchLexer implements Lexer<BatchTokenId> {

    private static final Set<String> KEYWORDS = new HashSet<>();

    static {
        // Batch keywords and commands (case-insensitive)
        String[] keywords = {
            "assoc", "break", "call", "cd", "chdir", "cls", "color", "copy", "date",
            "del", "dir", "echo", "endlocal", "erase", "exit", "for", "ftype",
            "goto", "if", "md", "mkdir", "move", "path", "pause", "popd", "prompt",
            "pushd", "rd", "rem", "ren", "rename", "rmdir", "set", "setlocal",
            "shift", "start", "time", "title", "type", "ver", "verify", "vol",
            "defined", "exist", "errorlevel", "cmdextversion", "not", "else",
            "in", "do", "equ", "neq", "lss", "leq", "gtr", "geq"
        };
        for (String keyword : keywords) {
            KEYWORDS.add(keyword.toLowerCase());
        }
    }

    private final LexerRestartInfo<BatchTokenId> info;

    public BatchLexer(LexerRestartInfo<BatchTokenId> info) {
        this.info = info;
    }

    @Override
    public org.netbeans.api.lexer.Token<BatchTokenId> nextToken() {
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
                        return info.tokenFactory().createToken(BatchTokenId.WHITESPACE);
                    }
                }
            }

            // Labels and :: comments
            if (c == ':') {
                int next = info.input().read();
                if (next == ':') {
                    // :: comment (rest of line)
                    while (true) {
                        c = info.input().read();
                        if (c == org.netbeans.spi.lexer.LexerInput.EOF || c == '\n' || c == '\r') {
                            info.input().backup(1);
                            return info.tokenFactory().createToken(BatchTokenId.COMMENT);
                        }
                    }
                } else {
                    // :label
                    info.input().backup(1);
                    while (true) {
                        c = info.input().read();
                        if (c == org.netbeans.spi.lexer.LexerInput.EOF ||
                            Character.isWhitespace(c)) {
                            info.input().backup(1);
                            return info.tokenFactory().createToken(BatchTokenId.LABEL);
                        }
                    }
                }
            }

            // Variables %VAR% or !VAR! (delayed expansion)
            if (c == '%' || c == '!') {
                int delimiter = c;
                boolean isSpecial = false;
                c = info.input().read();

                // Check for special variables like %1, %*, etc.
                if (delimiter == '%' && (Character.isDigit(c) || c == '*' || c == '~')) {
                    return info.tokenFactory().createToken(BatchTokenId.VARIABLE);
                }

                while (true) {
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF) {
                        return info.tokenFactory().createToken(BatchTokenId.VARIABLE);
                    }
                    if (c == delimiter) {
                        return info.tokenFactory().createToken(BatchTokenId.VARIABLE);
                    }
                    c = info.input().read();
                }
            }

            // Strings (double quotes)
            if (c == '"') {
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF) {
                        return info.tokenFactory().createToken(BatchTokenId.STRING);
                    }
                    if (c == '"') {
                        return info.tokenFactory().createToken(BatchTokenId.STRING);
                    }
                }
            }

            // Numbers
            if (Character.isDigit(c)) {
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF || !Character.isDigit(c)) {
                        info.input().backup(1);
                        return info.tokenFactory().createToken(BatchTokenId.NUMBER);
                    }
                }
            }

            // Identifiers and keywords
            if (Character.isJavaIdentifierStart(c) || c == '@') {
                StringBuilder sb = new StringBuilder();
                sb.append((char) c);
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF ||
                        (!Character.isJavaIdentifierPart(c) && c != '-')) {
                        info.input().backup(1);
                        String text = sb.toString();
                        // Remove @ prefix if present
                        String keyword = text.startsWith("@") ? text.substring(1) : text;
                        if (KEYWORDS.contains(keyword.toLowerCase())) {
                            return info.tokenFactory().createToken(BatchTokenId.KEYWORD);
                        }
                        return info.tokenFactory().createToken(BatchTokenId.IDENTIFIER);
                    }
                    sb.append((char) c);
                }
            }

            // Operators and separators
            switch (c) {
                case '+': case '-': case '*': case '/':
                case '=': case '<': case '>': case '&': case '|': case '^':
                    return info.tokenFactory().createToken(BatchTokenId.OPERATOR);
                case '(': case ')': case '[': case ']':
                case ',': case ';': case '.':
                    return info.tokenFactory().createToken(BatchTokenId.SEPARATOR);
                default:
                    return info.tokenFactory().createToken(BatchTokenId.ERROR);
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
