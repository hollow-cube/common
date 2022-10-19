package net.hollowcube.mql;

import net.hollowcube.mql.parser.MqlParser;
import net.hollowcube.mql.runtime.MqlRuntimeError;
import net.hollowcube.mql.runtime.MqlScope;
import net.hollowcube.mql.runtime.MqlScopeImpl;
import net.hollowcube.mql.runtime.MqlScriptScope;
import net.hollowcube.mql.value.MqlHolder;
import net.hollowcube.mql.value.MqlNumberValue;
import net.hollowcube.mql.value.MqlValue;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestMql {
    // This is a weird test case

    @Test
    public void basicQueryCall() {
        var source = "q.is_alive";
        var expr = new MqlParser(source).parse();

        var scope = new MqlScope() {
            @Override
            public @NotNull MqlValue get(@NotNull String name) {
                if (!name.equals("q") && !name.equals("query"))
                    throw new MqlRuntimeError("unknown environment object: " + name);
                return (MqlHolder) queryFunction -> switch (queryFunction) {
                    case "is_alive" -> new MqlNumberValue(1);
                    default -> throw new MqlRuntimeError("no such query function: " + queryFunction);
                };
            }
        };
        var result = expr.evaluate(scope);
        assertTrue(result instanceof MqlNumberValue);
        assertEquals(1, ((MqlNumberValue) result).value());
    }

    @Test
    public void testAdd() {
        var source = "10 + 10 + 10";

        var expr = new MqlParser(source).parse();
        var scopeImpl = new MqlScopeImpl();
        var vars = new MqlScopeImpl.Mutable();

        var scope = new MqlScriptScope(scopeImpl, vars, scopeImpl);
        var result = expr.evaluate(scope);

        assertTrue(result instanceof MqlNumberValue);
        assertEquals(30, ((MqlNumberValue) result).value());
    }

    @Test
    public void testSub() {
        var source = "10 - 10 - 10 - 10";

        var expr = new MqlParser(source).parse();
        var scopeImpl = new MqlScopeImpl();
        var vars = new MqlScopeImpl.Mutable();

        var scope = new MqlScriptScope(scopeImpl, vars, scopeImpl);
        var result = expr.evaluate(scope);

        assertTrue(result instanceof MqlNumberValue);
        assertEquals(-20, ((MqlNumberValue) result).value());
    }

    @Test
    public void testMul() {
        var source = "10 * 10 * 10 * 10";

        var expr = new MqlParser(source).parse();
        var scopeImpl = new MqlScopeImpl();
        var vars = new MqlScopeImpl.Mutable();

        var scope = new MqlScriptScope(scopeImpl, vars, scopeImpl);
        var result = expr.evaluate(scope);

        assertTrue(result instanceof MqlNumberValue);
        assertEquals(10 * 10 * 10 * 10, ((MqlNumberValue) result).value());
    }

    @Test
    public void testDiv() {
        var source = "10000 / 10 / 10 / 10";

        var expr = new MqlParser(source).parse();
        var scopeImpl = new MqlScopeImpl();
        var vars = new MqlScopeImpl.Mutable();

        var scope = new MqlScriptScope(scopeImpl, vars, scopeImpl);
        var result = expr.evaluate(scope);

        assertTrue(result instanceof MqlNumberValue);
        assertEquals(10000 / 10 / 10 / 10, ((MqlNumberValue) result).value());
    }

    @Test
    public void testMixed() {
        var source = "10 + 3 * 3 - 1 * 5";

        var expr = new MqlParser(source).parse();
        var scopeImpl = new MqlScopeImpl();
        var vars = new MqlScopeImpl.Mutable();

        var scope = new MqlScriptScope(scopeImpl, vars, scopeImpl);
        var result = expr.evaluate(scope);

        assertTrue(result instanceof MqlNumberValue);
        assertEquals(14, ((MqlNumberValue) result).value());
    }

    @Test
    public void testBracketRecursive() {
        var source = "((((1))))";

        var expr = new MqlParser(source).parse();

        var scopeImpl = new MqlScopeImpl();
        var vars = new MqlScopeImpl.Mutable();

        var scope = new MqlScriptScope(scopeImpl, vars, scopeImpl);
        var result = expr.evaluate(scope);

        assertTrue(result instanceof MqlNumberValue);
        assertEquals(1, ((MqlNumberValue) result).value());
    }

    @Test
    public void testBrackets() {
        var source = "(10 + 3) * (3 - 1) - 5 + (1 - 10)";

        var expr = new MqlParser(source).parse();

        var scopeImpl = new MqlScopeImpl();
        var vars = new MqlScopeImpl.Mutable();

        var scope = new MqlScriptScope(scopeImpl, vars, scopeImpl);
        var result = expr.evaluate(scope);

        assertTrue(result instanceof MqlNumberValue);
        assertEquals((10 + 3) * (3 - 1) - 5 + (1 - 10), ((MqlNumberValue) result).value());
    }

    @Test
    public void testBracketsNested() {
        var source = "((10 + (3)) * (3 - 1)) - 5 + ((2 * 3) - 10) * (10 + (10 * (3 + 3)))";

        var expr = new MqlParser(source).parse();

        var scopeImpl = new MqlScopeImpl();
        var vars = new MqlScopeImpl.Mutable();

        var scope = new MqlScriptScope(scopeImpl, vars, scopeImpl);
        var result = expr.evaluate(scope);

        assertTrue(result instanceof MqlNumberValue);
        assertEquals(((10 + (3)) * (3 - 1)) - 5 + ((2 * 3) - 10) * (10 + (10 * (3 + 3))), ((MqlNumberValue) result).value());
    }

    @Test
    public void testParams() {
        var source = "math.pow(10, 2)";

        var expr = new MqlParser(source).parse();

        var scopeImpl = new MqlScopeImpl();
        var vars = new MqlScopeImpl.Mutable();

        var scope = new MqlScriptScope(scopeImpl, vars, scopeImpl);
        var result = expr.evaluate(scope);

        assertTrue(result instanceof MqlNumberValue);
        assertEquals(10 * 10, ((MqlNumberValue) result).value());
    }

    @Test
    public void testParams2() {
        var source = "math.sqrt(4)";

        var expr = new MqlParser(source).parse();

        var scopeImpl = new MqlScopeImpl();
        var vars = new MqlScopeImpl.Mutable();

        var scope = new MqlScriptScope(scopeImpl, vars, scopeImpl);
        var result = expr.evaluate(scope);

        assertTrue(result instanceof MqlNumberValue);
        assertEquals(2, ((MqlNumberValue) result).value());
    }

    @Test
    public void testParamsExtended() {
        var source = "math.pow(10, 2) + 1";

        var expr = new MqlParser(source).parse();

        var scopeImpl = new MqlScopeImpl();
        var vars = new MqlScopeImpl.Mutable();

        var scope = new MqlScriptScope(scopeImpl, vars, scopeImpl);
        var result = expr.evaluate(scope);

        assertTrue(result instanceof MqlNumberValue);
        assertEquals(10 * 10 + 1, ((MqlNumberValue) result).value());
    }

    @Test
    public void testParamsExtended2() {
        var source = "math.pow(10 + 1, 2) + 1";

        var expr = new MqlParser(source).parse();

        var scopeImpl = new MqlScopeImpl();
        var vars = new MqlScopeImpl.Mutable();

        var scope = new MqlScriptScope(scopeImpl, vars, scopeImpl);
        var result = expr.evaluate(scope);

        assertTrue(result instanceof MqlNumberValue);
        assertEquals(11 * 11 + 1, ((MqlNumberValue) result).value());
    }

    @Test
    public void testParamsNested() {
        var source = "math.pow(math.pow(2 + 1, 3), math.pow(3 , 1)) + 1";

        var expr = new MqlParser(source).parse();

        var scopeImpl = new MqlScopeImpl();
        var vars = new MqlScopeImpl.Mutable();

        var scope = new MqlScriptScope(scopeImpl, vars, scopeImpl);
        var result = expr.evaluate(scope);

        assertTrue(result instanceof MqlNumberValue);
        assertEquals(19684, ((MqlNumberValue) result).value());
    }

    @Test
    public void testPI() {
        var source = "math.pi + 1";

        var expr = new MqlParser(source).parse();
        var scopeImpl = new MqlScopeImpl();
        var vars = new MqlScopeImpl.Mutable();

        var scope = new MqlScriptScope(scopeImpl, vars, scopeImpl);

        var result = expr.evaluate(scope);

        assertTrue(result instanceof MqlNumberValue);
        assertEquals(Math.PI + 1, ((MqlNumberValue) result).value());
    }

    @Test
    public void testEqTrue() {
        var source = "1 == 1";

        var expr = new MqlParser(source).parse();
        var scopeImpl = new MqlScopeImpl();
        var vars = new MqlScopeImpl.Mutable();

        var scope = new MqlScriptScope(scopeImpl, vars, scopeImpl);

        var result = expr.evaluate(scope);

        assertTrue(result instanceof MqlNumberValue);
        assertEquals(1, ((MqlNumberValue) result).value());
    }

    @Test
    public void testEqFalse() {
        var source = "1 == 2";

        var expr = new MqlParser(source).parse();

        var scopeImpl = new MqlScopeImpl();
        var vars = new MqlScopeImpl.Mutable();

        var scope = new MqlScriptScope(scopeImpl, vars, scopeImpl);

        var result = expr.evaluate(scope);

        assertTrue(result instanceof MqlNumberValue);
        assertEquals(0, ((MqlNumberValue) result).value());
    }

    @Test
    public void testGteTrue() {
        var source = "1 >= 1";

        var expr = new MqlParser(source).parse();
        var scopeImpl = new MqlScopeImpl();
        var vars = new MqlScopeImpl.Mutable();

        var scope = new MqlScriptScope(scopeImpl, vars, scopeImpl);

        var result = expr.evaluate(scope);

        assertTrue(result instanceof MqlNumberValue);
        assertEquals(1, ((MqlNumberValue) result).value());
    }

    @Test
    public void testGteFalse() {
        var source = "1 >= 2";

        var expr = new MqlParser(source).parse();
        var scopeImpl = new MqlScopeImpl();
        var vars = new MqlScopeImpl.Mutable();

        var scope = new MqlScriptScope(scopeImpl, vars, scopeImpl);

        var result = expr.evaluate(scope);

        assertTrue(result instanceof MqlNumberValue);
        assertEquals(0, ((MqlNumberValue) result).value());
    }

    @Test
    public void testGtTrue() {
        var source = "2 > 1";

        var expr = new MqlParser(source).parse();
        var scopeImpl = new MqlScopeImpl();
        var vars = new MqlScopeImpl.Mutable();

        var scope = new MqlScriptScope(scopeImpl, vars, scopeImpl);

        var result = expr.evaluate(scope);

        assertTrue(result instanceof MqlNumberValue);
        assertEquals(1, ((MqlNumberValue) result).value());
    }

    @Test
    public void testGtFalse() {
        var source = "1 > 1";

        var expr = new MqlParser(source).parse();
        var scopeImpl = new MqlScopeImpl();
        var vars = new MqlScopeImpl.Mutable();

        var scope = new MqlScriptScope(scopeImpl, vars, scopeImpl);

        var result = expr.evaluate(scope);

        assertTrue(result instanceof MqlNumberValue);
        assertEquals(0, ((MqlNumberValue) result).value());
    }

    @Test
    public void testLteTrue() {
        var source = "1 <= 1";

        var expr = new MqlParser(source).parse();
        var scopeImpl = new MqlScopeImpl();
        var vars = new MqlScopeImpl.Mutable();

        var scope = new MqlScriptScope(scopeImpl, vars, scopeImpl);

        var result = expr.evaluate(scope);

        assertTrue(result instanceof MqlNumberValue);
        assertEquals(1, ((MqlNumberValue) result).value());
    }

    @Test
    public void testLteFalse() {
        var source = "2 <= 1";

        var expr = new MqlParser(source).parse();
        var scopeImpl = new MqlScopeImpl();
        var vars = new MqlScopeImpl.Mutable();

        var scope = new MqlScriptScope(scopeImpl, vars, scopeImpl);

        var result = expr.evaluate(scope);

        assertTrue(result instanceof MqlNumberValue);
        assertEquals(0, ((MqlNumberValue) result).value());
    }

    @Test
    public void testLtTrue() {
        var source = "1 < 2";

        var expr = new MqlParser(source).parse();
        var scopeImpl = new MqlScopeImpl();
        var vars = new MqlScopeImpl.Mutable();

        var scope = new MqlScriptScope(scopeImpl, vars, scopeImpl);

        var result = expr.evaluate(scope);

        assertTrue(result instanceof MqlNumberValue);
        assertEquals(1, ((MqlNumberValue) result).value());
    }

    @Test
    public void testLtFalse() {
        var source = "1 < 1";

        var expr = new MqlParser(source).parse();
        var scopeImpl = new MqlScopeImpl();
        var vars = new MqlScopeImpl.Mutable();

        var scope = new MqlScriptScope(scopeImpl, vars, scopeImpl);

        var result = expr.evaluate(scope);

        assertTrue(result instanceof MqlNumberValue);
        assertEquals(0, ((MqlNumberValue) result).value());
    }

    @Test
    public void testNeqTrue() {
        var source = "1 != 2";

        var expr = new MqlParser(source).parse();
        var scopeImpl = new MqlScopeImpl();
        var vars = new MqlScopeImpl.Mutable();

        var scope = new MqlScriptScope(scopeImpl, vars, scopeImpl);

        var result = expr.evaluate(scope);

        assertTrue(result instanceof MqlNumberValue);
        assertEquals(1, ((MqlNumberValue) result).value());
    }

    @Test
    public void testNeqFalse() {
        var source = "1 != 1";

        var expr = new MqlParser(source).parse();
        var scopeImpl = new MqlScopeImpl();
        var vars = new MqlScopeImpl.Mutable();

        var scope = new MqlScriptScope(scopeImpl, vars, scopeImpl);

        var result = expr.evaluate(scope);

        assertTrue(result instanceof MqlNumberValue);
        assertEquals(0, ((MqlNumberValue) result).value());
    }
}
