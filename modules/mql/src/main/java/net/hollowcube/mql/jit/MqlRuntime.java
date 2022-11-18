package net.hollowcube.mql.jit;

public final class MqlRuntime {
    private MqlRuntime() {
    }

    public static double ternary(double condition, double ifTrue, double ifFalse) {
        return condition != 0 ? ifTrue : ifFalse;
    }

    public static double gte(double lhs, double rhs) {
        return lhs >= rhs ? 1 : 0;
    }

    public static double ge(double lhs, double rhs) {
        return lhs > rhs ? 1 : 0;
    }

    public static double lte(double lhs, double rhs) {
        return lhs <= rhs ? 1 : 0;
    }

    public static double le(double lhs, double rhs) {
        return lhs < rhs ? 1 : 0;
    }

    public static double eq(double lhs, double rhs) {
        return lhs == rhs ? 1 : 0;
    }

    public static double neq(double lhs, double rhs) {
        return lhs != rhs ? 1 : 0;
    }

}
