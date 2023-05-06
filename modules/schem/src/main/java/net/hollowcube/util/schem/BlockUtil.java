package net.hollowcube.util.schem;

import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;

final class BlockUtil {
    private BlockUtil() {}

    public static @NotNull String toStateString(@NotNull Block block) {
        if (block.properties().isEmpty())
            return block.name();

        var sb = new StringBuilder();
        sb.append(block.name()).append("[");
        for (var entry : block.properties().entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }

}
