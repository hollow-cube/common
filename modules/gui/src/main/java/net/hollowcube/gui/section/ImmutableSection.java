package net.hollowcube.gui.section;

import net.hollowcube.gui.actions.SectionAction;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class ImmutableSection implements Section {

    private final ItemStack[] itemStacks;
    private final int xSize;
    private final int ySize;
    private final HashMap<Integer, SectionAction> actions = new HashMap<>();

    public ImmutableSection(int xSize, int ySize, @NotNull List<ItemStack> itemStacks) {
        if (xSize <= 0 || xSize > 9) {
            throw new IllegalArgumentException("Invalid bounds for section x-size (needs to between 1 and 9 inclusive, got " + xSize);
        }
        if (ySize <= 0 || ySize >= 6) {
            throw new IllegalArgumentException("Invalid bounds for section y-size (needs to between 1 and 6 inclusive, got " + ySize);
        }
        this.itemStacks = itemStacks.toArray(new ItemStack[0]);
        this.xSize = xSize;
        this.ySize = ySize;
    }

    @Override
    public ItemStack[] getItemStacks() {
        return itemStacks;
    }

    @Override
    public boolean isModifiable() {
        return false;
    }

    @Override
    public int xSize() {
        return xSize;
    }

    @Override
    public int ySize() {
        return ySize;
    }

    @Override
    public void addSectionAction(int index, @NotNull SectionAction action) {
        if (index > 0 && index < xSize * ySize) {
            actions.put(index, action);
        } else {
            throw new IllegalArgumentException("Tried to add a SectionAction with invalid index " + index);
        }
    }

    @Override
    public void runSectionAction(int index, Player player, ClickType type, ItemStack clickedItem) {
        actions.get(index).onClick(player, this, type, clickedItem);
    }


}
