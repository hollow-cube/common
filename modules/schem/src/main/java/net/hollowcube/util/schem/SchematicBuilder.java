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
        this.offset = point;
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
        int xSize = xMax - xMin + 1;
        int ySize = yMax - yMin + 1;
        int zSize = zMax - zMin + 1;
        Point size = new Vec(xSize, ySize, zSize);

        // Map of Block -> Palette ID
        HashMap<Block, Integer> paletteMap = new HashMap<>();

        // Size of rectangular prism: 2(wl+hl+hw)
        byte[] blocks = new byte[xSize * ySize * zSize];
        // Determine if we have air in our palette
        // If the number of blocks in our blockset is equal to our byte array, we know we shouldn't fill in air as default since we have taken up every space
        if(blocks.length < blockSet.size()) {
            paletteMap.put(Block.AIR, 0);
        }

        // To convert point to index into array : ????????????
        for (var entry : blockSet.entrySet()) {
            Point point = entry.getKey();
            int blockId;
            if (!paletteMap.containsKey(entry.getValue())) {
                blockId = paletteMap.size();
                paletteMap.put(entry.getValue(), paletteMap.size());
            } else {
                blockId = paletteMap.get(entry.getValue());
            }
            int index = (point.blockX() - xMin) + (point.blockY() - yMin) + (point.blockZ() - zMin);
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
