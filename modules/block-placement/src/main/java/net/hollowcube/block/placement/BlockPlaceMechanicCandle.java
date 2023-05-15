package net.hollowcube.block.placement;

import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.instance.block.Block;

public class BlockPlaceMechanicCandle {

    public static void onPlace(Block block, PlayerBlockPlaceEvent event) {
        if (block.compare(Block.CAKE)) {
            // There must be a better way
            Block toPlace = event.getBlock();
            if (toPlace.compare(Block.CANDLE)) {
                event.setBlock(Block.CANDLE_CAKE);
            } else if (toPlace.compare(Block.WHITE_CANDLE)) {
                event.setBlock(Block.WHITE_CANDLE_CAKE);
            } else if (toPlace.compare(Block.ORANGE_CANDLE)) {
                event.setBlock(Block.ORANGE_CANDLE_CAKE);
            } else if (toPlace.compare(Block.MAGENTA_CANDLE)) {
                event.setBlock(Block.MAGENTA_CANDLE_CAKE);
            } else if (toPlace.compare(Block.LIGHT_BLUE_CANDLE)) {
                event.setBlock(Block.LIGHT_BLUE_CANDLE_CAKE);
            } else if (toPlace.compare(Block.YELLOW_CANDLE)) {
                event.setBlock(Block.YELLOW_CANDLE_CAKE);
            } else if (toPlace.compare(Block.LIME_CANDLE)) {
                event.setBlock(Block.LIME_CANDLE_CAKE);
            } else if (toPlace.compare(Block.PINK_CANDLE)) {
                event.setBlock(Block.PINK_CANDLE_CAKE);
            } else if (toPlace.compare(Block.GRAY_CANDLE)) {
                event.setBlock(Block.GRAY_CANDLE_CAKE);
            } else if (toPlace.compare(Block.LIGHT_GRAY_CANDLE)) {
                event.setBlock(Block.LIGHT_GRAY_CANDLE_CAKE);
            } else if (toPlace.compare(Block.CYAN_CANDLE)) {
                event.setBlock(Block.CYAN_CANDLE_CAKE);
            } else if (toPlace.compare(Block.PURPLE_CANDLE)) {
                event.setBlock(Block.PURPLE_CANDLE_CAKE);
            } else if (toPlace.compare(Block.BLUE_CANDLE)) {
                event.setBlock(Block.BLUE_CANDLE_CAKE);
            } else if (toPlace.compare(Block.BROWN_CANDLE)) {
                event.setBlock(Block.BROWN_CANDLE_CAKE);
            } else if (toPlace.compare(Block.GREEN_CANDLE)) {
                event.setBlock(Block.GREEN_CANDLE_CAKE);
            } else if (toPlace.compare(Block.RED_CANDLE)) {
                event.setBlock(Block.RED_CANDLE_CAKE);
            } else if (toPlace.compare(Block.BLACK_CANDLE)) {
                event.setBlock(Block.BLACK_CANDLE_CAKE);
            } else {
                event.consumeBlock(false);
            }
        } else if (HCPlacementRules.MINECRAFT_CANDLES.contains(block.namespace()) && block.compare(event.getBlock())) {
            int amount = Integer.parseInt(block.getProperty("candles"));
            if (amount < 4) {
                event.setBlock(block.withProperty("candles", String.valueOf(amount) + 1));
            } else {
                event.consumeBlock(false);
            }
        }
    }
}
