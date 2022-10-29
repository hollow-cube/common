package net.hollowcube.motion;

import net.hollowcube.motion.util.SchemBlockGetter;
import net.minestom.server.collision.BoundingBox;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.google.common.truth.Truth.assertThat;

// Test names are in the form {valid|invalid}_{path generator}_{pathfinder}_{path optimizer}
public class TestPathfinding {

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {
            "valid/2_2_corner",
            "valid/3_3_around",
            "valid/5_5_maze",
            "valid/1_4_4_staircase",
    })
    public void valid_LANDDIAGONAL_ASTAR_NONE(String name) {
        var bb = new BoundingBox(0.1, 0.1, 0.1);
        var world = new SchemBlockGetter(name);

        var result = Pathfinder.A_STAR.findPath(
                PathGenerator.LAND_DIAGONAL,
                world,
                world.start(),
                world.goal(),
                bb
        );
        assertThat(result).isNotNull();
        System.out.println(result);
    }

    @ParameterizedTest(name = "{0}")
    @ValueSource(strings = {
            "invalid/2_2_3_corner_impossible",
    })
    public void invalid_LANDDIAGONAL_ASTAR_NONE(String name) {
        var bb = new BoundingBox(0.1, 0.1, 0.1);
        var world = new SchemBlockGetter(name);

        var result = Pathfinder.A_STAR.findPath(
                PathGenerator.LAND_DIAGONAL,
                world,
                world.start(),
                world.goal(),
                bb
        );
        assertThat(result).isNull();
    }
}
