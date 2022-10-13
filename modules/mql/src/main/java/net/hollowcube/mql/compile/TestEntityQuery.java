package net.hollowcube.mql.compile;

import net.hollowcube.mql.foreign.Query;

public class TestEntityQuery {

    @Query
    public double is_alive() {
        System.out.println("IM STILL ALIVE");
        return 1.0;
    }

}
