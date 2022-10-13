package net.hollowcube.mql.tree;

import net.hollowcube.mql.foreign.MqlForeignFunctions;
import net.hollowcube.mql.runtime.MqlScope;
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
        MqlNumberValue lhsEval;

        var lhsVal = lhs().evaluate(scope);

        if (lhsVal instanceof MqlNumberValue) {
            lhsEval = lhsVal.cast(MqlNumberValue.class);
        } else if (lhsVal instanceof MqlForeignFunctions.ForeignCallable f) {
            lhsEval = f.call(List.of(((MqlAccessExpr)lhs).body().evaluate(scope))).cast(MqlNumberValue.class);;
        } else {
            throw new RuntimeException("lhs is not a number or foreign function");
        }

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
