package net.hollowcube.block.placement;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventBinding;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerBlockPlaceEvent;
import net.minestom.server.event.player.PlayerBlockUpdateNeighborEvent;
import net.minestom.server.event.trait.BlockEvent;
import net.minestom.server.gamedata.tags.Tag;
import net.minestom.server.instance.block.Block;

import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public final class HCPlacementRules {

    //TODO:
    // Twisting Vines
    // Weeping Vines
    // Small Dripleaf (convert Y)
    // Big Dripleaf (convert Y)
    // Candles (stacking)
    // Non-collding blocks to place inside player
    // Waterlogged state
    // Sunflower (place upper sunflower)
    // Fern (place upper fern)
    // Beds (place 2nd block)
    // Turtle eggs (stacking)
    // Snow layer combination
    // Tripwire

    /* Filters */

    private static final EventBinding<BlockEvent> STAIRS_BINDING = EventBinding.filtered(EventFilter.BLOCK, HCPlacementRules::isStairs)
            .map(PlayerBlockPlaceEvent.class, BlockPlaceMechanicStairShape::onPlace)
            .map(PlayerBlockUpdateNeighborEvent.class, BlockPlaceMechanicStairShape::onNeighbor)
            .build();

    private static final EventBinding<BlockEvent> WALLS_BINDING = EventBinding.filtered(EventFilter.BLOCK, HCPlacementRules::isWall)
            .map(PlayerBlockPlaceEvent.class, BlockPlaceMechanicWall::onPlace)
            .map(PlayerBlockUpdateNeighborEvent.class, BlockPlaceMechanicWall::onNeighbor)
            .build();

    private static final EventBinding<BlockEvent> SLAB_BINDING = EventBinding.filtered(EventFilter.BLOCK, HCPlacementRules::isSlab)
            .map(PlayerBlockPlaceEvent.class, BlockPlaceMechanicSlab::onPlace)
            .build();

    private static final EventBinding<BlockEvent> BUTTON_BINDING = EventBinding.filtered(EventFilter.BLOCK, HCPlacementRules::isButton)
            .map(PlayerBlockPlaceEvent.class, BlockPlaceMechanicButton::onPlace)
            .build();

    private static final EventBinding<BlockEvent> CHEST_BINDING = EventBinding.filtered(EventFilter.BLOCK, HCPlacementRules::isChest)
            .map(PlayerBlockPlaceEvent.class, BlockPlaceMechanicChestType::onPlace)
            .map(PlayerBlockUpdateNeighborEvent.class, BlockPlaceMechanicChestType::onNeighbor)
            .build();

    private static final EventBinding<BlockEvent> FENCE_BINDING = EventBinding.filtered(EventFilter.BLOCK, HCPlacementRules::isFence)
            .map(PlayerBlockPlaceEvent.class, BlockPlaceMechanicFence::onPlace)
            .map(PlayerBlockUpdateNeighborEvent.class, BlockPlaceMechanicFence::onNeighbor)
            .build();

    private static final EventBinding<BlockEvent> POINTED_DRIPSTONE_BINDING = EventBinding.filtered(EventFilter.BLOCK, HCPlacementRules::isPointedDripstone)
            .map(PlayerBlockPlaceEvent.class, BlockPlaceMechanicPointedDripstone::onPlace)
            .map(PlayerBlockUpdateNeighborEvent.class, BlockPlaceMechanicPointedDripstone::onNeighbor)
            .build();

    private static final EventBinding<BlockEvent> GLOW_LICHEN_BINDING = EventBinding.filtered(EventFilter.BLOCK, HCPlacementRules::isGlowLichen)
            .map(PlayerBlockPlaceEvent.class, BlockPlaceMechanicGlowLichen::onPlace)
            .build();

    private static final EventBinding<BlockEvent> VINE_BINDING = EventBinding.filtered(EventFilter.BLOCK, HCPlacementRules::isVine)
            .map(PlayerBlockPlaceEvent.class, BlockPlaceMechanicVine::onPlace)
            .build();

    private static final EventBinding<BlockEvent> ROTATION_BINDING = EventBinding.filtered(EventFilter.BLOCK, HCPlacementRules::hasRotation)
            .map(PlayerBlockPlaceEvent.class, BlockPlaceMechanicRotation::onPlace)
            .build();

    private static final EventBinding<BlockEvent> AXIS_BINDING = EventBinding.filtered(EventFilter.BLOCK, HCPlacementRules::hasAxis)
            .map(PlayerBlockPlaceEvent.class, BlockPlaceMechanicAxis::onPlace)
            .build();

    private static final EventBinding<BlockEvent> ANVIL_BINDING = EventBinding.filtered(EventFilter.BLOCK, HCPlacementRules::isAnvil)
            .map(PlayerBlockPlaceEvent.class, BlockPlaceMechanicAnvil::onPlace)
            .build();

    private static final EventBinding<BlockEvent> BELL_BINDING = EventBinding.filtered(EventFilter.BLOCK, block -> block.compare(Block.BELL))
            .map(PlayerBlockPlaceEvent.class, BlockPlaceMechanicBell::onPlace)
            .build();

    private static final EventBinding<BlockEvent> DOOR_BINDING = EventBinding.filtered(EventFilter.BLOCK, HCPlacementRules::isDoor)
            .map(PlayerBlockPlaceEvent.class, BlockPlaceMechanicDoor::onPlace)
            .build();


    private static final EventBinding<BlockEvent> HALF_BINDING = EventBinding.filtered(EventFilter.BLOCK, HCPlacementRules::hasHalf)
            .map(PlayerBlockPlaceEvent.class, BlockPlaceMechanicHalf::onPlace)
            .build();

    private static final EventBinding<BlockEvent> WALL_REPLACEMENT_BINDING =
            EventBinding.filtered(EventFilter.BLOCK, BlockPlaceMechanicWallReplacement::shouldReplace)
                    .map(PlayerBlockPlaceEvent.class, BlockPlaceMechanicWallReplacement::onPlace)
                    .build();

    private static final EventBinding<BlockEvent> ROTATION_16_BINDING =
            EventBinding.filtered(EventFilter.BLOCK, HCPlacementRules::hasRotation16)
                    .map(PlayerBlockPlaceEvent.class, BlockPlaceMechanicRotation16::onPlace)
                    .build();

    /* Checks */

    public static final Tag MINECRAFT_STAIRS = Objects.requireNonNull(MinecraftServer.getTagManager().getTag(Tag.BasicType.BLOCKS, "minecraft:stairs"));

    private static boolean isStairs(Block block) {
        return MINECRAFT_STAIRS.contains(block.namespace());
    }

    public static final Tag MINECRAFT_WALLS = Objects.requireNonNull(MinecraftServer.getTagManager().getTag(Tag.BasicType.BLOCKS, "minecraft:walls"));


    public static boolean isWall(Block block) {
        return MINECRAFT_WALLS.contains(block.namespace());
    }

    public static final Tag MINECRAFT_SLABS = Objects.requireNonNull(MinecraftServer.getTagManager().getTag(Tag.BasicType.BLOCKS, "minecraft:slabs"));

    private static boolean isSlab(Block block) {
        return MINECRAFT_SLABS.contains(block.namespace());
    }

    public static final Tag MINECRAFT_BUTTONS = Objects.requireNonNull(MinecraftServer.getTagManager().getTag(Tag.BasicType.BLOCKS, "minecraft:buttons"));

    private static boolean isButton(Block block) {
        return MINECRAFT_BUTTONS.contains(block.namespace()) || block.compare(Block.LEVER);
    }

    private static boolean isChest(Block block) {
        return block.compare(Block.CHEST) || block.compare(Block.TRAPPED_CHEST);
    }

    public static final Tag MINECRAFT_FENCES = Objects.requireNonNull(MinecraftServer.getTagManager().getTag(Tag.BasicType.BLOCKS, "minecraft:fences"));

    private static boolean isFence(Block block) {
        return MINECRAFT_FENCES.contains(block.namespace());
    }

    public static final Tag MINECRAFT_WALL_SIGNS = Objects.requireNonNull(MinecraftServer.getTagManager().getTag(Tag.BasicType.BLOCKS, "minecraft:wall_signs"));

    public static boolean isWallSign(Block block) {
        return MINECRAFT_WALL_SIGNS.contains(block.namespace());
    }

    private static boolean isPointedDripstone(Block block) {
        return block.compare(Block.POINTED_DRIPSTONE);
    }

    private static boolean isGlowLichen(Block block) {
        return block.compare(Block.GLOW_LICHEN);
    }

    private static boolean isVine(Block block) {
        return block.compare(Block.VINE);
    }

    private static boolean hasRotation(Block block) {
        return block.getProperty("facing") != null;
    }

    private static boolean hasAxis(Block block) {
        return block.getProperty("axis") != null;
    }

    private static boolean isAnvil(Block block) {
        return block.compare(Block.ANVIL);
    }

    private static boolean hasHalf(Block block) {
        return block.getProperty("half") != null;
    }

    private static boolean hasRotation16(Block block) { return block.getProperty("rotation") != null; }

    private static final Tag MINECRAFT_DOORS = Objects.requireNonNull(MinecraftServer.getTagManager().getTag(Tag.BasicType.BLOCKS, "minecraft:doors"));

    private static boolean isDoor(Block block) {
        return MINECRAFT_DOORS.contains(block.namespace());
    }

    /* Init */

    public static void init() {
        var eventNode = EventNode.type("hc-rotation", EventFilter.BLOCK);
        MinecraftServer.getGlobalEventHandler().addChild(eventNode);
        init(eventNode);
    }

    public static void init(EventNode<BlockEvent> handler) {

        // Replacements
        handler.register(WALL_REPLACEMENT_BINDING);

        // Blockstates
        handler.register(ROTATION_BINDING);
        handler.register(AXIS_BINDING);
        handler.register(HALF_BINDING);

        // Specific blocks
        handler.register(STAIRS_BINDING);
        handler.register(WALLS_BINDING);
        handler.register(SLAB_BINDING);
        handler.register(BUTTON_BINDING);
        handler.register(CHEST_BINDING);
        handler.register(FENCE_BINDING);
        handler.register(GLOW_LICHEN_BINDING);
        handler.register(VINE_BINDING);
        handler.register(POINTED_DRIPSTONE_BINDING);
        handler.register(ANVIL_BINDING);
        handler.register(BELL_BINDING);
        handler.register(DOOR_BINDING);
        handler.register(ROTATION_16_BINDING);

        for (short stateId = 0; stateId < Short.MAX_VALUE; stateId++) {
            Block block = Block.fromStateId(stateId);
            if (block == null) continue;

            BlockPlaceMechanicRotation.updateDataFromBlock(block);
        }
    }

}
