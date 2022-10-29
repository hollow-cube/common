package net.hollowcube.motion;

import net.minestom.server.attribute.Attribute;
import net.minestom.server.collision.CollisionUtils;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.utils.position.PositionUtils;
import net.minestom.server.utils.time.Cooldown;
import net.minestom.server.utils.time.TimeUnit;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.function.Supplier;

/**
 * A movement controller chooses how to move an entity towards a position.
 * <p>
 * The goal position is not necessarily the actual pathfinder goal, but rather the position the
 *  entity is currently trying to reach.
 */
public interface MoveController {

    void moveTowards(
            @NotNull LivingEntity entity,
            @NotNull Point goal
    );

    interface Factory {
        MoveController create();
    }

    /**
     * Default land-bound movement controller. Currently very basic (eg does not know how to swim)
     */
    Supplier<MoveController> WALKING = () -> new MoveController() {
        private final Cooldown jumpCooldown = new Cooldown(Duration.of(20, TimeUnit.SERVER_TICK));

        @Override
        public void moveTowards(@NotNull LivingEntity entity, @NotNull Point goal) {
            var now = System.currentTimeMillis();
            var speed = (double) entity.getAttributeValue(Attribute.MOVEMENT_SPEED);

            final Pos position = entity.getPosition();
            final double dx = goal.x() - position.x();
            final double dy = goal.y() - position.y();
            final double dz = goal.z() - position.z();

            // the purpose of these few lines is to slow down entities when they reach their destination
            final double distSquared = dx * dx + dy * dy + dz * dz;
            if (speed > distSquared) {
                speed = distSquared;
            }

            final double radians = Math.atan2(dz, dx);
            final double speedX = Math.cos(radians) * speed;
            final double speedY = dy * speed;
            final double speedZ = Math.sin(radians) * speed;

            final float yaw = PositionUtils.getLookYaw(dx, dz);
            final float pitch = PositionUtils.getLookPitch(dx, dy, dz);

            final var physicsResult = CollisionUtils.handlePhysics(entity, new Vec(speedX, speedY, speedZ));
            boolean willCollide = physicsResult.collisionX() || physicsResult.collisionY() || physicsResult.collisionZ();
            if (dy > 0 && willCollide && jumpCooldown.isReady(now)) {
                jumpCooldown.refreshLastUpdate(now);
                //todo magic
                entity.setVelocity(new Vec(0, 3.5f * 2.5f, 0));
            } else {
                entity.refreshPosition(Pos.fromPoint(physicsResult.newPosition()).withView(yaw, pitch));
            }
        }
    };

//    /**
//     * Similar to {@link #WALKING}, but will hop towards the target (eg, slimes/rabbits)
//     */
//    MoveController HOPPING = new MoveController() {};

//    /**
//     * Moves directly towards the target position, ignoring gravity.
//     */
//    MoveController DIRECT = new MoveController() {};

}
