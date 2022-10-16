@file:Suppress("UnstableApiUsage")

package net.hollowcube.canary.server

import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.event.player.PlayerSpawnEvent
import net.minestom.server.instance.Instance
import net.minestom.server.instance.block.Block
import net.minestom.server.thread.TickSchedulerThread
import java.net.InetSocketAddress



class CanaryServer {
    companion object {
        init {
            System.setProperty("minestom.terminal.disabled", "true")
        }
    }

    val process = MinecraftServer.updateProcess()

    val testInstance: Instance

    init {
        testInstance = process.instance().createInstanceContainer()
        testInstance.setGenerator { unit -> unit.modifier().fillHeight(0, 40, Block.STONE) }
        testInstance.timeRate = 0

        process.eventHandler().addListener(PlayerLoginEvent::class.java, ::playerLogin)
        process.eventHandler().addListener(PlayerSpawnEvent::class.java, ::playerSpawn)

        process.start(InetSocketAddress("localhost", 25565))
        TickSchedulerThread(process).start()
    }

    fun stop() {
        process.stop()
    }

    private fun playerLogin(event: PlayerLoginEvent) {
        event.setSpawningInstance(testInstance)
        event.player.respawnPoint = Pos(0.0, 42.0, 0.0)
    }

    private fun playerSpawn(event: PlayerSpawnEvent) {
        event.player.permissionLevel = 4
        event.player.gameMode = GameMode.CREATIVE
    }
}