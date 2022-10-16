package net.hollowcube.motion;

import net.minestom.server.coordinate.Point;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import org.jetbrains.annotations.NotNull;

public class MotionEntity extends Entity {
    private final MotionNavigator navigator;

    public MotionEntity() {
        super(EntityType.ZOMBIE);

        navigator = new MotionNavigator(this);
    }

    @Override
    public void update(long time) {
        super.update(time);
        navigator.tick(time);
    }

    public boolean setPathTo(@NotNull Point point) {
        return navigator.setPathTo(point);
    }

    public boolean isPathfinding() {
        return navigator.isActive();
    }
}
