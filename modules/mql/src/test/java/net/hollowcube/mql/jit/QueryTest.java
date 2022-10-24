package net.hollowcube.mql.jit;

import net.hollowcube.mql.foreign.Query;

public class QueryTest {

    @Query
    public double dbl(double value) {
        return value * 2;
    }

    @Query
    public double mul(double a, double b) {
        return a * b;
    }
}
