package wavelet;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import org.junit.jupiter.api.Test;

import transformation.ImageWaveletTransformation;
import waveletModel.ImageWaveletModel;

public class ImageWaveletTransformationTest {

    /*
    ImageFileManagerはまだ未完成なため、直接画像を読み込むためのメソッド
    */
    private double[][] loadImage(String filename) throws Exception {
        BufferedImage img = ImageIO.read(new File(filename));

        int width = img.getWidth();
        int height = img.getHeight();

        double[][] image = new double[width][height];

        for (int y=0; y < height; y++){
            for (int x=0; x < width; x++){
                int color = img.getRGB(x, y);
                
                int blue = color & 0xff;
                int green = (color & 0xff00) >> 8;
                int red = (color & 0xff0000) >> 16;

                image[x][y] = (red+green+blue)/3;
            }
        }

        return image;
    }

    @Test
    void changeWaveletDataで元信号をModelに設定できる() throws Exception{
        ImageWaveletTransformation transformation = new ImageWaveletTransformation();

        double[][] image = loadImage("images/abs.jpg");

        transformation.changeWaveletData(image);

        assertArrayEquals(
            image, 
            transformation.getImageWaveletModel().getOriginalImage()
        );
    }

    @Test
    void 偶数長の画像を変換できる(){
        ImageWaveletTransformation transformation = new ImageWaveletTransformation();

        transformation.changeWaveletData(new double[][]{{4.0,2.0},{6.0,8.0},{8.0,2.0},{4.0,6.0}});

        ImageWaveletModel result = transformation.startImageWaveletTransformation();

        assertArrayEquals(
            new double[][]{{5,0},{5,1},{-2,1},{0,2}}, 
            result.getTransformedImage()
        );
    }

    // @Test 
    // void 奇数長の画像はpaddingして変換できる(){

    // }

    // @Test
    // void heightとwidthが違う値の画像を変換できる(){

    // }
}
