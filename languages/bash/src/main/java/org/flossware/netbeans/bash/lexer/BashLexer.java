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

package org.flossware.netbeans.bash.lexer;

import java.util.HashSet;
import java.util.Set;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

/**
 * Lexer for Bash syntax highlighting
 */
public class BashLexer implements Lexer<BashTokenId> {

    private static final Set<String> KEYWORDS = new HashSet<>();

    static {
        // Bash keywords and builtins
        String[] keywords = {
            "if", "then", "else", "elif", "fi", "case", "esac", "for", "select",
            "while", "until", "do", "done", "in", "function", "time", "coproc",
            "break", "continue", "return", "exit", "shift", "export", "readonly",
            "local", "declare", "typeset", "unset", "set", "shopt", "source",
            "alias", "unalias", "bg", "fg", "jobs", "disown", "suspend",
            "echo", "printf", "read", "cd", "pwd", "pushd", "popd", "dirs",
            "let", "eval", "exec", "test", "true", "false", "kill", "wait",
            "trap", "umask", "ulimit", "getopts", "hash", "type", "command",
            "builtin", "enable", "help", "logout"
        };
        for (String keyword : keywords) {
            KEYWORDS.add(keyword);
        }
    }

    private final LexerRestartInfo<BashTokenId> info;

    public BashLexer(LexerRestartInfo<BashTokenId> info) {
        this.info = info;
    }

    @Override
    public org.netbeans.api.lexer.Token<BashTokenId> nextToken() {
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
                        return info.tokenFactory().createToken(BashTokenId.WHITESPACE);
                    }
                }
            }

            // Comments
            if (c == '#') {
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF || c == '\n' || c == '\r') {
                        info.input().backup(1);
                        return info.tokenFactory().createToken(BashTokenId.COMMENT);
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
                            return info.tokenFactory().createToken(BashTokenId.VARIABLE);
                        }
                        if (c == '}') {
                            return info.tokenFactory().createToken(BashTokenId.VARIABLE);
                        }
                    }
                } else if (Character.isJavaIdentifierStart(c) || Character.isDigit(c)) {
                    // $VAR or $1 form
                    while (true) {
                        c = info.input().read();
                        if (c == org.netbeans.spi.lexer.LexerInput.EOF ||
                            (!Character.isJavaIdentifierPart(c) && !Character.isDigit(c))) {
                            info.input().backup(1);
                            return info.tokenFactory().createToken(BashTokenId.VARIABLE);
                        }
                    }
                } else if (c == '(' || c == '@' || c == '*' || c == '#' || c == '?' || c == '-' || c == '$' || c == '!') {
                    // Special variables: $(), $@, $*, $#, $?, $-, $$, $!
                    return info.tokenFactory().createToken(BashTokenId.VARIABLE);
                } else {
                    info.input().backup(1);
                    return info.tokenFactory().createToken(BashTokenId.OPERATOR);
                }
            }

            // Strings - double quotes
            if (c == '"') {
                boolean escaped = false;
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF) {
                        return info.tokenFactory().createToken(BashTokenId.STRING);
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
                        return info.tokenFactory().createToken(BashTokenId.STRING);
                    }
                }
            }

            // Strings - single quotes
            if (c == '\'') {
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF) {
                        return info.tokenFactory().createToken(BashTokenId.STRING);
                    }
                    if (c == '\'') {
                        return info.tokenFactory().createToken(BashTokenId.STRING);
                    }
                }
            }

            // Backticks (command substitution)
            if (c == '`') {
                boolean escaped = false;
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF) {
                        return info.tokenFactory().createToken(BashTokenId.STRING);
                    }
                    if (escaped) {
                        escaped = false;
                        continue;
                    }
                    if (c == '\\') {
                        escaped = true;
                        continue;
                    }
                    if (c == '`') {
                        return info.tokenFactory().createToken(BashTokenId.STRING);
                    }
                }
            }

            // Numbers
            if (Character.isDigit(c)) {
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF || !Character.isDigit(c)) {
                        info.input().backup(1);
                        return info.tokenFactory().createToken(BashTokenId.NUMBER);
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
                        if (KEYWORDS.contains(text)) {
                            return info.tokenFactory().createToken(BashTokenId.KEYWORD);
                        }
                        return info.tokenFactory().createToken(BashTokenId.IDENTIFIER);
                    }
                    sb.append((char) c);
                }
            }

            // Operators and separators
            switch (c) {
                case '+': case '-': case '*': case '/': case '%': case '=':
                case '<': case '>': case '!': case '&': case '|': case '^':
                case '~': case '?': case ':':
                    return info.tokenFactory().createToken(BashTokenId.OPERATOR);
                case '(': case ')': case '[': case ']': case '{': case '}':
                case ',': case ';': case '.': case '@':
                    return info.tokenFactory().createToken(BashTokenId.SEPARATOR);
                default:
                    return info.tokenFactory().createToken(BashTokenId.ERROR);
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
