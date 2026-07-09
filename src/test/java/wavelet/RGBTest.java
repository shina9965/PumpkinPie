package wavelet;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import transformation.RGB;

public class RGBTest {

    private Image loadImage(String filename) throws Exception{
        BufferedImage image = ImageIO.read(new File(filename));

        assertNotNull(image,
            "Failed to load image: " + filename);

        return image;
    }
    
    @Test
    void decomposeRGBがちゃんと画像をRGB分解できているか() throws Exception{
        RGB decompose = new RGB(loadImage("src/test/java/wavelet/resources/smalltalkBalloon.jpg"));

        decompose.decomposeRGB();

        Imgcodecs.imwrite("src/test/java/wavelet/results/red_bw.jpg", decompose.getR());
        Imgcodecs.imwrite("src/test/java/wavelet/results/green_bw.jpg", decompose.getG());
        Imgcodecs.imwrite("src/test/java/wavelet/results/blue_bw.jpg", decompose.getB());
    }

    @Test
    void decomposeRGBがちゃんと配列をRGB分解できているか(){
        BufferedImage buffered = new BufferedImage(3,2,BufferedImage.TYPE_3BYTE_BGR);

        buffered.setRGB(0,0,(255 << 16) | (0 << 8) | 0);
        buffered.setRGB(1,0,(255 << 16) | (255 << 8) | 0);
        buffered.setRGB(2,0,(0 << 16) | (255 << 8) | 0);
        buffered.setRGB(0,1,(0 << 16) | (255 << 8) | 255);
        buffered.setRGB(1,1,(0 << 16) | (0 << 8) | 255);
        buffered.setRGB(2,1,(255 << 16) | (0 << 8) | 255);

        // buffered.setRGB(0,0,(0 << 16) | (0 << 8) | 255);
        // buffered.setRGB(1,0,(0 << 16) | (255 << 8) | 255);
        // buffered.setRGB(2,0,(0 << 16) | (255 << 8) | 0);
        // buffered.setRGB(0,1,(255 << 16) | (255 << 8) | 0);
        // buffered.setRGB(1,1,(255 << 16) | (0 << 8) | 255);
        // buffered.setRGB(2,1,(255 << 16) | (0 << 8) | 255);
        
        RGB decompose = new RGB(buffered);

        decompose.decomposeRGB();

        double[][] red = decompose.matToDoubleArray(decompose.getR());
        double[][] green = decompose.matToDoubleArray(decompose.getG());
        double[][] blue = decompose.matToDoubleArray(decompose.getB());

        assertArrayEquals(
            new double[][]{{255,255,0},{0,0,255}}, 
            red
        );

        assertArrayEquals(
            new double[][]{{0,255,255},{255,0,0}}, 
            green
        );

        assertArrayEquals(
            new double[][]{{0,0,0},{255,255,255}}, 
            blue
        );
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
    void RGBの配列がちゃんと作成されているか(){
        BufferedImage buffered = new BufferedImage(3,2,BufferedImage.TYPE_3BYTE_BGR);

        buffered.setRGB(0,0,(255 << 16) | (0 << 8) | 0);
        buffered.setRGB(1,0,(255 << 16) | (255 << 8) | 0);
        buffered.setRGB(2,0,(0 << 16) | (255 << 8) | 0);
        buffered.setRGB(0,1,(0 << 16) | (255 << 8) | 255);
        buffered.setRGB(1,1,(0 << 16) | (0 << 8) | 255);
        buffered.setRGB(2,1,(255 << 16) | (0 << 8) | 255);

        RGB decompose = new RGB(buffered);

        decompose.decomposeRGB();

        Imgcodecs.imwrite("src/test/java/wavelet/results/red0.jpg", decompose.createRedImage(decompose.getR()));
        Imgcodecs.imwrite("src/test/java/wavelet/results/green0.jpg", decompose.createGreenImage(decompose.getG()));
        Imgcodecs.imwrite("src/test/java/wavelet/results/blue0.jpg", decompose.createBlueImage(decompose.getB()));
    }

    @Test
    void 分解された画像を復元できているか() throws Exception{
        RGB decompose = new RGB(loadImage("src/test/java/wavelet/resources/smalltalkBalloon.jpg"));

        decompose.decomposeRGB();

        Imgcodecs.imwrite("src/test/java/wavelet/results/merged.jpg", decompose.mergedImage(decompose.getR(), decompose.getG(), decompose.getB()));
    }

    @Test 
    void 分解された配列を復元できているか() throws Exception{
        BufferedImage buffered = new BufferedImage(3,2,BufferedImage.TYPE_INT_RGB);

        buffered.setRGB(0,0,(255 << 16) | (0 << 8) | 0);
        buffered.setRGB(1,0,(255 << 16) | (255 << 8) | 0);
        buffered.setRGB(2,0,(0 << 16) | (255 << 8) | 0);
        buffered.setRGB(0,1,(0 << 16) | (255 << 8) | 255);
        buffered.setRGB(1,1,(0 << 16) | (0 << 8) | 255);
        buffered.setRGB(2,1,(255 << 16) | (0 << 8) | 255);
        
        RGB decompose = new RGB(buffered);

        decompose.decomposeRGB();

        Mat merged = decompose.mergedImage(decompose.getR(), decompose.getG(), decompose.getB());

        BufferedImage mergedBufferedImage = (BufferedImage) decompose.matToImage(merged);
        ImageIO.write(mergedBufferedImage, "png", new File("src/test/java/wavelet/results/merged0.png"));
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
