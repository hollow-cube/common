package net.hollowcube.block.placement;

import net.minestom.server.coordinate.Point;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;

import java.util.HashMap;

public class BlockPlaceMechanicDoor {

    public static void onPlace(Block block, PlayerBlockPlaceEvent event) {
        if (event.getBlockFace() != BlockFace.TOP) {
            event.setCancelled(true);
            return;
        }
        Point upperBlockPos = event.getBlockPosition().add(0, 1, 0);
        if (event.getInstance().getBlock(upperBlockPos).isAir()) {
            // We have space to place the other door half
            HashMap<String, String> properties = new HashMap<>();
            properties.put("half", "upper");
            properties.put("open", "false");
            properties.put("powered", "false");
            properties.put("hinge", "left");
            // TODO: Properly handle hinge
            /*
            By default, a door's "hinge" appears on the side of the half of the block that the player pointed at when placing and its "handle" on the opposite side, but the hinge is forced to other side by:
            Placing a door besides another door (creating a double door where both doors open away from each other)
            Placing a door between a full solid and any opaque block (top or bottom), making the hinge appear to attach to the solid block.
             */
            // Calculate facing direction
            var playerDir = event.getPlayer().getPosition().direction();
            double absX = Math.abs(playerDir.x());
            double absZ = Math.abs(playerDir.z());

            if (absX > absZ) {
                if (playerDir.x() > 0) {
                    properties.put("facing", "west");
                } else {
                    properties.put("facing", "east");
                }
            } else {
                if (playerDir.z() > 0) {
                    properties.put("facing", "north");
                } else {
                    properties.put("facing", "south");
                }
            }
            event.getInstance().setBlock(upperBlockPos, block.withProperties(properties));

            properties.put("half", "lower");
            event.setBlock(block.withProperties(properties));
        } else {
            event.setCancelled(true);
        }
    }
}
