package net.hollowcube.gui.actions;

import net.hollowcube.gui.GUI;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.click.ClickType;

@FunctionalInterface
public interface GUIAction {
    void onClick(Player player, GUI gui, ClickType clickType);


    GUIAction INCREMENT_PAGE = (player, inventory, clickType) -> inventory.changePage(1);
    GUIAction DECREMENT_PAGE = (player, inventory, clickType) -> inventory.changePage(-1);
}
