package waveletModel;

import java.awt.Image;
import java.io.File;
import app.BoolEx;
import fileManager.ImageFileManager;
import transformation.RGB;

/**
 * 画像ウェーブレット変換に使用するデータモデル
 * 元画像・変換係数・復元画像を保持し、ファイルI/Oも担う
 */
public class ImageWaveletModel extends WaveletModel {

    /** 元画像データ */
    private double[][] originalImage;

    /** 変換後ウェーブレット係数 */
    private double[][] transformedImage;

    /** 逆変換後画像データ */
    private double[][] reconstructedImage;

    /** padding前の横幅（横方向の画素数が奇数の場合に使用） */
    private int originalWidth;

    /** padding前の縦幅（縦方向の画素数が奇数の場合に使用） */
    private int originalHeight;

    /** 画像ファイル管理 */
    private final ImageFileManager imageFileManager;

    private RGB rgb;

    // ===== コンストラクタ =====

    /**
     * ImageWaveletModelを初期化する
     */
    public ImageWaveletModel() {
        this.imageFileManager   = new ImageFileManager();
        this.originalImage      = new double[0][0];
        this.transformedImage   = new double[0][0];
        this.reconstructedImage = new double[0][0];
        this.originalWidth      = 0;
        this.originalHeight     = 0;
    }

    // ===== ファイルI/O =====

    /**
     * ImageFileManagerを使って画像をファイルから読み込み、originalImageへ保存する
     * 画像サイズを originalWidth / originalHeight に保存する
     *
     * @param path 読み込む画像ファイルのパス
     */
    public void loadImage(String path) {
        File file           = new File(path);
        Image image         = imageFileManager.importFile(file);
        this.rgb            = new RGB(image);
        this.originalImage  = rgb.matToDoubleArray(rgb.getImage());
        this.originalHeight = originalImage.length;
        this.originalWidth  = originalImage[0].length;
    }

    /**
     * 復元画像または変換係数をファイルへ保存する
     * reconstructedImage が存在する場合はそちらを、なければ transformedImage を保存する
     *
     * @param file 保存先ファイル
     */
    public void saveImage(File file) {
        boolean hasReconstructed = reconstructedImage != null && reconstructedImage.length > 0;
        BoolEx.ifTrueElse(
            hasReconstructed,
            () -> imageFileManager.exportFile(file, rgb.matToImage(rgb.doubleArrayToMat(reconstructedImage))),
            () -> imageFileManager.exportFile(file, rgb.matToImage(rgb.doubleArrayToMat(transformedImage)))
        );
    }

    // ===== Getter / Setter =====

    /**
     * 元画像データを取得する
     *
     * @return originalImage
     */
    public double[][] getOriginalImage() {
        return originalImage;
    }

    /**
     * 元画像データを設定する
     *
     * @param image 元画像データ
     */
    public void setOriginalImage(double[][] image) {
        this.originalImage  = image;
        this.originalHeight = image.length;
        this.originalWidth  = (image.length > 0) ? image[0].length : 0;
    }

    /**
     * 変換後のウェーブレット係数を取得する
     *
     * @return transformedImage
     */
    public double[][] getTransformedImage() {
        return transformedImage;
    }

    /**
     * 変換後のウェーブレット係数を設定する
     *
     * @param coefficients ウェーブレット係数の2次元配列
     */
    public void setTransformedImage(double[][] coefficients) {
        this.transformedImage = coefficients;
    }

    /**
     * 逆変換後の復元画像データを取得する
     *
     * @return reconstructedImage
     */
    public double[][] getReconstructedImage() {
        return reconstructedImage;
    }

    /**
     * 逆変換後の復元画像データを設定する
     *
     * @param image 復元画像データ
     */
    public void setReconstructedImage(double[][] image) {
        this.reconstructedImage = image;
    }

    /**
     * padding前の横幅を取得する
     *
     * @return originalWidth
     */
    public int getOriginalWidth() {
        return originalWidth;
    }

    /**
     * padding前の縦幅を取得する
     *
     * @return originalHeight
     */
    public int getOriginalHeight() {
        return originalHeight;
    }
}