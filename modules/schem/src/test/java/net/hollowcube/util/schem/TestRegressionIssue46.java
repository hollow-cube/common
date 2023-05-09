package net.hollowcube.util.schem;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.block.Block;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TestRegressionIssue46 {

    @Test
    void test1x2x3SingleBlock() {
        var builder = new SchematicBuilder();
        builder.addBlock(0, 0, 0, Block.STONE); // Min point
        builder.addBlock(0, 1, 2, Block.STONE); // Max point

        var result = builder.build();
        assertEquals(new Vec(1, 2, 3), result.size());

        var blocks = new HashMap<Point, Block>();
        result.apply(Rotation.NONE, blocks::put);

        assertEquals(Block.STONE, blocks.get(new Vec(0, 0, 0)));
        assertEquals(Block.STONE, blocks.get(new Vec(0, 1, 2)));
    }

    @Test
    void testReadBuild() {
        // Reading, building, and comparing the built version to the original should yield the same thing... in theory

        var schemFile = getClass().getClassLoader().getResourceAsStream("big2.schem");
        assertNotNull(schemFile);
        var schem = SchematicReader.read(schemFile);

        var builder = new SchematicBuilder();
        schem.apply(Rotation.NONE, builder::addBlock);
        var result = builder.build();

        var expectedBlocks = new HashMap<>();
        schem.apply(Rotation.NONE, expectedBlocks::put);
        var actualBlocks = new HashMap<>();
        result.apply(Rotation.NONE, actualBlocks::put);

        for (var entry : expectedBlocks.entrySet()) {
            var expectedBlock = entry.getValue();
            var actualBlock = actualBlocks.get(entry.getKey());
            assertEquals(expectedBlock, actualBlock);
        }

//        assertEquals(schem, result);
        assertEquals(expectedBlocks, actualBlocks);
    }

}
