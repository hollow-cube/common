package net.hollowcube.mql.tree;

import net.hollowcube.mql.runtime.MqlScope;
import net.hollowcube.mql.value.MqlNumberValue;
import net.hollowcube.mql.value.MqlValue;
import org.jetbrains.annotations.NotNull;

public record MqlTernaryExpr(
        @NotNull MqlExpr condition,
        @NotNull MqlExpr trueCase,
        @NotNull MqlExpr falseCase
) implements MqlExpr {

    @Override
    public MqlValue evaluate(@NotNull MqlScope scope) {
        var condition = condition().evaluate(scope).cast(MqlNumberValue.class);
        return condition.value() != 0 ? trueCase().evaluate(scope) : falseCase().evaluate(scope);
    }

    @Override
    public <P, R> R visit(@NotNull MqlVisitor<P, R> visitor, P p) {
        return visitor.visitTernaryExpr(this, p);
    }
}
