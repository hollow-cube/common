package net.hollowcube.mql.compile;

import org.jetbrains.annotations.NotNull;

public interface MqlScript<_Query, _Context> {

    double evaluate(@NotNull _Query query, @NotNull _Context context);

}
