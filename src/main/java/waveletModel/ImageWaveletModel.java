package waveletModel;

import java.awt.Image;
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
    // ===== コンストラクタ =====

    /**
     * ImageWaveletModelを初期化する
     */
    public ImageWaveletModel() {
        this.originalImage      = new double[0][0];
        this.transformedImage   = new double[0][0];
        this.reconstructedImage = new double[0][0];
        this.originalWidth      = 0;
        this.originalHeight     = 0;
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

        public double[][] getLl() {
        return extractCoefficientArea(0, 0);
    }

    /**
     * 右上のLH係数を取得する。
     *
     * @return LH係数
     */
    public double[][] getLh() {
        return extractCoefficientArea(0, 1);
    }

    /**
     * 左下のHL係数を取得する。
     *
     * @return HL係数
     */
    public double[][] getHl() {
        return extractCoefficientArea(1, 0);
    }

    /**
     * 右下のHH係数を取得する。
     *
     * @return HH係数
     */
    public double[][] getHh() {
        return extractCoefficientArea(1, 1);
    }

    /**
     * transformedImageから指定された係数領域を切り出す。
     *
     * rowArea:
     * 0 = 上半分
     * 1 = 下半分
     *
     * colArea:
     * 0 = 左半分
     * 1 = 右半分
     *
     * @param rowArea 上側または下側
     * @param colArea 左側または右側
     * @return 切り出した係数領域
     */
    private double[][] extractCoefficientArea(
            int rowArea,
            int colArea) {

        if (transformedImage == null
                || transformedImage.length == 0
                || transformedImage[0].length == 0) {

            return new double[0][0];
        }

        int height = transformedImage.length;
        int width = transformedImage[0].length;

        int halfHeight = height / 2;
        int halfWidth = width / 2;

        int startRow = rowArea * halfHeight;
        int startCol = colArea * halfWidth;

        double[][] result =
            new double[halfHeight][halfWidth];

        for (int row = 0; row < halfHeight; row++) {
            System.arraycopy(
                transformedImage[startRow + row],
                startCol,
                result[row],
                0,
                halfWidth
            );
        }

        return result;
    }

    public double[][] getInverseImage() {
        return reconstructedImage;
    }
}
