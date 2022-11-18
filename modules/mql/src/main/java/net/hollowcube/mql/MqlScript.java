package net.hollowcube.mql;

import net.hollowcube.mql.parser.MqlParser;
import net.hollowcube.mql.runtime.MqlScope;
import net.hollowcube.mql.tree.MqlExpr;
import net.hollowcube.mql.tree.MqlNumberExpr;
import net.hollowcube.mql.value.MqlNumberValue;
import net.hollowcube.mql.value.MqlValue;
import org.jetbrains.annotations.NotNull;

public record MqlScript(@NotNull MqlExpr expr) {

//    public static final Codec<MqlScript> CODEC = Codec.either(Codec.STRING, Codec.STRING.listOf())
//            .xmap(either -> DFUUtil.value(either.mapRight(lines -> String.join("\n", lines))), Either::left)
//            .xmap(MqlScript::parse, unused -> {throw new RuntimeException("cannot serialize an mql script");});

    public static @NotNull MqlScript parse(@NotNull String script) {
        if (script.trim().isEmpty()) // Empty script is always the number 1
            return new MqlScript(new MqlNumberExpr(1));
        MqlExpr expr = new MqlParser(script).parse();
        return new MqlScript(expr);
    }

    public double evaluate(@NotNull MqlScope scope) {
        MqlValue result = expr().evaluate(scope);
        if (result instanceof MqlNumberValue num)
            return num.value();
        return 0.0;
    }

    public boolean evaluateToBool(@NotNull MqlScope scope) {
        MqlValue result = expr().evaluate(scope);
        if (result instanceof MqlNumberValue num)
            return num.value() != 0;
        return result != MqlValue.NULL;
    }

}
