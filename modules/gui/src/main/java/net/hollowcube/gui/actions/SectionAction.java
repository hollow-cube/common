package net.hollowcube.gui.actions;

import net.hollowcube.gui.GUI;
import net.hollowcube.gui.section.Section;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;

@FunctionalInterface
public interface SectionAction {
    /**
     *
     * @param player
     * @param parentGUI
     * @param section
     * @param clickType
     * @param clickedItem
     * @return true if the item should be grabbed out of the section, false if it should stay
     */
    boolean onClick(Player player, GUI parentGUI, Section section, ClickType clickType, ItemStack clickedItem);
}
