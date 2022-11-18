package net.hollowcube.motion.util;

import net.hollowcube.test.MockBlockGetter;
import net.hollowcube.util.schem.Rotation;
import net.hollowcube.util.schem.Schematic;
import net.hollowcube.util.schem.SchematicReader;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.HashMap;

import static com.google.common.truth.Truth.assertThat;

public class SchemBlockGetter implements Block.Getter {
    private final Schematic schematic;
    private final MockBlockGetter delegate;

    private Point start;
    private Point goal;

    public SchemBlockGetter(String name) {
        try {
            schematic = SchematicReader.read(getClass().getClassLoader().getResourceAsStream(String.format("schem/%s.schem", name)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        var blocks = new HashMap<Point, Block>();
        delegate = new MockBlockGetter(blocks);

        schematic.apply(Rotation.NONE, (pos, block) -> {
            if (block.id() == Block.GREEN_CANDLE.id()) {
                start = pos;
            } else if (block.id() == Block.RED_CANDLE.id()) {
                goal = pos;
            } else {
                blocks.put(pos, block);
            }
        });

        assertThat(start).isNotNull();
        assertThat(goal).isNotNull();
    }

    public Point start() {
        return start;
    }

    public Point goal() {
        return goal;
    }

    @Override
    public @UnknownNullability Block getBlock(int x, int y, int z, @NotNull Condition condition) {
        return isInside(new Vec(x, y, z)) ? delegate.getBlock(x, y, z, condition) : Block.STONE;
    }

    // Returns true if the pos is inside the schematic (taking into account offset and size)
    public boolean isInside(Point pos) {
        if (pos.x() < schematic.offset().x() || pos.x() >= schematic.offset().x() + schematic.size().x()) return false;
        if (pos.y() < schematic.offset().y() || pos.y() >= schematic.offset().y() + schematic.size().y()) return false;
        if (pos.z() < schematic.offset().z() || pos.z() >= schematic.offset().z() + schematic.size().z()) return false;
        return true;
    }
}
