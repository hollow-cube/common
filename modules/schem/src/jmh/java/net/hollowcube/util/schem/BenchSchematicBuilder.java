package net.hollowcube.util.schem;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class BenchSchematicBuilder {

    private static Schematic schematic;
    private static SchematicBuilder builder;

    static {
        try {
            var data = Files.readAllBytes(Path.of("todo"));
            schematic = SchematicReader.read(new ByteArrayInputStream(data));

            builder = new SchematicBuilder();
            schematic.apply(Rotation.NONE, builder::addBlock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Fork(value = 1, warmups = 1)
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void benchBuildToSchematic() {
        builder.toSchematic();
    }

}
