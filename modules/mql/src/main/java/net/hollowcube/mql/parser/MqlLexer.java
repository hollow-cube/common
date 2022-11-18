package net.hollowcube.mql.parser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MqlLexer {
    private final String source;

    private int start = 0;
    private int cursor = 0;

    public MqlLexer(@NotNull String source) {
        this.source = source;
    }

    /**
     * Returns the next token in the input, or null if the end of file was reached.
     *
     * @throws MqlParseError if there is an unexpected token.
     */
    public @Nullable MqlToken next() {
        start = cursor;

        if (atEnd()) return null;

        consumeWhitespace();

        char c = advance();
        if (isAlpha(c))
            return ident();
        if (isDigit(c))
            return number();

        return symbol(c);
    }

    /**
     * Returns the next token <i>without</i> stepping to the next token in the input,
     * or null if the end of file was reached.
     *
     * @throws MqlParseError if there is an unexpected token.
     */
    public @Nullable MqlToken peek() {
        var result = next();
        cursor = start; // Reset to where it was before the call to next.
        return result;
    }

    public void expect(@NotNull MqlToken.Type type) {
        var next = next();
        if (next == null || next.type() != type)
            throw new MqlParseError("Expected " + type + " but got " + next);
    }

    public @NotNull String span(@NotNull MqlToken token) {
        return source.substring(start, cursor).strip();
    }

    private void consumeWhitespace() {
        while (true) {
            switch (peek0()) {
                case ' ', '\t', '\r', '\n' -> advance();
                default -> {
                    return;
                }
            }
        }
    }

    private MqlToken ident() {
        while (isAlpha(peek0()) || isDigit(peek0())) {
            advance();
        }

        return new MqlToken(MqlToken.Type.IDENT, start, cursor);
    }

    private MqlToken number() {
        // Pre decimal
        while (isDigit(peek0()))
            advance();

        // Decimal, if present
        if (match('.')) {
            while (isDigit(peek0()))
                advance();
        }

        return new MqlToken(MqlToken.Type.NUMBER, start, cursor);
    }

    private MqlToken symbol(char c) {
        var tokenType = switch (c) {
            // @formatter:off
            case '+' -> MqlToken.Type.PLUS;
            case '-' -> MqlToken.Type.MINUS;
            case '*' -> MqlToken.Type.STAR;
            case '/' -> MqlToken.Type.SLASH;
            case '.' -> MqlToken.Type.DOT;
            case ',' -> MqlToken.Type.COMMA;
            case '?' -> {
                if (match('?')) {
                    yield MqlToken.Type.QUESTIONQUESTION;
                } else {
                    yield MqlToken.Type.QUESTION;
                }
            }
            case ':' -> MqlToken.Type.COLON;
            case '(' -> MqlToken.Type.LPAREN;
            case ')' -> MqlToken.Type.RPAREN;
            case '>' -> {
                if (match('=')) {
                    yield MqlToken.Type.GTE;
                } else {
                    yield MqlToken.Type.GE;
                }
            }
            case '<' -> {
                if (match('=')) {
                    yield MqlToken.Type.LTE;
                } else {
                    yield MqlToken.Type.LE;
                }
            }
            case '=' -> {
                if (match('=')) {
                    yield MqlToken.Type.EQ;
                } else {
                    throw new MqlParseError(String.format("unexpected token '%s' at %d.", c, cursor));
                }
            }
            case '!' -> {
                if (match('=')) {
                    yield MqlToken.Type.NEQ;
                } else {
                    throw new MqlParseError(String.format("unexpected token '%s' at %d.", c, cursor));
                }
            }
            default -> throw new MqlParseError(
                    String.format("unexpected token '%s' at %d.", c, cursor));
            // @formatter:on
        };
        return new MqlToken(tokenType, start, cursor);
    }

    private boolean atEnd() {
        return cursor >= source.length();
    }

    private char peek0() {
        if (atEnd())
            return '\u0000';
        return source.charAt(cursor);
    }

    private char advance() {
        if (atEnd()) throw new MqlParseError("unexpected end of input");
        return source.charAt(cursor++);
    }

    private boolean match(char c) {
        if (atEnd()) return false;
        if (peek0() != c) return false;
        advance();
        return true;
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }
}
