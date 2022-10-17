package net.hollowcube.mql.tree;

import net.hollowcube.mql.runtime.MqlScope;
import net.hollowcube.mql.value.MqlValue;
import org.jetbrains.annotations.NotNull;

public sealed interface MqlExpr permits MqlAccessExpr, MqlArgListExpr, MqlBinaryExpr, MqlCallExpr, MqlIdentExpr, MqlNumberExpr, MqlTernaryExpr, MqlUnaryExpr {

    MqlValue evaluate(@NotNull MqlScope scope);

    <P, R> R visit(@NotNull MqlVisitor<P, R> visitor, P p);
}
