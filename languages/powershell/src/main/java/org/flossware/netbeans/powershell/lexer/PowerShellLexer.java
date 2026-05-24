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

package org.flossware.netbeans.powershell.lexer;

import java.util.HashSet;
import java.util.Set;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

/**
 * Lexer for PowerShell syntax highlighting
 */
public class PowerShellLexer implements Lexer<PowerShellTokenId> {

    private static final Set<String> KEYWORDS = new HashSet<>();

    static {
        // PowerShell keywords
        String[] keywords = {
            "begin", "break", "catch", "class", "continue", "data", "define", "do",
            "dynamicparam", "else", "elseif", "end", "exit", "filter", "finally",
            "for", "foreach", "from", "function", "if", "in", "param", "process",
            "return", "switch", "throw", "trap", "try", "until", "using", "var",
            "while", "workflow", "parallel", "sequence", "inlinescript",
            "configuration", "enum", "hidden", "static", "interface", "namespace"
        };
        for (String keyword : keywords) {
            KEYWORDS.add(keyword);
        }
    }

    private final LexerRestartInfo<PowerShellTokenId> info;

    public PowerShellLexer(LexerRestartInfo<PowerShellTokenId> info) {
        this.info = info;
    }

    @Override
    public org.netbeans.api.lexer.Token<PowerShellTokenId> nextToken() {
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
                        return info.tokenFactory().createToken(PowerShellTokenId.WHITESPACE);
                    }
                }
            }

            // Comments
            if (c == '#') {
                // Check for end-of-line comment
                int next = info.input().read();
                if (next != '<') {
                    // Single-line comment
                    info.input().backup(1);
                    while (true) {
                        c = info.input().read();
                        if (c == org.netbeans.spi.lexer.LexerInput.EOF || c == '\n' || c == '\r') {
                            info.input().backup(1);
                            return info.tokenFactory().createToken(PowerShellTokenId.COMMENT);
                        }
                    }
                } else {
                    // Block comment <# ... #>
                    while (true) {
                        c = info.input().read();
                        if (c == org.netbeans.spi.lexer.LexerInput.EOF) {
                            return info.tokenFactory().createToken(PowerShellTokenId.COMMENT);
                        }
                        if (c == '#') {
                            c = info.input().read();
                            if (c == '>') {
                                return info.tokenFactory().createToken(PowerShellTokenId.COMMENT);
                            }
                            info.input().backup(1);
                        }
                    }
                }
            }

            // Variables
            if (c == '$') {
                c = info.input().read();
                if (c == '{') {
                    // ${VAR} form
                    while (true) {
                        c = info.input().read();
                        if (c == org.netbeans.spi.lexer.LexerInput.EOF) {
                            return info.tokenFactory().createToken(PowerShellTokenId.VARIABLE);
                        }
                        if (c == '}') {
                            return info.tokenFactory().createToken(PowerShellTokenId.VARIABLE);
                        }
                    }
                } else if (Character.isJavaIdentifierStart(c) || c == '_') {
                    // $VAR form
                    while (true) {
                        c = info.input().read();
                        if (c == org.netbeans.spi.lexer.LexerInput.EOF ||
                            (!Character.isJavaIdentifierPart(c) && c != '_')) {
                            info.input().backup(1);
                            return info.tokenFactory().createToken(PowerShellTokenId.VARIABLE);
                        }
                    }
                } else if (c == '$' || c == '^' || c == '?') {
                    // Special variables: $$, $^, $?
                    return info.tokenFactory().createToken(PowerShellTokenId.VARIABLE);
                } else {
                    info.input().backup(1);
                    return info.tokenFactory().createToken(PowerShellTokenId.OPERATOR);
                }
            }

            // Strings - double quotes
            if (c == '"') {
                boolean escaped = false;
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF) {
                        return info.tokenFactory().createToken(PowerShellTokenId.STRING);
                    }
                    if (escaped) {
                        escaped = false;
                        continue;
                    }
                    if (c == '`') {
                        escaped = true;
                        continue;
                    }
                    if (c == '"') {
                        return info.tokenFactory().createToken(PowerShellTokenId.STRING);
                    }
                }
            }

            // Strings - single quotes
            if (c == '\'') {
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF) {
                        return info.tokenFactory().createToken(PowerShellTokenId.STRING);
                    }
                    if (c == '\'') {
                        // Check for escaped single quote ''
                        int next = info.input().read();
                        if (next == '\'') {
                            continue;
                        }
                        info.input().backup(1);
                        return info.tokenFactory().createToken(PowerShellTokenId.STRING);
                    }
                }
            }

            // Numbers
            if (Character.isDigit(c)) {
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF ||
                        (!Character.isDigit(c) && c != '.' && c != 'e' && c != 'E' &&
                         c != 'x' && c != 'X' && c != 'L' && c != 'l' && c != 'd' && c != 'D')) {
                        info.input().backup(1);
                        return info.tokenFactory().createToken(PowerShellTokenId.NUMBER);
                    }
                }
            }

            // Identifiers and keywords
            if (Character.isJavaIdentifierStart(c)) {
                StringBuilder sb = new StringBuilder();
                sb.append((char) c);
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF ||
                        (!Character.isJavaIdentifierPart(c) && c != '-')) {
                        info.input().backup(1);
                        String text = sb.toString();
                        if (KEYWORDS.contains(text.toLowerCase())) {
                            return info.tokenFactory().createToken(PowerShellTokenId.KEYWORD);
                        }
                        return info.tokenFactory().createToken(PowerShellTokenId.IDENTIFIER);
                    }
                    sb.append((char) c);
                }
            }

            // Operators and separators
            switch (c) {
                case '+': case '*': case '/': case '%': case '=':
                case '<': case '>': case '!': case '&': case '|':
                case '~': case '?': case ':':
                    return info.tokenFactory().createToken(PowerShellTokenId.OPERATOR);
                case '-':
                    // Could be operator or start of parameter (-ParameterName)
                    int next = info.input().read();
                    if (Character.isJavaIdentifierStart(next)) {
                        // Parameter name
                        StringBuilder sb = new StringBuilder();
                        sb.append('-');
                        sb.append((char) next);
                        while (true) {
                            c = info.input().read();
                            if (c == org.netbeans.spi.lexer.LexerInput.EOF ||
                                !Character.isJavaIdentifierPart(c)) {
                                info.input().backup(1);
                                return info.tokenFactory().createToken(PowerShellTokenId.IDENTIFIER);
                            }
                            sb.append((char) c);
                        }
                    } else {
                        info.input().backup(1);
                        return info.tokenFactory().createToken(PowerShellTokenId.OPERATOR);
                    }
                case '(': case ')': case '[': case ']': case '{': case '}':
                case ',': case ';': case '.': case '@':
                    return info.tokenFactory().createToken(PowerShellTokenId.SEPARATOR);
                default:
                    return info.tokenFactory().createToken(PowerShellTokenId.ERROR);
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
