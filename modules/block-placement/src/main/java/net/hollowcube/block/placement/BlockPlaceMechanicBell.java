package net.hollowcube.block.placement;

import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.instance.block.Block;

import java.util.HashMap;
import java.util.Locale;

public class BlockPlaceMechanicBell {

    public static void onPlace(Block block, PlayerBlockPlaceEvent event) {
        block = event.getBlock();

        switch (event.getBlockFace()) {
            case NORTH -> event.setBlock(block.withProperty("facing", "east"));
            case EAST -> event.setBlock(block.withProperty("facing", "south"));
            case SOUTH -> event.setBlock(block.withProperty("facing", "west"));
            // West and others default to north
            default -> event.setBlock(block.withProperty("facing", "north"));
        }
        String attachment = switch (event.getBlockFace()) {
            case TOP -> "ceiling";
            case BOTTOM -> "floor";
            default -> "floor";
            // TODO: Handle double_wall and single_wall attachments
        };
        String facing = switch (event.getBlockFace()) {
            case TOP, BOTTOM -> "north";
            default -> event.getBlockFace().getOppositeFace().name().toLowerCase(Locale.ROOT);
        };
        HashMap<String, String> properties = new HashMap<>();
        properties.put("facing", facing);
        properties.put("powered", "false");
        properties.put("attachment", attachment);

        event.setBlock(block.withProperties(properties));
    }
}
