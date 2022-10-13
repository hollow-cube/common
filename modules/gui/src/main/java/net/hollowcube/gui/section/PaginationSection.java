package net.hollowcube.gui.section;

import net.hollowcube.gui.GUI;
import net.hollowcube.gui.actions.SectionAction;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PaginationSection implements Section {

    private List<ItemStack[]> pages;
    private int currentPageIndex;

    public void movePageCounter(int delta) {
        currentPageIndex = Math.abs(currentPageIndex + delta) % pages.size();
    }

    @Override
    public ItemStack[] getItemStacks() {
        return pages.get(currentPageIndex);
    }

    @Override
    public int xSize() {
        return 0;
    }

    @Override
    public int ySize() {
        return 0;
    }

    @Override
    public void addSectionAction(int index, @NotNull SectionAction action) {

    }

    @Override
    public boolean runSectionAction(int index, Player player, GUI parentGUI, ClickType type, ItemStack clickedItem) {
        return false;
    }
}
