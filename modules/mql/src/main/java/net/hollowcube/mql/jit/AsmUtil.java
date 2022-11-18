package net.hollowcube.mql.jit;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

public final class AsmUtil {
    private AsmUtil() {
    }

    public static @NotNull String toDescriptor(@NotNull Class<?> clazz) {
        if (boolean.class.equals(clazz)) {
            return "Z";
        } else if (byte.class.equals(clazz)) {
            return "B";
        } else if (char.class.equals(clazz)) {
            return "C";
        } else if (short.class.equals(clazz)) {
            return "S";
        } else if (int.class.equals(clazz)) {
            return "I";
        } else if (long.class.equals(clazz)) {
            return "J";
        } else if (float.class.equals(clazz)) {
            return "F";
        } else if (double.class.equals(clazz)) {
            return "D";
        } else if (void.class.equals(clazz)) {
            return "V";
        }
        return "L" + toName(clazz) + ";";
    }

    public static @NotNull String toDescriptor(@NotNull Method method) {
        var sb = new StringBuilder("(");
        for (var param : method.getParameterTypes()) {
            sb.append(toDescriptor(param));
        }
        sb.append(")");
        sb.append(toDescriptor(method.getReturnType()));
        return sb.toString();
    }

    public static @NotNull String toName(@NotNull Class<?> clazz) {
        return clazz.getName().replace(".", "/");
    }

    public static void convert(Class<?> to, Class<?> from, MethodVisitor mv) {
        if (to.isAssignableFrom(from)) {
            return;
        }
        //todo handle other conversions
        throw new UnsupportedOperationException("Cannot convert from " + from + " to " + to);
    }

    @SuppressWarnings("all")
    public static Class<?> loadClass(String className, byte[] b) {
        // Override defineClass (as it is protected) and define the class.
        Class<?> clazz = null;
        try {
            ClassLoader loader = ClassLoader.getSystemClassLoader();
            Class<?> cls = (Class<?>) Class.forName("java.lang.ClassLoader");
            java.lang.reflect.Method method =
                    cls.getDeclaredMethod(
                            "defineClass",
                            new Class[]{String.class, byte[].class, int.class, int.class});

            // Protected method invocation.
            method.setAccessible(true);
            try {
                Object[] args =
                        new Object[]{className, b, 0, b.length};
                clazz = (Class<?>) method.invoke(loader, args);
            } finally {
                method.setAccessible(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return clazz;
    }

    public static @NotNull String prettyPrintEvalMethod(byte[] bytecode) {
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
