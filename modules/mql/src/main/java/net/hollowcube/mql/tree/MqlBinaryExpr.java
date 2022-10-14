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
        MqlNumberValue lhsEval = lhs().evaluate(scope).cast(MqlNumberValue.class);
        MqlNumberValue rhsEval = rhs().evaluate(scope).cast(MqlNumberValue.class);

        return switch (operator()) {
            case PLUS -> new MqlNumberValue(lhsEval.value() + rhsEval.value());
            case MINUS -> new MqlNumberValue(lhsEval.value() - rhsEval.value());
            case DIV -> new MqlNumberValue(lhsEval.value() / rhsEval.value());
            case MUL -> new MqlNumberValue(lhsEval.value() * rhsEval.value());
        };
    }

    @Override
    public <P, R> R visit(@NotNull MqlVisitor<P, R> visitor, P p) {
        return visitor.visitBinaryExpr(this, p);
    }
}
