package net.hollowcube.mql.value;

import net.hollowcube.mql.runtime.MqlScope;
import net.hollowcube.mql.tree.MqlExpr;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@FunctionalInterface
public non-sealed interface MqlCallable extends MqlValue {

    /**
     * Returns the arity of the function, or -1 if it is variadic/otherwise unknown
     */
    default int arity() {
        return -1;
    }

    @NotNull MqlValue call(@NotNull List<MqlExpr> args, @Nullable MqlScope scope);

}
