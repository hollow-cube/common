package net.hollowcube.gui;

import net.hollowcube.gui.section.Section;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface GUI {

    /**
     * Switches the page index by the specified amount. The inventory will automatically wrap around from the last page to the first page and vice versa
     * @param amount The amount to change the page index by
     */
    void changePage(int amount);

    void addSection(int xOffset, int yOffset, @NotNull Section section);

    /**
     * Set the item to display when there isn't a section in its place, defaults to air
     * @param item the item to display
     */
    void setDefaultItem(@NotNull ItemStack item);
}
