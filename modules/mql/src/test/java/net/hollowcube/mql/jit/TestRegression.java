package net.hollowcube.mql.jit;

import net.hollowcube.mql.foreign.Query;
import net.hollowcube.mql.parser.MqlParser;
import net.hollowcube.mql.util.MqlPrinter;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestRegression {

    @Test
    public void clampParse() {
        parse("math.clamp(1, 10, 20)", "(? (. math clamp) (1.0 10.0 20.0))");
    }

    @Test
    public void mathAndClampParse() {
        parse("1 +  math.clamp(1, 10, 20)", "(+ 1.0 (? (. math clamp) (1.0 10.0 20.0)))");
    }

    @Test
    public void mathAndClampCompile() {
        compile(BaseScript.class, "1 + 1 + math.clamp(1, 10, 20)", """
                DCONST_1
                DCONST_1
                DADD
                DCONST_1
                LDC 10.0
                LDC 20.0
                INVOKESTATIC net/hollowcube/mql/runtime/MqlMath.clamp (DDD)D
                DADD
                DRETURN
                """);
    }

    @Test
    public void mathAndClampExec() {
        var script = execute(BaseScript.class, "1 + 1 + math.clamp(1, 10, 20)");
        assertEquals(12, script.evaluate());
    }

    public static class TestClass {
        @Query
        public double variable() {
            return 12;
        }
    }

    @FunctionalInterface
    public interface TestScript {
        double evaluate(@MqlEnv({"variable", "v"}) TestClass emitter);
    }

    @Test
    public void variableVariableParse() {
        parse("variable.variable", "(. variable variable)");
    }

    @Test
    public void variableVariable() {
        var script = execute(TestScript.class, "variable.variable");
        assertEquals(12, script.evaluate(new TestClass()));
    }

    @Test
    public void sameScriptTwice() {
        var script = execute(TestScript.class, "variable.variable + 1");
        assertEquals(13, script.evaluate(new TestClass()));
        script = execute(TestScript.class, "variable.variable + 1");
        assertEquals(13, script.evaluate(new TestClass()));
    }


    private void parse(String input, String expected) {
        var expr = new MqlParser(input).parse();
        var actual = new MqlPrinter().visit(expr, null);

        assertEquals(expected, actual);
    }

    private void compile(@NotNull Class<?> script, @NotNull String source, @NotNull String expected) {
        var compiler = new MqlCompiler<>(script);
        byte[] bytecode = compiler.compileBytecode("mql$test", source);

        var str = AsmUtil.prettyPrintEvalMethod(bytecode);
        assertEquals(expected, str);
    }

    private <T> T execute(@NotNull Class<T> scriptInterface, @NotNull String source) {
        var compiler = new MqlCompiler<>(scriptInterface);
        Class<T> scriptClass = compiler.compile(source);

        try {
            return scriptClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}