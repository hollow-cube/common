package net.hollowcube.mql.jit;

public interface QueryScript {
    double evaluate(@MqlEnv({"query", "q"}) QueryTest query);
}
