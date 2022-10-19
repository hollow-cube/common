package net.hollowcube.mql.compile;

import net.hollowcube.mql.jit.AsmUtil;
import net.hollowcube.mql.jit.MqlCompilerV2;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class TestExecutionV2 {

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

//    @Test
//    public void callQueryNoArgs() {
//        class TestQuery {
//            @Query
//            public double testValue() {
//                return 1.234;
//            }
//        }
//
//        var script = "q.test_value";
//        var expected = """
//                ALOAD 1
//                INVOKEVIRTUAL net/hollowcube/mql/compile/TestBasicCompilation$1TestQuery.testValue ()D
//                DRETURN
//                """;
//        check(TestQuery.class, Object.class, script, expected);
//    }
//
//    @Test
//    public void callQueryReturnTypeCoercion() {
//        class TestQuery {
//            @Query
//            public boolean testValue() {
//                return true;
//            }
//        }
//
//        var script = "q.test_value";
//        var expected = """
//                ALOAD 1
//                INVOKEVIRTUAL net/hollowcube/mql/compile/TestBasicCompilation$2TestQuery.testValue ()Z
//                INVOKESTATIC net/hollowcube/mql/compile/MqlRuntime.boolToDouble (Z)D
//                DRETURN
//                """;
//        check(TestQuery.class, Object.class, script, expected);
//    }

    private <T> T compile(@NotNull Class<T> scriptInterface, @NotNull String source) {
        var compiler = new MqlCompilerV2<>(scriptInterface);
        Class<T> scriptClass = compiler.compile(source);

        try {
            return scriptClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}