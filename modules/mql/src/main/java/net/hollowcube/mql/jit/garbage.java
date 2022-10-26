package net.hollowcube.mql.jit;

import net.hollowcube.mql.foreign.Query;

public class garbage {


    public static void main(String[] args) throws Exception {

        class MyQueryClass {
            private double age;

            @Query
            public double emitter_age() {
                return age;
            }
        }

        var compiler = new MqlCompiler<>(MqlQueryScript.class, MyQueryClass.class);
        var scriptClass = compiler.compile("q.");

        var script = scriptClass.newInstance();
        var result = script.evaluate(new MyQueryClass());
        System.out.println(result);

    }
}
