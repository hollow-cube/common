package net.hollowcube.mql.jit;

import net.hollowcube.mql.compile.MqlScript;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.TraceClassVisitor;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class AsmUtil {
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
    public static Class<?> loadClass(String className, byte[] b) {
        // Override defineClass (as it is protected) and define the class.
        Class<?> clazz = null;
        try {
            ClassLoader loader = ClassLoader.getSystemClassLoader();
            Class<?> cls = (Class<?>) Class.forName("java.lang.ClassLoader");
            java.lang.reflect.Method method =
                    cls.getDeclaredMethod(
                            "defineClass",
                            new Class[] { String.class, byte[].class, int.class, int.class });

            // Protected method invocation.
            method.setAccessible(true);
            try {
                Object[] args =
                        new Object[] { className, b, 0, b.length};
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


}
