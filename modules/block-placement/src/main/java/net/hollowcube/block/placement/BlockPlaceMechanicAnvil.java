package net.hollowcube.block.placement;

import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.instance.block.Block;

public class BlockPlaceMechanicAnvil {
    public static void onPlace(Block block, PlayerBlockPlaceEvent event) {
        block = event.getBlock();
        switch (event.getBlockFace()) {
            case NORTH -> event.setBlock(block.withProperty("facing", "east"));
            case EAST -> event.setBlock(block.withProperty("facing", "south"));
            case SOUTH -> event.setBlock(block.withProperty("facing", "west"));
            // West and others default to north
            default -> event.setBlock(block.withProperty("facing", "north"));
        }
    }
}
