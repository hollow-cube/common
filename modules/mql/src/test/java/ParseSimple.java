import net.hollowcube.mql.parser.MqlParser;
import net.hollowcube.mql.runtime.MqlScopeImpl;
import net.hollowcube.mql.runtime.MqlScriptScope;
import net.hollowcube.mql.value.MqlNumberValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParseSimple {
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
}
