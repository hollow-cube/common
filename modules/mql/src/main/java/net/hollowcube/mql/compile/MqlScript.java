package net.hollowcube.mql.compile;

import org.jetbrains.annotations.NotNull;

public interface MqlScript<_Query, _Context> {

    MqlScript<Object, Object> EMPTY = (unused1, unused2) -> 1.0;

    double evaluate(@NotNull _Query query, @NotNull _Context context);


    //todo perhaps should be MqlScript is the factory, and MqlScriptInstance is the actual script instance or something
    interface Factory<_Query, _Context> {
        @NotNull MqlScript<_Query, _Context> create();
    }

}
