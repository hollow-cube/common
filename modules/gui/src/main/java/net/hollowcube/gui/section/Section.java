package net.hollowcube.gui.section;

import net.hollowcube.gui.GUI;
import net.hollowcube.gui.actions.SectionAction;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface Section {

    ItemStack[] getItemStacks();

    int xSize();

    int ySize();

    void addSectionAction(int index, @NotNull SectionAction action);

    boolean runSectionAction(int index, Player player, GUI parentGUI, ClickType type, ItemStack clickedItem);
}
