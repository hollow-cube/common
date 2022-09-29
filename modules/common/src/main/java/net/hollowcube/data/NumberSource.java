package net.hollowcube.data;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

/**
 * A source of numbers, implementations decide where the numbers come from
 */
@FunctionalInterface
public interface NumberSource {

    static @NotNull NumberSource constant(double value) {
        return () -> value;
    }

    static @NotNull NumberSource threadLocalRandom() {
        return () -> ThreadLocalRandom.current().nextDouble();
    }


    double random();

}
