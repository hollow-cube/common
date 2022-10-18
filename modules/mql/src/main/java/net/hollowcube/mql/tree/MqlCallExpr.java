package net.hollowcube.mql.tree;

import net.hollowcube.mql.runtime.MqlScope;
import net.hollowcube.mql.value.MqlCallable;
import net.hollowcube.mql.value.MqlNumberValue;
import net.hollowcube.mql.value.MqlValue;
import org.jetbrains.annotations.NotNull;

public record MqlCallExpr(@NotNull MqlAccessExpr access, @NotNull MqlArgListExpr argList) implements MqlExpr {
    @Override
    public MqlValue evaluate(@NotNull MqlScope scope) {
        var callable = access().evaluate(scope).cast(MqlCallable.class);
        return callable.call(argList.args(), scope).cast(MqlNumberValue.class);
    }

    @Override
    public <P, R> R visit(@NotNull MqlVisitor<P, R> visitor, P p) {
        return visitor.visitCallExpr(this, p);
    }
}
