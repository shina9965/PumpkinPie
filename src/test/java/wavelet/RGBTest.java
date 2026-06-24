package wavelet;

import static org.junit.jupiter.api.Assertions.*;
import  org.junit.jupiter.api.BeforeAll;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;

import org.junit.jupiter.api.Test;
import transformation.RGB;

public class RGBTest {

    private Mat loadImage(String filename) {
        Mat image = Imgcodecs.imread(filename);

        assertFalse(image.empty(),
                "Failed to load image: " + filename);

        return image;
    }

    private void assertMatEquals(Mat expected, Mat actual) {

    assertEquals(expected.rows(), actual.rows());
    assertEquals(expected.cols(), actual.cols());
    assertEquals(expected.type(), actual.type());

    Mat diff = new Mat();

    Core.compare(expected, actual, diff, Core.CMP_NE);

    int differentPixels = Core.countNonZero(diff.reshape(1));

    assertEquals(0, differentPixels,
            "Images differ in " + differentPixels + " pixels");
    }
    
    @Test
    void decomposeRGBがちゃんとRGB分解できているか() {
        RGB decompose = new RGB(loadImage("src/test/wavelet/resources/smalltalkBalloon.jpg"));

        decompose.decomposeRGB();

        assertMatEquals(loadImage("src/test/wavelet/resources/red_bw.jpg"), decompose.getR());
        assertMatEquals(loadImage("src/test/wavelet/resources/green_bw.jpg"), decompose.getG());
        assertMatEquals(loadImage("src/test/wavelet/resources/blue_bw.jpg"), decompose.getB());
    }


    
}
