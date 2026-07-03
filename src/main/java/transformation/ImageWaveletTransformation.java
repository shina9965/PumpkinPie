package transformation;

import app.BoolEx;
import javafx.scene.image.Image;
import waveletModel.ImageWaveletModel;

public class ImageWaveletTransformation extends WaveletTransformation<ImageWaveletModel> {

  private final ImageWaveletModel imageWaveletModel;

  public ImageWaveletTransformation() {
    imageWaveletModel = new ImageWaveletModel();
  }

  @Override
  public ImageWaveletModel startWaveletTransformation() {
    double[][] original = imageWaveletModel.getOriginalImage();

    BoolEx.ifTrueElse(
        original.length <= 1 || original[0].length <= 1,
        () -> {
          throw new IllegalArgumentException("画像データは縦横2画素以上必要です。");
        });

    double[][] padded = padding(original);
    double[][] rowCoefficients = transformRows(padded);
    double[][] coefficients = transformColumns(rowCoefficients);

    imageWaveletModel.setTransformedImage(coefficients);
    return imageWaveletModel;
  }

  @Override
  public ImageWaveletModel startInverseWaveletTransformation() {
    double[][] coefficients = imageWaveletModel.getTransformedImage();
    double[][] columnRestored = inverseTransformColumns(coefficients);
    double[][] restored = inverseTransformRows(columnRestored);
    double[][] trimmed = imageWaveletModel.removePadding(restored);

    imageWaveletModel.setReconstructedImage(trimmed);
    return imageWaveletModel;
  }

  @Override
  public void changeWaveletData(double[] value) {
    imageWaveletModel.setOriginalImage(new double[][] {value});
  }

  public void changeWaveletImage(Image image) {
    imageWaveletModel.setOriginalImage(image);
  }

  public void changeWaveletImageData(double[][] image) {
    imageWaveletModel.setOriginalImage(image);
  }

  public double[][] transformRows(double[][] image) {
    int height = image.length;
    double[][] result = new double[height][image[0].length];
    int[] row = {0};

    BoolEx.forTrue(
        0,
        height,
        () -> {
          result[row[0]] = decompose(image[row[0]]);
          row[0]++;
        });

    return result;
  }

  public double[][] inverseTransformRows(double[][] image) {
    int height = image.length;
    double[][] result = new double[height][image[0].length];
    int[] row = {0};

    BoolEx.forTrue(
        0,
        height,
        () -> {
          result[row[0]] = reconstruct(image[row[0]]);
          row[0]++;
        });

    return result;
  }

  public double[][] transformColumns(double[][] image) {
    int height = image.length;
    int width = image[0].length;
    double[][] result = new double[height][width];
    int[] column = {0};

    BoolEx.forTrue(
        0,
        width,
        () -> {
          writeColumn(result, column[0], decompose(readColumn(image, column[0])));
          column[0]++;
        });

    return result;
  }

  public double[][] inverseTransformColumns(double[][] image) {
    int height = image.length;
    int width = image[0].length;
    double[][] result = new double[height][width];
    int[] column = {0};

    BoolEx.forTrue(
        0,
        width,
        () -> {
          writeColumn(result, column[0], reconstruct(readColumn(image, column[0])));
          column[0]++;
        });

    return result;
  }

  public double[][] padding(double[][] image) {
    int sourceHeight = image.length;
    int sourceWidth = image[0].length;
    int[] destinationHeight = {sourceHeight};
    int[] destinationWidth = {sourceWidth};

    BoolEx.ifTrueElse(
        sourceHeight % 2 != 0,
        () -> destinationHeight[0] = sourceHeight + 1);

    BoolEx.ifTrueElse(
        sourceWidth % 2 != 0,
        () -> destinationWidth[0] = sourceWidth + 1);

    double[][] padded = new double[destinationHeight[0]][destinationWidth[0]];
    int[] row = {0};

    BoolEx.forTrue(
        0,
        destinationHeight[0],
        () -> {
          int[] column = {0};
          BoolEx.forTrue(
              0,
              destinationWidth[0],
              () -> {
                int sourceRow = Math.min(row[0], sourceHeight - 1);
                int sourceColumn = Math.min(column[0], sourceWidth - 1);
                padded[row[0]][column[0]] = image[sourceRow][sourceColumn];
                column[0]++;
              });
          row[0]++;
        });

    return padded;
  }

  public ImageWaveletModel getImageWaveletModel() {
    return imageWaveletModel;
  }

  private double[] readColumn(double[][] image, int column) {
    double[] result = new double[image.length];
    int[] row = {0};

    BoolEx.forTrue(
        0,
        image.length,
        () -> {
          result[row[0]] = image[row[0]][column];
          row[0]++;
        });

    return result;
  }

  private void writeColumn(double[][] image, int column, double[] values) {
    int[] row = {0};

    BoolEx.forTrue(
        0,
        values.length,
        () -> {
          image[row[0]][column] = values[row[0]];
          row[0]++;
        });
  }
}
