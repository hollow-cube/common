package net.hollowcube.mql.parser;

import net.hollowcube.mql.tree.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MqlParser {
    private final MqlLexer lexer;

    public MqlParser(@NotNull String source) {
        this.lexer = new MqlLexer(source);
    }

    public @NotNull MqlExpr parse() {
        return expr(0);
    }

    private @NotNull MqlExpr expr(int minBindingPower) {
        MqlExpr lhs = lhs();

        while (true) {
            InfixOp op = infixOp();
            if (op == null) break;

            // Stop if operator left binding power is less than the current min
            if (op.lbp < minBindingPower) break;

            lexer.next(); // Operator token

            // Parse right side expression
            MqlExpr rhs = expr(op.rbp);
            lhs = switch (op) {
                case MEMBER_ACCESS -> {
                    if (!(rhs instanceof MqlIdentExpr ident))
                        throw new MqlParseError("rhs of member access must be an ident, was " + rhs);

                    var peek = lexer.peek();
                    if (peek != null && peek.type() == MqlToken.Type.LPAREN)
                        yield new MqlAccessExpr(lhs, ident.value(), expr(op.rbp));

                    yield(new MqlAccessExpr(lhs, ident.value(), null));
                }
                default -> new MqlBinaryExpr(op.op, lhs, rhs);
            };
        }

        return lhs;
    }

    /** Parses a possible left side expression. */
    private @NotNull MqlExpr lhs() {
        MqlToken token = lexer.next();
        if (token == null) throw new MqlParseError("unexpected end of input");

        return switch (token.type()) {
            case NUMBER -> new MqlNumberExpr(Double.parseDouble(lexer.span(token)));
            case IDENT -> new MqlIdentExpr(lexer.span(token));
            case LPAREN -> {
                var expr = expr(0);
                if (lexer.next().type() != MqlToken.Type.RPAREN)
                    throw new MqlParseError("expected ')'");
                yield expr;
            }
            //todo better error handling
            default -> throw new MqlParseError("unexpected token " + token);
        };
    }

    private @Nullable InfixOp infixOp() {
        MqlToken token = lexer.peek();
        if (token == null) return null;
        return switch (token.type()) {
            case PLUS -> InfixOp.PLUS;
            case MINUS -> InfixOp.MINUS;
            case DIV -> InfixOp.DIV;
            case MUL -> InfixOp.MUL;
            case DOT -> InfixOp.MEMBER_ACCESS;
            default -> null;
        };
    }

    private enum InfixOp {
        PLUS(25, 26, MqlBinaryExpr.Op.PLUS),
        MINUS(25, 26, MqlBinaryExpr.Op.MINUS),
        DIV(27, 28, MqlBinaryExpr.Op.DIV),
        MUL(27, 28, MqlBinaryExpr.Op.MUL),

        MEMBER_ACCESS(35, 36, null);

        private final int lbp;
        private final int rbp;
        private final MqlBinaryExpr.Op op;

        InfixOp(int lbp, int rbp, MqlBinaryExpr.Op op) {
            this.lbp = lbp;
            this.rbp = rbp;
            this.op = op;
        }
    }

}