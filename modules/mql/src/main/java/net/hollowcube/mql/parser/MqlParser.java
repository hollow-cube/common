package net.hollowcube.mql.parser;

import net.hollowcube.mql.tree.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

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
            Operator op = operator();
            if (op == null) break;

            var postfixBindingPower = op.postfixBindingPower();
            if (postfixBindingPower != -1) {
                if (postfixBindingPower < minBindingPower) break;
                lexer.next(); // Operator token

                lhs = switch (op) {
                    case TERNARY -> {
                        var trueExpr = expr(0);
                        lexer.expect(MqlToken.Type.COLON);
                        var falseExpr = expr(postfixBindingPower);
                        yield new MqlTernaryExpr(lhs, trueExpr, falseExpr);
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + op);
                };

                continue;
            }

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
                    if (peek != null && peek.type() == MqlToken.Type.LPAREN) {
                        var res = expr(op.rbp);

                        if (res instanceof MqlArgListExpr args) {
                            // Arg list returned
                            yield new MqlAccessExpr(lhs, ident.value(), args);
                        } else {
                            // Single param, create list
                            yield new MqlAccessExpr(lhs, ident.value(), new MqlArgListExpr(List.of(res)));
                        }
                    }

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
            case MINUS -> {
                var rhs = expr(Operator.MINUS.prefixBindingPower());
                yield new MqlUnaryExpr(MqlUnaryExpr.Op.NEGATE, rhs);
            }
            case LPAREN -> {
                var res = expr(0);
                var next = lexer.next();

                if (next != null && next.type() == MqlToken.Type.COMMA) {
                    List<MqlExpr> args = new ArrayList<>();
                    args.add(res);

                    while (next.type() == MqlToken.Type.COMMA) {
                        args.add(expr(0));
                        next = lexer.next();
                    }

                    lexer.expect(MqlToken.Type.RPAREN);

                    yield new MqlArgListExpr(args);
                } else {
                    lexer.expect(MqlToken.Type.RPAREN);

                    yield res;
                }
            }
            //todo better error handling
            default -> throw new MqlParseError("unexpected token " + token);
        };
    }

    private MqlParser.@Nullable Operator operator() {
        MqlToken token = lexer.peek();
        if (token == null) return null;
        return switch (token.type()) {
            case PLUS -> Operator.PLUS;
            case MINUS -> Operator.MINUS;
            case SLASH -> Operator.DIV;
            case STAR -> Operator.MUL;
            case DOT -> Operator.MEMBER_ACCESS;
            case QUESTION -> Operator.TERNARY;
            case QUESTIONQUESTION -> Operator.NULL_COALESCE;
            default -> null;
        };
    }

    private enum Operator {
        NULL_COALESCE(5, 6, MqlBinaryExpr.Op.NULL_COALESCE),
        PLUS(25, 26, MqlBinaryExpr.Op.PLUS),
        MINUS(25, 26, MqlBinaryExpr.Op.MINUS),
        DIV(27, 28, MqlBinaryExpr.Op.DIV),
        MUL(27, 28, MqlBinaryExpr.Op.MUL),

        MEMBER_ACCESS(35, 36, null),

        TERNARY(0, 0, null); // Open of a ternary expression (?), only a postfix operator

        private final int lbp;
        private final int rbp;
        private final MqlBinaryExpr.Op op;

        Operator(int lbp, int rbp, MqlBinaryExpr.Op op) {
            this.lbp = lbp;
            this.rbp = rbp;
            this.op = op;
        }

        public int prefixBindingPower() {
            return switch (this) {
                case MINUS -> 30;
                default -> -1;
            };
        }

        public int postfixBindingPower() {
            return switch (this) {
                case TERNARY -> 1;
                default -> -1;
            };
        }
    }

}
