/*
 * Copyright 2026 FlossWare.
 */

package org.flossware.netbeans.kotlin.lexer;

import java.util.HashSet;
import java.util.Set;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

public class KotlinLexer implements Lexer<KotlinTokenId> {

    private static final Set<String> KEYWORDS = new HashSet<>();

    static {
        String[] keywords = {
            "as", "break", "class", "continue", "do", "else", "false", "for", "fun", "if",
            "in", "interface", "is", "null", "object", "package", "return", "super", "this",
            "throw", "true", "try", "typealias", "typeof", "val", "var", "when", "while",
            "by", "catch", "constructor", "delegate", "dynamic", "field", "file", "finally",
            "get", "import", "init", "param", "property", "receiver", "set", "setparam",
            "where", "actual", "abstract", "annotation", "companion", "const", "crossinline",
            "data", "enum", "expect", "external", "final", "infix", "inline", "inner",
            "internal", "lateinit", "noinline", "open", "operator", "out", "override",
            "private", "protected", "public", "reified", "sealed", "suspend", "tailrec", "vararg"
        };
        for (String keyword : keywords) {
            KEYWORDS.add(keyword);
        }
    }

    private final LexerRestartInfo<KotlinTokenId> info;

    public KotlinLexer(LexerRestartInfo<KotlinTokenId> info) {
        this.info = info;
    }

    @Override
    public org.netbeans.api.lexer.Token<KotlinTokenId> nextToken() {
        while (true) {
            int c = info.input().read();

            if (c == org.netbeans.spi.lexer.LexerInput.EOF) {
                return null;
            }

            if (Character.isWhitespace(c)) {
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF || !Character.isWhitespace(c)) {
                        info.input().backup(1);
                        return info.tokenFactory().createToken(KotlinTokenId.WHITESPACE);
                    }
                }
            }

            if (c == '/') {
                c = info.input().read();
                if (c == '/') {
                    while (true) {
                        c = info.input().read();
                        if (c == org.netbeans.spi.lexer.LexerInput.EOF || c == '\n' || c == '\r') {
                            info.input().backup(1);
                            return info.tokenFactory().createToken(KotlinTokenId.COMMENT);
                        }
                    }
                } else if (c == '*') {
                    while (true) {
                        c = info.input().read();
                        if (c == org.netbeans.spi.lexer.LexerInput.EOF) {
                            return info.tokenFactory().createToken(KotlinTokenId.COMMENT);
                        }
                        if (c == '*') {
                            c = info.input().read();
                            if (c == '/') {
                                return info.tokenFactory().createToken(KotlinTokenId.COMMENT);
                            }
                            info.input().backup(1);
                        }
                    }
                } else {
                    info.input().backup(1);
                    return info.tokenFactory().createToken(KotlinTokenId.OPERATOR);
                }
            }

            if (c == '@') {
                if (Character.isJavaIdentifierStart(info.input().read())) {
                    info.input().backup(1);
                    while (true) {
                        c = info.input().read();
                        if (c == org.netbeans.spi.lexer.LexerInput.EOF || !Character.isJavaIdentifierPart(c)) {
                            info.input().backup(1);
                            return info.tokenFactory().createToken(KotlinTokenId.ANNOTATION);
                        }
                    }
                } else {
                    info.input().backup(1);
                    return info.tokenFactory().createToken(KotlinTokenId.OPERATOR);
                }
            }

            if (c == '"') {
                int next = info.input().read();
                if (next == '"') {
                    int third = info.input().read();
                    if (third == '"') {
                        while (true) {
                            c = info.input().read();
                            if (c == org.netbeans.spi.lexer.LexerInput.EOF) {
                                return info.tokenFactory().createToken(KotlinTokenId.STRING);
                            }
                            if (c == '"') {
                                c = info.input().read();
                                if (c == '"') {
                                    c = info.input().read();
                                    if (c == '"') {
                                        return info.tokenFactory().createToken(KotlinTokenId.STRING);
                                    }
                                    info.input().backup(1);
                                } else {
                                    info.input().backup(1);
                                }
                            }
                        }
                    } else {
                        info.input().backup(2);
                        return info.tokenFactory().createToken(KotlinTokenId.STRING);
                    }
                } else {
                    info.input().backup(1);
                    boolean escaped = false;
                    while (true) {
                        c = info.input().read();
                        if (c == org.netbeans.spi.lexer.LexerInput.EOF) {
                            return info.tokenFactory().createToken(KotlinTokenId.STRING);
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
                            return info.tokenFactory().createToken(KotlinTokenId.STRING);
                        }
                    }
                }
            }

            if (Character.isDigit(c)) {
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF ||
                        (!Character.isDigit(c) && c != '.' && c != 'e' && c != 'E' &&
                         c != 'x' && c != 'X' && c != 'L' && c != 'l' && c != 'f' && c != 'F' && c != '_')) {
                        info.input().backup(1);
                        return info.tokenFactory().createToken(KotlinTokenId.NUMBER);
                    }
                }
            }

            if (Character.isJavaIdentifierStart(c)) {
                StringBuilder sb = new StringBuilder();
                sb.append((char) c);
                while (true) {
                    c = info.input().read();
                    if (c == org.netbeans.spi.lexer.LexerInput.EOF || !Character.isJavaIdentifierPart(c)) {
                        info.input().backup(1);
                        String text = sb.toString();
                        if (KEYWORDS.contains(text)) {
                            return info.tokenFactory().createToken(KotlinTokenId.KEYWORD);
                        }
                        return info.tokenFactory().createToken(KotlinTokenId.IDENTIFIER);
                    }
                    sb.append((char) c);
                }
            }

            switch (c) {
                case '+': case '-': case '*': case '%': case '=':
                case '<': case '>': case '!': case '&': case '|': case '^':
                case '~': case '?': case ':':
                    return info.tokenFactory().createToken(KotlinTokenId.OPERATOR);
                case '(': case ')': case '[': case ']': case '{': case '}':
                case ',': case ';': case '.':
                    return info.tokenFactory().createToken(KotlinTokenId.SEPARATOR);
                default:
                    return info.tokenFactory().createToken(KotlinTokenId.ERROR);
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
