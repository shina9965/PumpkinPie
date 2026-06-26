package wavelet;

import static org.junit.jupiter.api.Assertions.*;
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
    
    @Test
    void decomposeRGBがちゃんとRGB分解できているか() {
        RGB decompose = new RGB(loadImage("src/test/java/wavelet/resources/smalltalkBalloon.jpg"));

        decompose.decomposeRGB();

        Imgcodecs.imwrite("src/test/java/wavelet/results/red_bw.jpg", decompose.getR());
        Imgcodecs.imwrite("src/test/java/wavelet/results/green_bw.jpg", decompose.getG());
        Imgcodecs.imwrite("src/test/java/wavelet/results/blue_bw.jpg", decompose.getB());
    }

    @Test
    void RGBの画像がちゃんと作成されているか(){
        RGB decompose = new RGB(loadImage("src/test/java/wavelet/resources/smalltalkBalloon.jpg"));

        decompose.decomposeRGB();

        Imgcodecs.imwrite("src/test/java/wavelet/results/red.jpg", decompose.createRedImage(decompose.getR()));
        Imgcodecs.imwrite("src/test/java/wavelet/results/green.jpg", decompose.createGreenImage(decompose.getG()));
        Imgcodecs.imwrite("src/test/java/wavelet/results/blue.jpg", decompose.createBlueImage(decompose.getB()));
    }

    @Test
    void 分解された画像を復元できているか(){
        RGB decompose = new RGB(loadImage("src/test/java/wavelet/resources/smalltalkBalloon.jpg"));

        decompose.decomposeRGB();

        Imgcodecs.imwrite("src/test/java/wavelet/results/merged.jpg", decompose.mergedImage(decompose.getR(), decompose.getG(), decompose.getB()));
    }

    @Test
    void 画像が空の場合の例外を投げる(){
        Mat image = new Mat();
        RGB decompose = new RGB(image);

        assertThrows(
            IllegalArgumentException.class,
            decompose::decomposeRGB
        );
    }

    
}
