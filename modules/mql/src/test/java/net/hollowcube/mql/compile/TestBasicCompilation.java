package net.hollowcube.mql.compile;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

import java.io.PrintWriter;
import java.io.StringWriter;

public class TestBasicCompilation {

    @Test
    public void experiment() throws Exception {
        var compiler = new MqlCompiler<>(Object.class, Object.class);
        byte[] bytecode = compiler.compileBytecode("1 + 2");

        var str = prettyPrintEvalMethod(bytecode);
        System.out.println(str);

//        var cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
//
//        var visitor = new InstrumentationClassVisitor(Opcodes.ASM9, cw);
//        visitor.visitSource("TestBasicCompilation.java", null);
//
//        var visitor = new AsmUtil.StringClassVisitor();
//        var ctx = new Context(TestEntityQuery.class, Object.class, visitor);
//
//        ctx.begin();
//        var expr = net.hollowcube.mql.MqlScript.parse("q.is_alive").expr();
//        new MqlCompilerOld().visit(expr, ctx);
//        byte[] classData = ctx.end();
//
//        System.out.println(visitor.bytecode());
//
//        Class<MqlScript<TestEntityQuery, Object>> c = AsmUtil.loadClass("mql$todo_add_better_name", classData);
//        var script = c.newInstance();
//        System.out.println("result: " + script.evaluate(new TestEntityQuery(), new Object()));
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
                str.append(sw);
            }
        }

        return str.toString();
    }
}