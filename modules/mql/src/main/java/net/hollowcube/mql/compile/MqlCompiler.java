package net.hollowcube.mql.compile;

import net.hollowcube.mql.parser.MqlParser;
import net.hollowcube.mql.tree.*;
import net.hollowcube.mql.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.util.HashMap;
import java.util.Map;

import static org.objectweb.asm.Opcodes.*;

/**
 * A compiler for MQL scripts.
 * <p>
 * Some reflection calls are cached, so this object should be reused as much as possible.
 * <p>
 * Note: This class is not thread-safe, and must be synchronized externally.
 */
public final class MqlCompiler<_Query, _Context> {
    private final ClassInfo queryClass;

    private final ClassInfo contextClass;

    public MqlCompiler(Class<_Query> queryClass, Class<_Context> contextClass) {
        this.queryClass = new ClassInfo(queryClass);
        this.contextClass = new ClassInfo(contextClass);
    }

    // Public API

    public @NotNull MqlScript<_Query, _Context> compile(@NotNull String source) {
        throw new RuntimeException("not implemented");
    }

    @TestOnly
    public byte[] compileBytecode(@NotNull String source) {
        String sourceHash = Integer.toHexString(source.hashCode());
        // Could cache based on the hash if compiling many times over is a valid use case, but I don't think it is.

        // Parse to an expression tree
        MqlExpr expr = new MqlParser(source).parse();

        // Create the class
        String className = "mql$" + sourceHash;
        ClassWriter scriptClass = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        var sig = String.format("L%s<%s%s>;", getClassName(MqlScript.class), queryClass.descriptor(), contextClass.descriptor());
        scriptClass.visit(V17, ACC_PUBLIC | ACC_FINAL | ACC_SYNTHETIC, className, sig, getClassName(Object.class), new String[]{AsmUtil.toName(MqlScript.class)});

        // Generate required synthetics
        generateSynthetics(className, scriptClass);

        // Generate evaluate method from input expression
        MethodVisitor evalMethod = scriptClass.visitMethod(ACC_PUBLIC, "evaluate", getEvalFnDescriptor(), null, null);
        evalMethod.visitCode();
        new BytecodeGeneratingVisitor(className, evalMethod).visit(expr, null);
        evalMethod.visitInsn(DRETURN); // ok because all expressions leave a double on the stack
        evalMethod.visitMaxs(0, 0);
        evalMethod.visitEnd();

        // Finish the class and return it
        scriptClass.visitEnd();

        return scriptClass.toByteArray();
    }

    // Internal helpers

    private @NotNull String getEvalFnDescriptor() {
        return String.format("(%s%s)D", queryClass.descriptor(), contextClass.descriptor());
    }

    private void generateSynthetics(@NotNull String className, @NotNull ClassVisitor scriptClass) {
        // Add empty constructor
        var mv = scriptClass.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();

        // Insert bridge method for evaluate
        mv = scriptClass.visitMethod(ACC_PUBLIC | ACC_SYNTHETIC | ACC_BRIDGE, "evaluate", "(Ljava/lang/Object;Ljava/lang/Object;)D", null, null);
        mv.visitCode();

        mv.visitVarInsn(ALOAD, 0); // this
        mv.visitVarInsn(ALOAD, 1); // query
        mv.visitTypeInsn(CHECKCAST, queryClass.name());
        mv.visitVarInsn(ALOAD, 2); // context
        mv.visitTypeInsn(CHECKCAST, contextClass.name());
        mv.visitMethodInsn(INVOKEVIRTUAL, className, "evaluate", getEvalFnDescriptor(), false);
        mv.visitInsn(DRETURN);

        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    // Utils

    /**
     * Ast visitor that generates bytecode for the given expression in the given method.
     * <p>
     * Note: Every expression _must_ leave a double on the stack. If the expression has no result, it should leave 0.
     */
    private class BytecodeGeneratingVisitor implements MqlVisitor<Void, Void> {
        private final String className;
        private final MethodVisitor method;

        private BytecodeGeneratingVisitor(@NotNull String className, @NotNull MethodVisitor method) {
            this.className = className;
            this.method = method;
        }

        @Override
        public Void visitBinaryExpr(@NotNull MqlBinaryExpr expr, Void unused) {
            // Visit both sides, resulting in two doubles on the stack
            visit(expr.lhs(), null);
            visit(expr.rhs(), null);

            // Perform the operation
            switch (expr.operator()) {
                case PLUS -> method.visitInsn(DADD);
                case MINUS -> method.visitInsn(DSUB);
                case MUL -> method.visitInsn(DMUL);
                case DIV -> method.visitInsn(DDIV);
            }

            return null;
        }

        @Override
        public Void visitAccessExpr(@NotNull MqlAccessExpr expr, Void unused) {
            if (expr.lhs() instanceof MqlIdentExpr ident) {
                if (!"q".equals(ident.value())) {
                    throw new UnsupportedOperationException("Only q is supported as a query object");
                }

                var methodName = StringUtil.snakeCaseToCamelCase(expr.target());
                var methodInfo = queryClass.getMethod(methodName);

                method.visitVarInsn(ALOAD, 1); // query parameter
                method.visitMethodInsn(INVOKEVIRTUAL, queryClass.name(), methodName, methodInfo.descriptor(), false);

                if (methodInfo.returnType() == boolean.class)
            }
            return null;
        }

        @Override
        public Void visitNumberExpr(@NotNull MqlNumberExpr expr, Void unused) {
            double value = expr.value().value();
            if (value == 0) {
                method.visitInsn(DCONST_0);
            } else if (value == 1) {
                method.visitInsn(DCONST_1);
            } else {
                method.visitLdcInsn(value);
            }
            return null;
        }

        @Override
        public Void visitRefExpr(@NotNull MqlIdentExpr expr, Void unused) {
            throw new UnsupportedOperationException();
        }

    }

    private static class ClassInfo {
        private final Class<?> clazz;
        private final Map<String, MethodInfo> methods = new HashMap<>();

        public ClassInfo(@NotNull Class<?> clazz) {
            this.clazz = clazz;
        }

        public @NotNull String name() {
            return getClassName(clazz);
        }

        public @NotNull String descriptor() {
            return "L" + name() + ";";
        }

        public MethodInfo getMethod(@NotNull String name) {
            return methods.computeIfAbsent(name, n -> {
                for (var candidate : clazz.getMethods()) {
                    if (candidate.getName().equals(n)) {
                        return new MethodInfo(candidate.getName(), candidate.getReturnType(), candidate.getParameterTypes());
                    }
                }
                throw new IllegalArgumentException("No such method: " + n);
            });
        }
    }

    private record MethodInfo(
            String name,
            Class<?> returnType,
            Class<?>[] parameterTypes
    ) {

        public @NotNull String descriptor() {
            var sb = new StringBuilder("(");
            for (var param : parameterTypes)
                sb.append(AsmUtil.toDescriptor(param));
            sb.append(')');
            sb.append(AsmUtil.toDescriptor(returnType));
            return sb.toString();
        }

    }

    private static @NotNull String getClassName(@NotNull Class<?> clazz) {
        return clazz.getName().replace(".", "/");
    }
}
