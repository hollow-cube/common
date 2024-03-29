package net.hollowcube.util.schem;

import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.block.Block;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestSchematicBuilder {

    @Test
    void testSchematicBuild() {
        SchematicBuilder builder = new SchematicBuilder();

        builder.addBlock(new Vec(5, 2, 5), Block.STONE);
        builder.addBlock(new Vec(5, 3, 5), Block.STONE);
        builder.addBlock(new Vec(5, 4, 5), Block.STONE);
        builder.addBlock(new Vec(4, 2, 5), Block.DIORITE);
        builder.addBlock(new Vec(4, 3, 5), Block.DIORITE);
        builder.addBlock(new Vec(4, 4, 5), Block.DIORITE);

        Schematic result = builder.build();
        // Since we should have perfect encapsulation in the schematic because it is rectangular, we shouldn't have any air entries
        assertEquals(2, result.palette().length);
        assertEquals(new Vec(4, 2, 5), result.offset());

        builder.addBlock(new Vec(3, 2, 5), Block.COAL_BLOCK);

        // Should have air and coal in the schematic now
        result = builder.build();
        assertEquals(4, result.palette().length);
    }
}
