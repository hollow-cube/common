package net.hollowcube.util.schem;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TestSchematicWriter {

    @Test
    void readWriteReadTest() throws Exception {
        var schemFile = getClass().getClassLoader().getResourceAsStream("big.schem");
        assertNotNull(schemFile);
        var schem = SchematicReader.read(schemFile);

        var tempFile = Files.createTempDirectory("schem-test").resolve("big.schem");
        SchematicWriter.write(schem, tempFile);

        var schem2 = SchematicReader.read(tempFile);
        assertEquals(schem, schem2);
    }
}
