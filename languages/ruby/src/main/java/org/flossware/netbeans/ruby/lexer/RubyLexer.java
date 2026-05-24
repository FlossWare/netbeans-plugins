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

package org.flossware.netbeans.ruby.lexer;

import java.util.HashSet;
import java.util.Set;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

/**
 * Lexer for Ruby syntax highlighting
 */
public class RubyLexer implements Lexer<RubyTokenId> {

    private static final Set<String> KEYWORDS = new HashSet<>();

    static {
        // Ruby keywords
        String[] keywords = {
            "BEGIN", "END", "__ENCODING__", "__END__", "__FILE__", "__LINE__",
            "alias", "and", "begin", "break", "case", "class", "def", "defined?",
            "do", "else", "elsif", "end", "ensure", "false", "for", "if", "in",
            "module", "next", "nil", "not", "or", "redo", "rescue", "retry",
            "return", "self", "super", "then", "true", "undef", "unless", "until",
            "when", "while", "yield"
        };
        for (String keyword : keywords) {
            KEYWORDS.add(keyword);
        }
    }

    private final LexerRestartInfo<RubyTokenId> info;

    public RubyLexer(LexerRestartInfo<RubyTokenId> info) {
        this.info = info;
    }

    @Override
    public org.netbeans.api.lexer.Token<RubyTokenId> nextToken() {
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
                        return info.tokenFactory().createToken(RubyTokenId.WHITESPACE);
                    }
                }
            }

            // Comments
            if (c == '#') {
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF || c == '\n' || c == '\r') {
                        info.input().backup(1);
                        return info.tokenFactory().createToken(RubyTokenId.COMMENT);
                    }
                }
            }

            // Global variables $var
            if (c == '$') {
                c = info.input().read();
                if (Character.isJavaIdentifierStart(c) || c == '_') {
                    while (true) {
                        c = info.input().read();
                        if (c == org.netbeans.spi.lexer.LexerInput.EOF ||
                            (!Character.isJavaIdentifierPart(c) && c != '_')) {
                            info.input().backup(1);
                            return info.tokenFactory().createToken(RubyTokenId.GLOBAL_VARIABLE);
                        }
                    }
                } else {
                    // Special global variables like $0, $!, $@, etc.
                    return info.tokenFactory().createToken(RubyTokenId.GLOBAL_VARIABLE);
                }
            }

            // Instance variables @var
            if (c == '@') {
                c = info.input().read();
                if (c == '@') {
                    // Class variable @@var
                    c = info.input().read();
                    if (Character.isJavaIdentifierStart(c) || c == '_') {
                        while (true) {
                            c = info.input().read();
                            if (c == org.netbeans.spi.lexer.LexerInput.EOF ||
                                (!Character.isJavaIdentifierPart(c) && c != '_')) {
                                info.input().backup(1);
                                return info.tokenFactory().createToken(RubyTokenId.CLASS_VARIABLE);
                            }
                        }
                    } else {
                        info.input().backup(2);
                        return info.tokenFactory().createToken(RubyTokenId.OPERATOR);
                    }
                } else if (Character.isJavaIdentifierStart(c) || c == '_') {
                    // Instance variable @var
                    while (true) {
                        c = info.input().read();
                        if (c == org.netbeans.spi.lexer.LexerInput.EOF ||
                            (!Character.isJavaIdentifierPart(c) && c != '_')) {
                            info.input().backup(1);
                            return info.tokenFactory().createToken(RubyTokenId.INSTANCE_VARIABLE);
                        }
                    }
                } else {
                    info.input().backup(1);
                    return info.tokenFactory().createToken(RubyTokenId.OPERATOR);
                }
            }

            // Symbols :symbol
            if (c == ':') {
                c = info.input().read();
                if (Character.isJavaIdentifierStart(c) || c == '_') {
                    while (true) {
                        c = info.input().read();
                        if (c == org.netbeans.spi.lexer.LexerInput.EOF ||
                            (!Character.isJavaIdentifierPart(c) && c != '_' && c != '?' && c != '!')) {
                            info.input().backup(1);
                            return info.tokenFactory().createToken(RubyTokenId.SYMBOL);
                        }
                    }
                } else {
                    info.input().backup(1);
                    return info.tokenFactory().createToken(RubyTokenId.OPERATOR);
                }
            }

            // Strings - double quotes
            if (c == '"') {
                boolean escaped = false;
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF) {
                        return info.tokenFactory().createToken(RubyTokenId.STRING);
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
                        return info.tokenFactory().createToken(RubyTokenId.STRING);
                    }
                }
            }

            // Strings - single quotes
            if (c == '\'') {
                boolean escaped = false;
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF) {
                        return info.tokenFactory().createToken(RubyTokenId.STRING);
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
                        return info.tokenFactory().createToken(RubyTokenId.STRING);
                    }
                }
            }

            // Numbers
            if (Character.isDigit(c)) {
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF ||
                        (!Character.isDigit(c) && c != '.' && c != 'e' && c != 'E' &&
                         c != 'x' && c != 'X' && c != 'b' && c != 'B' && c != 'o' && c != 'O' && c != '_')) {
                        info.input().backup(1);
                        return info.tokenFactory().createToken(RubyTokenId.NUMBER);
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
                        (!Character.isJavaIdentifierPart(c) && c != '?' && c != '!')) {
                        info.input().backup(1);
                        String text = sb.toString();
                        if (KEYWORDS.contains(text)) {
                            return info.tokenFactory().createToken(RubyTokenId.KEYWORD);
                        }
                        return info.tokenFactory().createToken(RubyTokenId.IDENTIFIER);
                    }
                    sb.append((char) c);
                }
            }

            // Operators and separators
            switch (c) {
                case '+': case '-': case '*': case '/': case '%': case '=':
                case '<': case '>': case '!': case '&': case '|': case '^':
                case '~': case '?':
                    return info.tokenFactory().createToken(RubyTokenId.OPERATOR);
                case '(': case ')': case '[': case ']': case '{': case '}':
                case ',': case ';': case '.':
                    return info.tokenFactory().createToken(RubyTokenId.SEPARATOR);
                default:
                    return info.tokenFactory().createToken(RubyTokenId.ERROR);
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
