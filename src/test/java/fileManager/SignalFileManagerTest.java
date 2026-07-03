package fileManager;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import org.junit.jupiter.api.Test;

class SignalFileManagerTest {

  @Test
  void importsFixedSampleTxtFile() throws IOException, URISyntaxException {
    SignalFileManager manager = new SignalFileManager();

    assertArrayEquals(
        new double[] {1.0, 2.5, -3.0, 4.25},
        manager.importFile(resourceFile("sample-signal.txt")),
        0.000001);
  }

  @Test
  void importsFixedSampleCsvFile() throws IOException, URISyntaxException {
    SignalFileManager manager = new SignalFileManager();

    assertArrayEquals(
        new double[] {10.0, 20.5, 30.25},
        manager.importFile(resourceFile("sample-signal.csv")),
        0.000001);
  }

  @Test
  void importsFixedSampleBinFile() throws IOException, URISyntaxException {
    SignalFileManager manager = new SignalFileManager();

    assertArrayEquals(
        new double[] {1.25, -2.5, 3.75},
        manager.importFile(resourceFile("sample-signal.bin")),
        0.000001);
  }

  @Test
  void supportsFixedSampleSignalFileTypes() throws URISyntaxException {
    SignalFileManager manager = new SignalFileManager();

    assertTrue(manager.isSupportedFileType(resourceFile("sample-signal.txt")));
    assertTrue(manager.isSupportedFileType(resourceFile("sample-signal.csv")));
    assertTrue(manager.isSupportedFileType(resourceFile("sample-signal.bin")));
  }

  private File resourceFile(String fileName) throws URISyntaxException {
    URL resource = getClass().getResource("/fileManager/signals/" + fileName);

    assertNotNull(resource);

    return new File(resource.toURI());
  }
}
