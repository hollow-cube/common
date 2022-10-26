package net.hollowcube.mql.util;

import net.hollowcube.mql.tree.*;
import org.jetbrains.annotations.NotNull;

public class MqlPrinter implements MqlVisitor<Void, String> {

    @Override
    public String visitBinaryExpr(@NotNull MqlBinaryExpr expr, Void unused) {
        return String.format(
                "(%s %s %s)",
                switch (expr.operator()) {
                    case PLUS -> "+";
                    case MINUS -> "-";
                    case MUL -> "*";
                    case DIV -> "/";
                    case NULL_COALESCE -> "??";
                    case GTE -> ">=";
                    case GE -> ">";
                    case LTE -> "<=";
                    case LE -> "<";
                    case EQ -> "==";
                    case NEQ -> "!=";
                },
                visit(expr.lhs(), null),
                visit(expr.rhs(), null)
        );
    }

    @Override
    public String visitUnaryExpr(@NotNull MqlUnaryExpr expr, Void unused) {
        return String.format(
                "(%s %s)",
                switch (expr.operator()) {
                    case NEGATE -> "-";
                },
                visit(expr.rhs(), null)
        );
    }

    @Override
    public String visitAccessExpr(@NotNull MqlAccessExpr expr, Void unused) {
        return String.format(
                "(. %s %s)",
                visit(expr.lhs(), null),
                expr.target()
        );
    }

    @Override
    public String visitNumberExpr(@NotNull MqlNumberExpr expr, Void unused) {
        return String.valueOf(expr.value());
    }

    @Override
    public String visitRefExpr(@NotNull MqlIdentExpr expr, Void unused) {
        return expr.value();
    }

    @Override
    public String visitArgListExpr(@NotNull MqlArgListExpr expr, Void unused) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");

        for (int i = 0; i < expr.args().size(); i++) {
            sb.append(visit(expr.args().get(i), null));
            if (i != expr.args().size() - 1) {
                sb.append(" ");
            }
        }

        sb.append(")");
        return sb.toString();
    }

    @Override
    public String visitTernaryExpr(MqlTernaryExpr expr, Void unused) {
        return String.format(
                "(? %s %s %s)",
                visit(expr.condition(), null),
                visit(expr.trueCase(), null),
                visit(expr.falseCase(), null)
        );
    }

    @Override
    public String visitCallExpr(MqlCallExpr expr, Void unused) {
        return String.format(
                "(? %s %s)",
                visit(expr.access(), null),
                visit(expr.argList(), null)
        );
    }

    @Override
    public String defaultValue() {
        return "##Error";
    }
}
