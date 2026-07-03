package wavelet;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import transformation.ImageWaveletTransformation;
import waveletModel.ImageWaveletModel;

class ImageWaveletTransformationTest {

  @Test
  void restoresEvenSizedImageAfterTransformAndInverse() {
    ImageWaveletTransformation transformation = new ImageWaveletTransformation();
    double[][] original = {
        {10.0, 20.0},
        {30.0, 40.0}
    };

    transformation.changeWaveletImageData(original);
    transformation.startWaveletTransformation();

    ImageWaveletModel result = transformation.startInverseWaveletTransformation();

    assertArrayEquals(original[0], result.getReconstructedImage()[0], 0.000001);
    assertArrayEquals(original[1], result.getReconstructedImage()[1], 0.000001);
  }

  @Test
  void removesPaddingForOddSizedImageAfterInverse() {
    ImageWaveletTransformation transformation = new ImageWaveletTransformation();
    double[][] original = {
        {10.0, 20.0, 30.0},
        {40.0, 50.0, 60.0},
        {70.0, 80.0, 90.0}
    };

    transformation.changeWaveletImageData(original);
    transformation.startWaveletTransformation();

    ImageWaveletModel result = transformation.startInverseWaveletTransformation();

    assertArrayEquals(original[0], result.getReconstructedImage()[0], 0.000001);
    assertArrayEquals(original[1], result.getReconstructedImage()[1], 0.000001);
    assertArrayEquals(original[2], result.getReconstructedImage()[2], 0.000001);
  }

  @Test
  void getImageWaveletModelReturnsSameModel() {
    ImageWaveletTransformation transformation = new ImageWaveletTransformation();

    transformation.changeWaveletImageData(new double[][] {
        {1.0, 2.0},
        {3.0, 4.0}
    });

    ImageWaveletModel model1 = transformation.getImageWaveletModel();
    ImageWaveletModel model2 = transformation.startWaveletTransformation();

    assertSame(model1, model2);
  }

  @Test
  void rejectsTooSmallImageData() {
    ImageWaveletTransformation transformation = new ImageWaveletTransformation();

    transformation.changeWaveletImageData(new double[][] {{1.0}});

    assertThrows(
        IllegalArgumentException.class,
        () -> transformation.startWaveletTransformation());
  }
}
