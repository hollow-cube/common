package net.hollowcube.gui;

import net.hollowcube.gui.section.Section;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface GUI {
    void addSection(int xOffset, int yOffset, @NotNull Section section);

    /**
     * Set the item to display when there isn't a section in its place, defaults to air
     * @param item the item to display
     */
    void setDefaultItem(@NotNull ItemStack item);
}
