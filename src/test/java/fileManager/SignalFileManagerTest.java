package fileManager;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class SignalFileManagerTest {

  @TempDir
  Path tempDirectory;

  @Test
  void importsTxtFile() throws IOException {
    SignalFileManager manager = new SignalFileManager();
    File file = tempDirectory.resolve("sample.txt").toFile();
    Files.writeString(file.toPath(), "1.0, 2.5, 3.0");

    assertArrayEquals(
        new double[] {1.0, 2.5, 3.0},
        manager.importFile(file),
        0.000001);
  }

  @Test
  void importsCsvFile() throws IOException {
    SignalFileManager manager = new SignalFileManager();
    File file = tempDirectory.resolve("sample.csv").toFile();
    Files.writeString(file.toPath(), "1.0,2.0\n3.0");

    assertArrayEquals(
        new double[] {1.0, 2.0, 3.0},
        manager.importFile(file),
        0.000001);
  }

  @Test
  void importsExportedBinFile() throws IOException {
    SignalFileManager manager = new SignalFileManager();
    File file = tempDirectory.resolve("signal.bin").toFile();
    double[] signal = {1.25, -2.5, 3.75};

    manager.exportFile(file, signal);

    assertTrue(file.exists());
    assertArrayEquals(signal, manager.importFile(file), 0.000001);
  }

  @Test
  void rejectsNullFileOnImport() {
    SignalFileManager manager = new SignalFileManager();

    assertThrows(
        IllegalArgumentException.class,
        () -> manager.importFile(null));
  }

  @Test
  void rejectsUnsupportedExtensionOnImport() throws IOException {
    SignalFileManager manager = new SignalFileManager();
    File file = tempDirectory.resolve("sample.png").toFile();
    Files.writeString(file.toPath(), "1.0");

    assertThrows(
        IllegalArgumentException.class,
        () -> manager.importFile(file));
  }

  @Test
  void rejectsNullSignalOnExport() {
    SignalFileManager manager = new SignalFileManager();
    File file = tempDirectory.resolve("signal.bin").toFile();

    assertThrows(
        IllegalArgumentException.class,
        () -> manager.exportFile(file, null));
  }
}
