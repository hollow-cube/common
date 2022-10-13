package net.hollowcube.mql.compile;

import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintWriter;
import java.io.StringWriter;

final class AsmUtil {
    private AsmUtil() {}

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

    public static @NotNull String toName(@NotNull Class<?> clazz) {
        return clazz.getName().replace(".", "/");
    }

    @SuppressWarnings("all")
    public static <_Query, _Context> Class<MqlScript<_Query, _Context>> loadClass(String className, byte[] b) {
        // Override defineClass (as it is protected) and define the class.
        Class<MqlScript<_Query, _Context>> clazz = null;
        try {
            ClassLoader loader = ClassLoader.getSystemClassLoader();
            Class<MqlScript<_Query, _Context>> cls = (Class<MqlScript<_Query, _Context>>) Class.forName("java.lang.ClassLoader");
            java.lang.reflect.Method method =
                    cls.getDeclaredMethod(
                            "defineClass",
                            new Class[] { String.class, byte[].class, int.class, int.class });

            // Protected method invocation.
            method.setAccessible(true);
            try {
                Object[] args =
                        new Object[] { className, b, 0, b.length};
                clazz = (Class<MqlScript<_Query, _Context>>) method.invoke(loader, args);
            } finally {
                method.setAccessible(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return clazz;
    }

    public static class StringClassVisitor extends ClassVisitor {
        private final ClassWriter cw;
        private final StringWriter sw;

        public StringClassVisitor() {
            this(new ClassWriter(ClassWriter.COMPUTE_MAXS), new StringWriter());
        }

        public StringClassVisitor(ClassWriter cw) {
            this(cw, new StringWriter());
        }

        private StringClassVisitor(ClassWriter cw, StringWriter sw) {
            super(Opcodes.ASM9, new TraceClassVisitor(cw, new PrintWriter(sw)));
            this.cw = cw;
            this.sw = sw;
        }

        public ClassWriter classWriter() {
            return cw;
        }

        public String bytecode() {
            return sw.toString();
        }
    }

}
