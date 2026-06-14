package fileManager;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SignalFileManagerTest {

    @TempDir
    Path tempDir;

    @Test
    public void testReadCsvFile() throws IOException {
        Path csvPath = tempDir.resolve("sample.csv");
        Files.writeString(csvPath, "1.0, 2.0, 3.0\n4.0, 5.0");

        SignalFileManager manager = new SignalFileManager();
        double[] result = manager.importFile(csvPath.toFile());

        assertArrayEquals(
                new double[] {1.0, 2.0, 3.0, 4.0, 5.0},
                result,
                0.000001
        );
    }

    @Test
    public void testReadTextFile() throws IOException {
        Path textPath = tempDir.resolve("sample.txt");
        Files.writeString(textPath, "1.0\n2.0\n3.0\n");

        SignalFileManager manager = new SignalFileManager();
        double[] result = manager.importFile(textPath.toFile());

        assertArrayEquals(
                new double[] {1.0, 2.0, 3.0},
                result,
                0.000001
        );
    }

    @Test
    public void testReadBinaryFile() throws IOException {
        Path binaryPath = tempDir.resolve("sample.bin");

        try (DataOutputStream outputStream = new DataOutputStream(
                Files.newOutputStream(binaryPath)
        )) {
            outputStream.writeFloat(1.0f);
            outputStream.writeFloat(2.0f);
            outputStream.writeFloat(3.0f);
        }

        SignalFileManager manager = new SignalFileManager();
        double[] result = manager.importFile(binaryPath.toFile());

        assertArrayEquals(
                new double[] {1.0, 2.0, 3.0},
                result,
                0.000001
        );
    }

    @Test
    public void testExportBinaryFile() throws IOException {
        Path outputPath = tempDir.resolve("output.bin");

        SignalFileManager manager = new SignalFileManager();
        manager.exportFile(
                outputPath.toFile(),
                new double[] {1.0, 2.0, 3.0}
        );

        double[] result = manager.importFile(outputPath.toFile());

        assertArrayEquals(
                new double[] {1.0, 2.0, 3.0},
                result,
                0.000001
        );
    }

    @Test
    public void testUnsupportedExtension() throws IOException {
        Path pngPath = tempDir.resolve("sample.png");
        Files.writeString(pngPath, "1.0, 2.0, 3.0");

        SignalFileManager manager = new SignalFileManager();

        assertThrows(
                IllegalArgumentException.class,
                () -> manager.importFile(pngPath.toFile())
        );
    }

    @Test
    public void testInvalidTextFile() throws IOException {
        Path csvPath = tempDir.resolve("invalid.csv");
        Files.writeString(csvPath, "1.0, abc, 3.0");

        SignalFileManager manager = new SignalFileManager();

        assertThrows(
                IllegalArgumentException.class,
                () -> manager.importFile(csvPath.toFile())
        );
    }

    @Test
    public void testInvalidBinaryFileSize() throws IOException {
        Path binaryPath = tempDir.resolve("invalid.bin");
        Files.write(binaryPath, new byte[] {1, 2, 3});

        SignalFileManager manager = new SignalFileManager();

        assertThrows(
                IllegalArgumentException.class,
                () -> manager.importFile(binaryPath.toFile())
        );
    }

    @Test
    public void testNullFile() {
        SignalFileManager manager = new SignalFileManager();

        assertThrows(
                IllegalArgumentException.class,
                () -> manager.importFile(null)
        );
    }

    @Test
    public void testSupportedFileType() {
        File file = tempDir.resolve("sample.csv").toFile();

        SignalFileManager manager = new SignalFileManager();

        assertTrue(manager.isSupportedFileType(file));
    }
}

