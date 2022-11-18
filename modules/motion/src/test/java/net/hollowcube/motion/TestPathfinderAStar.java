package net.hollowcube.motion;

import net.hollowcube.motion.util.SchemBlockGetter;
import net.hollowcube.test.MockBlockGetter;
import net.minestom.server.collision.BoundingBox;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.Direction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static net.minestom.server.instance.block.Block.Getter.Condition;

public class TestPathfinderAStar {

    @Test
    public void testSamePoint() {
        var bb = new BoundingBox(0.1, 0.1, 0.1);
        var world = MockBlockGetter.empty();
        var start = new Vec(0, 0, 0);
        var goal = new Vec(0, 0, 0);

        var result = Pathfinder.A_STAR.findPath(ALL, world, start, goal, bb);
        assertThat(result).isNotNull();
        assertThat(result.nodes()).containsExactly(new Vec(0, 0, 0));
    }

    @Test
    public void testBasicLine() {
        var bb = new BoundingBox(0.1, 0.1, 0.1);
        var world = MockBlockGetter.empty();
        var start = new Vec(0.5, 0, 0.5);
        var goal = new Vec(3, 0, 0);

        var result = Pathfinder.A_STAR.findPath(ALL, world, start, goal, bb);
        assertThat(result).isNotNull();
        assertThat(result.nodes()).containsExactly(
                new Vec(0.5, 0, 0.5),
                new Vec(1.5, 0, 0.5),
                new Vec(2.5, 0, 0.5)
        );
    }

    @Test
    public void testBasicAvoidance() {
        var bb = new BoundingBox(0.1, 0.1, 0.1);
        var world = MockBlockGetter.block(1, 0, 0, Block.STONE);
        var start = new Vec(0.5, 0, 0.5);
        var goal = new Vec(2.5, 0, 0.5);

        var result = Pathfinder.A_STAR.findPath(ALL, world, start, goal, bb);
        assertThat(result).isNotNull();
        assertThat(result.nodes()).containsExactly(
                new Vec(0.5, 0, 0.5),
                new Vec(0.5, 0, 1.5),
                new Vec(1.5, 0, 1.5),
                new Vec(2.5, 0, 1.5),
                new Vec(2.5, 0, 0.5)
        );
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {
            "valid/2_2_corner",
            "valid/3_3_around",
            "valid/5_5_maze",
            "valid/1_4_4_staircase",
    })
    public void testSchematics(String name) {
        var bb = new BoundingBox(0.1, 0.1, 0.1);
        var world = new SchemBlockGetter(name);

        var result = Pathfinder.A_STAR.findPath(ALL, world, world.start(), world.goal(), bb);
        assertThat(result).isNotNull();
        System.out.println(result);
    }


    // A path generator which returns any air block in a direction (up/down/nsew)
    private static final PathGenerator ALL = (world, pos, bb) -> {
        pos = new Vec(pos.blockX() + 0.5, pos.blockY(), pos.blockZ() + 0.5);
        List<Point> neighbors = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            var neighbor = pos.add(direction.normalX(), direction.normalY(), direction.normalZ());
            if (!world.getBlock(neighbor, Condition.TYPE).isAir()) continue;
            neighbors.add(neighbor);
        }
        return neighbors;
    };

}
