package waveletModel;

import java.awt.Image;
import java.io.File;

import app.BoolEx;
import fileManager.ImageFileManager;

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
        this.originalImage  = convertImageToDoubleArray(image);
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
            () -> imageFileManager.exportFile(file, convertDoubleArrayToImage(reconstructedImage)),
            () -> imageFileManager.exportFile(file, convertDoubleArrayToImage(transformedImage))
        );
    }

    // ===== 変換ユーティリティ（Image ↔ double[][]） =====

    /**
     * java.awt.Image を double[][] へ変換する（グレースケール輝度値）
     *
     * @param image 入力画像
     * @return グレースケール輝度値の2次元配列
     */
    private double[][] convertImageToDoubleArray(Image image) {
        java.awt.image.BufferedImage buffered = toBufferedImage(image);
        int height        = buffered.getHeight();
        int width         = buffered.getWidth();
        double[][] result = new double[height][width];
        int[] row         = {0};
        BoolEx.forTrue(0, height, () -> {
            int[] col = {0};
            BoolEx.forTrue(0, width, () -> {
                int rgb = buffered.getRGB(col[0], row[0]);
                int r   = (rgb >> 16) & 0xFF;
                int g   = (rgb >>  8) & 0xFF;
                int b   =  rgb        & 0xFF;
                result[row[0]][col[0]] = 0.299 * r + 0.587 * g + 0.114 * b;
                col[0]++;
            });
            row[0]++;
        });
        return result;
    }

    /**
     * double[][] を java.awt.Image へ変換する（グレースケール）
     *
     * @param data グレースケール輝度値の2次元配列
     * @return 変換後の Image
     */
    private Image convertDoubleArrayToImage(double[][] data) {
        int height = data.length;
        int width  = data[0].length;
        java.awt.image.BufferedImage buffered =
            new java.awt.image.BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_RGB);
        int[] row = {0};
        BoolEx.forTrue(0, height, () -> {
            int[] col = {0};
            BoolEx.forTrue(0, width, () -> {
                int[] clamped = {(int) Math.round(data[row[0]][col[0]])};
                BoolEx.ifTrueElse(clamped[0] < 0,  () -> clamped[0] = 0);
                BoolEx.ifTrueElse(clamped[0] > 255, () -> clamped[0] = 255);
                int grayRgb = (clamped[0] << 16) | (clamped[0] << 8) | clamped[0];
                buffered.setRGB(col[0], row[0], grayRgb);
                col[0]++;
            });
            row[0]++;
        });
        return buffered;
    }

    /**
     * java.awt.Image を BufferedImage へ変換する
     *
     * @param image 変換元 Image
     * @return BufferedImage
     */
    private java.awt.image.BufferedImage toBufferedImage(Image image) {
        if (image instanceof java.awt.image.BufferedImage bi) {
            return bi;
        }
        java.awt.image.BufferedImage buffered = new java.awt.image.BufferedImage(
            image.getWidth(null),
            image.getHeight(null),
            java.awt.image.BufferedImage.TYPE_INT_RGB
        );
        java.awt.Graphics2D g2d = buffered.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        return buffered;
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