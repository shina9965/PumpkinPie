package transformation;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import app.BoolEx;

public class RGB{
    //フィールド
    private Mat image;  //入力された原画像
    private Mat R;      //分解された画像の赤チャネル画像
    private Mat G;      //分解された画像の緑チャネル画像
    private Mat B;      //分解された画像の青チャネル画像

    //コンストラクタ
    public RGB(Mat image){
        this.image = image;
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
        BoolEx.ifTrueElse(
            image == null || image.empty(),
            () -> { throw new IllegalArgumentException("入力画像がありません"); },
            () -> {}
        );

        List<Mat> channels = new ArrayList<>();
        Core.split(image, channels);

        if (channels.size() < 3) {
            throw new IllegalArgumentException("入力画像のチャネル数は3ではない");
        }

        this.B = channels.get(0);
        this.G = channels.get(1);
        this.R = channels.get(2);
    }                                        //imageを分解し、リストたちに値を代入
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
            throw new IllegalArgumentException("Channel sizes do not match");
        }


        List<Mat> channels = new ArrayList<>();
        
        channels.add(B);
        channels.add(G);
        channels.add(R);

        Mat mergedImage = new Mat();

        Core.merge(channels, mergedImage);

        return mergedImage;
    }
}
