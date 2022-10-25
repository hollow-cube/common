package net.hollowcube.gui.section;

import net.hollowcube.gui.GUI;
import net.hollowcube.gui.actions.SectionAction;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;

// Intended for use in menu nesting
public class HistorySection implements Section {

    private final HashMap<Integer, SectionAction> actions = new HashMap<>();
    private final int xSize;
    private final int ySize;
    private final ItemStack[] itemStacks;

    private final Stack<Section> historyList = new Stack<>();

    public HistorySection(int xSize, int ySize, @NotNull List<ItemStack> itemStacks) {
        if (xSize <= 0 || xSize > 9) {
            throw new IllegalArgumentException("Invalid bounds for section x-size (needs to between 1 and 9 inclusive, got " + xSize);
        }
        if (ySize <= 0 || ySize >= 6) {
            throw new IllegalArgumentException("Invalid bounds for section y-size (needs to between 1 and 6 inclusive, got " + ySize);
        }
        this.xSize = xSize;
        this.ySize = ySize;
        this.itemStacks = itemStacks.toArray(new ItemStack[0]);
    }

    @Override
    public ItemStack[] getItemStacks() {
        if(historyList.isEmpty()) {
            return itemStacks;
        } else {
            return historyList.peek().getItemStacks();
        }
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
        actions.put(index, action);
    }

    @Override
    public boolean runSectionAction(int index, Player player, GUI parentGUI, ClickType type, ItemStack clickedItem) {
        return actions.get(index).onClick(player, parentGUI, this, type, clickedItem);
    }

    public void addToHistory(Section section) {
        historyList.push(section);
    }

    public void goBack() {
        historyList.pop();
    }
}
