import net.hollowcube.util.schem.Rotation;
import net.hollowcube.util.schem.Schematic;
import net.hollowcube.util.schem.SchematicReader;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.instance.InstanceChunkLoadEvent;
import net.minestom.server.event.instance.InstanceChunkUnloadEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.test.Env;
import net.minestom.server.test.EnvTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@EnvTest
public class SchemPasterTest {

    @Test
    public void testSchemPaste(Env env) {
        AtomicInteger chunksLoaded = new AtomicInteger();
        AtomicInteger chunksUnloaded = new AtomicInteger();
        Instance instance = env.createFlatInstance();
        instance.eventNode().addListener(InstanceChunkLoadEvent.class, event -> chunksLoaded.getAndIncrement());
        instance.eventNode().addListener(InstanceChunkUnloadEvent.class, event -> chunksUnloaded.getAndIncrement());
        try {
            Schematic big = SchematicReader.read(Path.of("src/test/resources/big.schem"));
            CompletableFuture<Void> pasteProcess = big.applyToInstance(instance, new Pos(15, 40, 15), true, Rotation.NONE, null);
            pasteProcess.join();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertEquals(2, chunksLoaded.get());
        assertEquals(chunksLoaded.get(), chunksUnloaded.get());
    }
}
