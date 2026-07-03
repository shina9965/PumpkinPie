package fileManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ImageFileManagerTest {

  static {
    System.setProperty("java.awt.headless", "true");
  }

  @TempDir
  Path tempDirectory;

  @Test
  void importsPngImage() throws IOException {
    ImageFileManager manager = new ImageFileManager();
    File imageFile = createImageFile("sample.png", "png", 12, 8);

    Image image = manager.importFile(imageFile);

    assertEquals(12.0, image.getWidth());
    assertEquals(8.0, image.getHeight());
    assertFalse(image.isError());
  }

  @Test
  void importsJpgImage() throws IOException {
    ImageFileManager manager = new ImageFileManager();
    File imageFile = createImageFile("sample.jpg", "jpg", 10, 7);

    Image image = manager.importFile(imageFile);

    assertEquals(10.0, image.getWidth());
    assertEquals(7.0, image.getHeight());
  }

  @Test
  void importsJpegImage() throws IOException {
    ImageFileManager manager = new ImageFileManager();
    File imageFile = createImageFile("sample.jpeg", "jpeg", 9, 6);

    Image image = manager.importFile(imageFile);

    assertEquals(9.0, image.getWidth());
    assertEquals(6.0, image.getHeight());
  }

  @Test
  void importsPngImageWithUppercaseExtension() throws IOException {
    ImageFileManager manager = new ImageFileManager();
    File imageFile = createImageFile("sample.PNG", "png", 5, 4);

    Image image = manager.importFile(imageFile);

    assertEquals(5.0, image.getWidth());
    assertEquals(4.0, image.getHeight());
  }

  @Test
  void exportsJavaFxImageAsPng() throws IOException {
    ImageFileManager manager = new ImageFileManager();
    Image image = createFxImage(11, 9);
    File outputFile = tempDirectory.resolve("result.png").toFile();

    manager.exportFile(outputFile, image);

    assertTrue(outputFile.exists());
    assertEquals(11, ImageIO.read(outputFile).getWidth());
    assertEquals(9, ImageIO.read(outputFile).getHeight());
  }

  @Test
  void importsExportedPng() throws IOException {
    ImageFileManager manager = new ImageFileManager();
    Image image = createFxImage(13, 10);
    File outputFile = tempDirectory.resolve("reloaded.png").toFile();

    manager.exportFile(outputFile, image);
    Image reloadedImage = manager.importFile(outputFile);

    assertEquals(13.0, reloadedImage.getWidth());
    assertEquals(10.0, reloadedImage.getHeight());
  }

  @Test
  void appendsPngExtensionOnExport() throws IOException {
    ImageFileManager manager = new ImageFileManager();
    Image image = createFxImage(3, 2);
    File outputFileWithoutExtension =
        tempDirectory.resolve("result").toFile();

    manager.exportFile(outputFileWithoutExtension, image);

    assertTrue(tempDirectory.resolve("result.png").toFile().exists());
  }

  @Test
  void replacesDifferentExtensionWithPngOnExport() throws IOException {
    ImageFileManager manager = new ImageFileManager();
    Image image = createFxImage(3, 2);
    File jpgOutputFile = tempDirectory.resolve("result.jpg").toFile();

    manager.exportFile(jpgOutputFile, image);

    assertTrue(tempDirectory.resolve("result.png").toFile().exists());
    assertFalse(tempDirectory.resolve("result.jpg.png").toFile().exists());
  }

  @Test
  void rejectsNullFileOnImport() {
    ImageFileManager manager = new ImageFileManager();

    assertThrows(
        IllegalArgumentException.class,
        () -> manager.importFile(null)
    );
  }

  @Test
  void rejectsMissingFileOnImport() {
    ImageFileManager manager = new ImageFileManager();
    File missingFile = tempDirectory.resolve("missing.png").toFile();

    assertThrows(
        IllegalArgumentException.class,
        () -> manager.importFile(missingFile)
    );
  }

  @Test
  void rejectsDirectoryOnImport() throws IOException {
    ImageFileManager manager = new ImageFileManager();
    Path directory = Files.createDirectory(tempDirectory.resolve("images.png"));

    assertThrows(
        IllegalArgumentException.class,
        () -> manager.importFile(directory.toFile())
    );
  }

  @Test
  void rejectsUnsupportedExtensionOnImport() throws IOException {
    ImageFileManager manager = new ImageFileManager();
    File textFile = tempDirectory.resolve("sample.gif").toFile();
    Files.writeString(textFile.toPath(), "not image");

    assertThrows(
        IllegalArgumentException.class,
        () -> manager.importFile(textFile)
    );
  }

  @Test
  void rejectsNonImagePngFile() throws IOException {
    ImageFileManager manager = new ImageFileManager();
    File invalidPngFile = tempDirectory.resolve("invalid.png").toFile();
    Files.writeString(invalidPngFile.toPath(), "not image");

    assertThrows(
        IOException.class,
        () -> manager.importFile(invalidPngFile)
    );
  }

  @Test
  void rejectsBrokenJpegFile() throws IOException {
    ImageFileManager manager = new ImageFileManager();
    File invalidJpegFile = tempDirectory.resolve("broken.jpg").toFile();
    Files.write(invalidJpegFile.toPath(), new byte[] { 0x01, 0x02, 0x03 });

    assertThrows(
        IOException.class,
        () -> manager.importFile(invalidJpegFile)
    );
  }

  @Test
  void rejectsNullImageOnExport() {
    ImageFileManager manager = new ImageFileManager();
    File outputFile = tempDirectory.resolve("result.png").toFile();

    assertThrows(
        IllegalArgumentException.class,
        () -> manager.exportFile(outputFile, null)
    );
  }

  @Test
  void rejectsMissingOutputDirectory() {
    ImageFileManager manager = new ImageFileManager();
    Image image = createFxImage(2, 2);
    File outputFile =
        tempDirectory.resolve("missing").resolve("result.png").toFile();

    assertThrows(
        IllegalArgumentException.class,
        () -> manager.exportFile(outputFile, image)
    );
  }

  @Test
  void rejectsUnwritableOutputDirectory() throws IOException {
    ImageFileManager manager = new ImageFileManager();
    Image image = createFxImage(2, 2);
    File readOnlyDirectory =
        Files.createDirectory(tempDirectory.resolve("readOnly")).toFile();

    boolean changed = readOnlyDirectory.setWritable(false, false);
    assumeTrue(changed && !readOnlyDirectory.canWrite());

    try {
      File outputFile = new File(readOnlyDirectory, "result.png");

      assertThrows(
          IllegalArgumentException.class,
          () -> manager.exportFile(outputFile, image)
      );
    } finally {
      readOnlyDirectory.setWritable(true, false);
    }
  }

  @Test
  void isSupportedFileTypeReturnsTrueOnlyForSupportedExtensions() {
    ImageFileManager manager = new ImageFileManager();

    assertTrue(manager.isSupportedFileType(new File("sample.png")));
    assertTrue(manager.isSupportedFileType(new File("sample.JPG")));
    assertTrue(manager.isSupportedFileType(new File("sample.jpeg")));
    assertFalse(manager.isSupportedFileType(new File("sample.webp")));
    assertFalse(manager.isSupportedFileType(new File(".png")));
    assertFalse(manager.isSupportedFileType(null));
  }

  private File createImageFile(
      String fileName,
      String formatName,
      int width,
      int height
  ) throws IOException {
    File file = tempDirectory.resolve(fileName).toFile();
    BufferedImage bufferedImage = createBufferedImage(width, height);

    ImageIO.write(bufferedImage, formatName, file);

    return file;
  }

  private Image createFxImage(int width, int height) {
    return SwingFXUtils.toFXImage(
        createBufferedImage(width, height),
        null
    );
  }

  private BufferedImage createBufferedImage(int width, int height) {
    BufferedImage bufferedImage = new BufferedImage(
        width,
        height,
        BufferedImage.TYPE_INT_RGB
    );
    Graphics2D graphics = bufferedImage.createGraphics();

    try {
      graphics.setColor(Color.ORANGE);
      graphics.fillRect(0, 0, width, height);
      graphics.setColor(Color.BLUE);
      graphics.fillRect(0, 0, Math.max(1, width / 2), Math.max(1, height / 2));
    } finally {
      graphics.dispose();
    }

    return bufferedImage;
  }
}
