package net.hollowcube.mql.compile;

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


public class TestBasicCompilation {

    @Test
    public void singleNumber() {
        check("0", """
                DCONST_0
                DRETURN
                """);
        check("1", """
                DCONST_1
                DRETURN
                """);
        check("1.234", """
                LDC 1.234
                DRETURN
                """);
    }

    @Test
    public void simpleAddition() {
        check("1 + 1", """
                DCONST_1
                DCONST_1
                DADD
                DRETURN
                """);
    }

    private void check(@NotNull String source, @NotNull String expected) {
        var compiler = new MqlCompiler<>(Object.class, Object.class);
        byte[] bytecode = compiler.compileBytecode(source);

        var str = prettyPrintEvalMethod(bytecode);
        assertEquals(expected, str);
    }

    private static @NotNull String prettyPrintEvalMethod(byte[] bytecode) {
        var str = new StringBuilder();
        var printer = new Textifier();
        var mp = new TraceMethodVisitor(printer);

        var cr = new ClassReader(bytecode);
        var cn = new ClassNode();
        cr.accept(cn, 0);
        for (var method : cn.methods) {
            // Ignore any method that isnt evaluate and not a bridge (we want the concrete impl)
            if (!"evaluate".equals(method.name) || (method.access & Opcodes.ACC_BRIDGE) != 0)
                continue;
            InsnList insns = method.instructions;
            for (var insn : insns) {
                insn.accept(mp);
                StringWriter sw = new StringWriter();
                printer.print(new PrintWriter(sw));
                printer.getText().clear();
                str.append(sw.toString().trim()).append("\n");
            }
        }

        return str.toString();
    }
}