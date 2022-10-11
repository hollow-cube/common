package net.hollowcube.gui.actions;

import net.hollowcube.gui.section.Section;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;

@FunctionalInterface
public interface SectionAction {
    void onClick(Player player, Section section, ClickType clickType, ItemStack clickedItem);
}
