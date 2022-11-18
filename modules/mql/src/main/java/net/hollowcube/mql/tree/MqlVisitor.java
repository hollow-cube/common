package net.hollowcube.mql.tree;

import org.jetbrains.annotations.NotNull;

// @formatter:off
public interface MqlVisitor<P, R> {

    default R visitBinaryExpr(@NotNull MqlBinaryExpr expr, P p) {
        return defaultValue();
    }

    default R visitUnaryExpr(@NotNull MqlUnaryExpr expr, P p) {
        return defaultValue();
    }

    default R visitAccessExpr(@NotNull MqlAccessExpr expr, P p) {
        return defaultValue();
    }

    default R visitNumberExpr(@NotNull MqlNumberExpr expr, P p) {
        return defaultValue();
    }

    default R visitRefExpr(@NotNull MqlIdentExpr expr, P p) {
        return defaultValue();
    }

    default R visitArgListExpr(MqlArgListExpr mqlArgListExpr, P p) {
        return defaultValue();
    }

    ;

    default R visitTernaryExpr(MqlTernaryExpr expr, P p) {
        return defaultValue();
    }

    default R visitCallExpr(MqlCallExpr expr, P p) {
        return defaultValue();
    }

    default R visit(@NotNull MqlExpr expr, P p) {
        return expr.visit(this, p);
    }

    default R defaultValue() {
        return null;
    }
}
// @formatter:on
