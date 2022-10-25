package net.hollowcube.gui.section;

import net.hollowcube.gui.GUI;
import net.hollowcube.gui.actions.SectionAction;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class PaginationSection implements Section {

    private List<ItemStack[]> pages;
    private int currentPageIndex;
    private final HashMap<Integer, SectionAction> actions = new HashMap<>();

    private final int xSize;
    private final int ySize;

    public PaginationSection(int xSize, int ySize, @NotNull List<ItemStack[]> itemStacks) {
        if (xSize <= 0 || xSize > 9) {
            throw new IllegalArgumentException("Invalid bounds for section x-size (needs to between 1 and 9 inclusive, got " + xSize);
        }
        if (ySize <= 0 || ySize >= 6) {
            throw new IllegalArgumentException("Invalid bounds for section y-size (needs to between 1 and 6 inclusive, got " + ySize);
        }
        this.pages = itemStacks;
        this.xSize = xSize;
        this.ySize = ySize;
    }

    public void movePageCounter(int delta) {
        currentPageIndex = Math.abs(currentPageIndex + delta) % pages.size();
    }

    @Override
    public ItemStack[] getItemStacks() {
        return pages.get(currentPageIndex);
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
        if (index > 0 && index < xSize() * ySize()) {
            actions.put(index, action);
        } else {
            throw new IllegalArgumentException("Tried to add a SectionAction with invalid index " + index);
        }
    }

    @Override
    public boolean runSectionAction(int index, Player player, GUI parentGUI, ClickType type, ItemStack clickedItem) {
        return actions.get(index).onClick(player, parentGUI, this, type, clickedItem);
    }
}
