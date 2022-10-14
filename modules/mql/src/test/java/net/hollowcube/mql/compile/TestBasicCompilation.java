package net.hollowcube.mql.compile;

import org.junit.jupiter.api.Test;

public class TestBasicCompilation {

    @Test
    public void experiment() throws Exception {
        var visitor = new AsmUtil.StringClassVisitor();
        var ctx = new Context(TestEntityQuery.class, Object.class, visitor);

        ctx.begin();
        var expr = net.hollowcube.mql.MqlScript.parse("q.is_alive").expr();
        new MqlCompilerOld().visit(expr, ctx);
        byte[] classData = ctx.end();

        System.out.println(visitor.bytecode());

        Class<MqlScript<TestEntityQuery, Object>> c = AsmUtil.loadClass("mql$todo_add_better_name", classData);
        var script = c.newInstance();
        System.out.println("result: " + script.evaluate(new TestEntityQuery(), new Object()));
    }

}
