package net.hollowcube.motion;

import net.hollowcube.motion.util.PhysicsUtil;
import net.minestom.server.collision.BoundingBox;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.block.Block;
import net.minestom.server.utils.Direction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static net.minestom.server.instance.block.Block.Getter.Condition;

/**
 * Generates paths to be consumed by a {@link Pathfinder}.
 * <p>
 * Should take into account the entity capabilities, e.g. avoiding danger.
 */
public interface PathGenerator {

    @NotNull Collection<Point> generate(@NotNull Block.Getter world, @NotNull Point pos, @NotNull BoundingBox bb);


    PathGenerator LAND = (world, pos, bb) -> {
        pos = new Vec(pos.blockX() + 0.5, pos.blockY(), pos.blockZ() + 0.5);
        List<Point> neighbors = new ArrayList<>();
        for (Direction direction : Direction.HORIZONTAL) {
            for (int y = -1; y <= 1; y++) {
                var neighbor = pos.add(direction.normalX(), direction.normalY() + y, direction.normalZ());
                // Block below must be solid, or we cannot move to it
                try {
                    if (!world.getBlock(neighbor.add(0, -1, 0), Condition.TYPE).isSolid()) continue;
                } catch (RuntimeException e) {
                    //todo need a better solution here. Instance throws an exception if the chunk is unloaded
                    //     but that is kinda awful behavior here. Probably i will need to check if the chunk
                    //     is loaded, but then i cant use a block getter
                    continue;
                }
                // Ensure the BB fits at that block
                if (PhysicsUtil.testCollision(world, neighbor, bb)) continue;

                neighbors.add(neighbor);
            }
        }
        return neighbors;
    };

    /**
     * Land pathfinder with the capability to walk diagonally (only horizontally, not up or down diagonals)
     */
    PathGenerator LAND_DIAGONAL = (world, pos, bb) -> {
        pos = new Vec(pos.blockX() + 0.5, pos.blockY(), pos.blockZ() + 0.5);
        List<Point> neighbors = new ArrayList<>();
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                if (x == 0 && z == 0) continue;

                int minY = x == 0 || z == 0 ? -1 : 0;
                int maxY = x == 0 || z == 0 ? 1 : 0;
                for (int y = minY; y <= maxY; y++) {
                    var neighbor = pos.add(x, y, z);

                    // Block below must be solid, or we cannot move to it
                    try {
                        if (!world.getBlock(neighbor.add(0, -1, 0), Condition.TYPE).isSolid()) continue;
                    } catch (RuntimeException e) {
                        //todo need a better solution here. Instance throws an exception if the chunk is unloaded
                        //     but that is kinda awful behavior here. Probably i will need to check if the chunk
                        //     is loaded, but then i cant use a block getter
                        continue;
                    }

                    // Ensure the movement from pos to neighbor is valid
                    // This seems fairly slow, might be able to do a faster check for simple cases
                    if (neighbor.y() > pos.y()) {
                        // If the target is above the current, try to move to the targetPosition, then over
                        var target = pos.withY(neighbor.y());
                        if (PhysicsUtil.testCollisionSwept(world, bb, pos, target)) continue;
                        if (PhysicsUtil.testCollisionSwept(world, bb, target, neighbor)) continue;
                    } else if (neighbor.y() < pos.y()) {
                        // If the target is below the current, try to move to the targetPosition, then down
                        var target = neighbor.withY(pos.y());
                        if (PhysicsUtil.testCollisionSwept(world, bb, pos, target)) continue;
                        if (PhysicsUtil.testCollisionSwept(world, bb, target, neighbor)) continue;
                    } else {
                        // Same Y, so we can just make sure the direct movement is valid.
                        if (PhysicsUtil.testCollisionSwept(world, bb, pos, neighbor)) continue;
                    }

                    neighbors.add(neighbor);
                }
            }
        }
        return neighbors;
    };

    PathGenerator WATER = (world, pos, bb) -> {
        pos = new Vec(pos.blockX() + 0.5, pos.blockY(), pos.blockZ() + 0.5);
        List<Point> neighbors = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            var neighbor = pos.add(direction.normalX(), direction.normalY(), direction.normalZ());
            // Ensure the block is water, otherwise we cannot move to it
            if (world.getBlock(neighbor, Condition.TYPE).id() != Block.WATER.id()) continue;
            // Ensure the BB fits at that block
            if (PhysicsUtil.testCollision(world, neighbor, bb)) continue;

            neighbors.add(neighbor);
        }
        return neighbors;
    };

    PathGenerator AIR = (world, pos, bb) -> {
        //todo
        return List.of();
    };

}
