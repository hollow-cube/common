package gui;

import net.hollowcube.gui.GUI;
import net.hollowcube.gui.GUIImpl;
import net.hollowcube.gui.section.ImmutableSection;
import net.hollowcube.gui.section.Section;
import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BasicGUITest {

    @Test
    public void testGUIConstruct() {
        GUI gui = new GUIImpl(InventoryType.FURNACE, Component.text("test"));
        GUI gui2 = new GUIImpl(InventoryType.CHEST_1_ROW, Component.text("test"));
        GUI gui3 = new GUIImpl(InventoryType.CHEST_5_ROW, Component.text("test"));
    }

    @Test
    public void testAddSection() {
        GUI gui = new GUIImpl(InventoryType.CHEST_2_ROW, Component.text("test"));
        Section section = new ImmutableSection(3, 2, List.of());
        gui.addSection(0, 0, section);
        assertThrows(IllegalArgumentException.class, () -> gui.addSection(8, 0, section));
        assertThrows(IllegalArgumentException.class, () -> gui.addSection(1, 0, section));
        assertThrows(IllegalArgumentException.class, () -> gui.addSection(4, 1, section));
        assertThrows(IllegalArgumentException.class, () -> gui.addSection(-1, 1, section));
        gui.addSection(5, 0, section);
    }

    @Test
    public void testItemStackOrdering() {
        GUI gui = new GUIImpl(InventoryType.CHEST_3_ROW, Component.text("Test"));

        Section section = new ImmutableSection(2, 2, List.of(ItemStack.of(Material.OAK_LOG), ItemStack.of(Material.WOODEN_AXE), ItemStack.of(Material.WOODEN_PICKAXE), ItemStack.of(Material.AIR)));
        gui.addSection(2, 0, section);

        ItemStack[] stacks = gui.getInventory().getItemStacks();

        // Our items at 2, 0 should be oak log, 3, 0 as wooden axe, 2, 1 as wooden pickaxe
        // Translated to a singular index, the indicies would be 2, 3, 11
        assertEquals(Material.OAK_LOG, stacks[2].material());
        assertEquals(Material.WOODEN_AXE, stacks[3].material());
        assertEquals(Material.WOODEN_PICKAXE, stacks[11].material());
    }
}
