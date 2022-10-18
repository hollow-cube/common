import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.hollowcube.canary.Test
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.EntityType
import net.minestom.server.entity.LivingEntity

@Test(timeout = 1000)
suspend fun `my entity test`(t: Test.Env) {
    println("Running test")

    val entity = LivingEntity(EntityType.VILLAGER)
    entity.health = entity.maxHealth
    t.spawnEntity(entity, Pos(0.0, 0.0, 0.0))
    coroutineScope {
        launch {
            delay(100)
            entity.health = 2f
        }
    }

    t.expect {
        entity.health == 2f
    }
}

@Test
suspend fun `another test`(t: Test.Env) {
    println("ANOTHER TEST!!!!")
}

@Test
suspend fun `third test`(t: Test.Env) {
    println("THIRD TEST!!!!")
}

@Test
suspend fun `fourth test`(t: Test.Env) {
    println("THIRD TEST!!!!")
}
