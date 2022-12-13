package net.hollowcube.test;

import net.hollowcube.block.placement.HCPlacementRules;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.timer.TaskSchedule;

import java.time.Duration;

public class TestServer {
    public static void main(String[] args) {
        var server = MinecraftServer.init();

        var instanceManager = MinecraftServer.getInstanceManager();
        var instance = instanceManager.createInstanceContainer();
        instance.setGenerator(unit -> unit.modifier().fillHeight(0, 40, Block.STONE));

        var eventHandler = MinecraftServer.getGlobalEventHandler();
        eventHandler.addListener(PlayerLoginEvent.class, event -> {
            event.setSpawningInstance(instance);
            event.getPlayer().setRespawnPoint(new Pos(0, 40, 0));
        });
        eventHandler.addListener(PlayerSpawnEvent.class, event -> {
            var player = event.getPlayer();
            player.setGameMode(GameMode.CREATIVE);
            MinecraftServer.getSchedulerManager().scheduleTask(() -> {
                player.sendMessage(player.getPosition().direction().toString());
            }, TaskSchedule.duration(Duration.ofSeconds(5)), TaskSchedule.duration(Duration.ofSeconds(1)));
        });

        HCPlacementRules.init();

        server.start("localhost", 25565);
    }
}
