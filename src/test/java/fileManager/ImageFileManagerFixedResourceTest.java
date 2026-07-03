package fileManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import javafx.scene.image.Image;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ImageFileManagerFixedResourceTest {

  static {
    System.setProperty("java.awt.headless", "true");
  }

  @TempDir
  Path tempDirectory;

  @Test
  void importsFixedPngResource() throws IOException, URISyntaxException {
    ImageFileManager manager = new ImageFileManager();

    Image image = manager.importFile(resourceFile("sample-fixed.png"));

    assertEquals(12.0, image.getWidth());
    assertEquals(8.0, image.getHeight());
    assertFalse(image.isError());
  }

  @Test
  void importsFixedJpgResource() throws IOException, URISyntaxException {
    ImageFileManager manager = new ImageFileManager();

    Image image = manager.importFile(resourceFile("sample-fixed.jpg"));

    assertEquals(10.0, image.getWidth());
    assertEquals(7.0, image.getHeight());
  }

  @Test
  void importsFixedJpegResource() throws IOException, URISyntaxException {
    ImageFileManager manager = new ImageFileManager();

    Image image = manager.importFile(resourceFile("sample-fixed.jpeg"));

    assertEquals(9.0, image.getWidth());
    assertEquals(6.0, image.getHeight());
  }

  @Test
  void importsFixedPngResourceWithUppercaseExtension()
      throws IOException, URISyntaxException {
    ImageFileManager manager = new ImageFileManager();

    Image image = manager.importFile(resourceFile("sample-fixed-uppercase.PNG"));

    assertEquals(5.0, image.getWidth());
    assertEquals(4.0, image.getHeight());
  }

  @Test
  void exportsImageLoadedFromFixedResourceAsPng()
      throws IOException, URISyntaxException {
    ImageFileManager manager = new ImageFileManager();
    Image image = manager.importFile(resourceFile("sample-fixed.png"));
    File outputFile = tempDirectory.resolve("fixed-output.png").toFile();

    manager.exportFile(outputFile, image);

    assertTrue(outputFile.exists());
    assertEquals(12, ImageIO.read(outputFile).getWidth());
    assertEquals(8, ImageIO.read(outputFile).getHeight());
  }

  @Test
  void supportsFixedResourceImageExtensions() throws URISyntaxException {
    ImageFileManager manager = new ImageFileManager();

    assertTrue(manager.isSupportedFileType(resourceFile("sample-fixed.png")));
    assertTrue(manager.isSupportedFileType(resourceFile("sample-fixed.jpg")));
    assertTrue(manager.isSupportedFileType(resourceFile("sample-fixed.jpeg")));
    assertTrue(manager.isSupportedFileType(resourceFile("sample-fixed-uppercase.PNG")));
  }

  private File resourceFile(String fileName) throws URISyntaxException {
    URL resource = getClass().getResource("/fileManager/images/" + fileName);

    assertNotNull(resource);

    return new File(resource.toURI());
  }
}
