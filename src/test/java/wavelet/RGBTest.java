package wavelet;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;

import java.awt.Image;

import org.junit.jupiter.api.Test;
import transformation.RGB;

public class RGBTest {

    private Image loadImage(String filename) throws Exception{
        BufferedImage image = ImageIO.read(new File(filename));

        assertNotNull(image,
            "Failed to load image: " + filename);

        return image;
    }
    
    @Test
    void decomposeRGBがちゃんとRGB分解できているか() throws Exception{
        RGB decompose = new RGB(loadImage("src/test/java/wavelet/resources/smalltalkBalloon.jpg"));

        decompose.decomposeRGB();

        Imgcodecs.imwrite("src/test/java/wavelet/results/red_bw.jpg", decompose.getR());
        Imgcodecs.imwrite("src/test/java/wavelet/results/green_bw.jpg", decompose.getG());
        Imgcodecs.imwrite("src/test/java/wavelet/results/blue_bw.jpg", decompose.getB());
    }

    @Test
    void RGBの画像がちゃんと作成されているか() throws Exception{
        RGB decompose = new RGB(loadImage("src/test/java/wavelet/resources/smalltalkBalloon.jpg"));

        decompose.decomposeRGB();

        Imgcodecs.imwrite("src/test/java/wavelet/results/red.jpg", decompose.createRedImage(decompose.getR()));
        Imgcodecs.imwrite("src/test/java/wavelet/results/green.jpg", decompose.createGreenImage(decompose.getG()));
        Imgcodecs.imwrite("src/test/java/wavelet/results/blue.jpg", decompose.createBlueImage(decompose.getB()));
    }

    @Test
    void 分解された画像を復元できているか() throws Exception{
        RGB decompose = new RGB(loadImage("src/test/java/wavelet/resources/smalltalkBalloon.jpg"));

        decompose.decomposeRGB();

        Imgcodecs.imwrite("src/test/java/wavelet/results/merged.jpg", decompose.mergedImage(decompose.getR(), decompose.getG(), decompose.getB()));
    }

    @Test
    void 画像が空の場合の例外を投げる(){
        assertThrows(
            IllegalArgumentException.class,
            () -> new RGB((Image) null)
        );
    }
    
    @Test
    void matToDoubleArrayがちゃんと動いているか() throws Exception{
        BufferedImage buffered = new BufferedImage(2,2,BufferedImage.TYPE_BYTE_GRAY);

        buffered.getRaster().setSample(0,0,0,10);
        buffered.getRaster().setSample(1,0,0,20);
        buffered.getRaster().setSample(0,1,0,30);
        buffered.getRaster().setSample(1,1,0,40);

        RGB decompose = new RGB(buffered);

        double[][] array = decompose.matToDoubleArray(decompose.getImage());

        assertArrayEquals(
            new double[][]{{10,20},{30,40}},
            array
        );
    }

    @Test
    void doubleArrayToMatがちゃんと動いているか(){
        BufferedImage buffered = new BufferedImage(4,4,BufferedImage.TYPE_BYTE_GRAY);

        RGB decompose = new RGB(buffered);

        double[][] array = new double[][]{{10,20},{30,40}};

        Mat mat = decompose.doubleArrayToMat(array);

        Imgcodecs.imwrite("src/test/java/wavelet/results/doubleArrayToMat.jpg", mat);
    }

    @Test 
    void matToImageがちゃんと動いているか() throws Exception{
        BufferedImage buffered = new BufferedImage(2,2,BufferedImage.TYPE_BYTE_GRAY);

        buffered.getRaster().setSample(0,0,0,10);
        buffered.getRaster().setSample(1,0,0,20);
        buffered.getRaster().setSample(0,1,0,30);
        buffered.getRaster().setSample(1,1,0,40);

        RGB rgb = new RGB(buffered);

        Image image = rgb.matToImage(rgb.getImage());

        ImageIO.write(
            (BufferedImage) image,
            "jpg",
            new File("src/test/java/wavelet/results/matToImage.jpg")
        );
    }
}
