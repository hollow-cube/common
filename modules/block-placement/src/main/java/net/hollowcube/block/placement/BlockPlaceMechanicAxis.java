package net.hollowcube.block.placement;

import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.instance.block.Block;

public class BlockPlaceMechanicAxis {

    public static void onPlace(Block block, PlayerBlockPlaceEvent event) {
        block = event.getBlock();

        switch (event.getBlockFace()) {
            case EAST, WEST -> event.setBlock(block.withProperty("axis", "x"));
            case NORTH, SOUTH -> event.setBlock(block.withProperty("axis", "z"));
            default -> event.setBlock(block.withProperty("axis", "y"));
        }
    }

}
