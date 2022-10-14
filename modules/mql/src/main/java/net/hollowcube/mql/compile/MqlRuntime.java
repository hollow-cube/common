package net.hollowcube.mql.compile;

import org.jetbrains.annotations.ApiStatus;

/**
 * Runtime utility functions for compiled MQL scripts.
 * <p>
 * Intended to avoid generating some bytecode. May remove these in the future.
 */
@ApiStatus.Internal
public final class MqlRuntime {
    private MqlRuntime() {}

    public static double bool2double(boolean value) {
        return value ? 1 : 0;
    }

}
