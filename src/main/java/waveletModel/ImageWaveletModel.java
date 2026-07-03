package waveletModel;

import app.BoolEx;
import fileManager.ImageFileManager;
import java.io.File;
import java.io.IOException;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * 画像ウェーブレット変換に使用するデータモデル。
 * 元画像、変換係数、復元画像を保持し、画像ファイル入出力も扱う。
 */
public class ImageWaveletModel extends WaveletModel {

  private double[][] originalImage;
  private double[][] transformedImage;
  private double[][] reconstructedImage;
  private int originalWidth;
  private int originalHeight;
  private final ImageFileManager imageFileManager;

  public ImageWaveletModel() {
    imageFileManager = new ImageFileManager();
    originalImage = new double[0][0];
    transformedImage = new double[0][0];
    reconstructedImage = new double[0][0];
    originalWidth = 0;
    originalHeight = 0;
  }

  public void loadImage(File file) throws IOException {
    setOriginalImage(imageFileManager.importFile(file));
  }

  public void loadImage(String path) throws IOException {
    loadImage(new File(path));
  }

  public void saveImage(File file) throws IOException {
    double[][][] outputImage = {reconstructedImage};

    BoolEx.ifTrueElse(
        reconstructedImage == null || reconstructedImage.length == 0,
        () -> outputImage[0] = transformedImage);

    imageFileManager.exportFile(file, convertDoubleArrayToImage(outputImage[0]));
  }

  public double[][] removePadding(double[][] image) {
    double[][] trimmed = new double[originalHeight][originalWidth];
    int[] row = {0};

    BoolEx.forTrue(
        0,
        originalHeight,
        () -> {
          System.arraycopy(image[row[0]], 0, trimmed[row[0]], 0, originalWidth);
          row[0]++;
        });

    return trimmed;
  }

  public double[][] getOriginalImage() {
    return originalImage;
  }

  public void setOriginalImage(double[][] image) {
    originalImage = image;
    originalHeight = image.length;
    originalWidth = 0;

    BoolEx.ifTrueElse(
        image.length > 0,
        () -> originalWidth = image[0].length);
  }

  public void setOriginalImage(Image image) {
    setOriginalImage(convertImageToDoubleArray(image));
  }

  public double[][] getTransformedImage() {
    return transformedImage;
  }

  public void setTransformedImage(double[][] coefficients) {
    transformedImage = coefficients;
  }

  public double[][] getReconstructedImage() {
    return reconstructedImage;
  }

  public void setReconstructedImage(double[][] image) {
    reconstructedImage = image;
  }

  public Image getReconstructedFxImage() {
    return convertDoubleArrayToImage(reconstructedImage);
  }

  public int getOriginalWidth() {
    return originalWidth;
  }

  public int getOriginalHeight() {
    return originalHeight;
  }

  private double[][] convertImageToDoubleArray(Image image) {
    BoolEx.ifTrueElse(
        image == null,
        () -> {
          throw new IllegalArgumentException("画像がnullです。");
        });

    int width = (int) Math.round(image.getWidth());
    int height = (int) Math.round(image.getHeight());
    double[][] result = new double[height][width];
    PixelReader pixelReader = image.getPixelReader();

    BoolEx.ifTrueElse(
        pixelReader == null,
        () -> {
          throw new IllegalArgumentException("画像の画素を読み取れません。");
        });

    int[] row = {0};
    BoolEx.forTrue(
        0,
        height,
        () -> {
          int[] column = {0};
          BoolEx.forTrue(
              0,
              width,
              () -> {
                Color color = pixelReader.getColor(column[0], row[0]);
                result[row[0]][column[0]] =
                    (0.299 * color.getRed()
                        + 0.587 * color.getGreen()
                        + 0.114 * color.getBlue()) * 255.0;
                column[0]++;
              });
          row[0]++;
        });

    return result;
  }

  private Image convertDoubleArrayToImage(double[][] data) {
    BoolEx.ifTrueElse(
        data == null || data.length == 0 || data[0].length == 0,
        () -> {
          throw new IllegalArgumentException("画像データが空です。");
        });

    int height = data.length;
    int width = data[0].length;
    WritableImage image = new WritableImage(width, height);
    PixelWriter pixelWriter = image.getPixelWriter();
    int[] row = {0};

    BoolEx.forTrue(
        0,
        height,
        () -> {
          int[] column = {0};
          BoolEx.forTrue(
              0,
              width,
              () -> {
                double gray = clamp(data[row[0]][column[0]]) / 255.0;
                pixelWriter.setColor(column[0], row[0], Color.gray(gray));
                column[0]++;
              });
          row[0]++;
        });

    return image;
  }

  private double clamp(double value) {
    double[] result = {value};

    BoolEx.ifTrueElse(
        result[0] < 0.0,
        () -> result[0] = 0.0);

    BoolEx.ifTrueElse(
        result[0] > 255.0,
        () -> result[0] = 255.0);

    return result[0];
  }
}
