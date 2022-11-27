package net.hollowcube.util.schem;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SchematicBuilder {

    private final Map<Point, Block> blockSet = new HashMap<>();

    private Point offset = Vec.ZERO;


    public void addBlock(@NotNull Point point, @NotNull Block block) {
        blockSet.put(point, block);
    }

    public void setOffset(@NotNull Point point) {
        this.offset = offset;
    }

    public Schematic toSchematic() {
        // Find lowest and highest x, y, z
        int xMin = Integer.MAX_VALUE;
        int yMin = Integer.MAX_VALUE;
        int zMin = Integer.MAX_VALUE;
        int xMax = Integer.MIN_VALUE;
        int yMax = Integer.MIN_VALUE;
        int zMax = Integer.MIN_VALUE;

        for (Point point : blockSet.keySet()) {
            if (point.blockX() < xMin) {
                xMin = point.blockX();
            }
            if (point.blockX() > xMax) {
                xMax = point.blockX();
            }
            if (point.blockY() < yMin) {
                yMin = point.blockY();
            }
            if (point.blockY() > yMax) {
                yMax = point.blockY();
            }
            if (point.blockZ() < zMin) {
                zMin = point.blockZ();
            }
            if (point.blockZ() > zMax) {
                zMax = point.blockZ();
            }
        }
        int xSize = xMax - xMin;
        int ySize = yMax - yMin;
        int zSize = zMax - zMin;
        Point size = new Vec(xSize, ySize, zSize);

        // Map of Block -> Palette ID
        HashMap<Block, Integer> paletteMap = new HashMap<>();
        paletteMap.put(Block.AIR, 0);

        byte[] blocks = new byte[size.blockX() * size.blockY() * size.blockZ()];
        // To convert point to index into array : (pointX - xMin) * xSize + (pointY - yMin) * ySize + (pointZ - zMin) * zSize
        for (var entry : blockSet.entrySet()) {
            Point point = entry.getKey();
            int blockId = -1;
            if (!paletteMap.containsKey(entry.getValue())) {
                blockId = paletteMap.size();
                paletteMap.put(entry.getValue(), paletteMap.size());
            } else {
                blockId = paletteMap.get(entry.getValue());
            }
            int index = (point.blockX() - xMin) * xSize + (point.blockY() - yMin) * ySize + (point.blockZ() - zMin) * zSize;
            blocks[index] = (byte) blockId;
        }

        Block[] palette = new Block[paletteMap.size()];
        for (var entry : paletteMap.entrySet()) {
            palette[entry.getValue()] = entry.getKey();
        }

        return new Schematic(
                size,
                offset,
                palette,
                blocks
        );
    }
}
