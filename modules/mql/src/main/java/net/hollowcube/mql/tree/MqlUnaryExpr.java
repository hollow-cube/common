package net.hollowcube.mql.tree;

import net.hollowcube.mql.runtime.MqlScope;
import net.hollowcube.mql.value.MqlNumberValue;
import net.hollowcube.mql.value.MqlValue;
import org.jetbrains.annotations.NotNull;

public record MqlUnaryExpr(
        @NotNull Op operator,
        @NotNull MqlExpr rhs
) implements MqlExpr {
    public enum Op {
        NEGATE,
    }

    @Override
    public MqlValue evaluate(@NotNull MqlScope scope) {
        MqlNumberValue rhs = rhs().evaluate(scope).cast(MqlNumberValue.class);

        return switch (operator()) {
            case NEGATE -> new MqlNumberValue(-rhs.value());
        };
    }

    @Override
    public <P, R> R visit(@NotNull MqlVisitor<P, R> visitor, P p) {
        return visitor.visitUnaryExpr(this, p);
    }
}
