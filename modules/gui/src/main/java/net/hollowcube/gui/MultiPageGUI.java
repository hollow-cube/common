package net.hollowcube.gui;

import net.hollowcube.gui.section.Section;
import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MultiPageGUI extends Inventory implements GUI {

    private final List<SinglePageGUI> pages = new ArrayList<>();
    private int pageIndex = 0;

    public MultiPageGUI(@NotNull InventoryType inventoryType, @NotNull Component title) {
        super(inventoryType, title);
    }

    @Override
    public void changePage(int amount) {
        if (pages.isEmpty()) {
            throw new IllegalStateException("Tried to change a page in a MultiChangeGUI, but there are no pages!");
        }
        pageIndex = Math.abs((pageIndex + amount) % pages.size());
        // Update the gui so the item stacks are refreshed
        update();
    }

    public void addPage(SinglePageGUI gui) {
        pages.add(gui);
    }

    public void addPage(int index, SinglePageGUI gui) {
        pages.add(index, gui);
    }

    @Override
    public void addSection(int xOffset, int yOffset, @NotNull Section section) {
        if (pages.isEmpty()) {
            throw new IllegalStateException("Tried add a section to a page in a MultiChangeGUI, but there are no pages!");
        }
        pages.get(pageIndex).addSection(xOffset, yOffset, section);
    }

    @Override
    public void setDefaultItem(@NotNull ItemStack item) {
        if (pages.isEmpty()) {
            throw new IllegalStateException("Tried to set the item stacks of a page in a MultiChangeGUI, but there are no pages!");
        }
        pages.get(pageIndex).setDefaultItem(item);
    }

    @Override
    public @NotNull ItemStack[] getItemStacks() {
        if (pages.isEmpty()) {
            throw new IllegalStateException("Tried to get the item stacks of a page in a MultiChangeGUI, but there are no pages!");
        }
        return pages.get(pageIndex).getItemStacks();
    }
}
