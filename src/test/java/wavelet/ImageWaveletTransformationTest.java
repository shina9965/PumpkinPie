package wavelet;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    private BufferedImage createImage(double[][] image) {
        int width = image.length;
        int height = image[0].length;

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int gray = (int) Math.round(image[x][y]);

                // Clamp to [0,255]
                gray = Math.max(0, Math.min(255, gray));

                int rgb = (gray << 16) | (gray << 8) | gray;

                img.setRGB(x, y, rgb);
            }
        }

        return img;
    }

    private void saveImage(double[][] image, String filename) throws IOException {
        BufferedImage img = createImage(image);
        ImageIO.write(img, "jpg", new File(filename));
    }

    @Test
    void changeWaveletDataで元信号をModelに設定できる() throws Exception{
        ImageWaveletTransformation transformation = new ImageWaveletTransformation();

        double[][] image = loadImage("src/test/java/wavelet/images/smalltalkBalloon.jpg");

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

    @Test 
    void 奇数長の画像はpaddingして変換できる(){
        ImageWaveletTransformation transformation = new ImageWaveletTransformation();

        transformation.changeWaveletData(new double[][]{{4.0,2.0},{6.0,8.0},{8.0,2.0}});

        ImageWaveletModel result = transformation.startImageWaveletTransformation();

        assertArrayEquals(
            new double[][]{{5,0},{5,3},{-2,1},{0,0}}, 
            result.getTransformedImage()
        );
    }

    @Test
    void 画像は変換できるか() throws Exception{
        ImageWaveletTransformation transformation = new ImageWaveletTransformation();

        double[][] image = loadImage("src/test/java/wavelet/images/smalltalkBalloon.jpg");

        transformation.changeWaveletData(image);

        ImageWaveletModel result = transformation.startImageWaveletTransformation();

        saveImage(result.getTransformedImage(), "src/test/java/wavelet/results/transformed.jpg");
    }

    @Test
    void 偶数長の画像を変換して逆変換すると元に戻る() {
        ImageWaveletTransformation transformation = new ImageWaveletTransformation();

        double[][] original = {{10.0,6.0,4.0,2.0}, {6.0,10.0,2.0,4.0}, {4.0,2.0,10.0,6.0}, {2.0,4.0,6.0,10.0}};

        transformation.changeWaveletData(original);
        transformation.startWaveletTransformation();

        ImageWaveletModel result = transformation.startInverseWaveletTransformation();

        assertArrayEquals(
            original,
            result.getReconstructedImage()
        );
    }

    @Test
    void 奇数長の画像を変換して逆変換するとpaddingが除去されて元に戻る() {
        ImageWaveletTransformation transformation = new ImageWaveletTransformation();

        double[][] original = {{10.0,6.0,4.0}, {6.0,10.0,2.0}, {4.0,2.0,10.0}};

        transformation.changeWaveletData(original);
        transformation.startWaveletTransformation();

        ImageWaveletModel result = transformation.startInverseWaveletTransformation();

        assertArrayEquals(
            original,
            result.getReconstructedImage()
        );
    }

    @Test
    void 画像は逆変換できるか() throws Exception{
        ImageWaveletTransformation transformation = new ImageWaveletTransformation();

        double[][] image = loadImage("src/test/java/wavelet/images/smalltalkBalloon.jpg");

        transformation.changeWaveletData(image);
        transformation.startWaveletTransformation();

        ImageWaveletModel result = transformation.startInverseWaveletTransformation();

        saveImage(result.getReconstructedImage(), "src/test/java/wavelet/results/reconstructed.jpg");
    }

    @Test
    void getSignalWaveletModelで同じModelを取得できる() {
    ImageWaveletTransformation transformation = new ImageWaveletTransformation();

    transformation.changeWaveletData(new double[][]{{1.0, 2.0},{2,3}});

    ImageWaveletModel model1 = transformation.getImageWaveletModel();
    ImageWaveletModel model2 = transformation.startWaveletTransformation();

    assertSame(model1, model2);
    }

    @Test
    void 入力が空の配列の場合例外を投げる() {
    ImageWaveletTransformation transformation = new ImageWaveletTransformation();

    transformation.changeWaveletData(new double[][]{{}});

    assertThrows(
        IllegalArgumentException.class,
        () -> transformation.startWaveletTransformation()
    );
    }

    @Test
    void 入力が1要素の場合例外を投げる() {
    ImageWaveletTransformation transformation = new ImageWaveletTransformation();

    transformation.changeWaveletData(new double[][]{{1.0,1}});

    assertThrows(
        IllegalArgumentException.class,
        () -> transformation.startWaveletTransformation()
    );
    }

}
