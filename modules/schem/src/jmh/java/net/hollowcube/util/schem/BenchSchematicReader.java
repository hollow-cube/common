package net.hollowcube.util.schem;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class BenchSchematicReader {

    private static byte[] data;

    static {
        try {
            data = Files.readAllBytes(Path.of("todo"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//        try (var is = BenchSchematicReader.class.getResourceAsStream("mm-spawn-1-27.schem")) {
//            assert is != null;
//            data = is.readAllBytes();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }

    @Fork(value = 1, warmups = 1)
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void benchReadFromFile() throws Exception {
        SchematicReader.read(new ByteArrayInputStream(data));
    }

}
