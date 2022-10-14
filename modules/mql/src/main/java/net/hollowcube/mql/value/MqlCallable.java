package net.hollowcube.mql.value;

import net.hollowcube.mql.runtime.MqlScope;
import net.hollowcube.mql.tree.MqlArgListExpr;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public non-sealed interface MqlCallable extends MqlValue {

    /** Returns the arity of the function, or -1 if it is variadic/otherwise unknown */
    default int arity() { return -1; }

    @NotNull MqlValue call(@NotNull MqlArgListExpr argListExpr, MqlScope scope);

}
