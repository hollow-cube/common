package net.hollowcube.mql.compile;

import net.hollowcube.mql.tree.*;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.Opcodes;

public class MqlCompilerOld implements MqlVisitor<Context, Void> {
    @Override
    public Void visitBinaryExpr(@NotNull MqlBinaryExpr expr, Context ctx) {
        visit(expr.lhs(), ctx);
        visit(expr.rhs(), ctx);

        var method = ctx.method();
        switch (expr.operator()) {
            case PLUS -> method.visitInsn(Opcodes.DADD);
        }

        return null;
    }

    @Override
    public Void visitAccessExpr(@NotNull MqlAccessExpr expr, Context ctx) {
        if (expr.lhs() instanceof MqlIdentExpr ident) {
            if (!ident.value().equals("q")) {
                throw new UnsupportedOperationException("Only q is supported as a query object");
            }

            var method = ctx.method();
            method.visitVarInsn(Opcodes.ALOAD, 1);
            method.visitMethodInsn(Opcodes.INVOKEVIRTUAL, ctx.queryClassName(), expr.target(), "()D", false);
        } else {
            throw new RuntimeException("Not implemented");
        }
        System.out.println("ACCESS " + expr.lhs() + " " + expr.target());
        return null;
    }

    @Override
    public Void visitNumberExpr(@NotNull MqlNumberExpr expr, Context ctx) {
        var method = ctx.method();
        method.visitLdcInsn(expr.value().value());
        return null;
    }

    @Override
    public Void visitRefExpr(@NotNull MqlIdentExpr expr, Context ctx) {
        System.out.println("REF");
        return null;
    }

    @Override
    public Void defaultValue() {
        return null;
    }
}
