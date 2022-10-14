package net.hollowcube.mql.tree;

import net.hollowcube.mql.runtime.MqlScope;
import net.hollowcube.mql.value.MqlCallable;
import net.hollowcube.mql.value.MqlHolder;
import net.hollowcube.mql.value.MqlNumberValue;
import net.hollowcube.mql.value.MqlValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record MqlAccessExpr(
        @NotNull MqlExpr lhs,
        @NotNull String target,
        @Nullable MqlArgListExpr body) implements MqlExpr {

    @Override
    public MqlValue evaluate(@NotNull MqlScope scope) {
        var lhs = lhs().evaluate(scope).cast(MqlHolder.class);
        var res = lhs.get(target());

        if (body() != null && res instanceof MqlCallable f) {
            return f.call(body(), scope).cast(MqlNumberValue.class);
        }

        return res;
    }

    @Override
    public <P, R> R visit(@NotNull MqlVisitor<P, R> visitor, P p) {
        return visitor.visitAccessExpr(this, p);
    }
}
