package net.hollowcube.gui.section;

import net.hollowcube.gui.actions.SectionAction;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface Section {

    ItemStack[] getItemStacks();

    /**
     * Returns whether this section can have items manipulated in it
     * @return true if this section can be changed, false if it cannot be
     */
    boolean isModifiable();

    int xSize();

    int ySize();

    void addSectionAction(int index, @NotNull SectionAction action);

    void runSectionAction(int index, Player player, ClickType type);
}
