package transformation;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.Graphics2D;

public class RGB{
    //フィールド
    private Mat image;  //入力された原画像
    private Mat R;      //分解された画像の赤チャネル画像
    private Mat G;      //分解された画像の緑チャネル画像
    private Mat B;      //分解された画像の青チャネル画像

    //コンストラクタ
    public RGB(Image img){
        if (img == null) {
            throw new IllegalArgumentException("入力画像がありません。");
        }

        BufferedImage buffered;

        buffered = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_3BYTE_BGR);

        Graphics2D g = buffered.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();

        this.image = new Mat(buffered.getHeight(), buffered.getWidth(),CvType.CV_8UC3);
        byte[] pixels = ((DataBufferByte) buffered.getRaster().getDataBuffer()).getData();
        
        this.image.put(0, 0, pixels);
        this.R = new Mat();
        this.G = new Mat();
        this.B = new Mat();
    }
    

    //メソッド

    static {
        System.load(
            System.getProperty("user.dir")
            + "/target/opencv/nu/pattern/opencv/osx/ARMv8/libopencv_java490.dylib"
        );
    }

    public void decomposeRGB(){
        List<Mat> channels = new ArrayList<>();
        Core.split(image,channels);
        this.B = channels.get(0);
        this.G = channels.get(1);
        this.R = channels.get(2);
         
    }                                        //imageを分解し、リストたちに値を代入

    public Mat getImage(){
        return image;
    }

    public Mat getR(){                               //RListを返す
        return R;
    }
    public Mat getG(){                               //GListを返す
        return G;
    }
    public Mat getB(){                               //BListを返す
        return B;
    }
    public Mat createRedImage (Mat R){     //赤チャネルの写真作成
        Mat zero = Mat.zeros(R.size(),R.type());
        List<Mat> channels = new ArrayList<>();

        channels.add(zero);
        channels.add(zero);
        channels.add(R);
        
        Mat redImage = new Mat();

        Core.merge(channels, redImage);

        return redImage;
    }
    public Mat createGreenImage (Mat G){   //緑チャネルの写真作成
        Mat zero = Mat.zeros(G.size(),G.type());
        List<Mat> channels = new ArrayList<>();

        channels.add(zero);
        channels.add(G);
        channels.add(zero);
        
        Mat greenImage = new Mat();

        Core.merge(channels, greenImage);

        return greenImage;
    }
    public Mat createBlueImage (Mat B){    //青チャネルの写真作成
        Mat zero = Mat.zeros(B.size(),B.type());
        List<Mat> channels = new ArrayList<>();

        channels.add(B);
        channels.add(zero);
        channels.add(zero);
        
        Mat blueImage = new Mat();

        Core.merge(channels, blueImage);

        return blueImage;
    }
    //分解されたチャネルをまた合成する
    public Mat mergedImage (Mat R, Mat G, Mat B){
        if (!R.size().equals(G.size()) || !R.size().equals(B.size())) {
            throw new IllegalArgumentException("チャネルのサイズが合っていない");
        }


        List<Mat> channels = new ArrayList<>();
        
        channels.add(B);
        channels.add(G);
        channels.add(R);

        Mat mergedImage = new Mat();

        Core.merge(channels, mergedImage);

        return mergedImage;
    }

    public double[][] matToDoubleArray(Mat mat) {

        int rows = mat.rows();
        int cols = mat.cols();

        double[][] result = new double[rows][cols];

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                result[y][x] = mat.get(y, x)[0];
            }
        }

        return result;
    }

    public Mat doubleArrayToMat(double[][] data) {
        int rows = data.length;
        int cols = data[0].length;

        Mat mat = new Mat(rows, cols, CvType.CV_8UC1);

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                double value = Math.max(0, Math.min(255, data[y][x]));
                mat.put(y, x, value);
            }
        }

        return mat;
    }

    public Image matToImage(Mat mat) {

        BufferedImage image = new BufferedImage(
            mat.cols(),
            mat.rows(),
            BufferedImage.TYPE_3BYTE_BGR);

        byte[] source = new byte[(int) (mat.total() * mat.channels())];
        mat.get(0, 0, source);

        byte[] target = ((DataBufferByte) image.getRaster()
            .getDataBuffer()).getData();

        System.arraycopy(source, 0, target, 0, source.length);

        return image;
    }
}
