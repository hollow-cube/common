package net.hollowcube.test;

import net.hollowcube.block.placement.BlockPlaceMechanicRotation16;
import net.minestom.server.coordinate.Vec;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Rotation16Test {

    @Test
    public void testRotation16() {
        assertEquals(0, BlockPlaceMechanicRotation16.getRotationNumber(new Vec(0, -1)));
        assertEquals(8, BlockPlaceMechanicRotation16.getRotationNumber(new Vec(0, 1)));
        assertEquals(4, BlockPlaceMechanicRotation16.getRotationNumber(new Vec(1, 0)));
        assertEquals(12, BlockPlaceMechanicRotation16.getRotationNumber(new Vec(-1, 0)));
    }
}
