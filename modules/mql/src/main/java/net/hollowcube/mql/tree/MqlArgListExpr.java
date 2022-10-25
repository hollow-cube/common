package net.hollowcube.mql.tree;

import net.hollowcube.mql.runtime.MqlScope;
import net.hollowcube.mql.value.MqlValue;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record MqlArgListExpr(List<MqlExpr> args) implements MqlExpr {
    @Override
    public MqlValue evaluate(@NotNull MqlScope scope) {
        return null;
    }

    @Override
    public <P, R> R visit(@NotNull MqlVisitor<P, R> visitor, P p) {
        return visitor.visitArgListExpr(this, p);
    }

    public int size() {
        return args().size();
    }
}
