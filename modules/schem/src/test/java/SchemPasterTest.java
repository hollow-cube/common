import net.hollowcube.util.schem.Rotation;
import net.hollowcube.util.schem.Schematic;
import net.hollowcube.util.schem.SchematicReader;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

public class SchemPasterTest {

    @Test
    public void testSchemPaste() {
        MinecraftServer.init();
        Instance instance = MinecraftServer.getInstanceManager().createInstanceContainer();
        try {
            Schematic big = SchematicReader.read(Path.of("src/test/resources/big.schem"));
            big.applyToInstance(instance, new Pos(15, 40, 15), true, Rotation.NONE, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
