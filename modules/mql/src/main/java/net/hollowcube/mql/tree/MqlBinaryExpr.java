package net.hollowcube.mql.tree;

import net.hollowcube.mql.runtime.MqlScope;
import net.hollowcube.mql.value.MqlCallable;
import net.hollowcube.mql.value.MqlNumberValue;
import net.hollowcube.mql.value.MqlValue;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record MqlBinaryExpr(
        @NotNull Op operator,
        @NotNull MqlExpr lhs,
        @NotNull MqlExpr rhs
) implements MqlExpr {
    public enum Op {
        PLUS,
        MINUS,
        DIV,
        MUL
    }

    @Override
    public MqlValue evaluate(@NotNull MqlScope scope) {
        MqlNumberValue lhs = lhs().evaluate(scope).cast(MqlNumberValue.class);
        MqlNumberValue rhs = rhs().evaluate(scope).cast(MqlNumberValue.class);

        return switch (operator()) {
            case PLUS -> new MqlNumberValue(lhs.value() + rhs.value());
            case MINUS -> new MqlNumberValue(lhs.value() - rhs.value());
            case DIV -> new MqlNumberValue(lhs.value() / rhs.value());
            case MUL -> new MqlNumberValue(lhs.value() * rhs.value());
        };
    }

    @Override
    public <P, R> R visit(@NotNull MqlVisitor<P, R> visitor, P p) {
        return visitor.visitBinaryExpr(this, p);
    }
}
