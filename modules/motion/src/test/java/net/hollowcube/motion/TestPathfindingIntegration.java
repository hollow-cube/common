package net.hollowcube.motion;

import net.hollowcube.motion.util.TestEntity;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.instance.Instance;
import net.minestom.server.test.Env;
import net.minestom.server.test.EnvTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertTrue;

@EnvTest
public class TestPathfindingIntegration {
    private Instance instance;

    @BeforeEach
    public void setup(Env env) {
        System.out.println("init");
        instance = env.createFlatInstance();
        var chunks = new CompletableFuture[9];
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                chunks[(x + 1) * 3 + (z + 1)] = instance.loadChunk(x, z);
            }
        }
        CompletableFuture.allOf(chunks).join();
        env.tick(); // Tick once to initialize world
    }

//    @Test
//    public void testNoObstacle(Env env) {
//        var entity = new TestEntity(PathGenerator.LAND_DIAGONAL, Pathfinder.A_STAR, null, MoveController.WALKING);
//        entity.setInstance(instance, new Pos(0, 40, 0)).join();
//
//        var goal = new Pos(0, 40, 10);
//        var foundPath = entity.navigator().setPathTo(goal);
//        assertTrue(foundPath);
//
//        // Run until entity is within half a block of the target
//        boolean passed = env.tickWhile(() -> {
//            System.out.println(entity.getPosition() + " " + entity.getPosition().distance(goal));
//            return entity.getPosition().distance(goal) > 0.8;
//        }, Duration.ofSeconds(5));
//        assertTrue(passed);
//    }
}
