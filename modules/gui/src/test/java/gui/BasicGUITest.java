package gui;

import net.hollowcube.gui.GUI;
import net.hollowcube.gui.GUIImpl;
import net.hollowcube.gui.section.ImmutableSection;
import net.hollowcube.gui.section.Section;
import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.InventoryType;
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
}
