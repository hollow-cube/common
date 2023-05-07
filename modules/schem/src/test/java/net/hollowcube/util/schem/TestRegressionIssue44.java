package net.hollowcube.util.schem;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.block.Block;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <a href="https://github.com/hollow-cube/common/issues/44">#44</a>
 */
class TestRegressionIssue44 {

    @Test
    void testNegative3x3() {
        SchematicBuilder builder = new SchematicBuilder();

        int startX = -12, startY = 0, startZ = -12;
        int endX = -10, endY = 2, endZ = -10;
        for (int y = startY; y <= endY; y++) {
            for (int z = startZ; z <= endZ; z++) {
                for (int x = startX; x <= endX; x++) {
                    builder.addBlock(new Vec(x, y, z), Block.STONE);
                }
            }
        }

        Schematic result = builder.build();
        assertEquals(new Vec(3, 3, 3), result.size());
        assertEquals(new Vec(-12, 0, -12), result.offset());

        var appliedBlocks = new HashMap<Point, Block>();
        result.apply(Rotation.NONE, appliedBlocks::put);

        for (int y = startY; y <= endY; y++) {
            for (int z = startZ; z <= endZ; z++) {
                for (int x = startX; x <= endX; x++) {
                    assertEquals(Block.STONE, appliedBlocks.get(new Vec(x, y, z)));
                }
            }
        }
    }
}
