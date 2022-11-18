package net.hollowcube.mql.runtime;

import net.hollowcube.mql.value.MqlHolder;
import net.hollowcube.mql.value.MqlValue;
import org.jetbrains.annotations.NotNull;

public interface MqlScope extends MqlHolder {

    MqlScope EMPTY = unused -> MqlValue.NULL;

    @NotNull MqlValue get(@NotNull String name);

    interface Mutable extends MqlScope {

        void set(@NotNull String name, @NotNull MqlValue value);

    }

}
