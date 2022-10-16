package net.hollowcube.canary

import net.minestom.server.coordinate.Point
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Entity

annotation class Test(val timeout: Long = 10000) {

    interface Env {

        /** Convert a position to a local pos */
        fun <T : Point> local(point: T): T
        /** Convert a position to a global pos in the test instance */
        fun <T : Point> global(point: T): T

        suspend fun spawnEntity(entity: Entity, pos: Pos)

        suspend fun expect(condition: () -> Boolean)

    }
}
