package net.hollowcube.motion.util;

import net.hollowcube.motion.*;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class TestEntity extends LivingEntity {
    private final MotionNavigator navigator;

    public TestEntity(PathGenerator generator, Pathfinder finder, PathOptimizer optimizer, Supplier<MoveController> controller) {
        super(EntityType.ZOMBIE);
        navigator = new MotionNavigator(this, generator, finder, optimizer, controller);
    }

    public @NotNull MotionNavigator navigator() {
        return navigator;
    }

    @Override
    public void update(long time) {
        super.update(time);

        if (navigator.isActive()) {
            navigator.tick(time);
        }
    }
}
