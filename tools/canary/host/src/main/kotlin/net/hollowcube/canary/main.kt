@file:JvmName("Main")

package net.hollowcube.canary

import kotlinx.coroutines.runBlocking
import net.hollowcube.canary.script.evalToConfiguration
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Entity
import net.minestom.server.entity.GameMode
import net.minestom.server.event.EventListener
import net.minestom.server.event.instance.InstanceTickEvent
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.instance.Instance
import net.minestom.server.instance.block.Block
import java.nio.file.Path
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.reflect.KCallable
import kotlin.reflect.KFunction
import kotlin.reflect.full.callSuspend
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions

/*
MOST BASIC MVP
- DONE specify tests in kotlin script
  - FUTURE support multiple scripts
  - FUTURE hot reload scripts as they are modified
- DONE launch daemon
  - DONE start server, create a test instance
    - FUTURE allow configuring server startup with an init script or something
  - DONE layout every test into the instance
    - -- load the test's schematic
      - FUTURE supply some sample schematics for common use (eg a 5x5 square or something)
- run tests
  - FUTURE allow running individual tests/filters/etc
  - DONE execute the test function
  - DONE wait for the test to finish
  - -- report the result to terminal

FUTURE
- serialize entire daemon server state to build dir and restore it on next launch


 */

class TestSpec(
    val name: String,
    val timeout: Long,
    val test: KFunction<*>
)

fun main() {
    CanaryDaemon.start()



//
//
//    val path = Path.of("/Users/matt/dev/projects/mmo/common/tools/canary/host/src/test/java/test2.test.kts")
//    val (scriptClass, scriptInstance) = evalToConfiguration(path)
//
//    val tests = mutableListOf<TestSpec>()
//    for (func in scriptClass.functions) {
//        val name = func.name
//        val annotation = func.findAnnotation<Test>() ?: continue
//        if (!func.isSuspend) {
//            println("Test function $name is not a suspend function, skipping")
//            continue
//        }
//        //todo check parameters
//
//        val timeout = annotation.timeout
//        tests.add(TestSpec(
//            name = name,
//            timeout = timeout,
//            test = func
//        ))
//    }
//
//    val testInstance = launchServer()
//
//    val test = tests.first()
//
//    val env = object : Test.Env {
//        override suspend fun spawnEntity(entity: Entity, pos: Pos) = suspendCoroutine { cont ->
//            entity.setInstance(testInstance, pos).thenRun {
//                cont.resumeWith(Result.success(Unit))
//            }
//        }
//
//        override suspend fun expect(condition: () -> Boolean) = suspendCoroutine { cont ->
//            testInstance.eventNode().addListener(EventListener.builder(InstanceTickEvent::class.java)
//                .expireWhen {
//                    if (condition()) {
//                        cont.resume(Unit)
//                        true
//                    } else false
//                }
//                .build())
//        }
//    }
//
//    runBlocking {
//        test.test.callSuspend(scriptInstance, env)
//    }
//
//    println("DONE!!!!!")
//
//    MinecraftServer.stopCleanly()

}

private fun launchServer(): Instance {
    System.setProperty("minestom.terminal.disabled", "true")
    val server = MinecraftServer.init()

    val instanceManager = MinecraftServer.getInstanceManager()

    val instance = instanceManager.createInstanceContainer()
    instanceManager.registerInstance(instance);
    instance.setGenerator { unit -> unit.modifier().fillHeight(0, 40, Block.STONE) }

    val eventHandler = MinecraftServer.getGlobalEventHandler()
    eventHandler.addListener(PlayerLoginEvent::class.java) { event ->
        val player = event.player
        event.setSpawningInstance(instance)
        player.respawnPoint = Pos(0.0, 42.0, 0.0)
    }
    eventHandler.addListener(PlayerSpawnEvent::class.java) { event ->
        val player = event.player
        player.gameMode = GameMode.CREATIVE
        player.permissionLevel = 4
    }

    server.start("localhost", 25565)
    return instance
}