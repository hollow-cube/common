package net.hollowcube.mql.compile;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

final class Context {
    private final Class<?> queryClass; // Class mapped to `query`
    private final Class<?> contextClass; // Class mapped to `context`

    private final String className;
    private final ClassVisitor scriptClass;
    private MethodVisitor evalMethod;

    public Context(@NotNull Class<?> queryClass, @Nullable Class<?> contextClass) {
        this(queryClass, contextClass, new ClassWriter(ClassWriter.COMPUTE_MAXS));
    }

    public Context(@NotNull Class<?> queryClass, @Nullable Class<?> contextClass, @NotNull ClassVisitor builder) {
        this.queryClass = queryClass;
        this.contextClass = contextClass == null ? Void.class : contextClass;
        this.className = "mql$todo_add_better_name";
        this.scriptClass = builder;
    }

    public void begin() {
        var sig = String.format("L%s<%s%s>;", AsmUtil.toName(MqlScript.class), AsmUtil.toDescriptor(queryClass), AsmUtil.toDescriptor(contextClass));
        scriptClass.visit(V17, ACC_PUBLIC | ACC_FINAL, className, sig, "java/lang/Object", new String[]{AsmUtil.toName(MqlScript.class)});

        generateSynthetics();

        //todo if for any reason queryClass or contextClass has a generic parameter then this will fail because `signature` needs to be provided. Either can make this more robust or just disallow generic parameters on queryClass and contextClass.
        evalMethod = scriptClass.visitMethod(ACC_PUBLIC, "evaluate", evaluateDescriptor(), null, null);
        evalMethod.visitCode();
    }

    public byte[] end() {
        evalMethod.visitInsn(DRETURN); // ok because all expressions leave a double on the stack
        evalMethod.visitMaxs(0, 0);
        evalMethod.visitEnd();

        scriptClass.visitEnd();

        if (scriptClass instanceof ClassWriter cw) {
            return cw.toByteArray();
        } else if (scriptClass instanceof AsmUtil.StringClassVisitor visitor) {
            return visitor.classWriter().toByteArray();
        } else {
            throw new IllegalStateException("Cannot get bytecode from " + scriptClass.getClass().getName());
        }
    }

    public @UnknownNullability MethodVisitor method() {
        return evalMethod;
    }

    public @NotNull String queryClassName() {
        return AsmUtil.toName(queryClass);
    }

    public @NotNull String contextClassName() {
        return AsmUtil.toName(contextClass);
    }

    private void generateSynthetics() {
        // Add empty constructor
        var mv = scriptClass.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(1,1);
        mv.visitEnd();

        // Insert bridge method for evaluate
        mv = scriptClass.visitMethod(ACC_PUBLIC | ACC_SYNTHETIC | ACC_BRIDGE, "evaluate", "(Ljava/lang/Object;Ljava/lang/Object;)D", null, null);
        mv.visitCode();

        mv.visitVarInsn(ALOAD, 0); // this
        mv.visitVarInsn(ALOAD, 1); // query
        mv.visitTypeInsn(CHECKCAST, AsmUtil.toName(queryClass));
        mv.visitVarInsn(ALOAD, 2); // context
        mv.visitTypeInsn(CHECKCAST, AsmUtil.toName(contextClass));
        mv.visitMethodInsn(INVOKEVIRTUAL, className, "evaluate", evaluateDescriptor(), false);
        mv.visitInsn(DRETURN);

        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    private String evaluateDescriptor() {
        return String.format("(%s%s)D", AsmUtil.toDescriptor(queryClass), AsmUtil.toDescriptor(contextClass));
    }
}
