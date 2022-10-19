package net.hollowcube.mql.jit;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestExecution {

    @Test
    public void singleNumber() {
        var script = compile(BaseScript.class, "0");
        assertEquals(0, script.evaluate());
    }

    @Test
    public void simpleAddition() {
        var script = compile(BaseScript.class, "1+1");
        assertEquals(2, script.evaluate());
    }

    @Test
    public void simpleQueryCall() {
        var script = compile(QueryScript.class, "q.dbl(2.0)");
        assertEquals(4, script.evaluate(new QueryTest()));
    }

    private <T> T compile(@NotNull Class<T> scriptInterface, @NotNull String source) {
        var compiler = new MqlCompiler<>(scriptInterface);
        Class<T> scriptClass = compiler.compile(source);

        try {
            return scriptClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}