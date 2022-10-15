package net.hollowcube.mql.tree;

import net.hollowcube.mql.runtime.MqlScope;
import net.hollowcube.mql.value.MqlNumberValue;
import net.hollowcube.mql.value.MqlValue;
import org.jetbrains.annotations.NotNull;

public record MqlBinaryExpr(
        @NotNull Op operator,
        @NotNull MqlExpr lhs,
        @NotNull MqlExpr rhs
) implements MqlExpr {
    public enum Op {
        PLUS,
        MINUS,
        DIV,
        MUL,
        NULL_COALESCE
    }

    @Override
    public MqlValue evaluate(@NotNull MqlScope scope) {
        // Handle lazy evaluation of null coalescing lhs and rhs
        if (operator() == Op.NULL_COALESCE) {
            var lhs = lhs().evaluate(scope).cast(MqlNumberValue.class);
            if (lhs.value() != 0) {
                return lhs;
            }

            return rhs().evaluate(scope);
        }


        // The rest use both lhs and rhs always
        MqlNumberValue lhs = lhs().evaluate(scope).cast(MqlNumberValue.class);
        MqlNumberValue rhs = rhs().evaluate(scope).cast(MqlNumberValue.class);

        return switch (operator()) {
            case PLUS -> new MqlNumberValue(lhs.value() + rhs.value());
            case MINUS -> new MqlNumberValue(lhs.value() - rhs.value());
            case DIV -> new MqlNumberValue(lhs.value() / rhs.value());
            case MUL -> new MqlNumberValue(lhs.value() * rhs.value());
            case NULL_COALESCE -> throw new RuntimeException("unreachable");
        };
    }

    @Override
    public <P, R> R visit(@NotNull MqlVisitor<P, R> visitor, P p) {
        return visitor.visitBinaryExpr(this, p);
    }
}
