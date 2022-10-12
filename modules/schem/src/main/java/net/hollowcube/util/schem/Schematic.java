package net.hollowcube.util.schem;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.batch.BatchOption;
import net.minestom.server.instance.batch.RelativeBlockBatch;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Represents a schematic file which can be manipulated in the world.
 */
public record Schematic(
        Point size,
        Point offset,
        Block[] palette,
        byte[] blocks
) {
    public Schematic {
        palette = Arrays.copyOf(palette, palette.length);
        blocks = Arrays.copyOf(blocks, blocks.length);
    }

    @Override
    public Block[] palette() {
        return Arrays.copyOf(palette, palette.length);
    }

    @Override
    public byte[] blocks() {
        return Arrays.copyOf(blocks, blocks.length);
    }

    /**
     * Convert the schematic into a {@link RelativeBlockBatch} which can be applied to an instance.
     * The schematic can be rotated around its {@link #offset()} before placement.
     * <p>
     * Note: Right now only stairs are rotated correctly, other block states are unchanged.
     *
     * @param rotation The rotation to apply to the schematic.
     * @param blockModifier If present, called on each individual block before it is placed.
     * @return A {@link RelativeBlockBatch} which represents the schematic file at its offset.
     */
    public @NotNull RelativeBlockBatch build(@NotNull Rotation rotation, @Nullable Function<Block, Block> blockModifier) {
        RelativeBlockBatch batch = new RelativeBlockBatch(new BatchOption().setCalculateInverse(true));
        apply(rotation, (pos, block) -> batch.setBlock(pos, blockModifier == null ? block : blockModifier.apply(block)));
        return batch;
    }

    /**
     * Apply the schematic directly given a rotation. The applicator function will be called for each block in the schematic.
     * <p>
     * Note: The {@link Point} passed to `applicator` is relative to the {@link #offset()}.
     *
     * @param rotation The rotation to apply before placement.
     * @param applicator The function to call for each block in the schematic.
     */
    public void apply(@NotNull Rotation rotation, @NotNull BiConsumer<Point, Block> applicator) {
        ByteBuffer blocks = ByteBuffer.wrap(this.blocks);
        for (int y = 0; y < size().y(); y++) {
            for (int z = 0; z < size.z(); z++) {
                for (int x = 0; x < size.x(); x++) {
                    int blockVal = Utils.readVarInt(blocks);
                    Block b = palette[blockVal];

                    if (b == null || b.isAir()) {
                        continue;
                    }

                    Vec blockPos = new Vec(x + offset.x(), y + offset.y(), z + offset.z());
                    applicator.accept(rotatePos(blockPos, rotation), rotateBlock(b, rotation));
                }
            }
        }
    }

    private @NotNull Point rotatePos(@NotNull Point point, @NotNull Rotation rotation) {
        return switch (rotation) {
            case NONE -> point;
            case CLOCKWISE_90 -> new Vec(-point.z(), point.y(), point.x());
            case CLOCKWISE_180 -> new Vec(-point.x(), point.y(), -point.z());
            case CLOCKWISE_270 -> new Vec(point.z(), point.y(), -point.x());
        };
    }

    private @NotNull Block rotateBlock(@NotNull Block block, @NotNull Rotation rotation) {
        if (block.name().contains("stair")) {
            return rotateStair(block, rotation);
        } else {
            return block;
        }
    }

    private static Block rotateStair(Block block, Rotation rotation) {
        return switch (rotation) {
            case NONE -> block;
            case CLOCKWISE_90 -> block.withProperty("facing", rotate90(block.getProperty("facing")));
            case CLOCKWISE_180 -> block.withProperty("facing", rotate90(rotate90(block.getProperty("facing"))));
            case CLOCKWISE_270 -> block.withProperty("facing", rotate90(rotate90(rotate90(block.getProperty("facing")))));
        };
    }

    private static String rotate90(String in) {
        return switch (in) {
            case "north" -> "east";
            case "east" -> "south";
            case "south" -> "west";
            default -> "north";
        };
    }
}
