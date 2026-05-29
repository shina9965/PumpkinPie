
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class RGB{
    //フィールド
    private BufferedImage image;        //入力された原画像
    private ArrayList<Integer> RList;   //分解された画像の赤チャネルをリストに
    private ArrayList<Integer> GList;   //分解された画像の緑チャネルをリストに
    private ArrayList<Integer> BList;   //分解された画像の青チャネルをリストに

    //コンストラクタ
    public RGB(BufferedImage image){

    }

    //メソッド
    public void decomposeRGB(){}                                        //imageを分解し、リストたちに値を代入
    public ArrayList<Integer> getRList(){                               //RListを返す
        return null;
    }
    public ArrayList<Integer> getGList(){                               //GListを返す
        return null;
    }
    public ArrayList<Integer> getBList(){                               //BListを返す
        return null;
    }
    public BufferedImage createRedImage (ArrayList<Integer> RList){     //赤チャネルの写真作成
        return null;
    }
    public BufferedImage createGreenImage (ArrayList<Integer> GList){   //緑チャネルの写真作成
        return null;
    }
    public BufferedImage createBlueImage (ArrayList<Integer> BList){    //青チャネルの写真作成
        return null;
    }
    //分解されたチャネルをまた合成する
    public BufferedImage mergedImage (ArrayList<Integer> RList, ArrayList<Integer> GList, ArrayList<Integer> BList){
        return null;
    }
}
