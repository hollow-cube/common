import net.hollowcube.canary.Test
import net.hollowcube.motion.MotionEntity
import net.minestom.server.coordinate.Pos

@Test
suspend fun `first test`(t: Test.Env) {
    val entity = MotionEntity()
    t.spawnEntity(entity, Pos(-1.0, 0.0, 0.0))

    val target = t.global(Pos(1.0, 0.0, 0.0))
    entity.setPathTo(target)

    t.expect { !entity.isPathfinding }
    t.expect { target.sameBlock(entity.position) }
}
