package net.hollowcube.block.placement;

import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;

public class BlockPlaceMechanicGlowLichen {

    public static void onPlace(Block block, PlayerBlockPlaceEvent event) {
        BlockFace face = event.getBlockFace().getOppositeFace();

        String faceName = face.name().toLowerCase();
        if (face == BlockFace.TOP) faceName = "up";
        if (face == BlockFace.BOTTOM) faceName = "down";

        Block oldBlock = event.getInstance().getBlock(event.getBlockPosition());
        if (oldBlock.compare(block)) {
            block = block.withProperties(oldBlock.properties());
        }

        block = block.withProperty(faceName, "true");

        event.setBlock(block);
    }

}
