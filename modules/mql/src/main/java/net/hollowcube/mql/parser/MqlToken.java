package net.hollowcube.mql.parser;

import org.jetbrains.annotations.NotNull;

record MqlToken(@NotNull Type type, int start, int end) {

    enum Type {
        PLUS, MINUS, MUL, DIV,
        LPAREN, RPAREN,
        DOT,
        NUMBER, IDENT;
    }

}