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

package org.flossware.netbeans.lisp.lexer;

import java.util.HashSet;
import java.util.Set;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

/**
 * Lexer for Common Lisp syntax highlighting
 */
public class LispLexer implements Lexer<LispTokenId> {

    private static final Set<String> KEYWORDS = new HashSet<>();

    static {
        // Common Lisp keywords
        String[] keywords = {
            "defun", "defmacro", "defvar", "defparameter", "defconstant", "defclass",
            "defgeneric", "defmethod", "defpackage", "in-package",
            "let", "let*", "lambda", "labels", "flet", "macrolet",
            "if", "when", "unless", "cond", "case", "typecase", "ecase", "etypecase",
            "do", "do*", "dolist", "dotimes", "loop",
            "setf", "setq", "psetf", "psetq",
            "quote", "function", "backquote",
            "and", "or", "not",
            "progn", "prog1", "prog2",
            "return", "return-from", "block", "tagbody", "go",
            "catch", "throw", "unwind-protect",
            "t", "nil"
        };
        for (String keyword : keywords) {
            KEYWORDS.add(keyword.toLowerCase());
        }
    }

    private final LexerRestartInfo<LispTokenId> info;

    public LispLexer(LexerRestartInfo<LispTokenId> info) {
        this.info = info;
    }

    @Override
    public org.netbeans.api.lexer.Token<LispTokenId> nextToken() {
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
                        return info.tokenFactory().createToken(LispTokenId.WHITESPACE);
                    }
                }
            }

            // Comments - line comment ;
            if (c == ';') {
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF || c == '\n' || c == '\r') {
                        info.input().backup(1);
                        return info.tokenFactory().createToken(LispTokenId.COMMENT);
                    }
                }
            }

            // Comments - block comment #| |#
            if (c == '#') {
                int next = info.input().read();
                if (next == '|') {
                    while (true) {
                        c = info.input().read();
                        if (c == org.netbeans.spi.lexer.LexerInput.EOF) {
                            return info.tokenFactory().createToken(LispTokenId.COMMENT);
                        }
                        if (c == '|') {
                            c = info.input().read();
                            if (c == '#') {
                                return info.tokenFactory().createToken(LispTokenId.COMMENT);
                            }
                            info.input().backup(1);
                        }
                    }
                } else if (next == '\\') {
                    // Character literal #\c
                    c = info.input().read();
                    return info.tokenFactory().createToken(LispTokenId.STRING);
                } else {
                    info.input().backup(1);
                    return info.tokenFactory().createToken(LispTokenId.OPERATOR);
                }
            }

            // Strings
            if (c == '"') {
                boolean escaped = false;
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF) {
                        return info.tokenFactory().createToken(LispTokenId.STRING);
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
                        return info.tokenFactory().createToken(LispTokenId.STRING);
                    }
                }
            }

            // Quoted symbols
            if (c == '\'') {
                return info.tokenFactory().createToken(LispTokenId.OPERATOR);
            }

            // Backquote
            if (c == '`') {
                return info.tokenFactory().createToken(LispTokenId.OPERATOR);
            }

            // Comma (unquote)
            if (c == ',') {
                int next = info.input().read();
                if (next == '@') {
                    return info.tokenFactory().createToken(LispTokenId.OPERATOR);
                }
                info.input().backup(1);
                return info.tokenFactory().createToken(LispTokenId.OPERATOR);
            }

            // Numbers
            if (Character.isDigit(c) || (c == '-' && Character.isDigit(info.input().read()))) {
                if (c == '-') {
                    info.input().backup(1);
                }
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF ||
                        (!Character.isDigit(c) && c != '.' && c != 'e' && c != 'E' && c != '-' && c != '+' && c != '/')) {
                        info.input().backup(1);
                        return info.tokenFactory().createToken(LispTokenId.NUMBER);
                    }
                }
            }

            // Symbols and keywords
            if (Character.isJavaIdentifierStart(c) || c == '-' || c == '+' || c == '*' || c == '/' ||
                c == '<' || c == '>' || c == '=' || c == '!' || c == '?' || c == ':') {
                StringBuilder sb = new StringBuilder();
                sb.append((char) c);
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF ||
                        Character.isWhitespace(c) || c == '(' || c == ')' || c == '\'' || c == '"' || c == '`' || c == ',') {
                        info.input().backup(1);
                        String text = sb.toString().toLowerCase();
                        if (KEYWORDS.contains(text)) {
                            return info.tokenFactory().createToken(LispTokenId.KEYWORD);
                        }
                        return info.tokenFactory().createToken(LispTokenId.SYMBOL);
                    }
                    sb.append((char) c);
                }
            }

            // Parentheses and separators
            if (c == '(' || c == ')') {
                return info.tokenFactory().createToken(LispTokenId.SEPARATOR);
            }

            return info.tokenFactory().createToken(LispTokenId.ERROR);
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
