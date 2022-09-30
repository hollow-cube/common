package net.hollowcube.gui;

import net.hollowcube.gui.actions.GUIAction;
import net.hollowcube.gui.section.Section;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class SinglePageGUI extends Inventory implements GUI {

    private ItemStack defaultItem = ItemStack.AIR;

    // Integer represents offset of the section
    private final HashMap<Integer, Section> sectionMap = new HashMap<>();
    private final HashMap<Integer, GUIAction> actionMap = new HashMap<>();

    public SinglePageGUI(@NotNull InventoryType inventoryType, @NotNull Component title) {
        super(inventoryType, title);
    }

    @Override
    public void changePage(int amount) {
        // Do nothing, we're a single page
    }

    @Override
    public void addSection(int xOffset, int yOffset, @NotNull Section section) {
        if (xOffset < 0 || yOffset < 0) {
            throw new IllegalArgumentException("Cannot have a negative offset");
        }
        // Calculate the maximum size of this inventory, so we can determine if we can fit this section in here
        int maxXWidth = getMaxWidth();
        int maxYHeight = getMaxHeight();

        if (xOffset + section.xSize() > maxXWidth) {
            throw new IllegalArgumentException("Tried to add a section, but its x-size was too big!");
        }

        if (yOffset + section.ySize() > maxYHeight) {
            throw new IllegalArgumentException("Tried to add a section, but its y-size was too big!");
        }

        // Make sure it does not overlap with another section
        for (int x = xOffset; x < xOffset + section.xSize(); x++) {
            for (int y = yOffset; y < yOffset + section.ySize(); y++) {
                if (getSectionEntry(x, y) != null) {
                    throw new IllegalArgumentException("Tried to add a section, there was already a section present at (" + x + ", " + y + ")!");
                }
            }
        }
        // We can add it safely
        int index = yOffset * maxXWidth + xOffset;
        sectionMap.put(index, section);
    }

    @Override
    public void setDefaultItem(@NotNull ItemStack item) {
        defaultItem = item;
    }

    @Override
    public @NotNull ItemStack[] getItemStacks() {
        throw new UnsupportedOperationException("TODO");
        // TODO combine sections together into entire array
    }

    private @Nullable Map.Entry<Integer, Section> getSectionEntry(int x, int y) {
        for (var entry : sectionMap.entrySet()) {
            int xOffset = entry.getKey() % getMaxWidth();
            int yOffset = entry.getKey() / getMaxWidth();
            if (x >= xOffset && x < xOffset + entry.getValue().xSize() && y >= yOffset && y < yOffset + entry.getValue().ySize()) {
                return entry;
            }
        }
        return null;
    }

    // Note: Sometimes I had to make some odd choices since things aren't always rectangular, but hey, pretend the furnace is a 1x3 rectangle like a vertical anvil
    // Also the crafting table is blind to the output slot, but please use something else if you're doing that
    private int getMaxWidth() {
        return switch (getInventoryType()) {
            case BEACON, BLAST_FURNACE, BREWING_STAND, FURNACE, SMOKER, CARTOGRAPHY -> 1;
            case LOOM, ENCHANTMENT, STONE_CUTTER -> 2;
            case ANVIL, WINDOW_3X3, CRAFTING, GRINDSTONE, MERCHANT, SMITHING -> 3;
            case HOPPER -> 5;
            case CHEST_1_ROW, CHEST_2_ROW, CHEST_3_ROW, CHEST_4_ROW, CHEST_5_ROW, CHEST_6_ROW, SHULKER_BOX -> 9;
            case LECTERN -> throw new IllegalStateException("Single page GUI was created with a Lecturn inventory type!");
        };
    }

    private int getMaxHeight() {
        return switch (getInventoryType()) {
            case ANVIL, CHEST_1_ROW, BEACON, ENCHANTMENT, GRINDSTONE, HOPPER, MERCHANT, SMITHING, STONE_CUTTER -> 1;
            case CHEST_2_ROW, LOOM -> 2;
            case CHEST_3_ROW, WINDOW_3X3, BLAST_FURNACE, CRAFTING, FURNACE, SHULKER_BOX, SMOKER, CARTOGRAPHY -> 3;
            case CHEST_4_ROW -> 4;
            case CHEST_5_ROW, BREWING_STAND -> 5;
            case CHEST_6_ROW -> 6;
            case LECTERN -> throw new IllegalStateException("Single page GUI was created with a Lecturn inventory type!");
        };
    }

    // ON CLICK HANDLING

    @Override
    public boolean leftClick(@NotNull Player player, int slot) {
        boolean result = super.leftClick(player, slot);
        if (result) {
            return tryRunAction(slot, player, ClickType.LEFT_CLICK);
        }
        return false;
    }

    @Override
    public boolean rightClick(@NotNull Player player, int slot) {
        boolean result = super.rightClick(player, slot);
        if (result) {
            return tryRunAction(slot, player, ClickType.RIGHT_CLICK);
        }
        return false;
    }

    @Override
    public boolean shiftClick(@NotNull Player player, int slot) {
        boolean result = super.shiftClick(player, slot);
        if (result) {
            return tryRunAction(slot, player, ClickType.SHIFT_CLICK);
        }
        return false;
    }

    /**
     *
     * @param slot
     * @param player
     * @param type
     * @return true if the player can grab the item, false otherwise
     */
    private boolean tryRunAction(int slot, @NotNull Player player, ClickType type) {
        // Sections have priority
        int x = slot % getMaxWidth();
        int y = slot / getMaxWidth();
        var entry = getSectionEntry(x, y);
        if (entry != null) {
            // Calculate index
            int offset = slot - entry.getKey();
            // We now have the slot difference between our starting point of the section and the ending point of the section, now to figure out section position
            int xOffset = offset % getMaxWidth();
            int yOffset = offset / getMaxWidth();
            entry.getValue().runSectionAction(
                    yOffset * entry.getValue().ySize() + xOffset,
                    player, type);
            return entry.getValue().isModifiable();
        } else {
            // See if we have any actions at the index
            if (actionMap.containsKey(slot)) {
                actionMap.get(slot).onClick(player, this, type);
            }
        }
        return false;
    }
}
