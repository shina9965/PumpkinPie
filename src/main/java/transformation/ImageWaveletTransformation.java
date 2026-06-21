package transformation;

import app.BoolEx;
import waveletModel.ImageWaveletModel;

/**
 * 画像向けウェーブレット変換の具象クラス
 * WaveletTransformationを継承し、ImageWaveletModelを用いて
 * 画像の2次元Haarウェーブレット変換・逆変換を実現する
 */
public class ImageWaveletTransformation extends WaveletTransformation<ImageWaveletModel, double[][]> { //changeWaveletDataのための変更有(ジェネリクス化)

    /** Image用Model：元画像・係数へのアクセスに使用 */
    private final ImageWaveletModel imageWaveletModel;

    // ===== コンストラクタ =====

    /**
     * ImageWaveletModelをここでnewし、インスタンスを初期化する
     */
    public ImageWaveletTransformation() {
        this.imageWaveletModel = new ImageWaveletModel();
    }

    // ===== WaveletTransformation オーバーライド =====

    /**
     * 画像のウェーブレット変換を開始する
     * 行方向へ1段分のHaar分解、列方向へ1段分のHaar分解、
     * LL/LH/HL/HH係数生成、変換結果をImageWaveletModelへ保存する
     *
     * @return 変換結果を保持したImageWaveletModel
     */
    @Override
    public ImageWaveletModel startWaveletTransformation() {
        return startImageWaveletTransformation();
    }

    /**
     * 画像のウェーブレット逆変換を開始する
     * 列方向逆変換、行方向逆変換を行い、reconstructedImageへ保存する
     *
     * @return 復元結果を保持したImageWaveletModel
     */
    @Override
    public ImageWaveletModel startInverseWaveletTransformation() {
        return startInverseImageWaveletTransformation();
    }

    /**
     * ウェーブレットデータ（元画像）をModelへ設定する
     *
     * @param value 新しい画像データ
     */
    @Override
    public void changeWaveletData(double[][] value) {
        imageWaveletModel.setOriginalImage(value);
    }

    // ===== Image固有のメソッド =====

    /**
     * 画像のウェーブレット変換を実行し、変換後のModelを返す
     *
     * @return 変換結果を保持したImageWaveletModel
     */
    public ImageWaveletModel startImageWaveletTransformation() {
        double[][] original   = imageWaveletModel.getOriginalImage();
        double[][] padded     = padding(original);
        double[][] rowApplied = transformRows(padded);
        double[][] transposed = transpose(rowApplied);
        double[][] colApplied = transformRows(transposed);
        double[][] result     = transpose(colApplied);
        imageWaveletModel.setTransformedImage(result);
        return imageWaveletModel;
    }

    /**
     * 画像のウェーブレット逆変換を実行し、復元後のModelを返す
     *
     * @return 復元結果を保持したImageWaveletModel
     */
    public ImageWaveletModel startInverseImageWaveletTransformation() {
        double[][] coefficients = imageWaveletModel.getTransformedImage();
        double[][] transposed1  = transpose(coefficients);
        double[][] colRestored  = inverseTransformRows(transposed1);
        double[][] transposed2  = transpose(colRestored);
        double[][] rowRestored  = inverseTransformRows(transposed2);
        double[][] trimmed      = removePadding(rowRestored);
        imageWaveletModel.setReconstructedImage(trimmed);
        return imageWaveletModel;
    }

    /**
     * 各行へ1次元ウェーブレット変換を適用する
     * 1行ずつapplyWaveletToRowに渡す
     *
     * @param image 入力画像データ
     * @return 各行へウェーブレット変換を適用した2次元配列
     */
    public double[][] transformRows(double[][] image) {
        int height        = image.length;
        int[] width       = {0};
        BoolEx.ifTrueElse(
            height > 0,
            () -> width[0] = image[0].length
        );
        double[][] result = new double[height][width[0]];
        int[] row         = {0};
        BoolEx.forTrue(0, height, () -> {
            result[row[0]] = applyWaveletToRow(image[row[0]]);
            row[0]++;
        });
        return result;
    }

    /**
     * 各行へ1次元ウェーブレット逆変換を適用する
     * 1行ずつapplyInverseWaveletToRowに渡す
     *
     * @param image 入力画像データ
     * @return 各行へウェーブレット逆変換を適用した2次元配列
     */
    public double[][] inverseTransformRows(double[][] image) {
        int height        = image.length;
        int[] width       = {0};
        BoolEx.ifTrueElse(
            height > 0,
            () -> width[0] = image[0].length
        );
        double[][] result = new double[height][width[0]];
        int[] row         = {0};
        BoolEx.forTrue(0, height, () -> {
            result[row[0]] = applyInverseWaveletToRow(image[row[0]]);
            row[0]++;
        });
        return result;
    }

    /**
     * 行列の転置を行う
     * 行方向処理と列方向処理を共通化するために使用する
     *
     * @param matrix 入力行列
     * @return 転置行列
     */
    public double[][] transpose(double[][] matrix) {
        int rows          = matrix.length;
        int[] cols        = {0};
        BoolEx.ifTrueElse(
            rows > 0,
            () -> cols[0] = matrix[0].length
        );
        double[][] result = new double[cols[0]][rows];
        int[] row         = {0};
        BoolEx.forTrue(0, rows, () -> {
            int[] col = {0};
            BoolEx.forTrue(0, cols[0], () -> {
                result[col[0]][row[0]] = matrix[row[0]][col[0]];
                col[0]++;
            });
            row[0]++;
        });
        return result;
    }

    /**
     * 縦・横の画素数が奇数の場合にpaddingを追加する（末尾画素の複製）
     * 偶数長の場合はそのまま返す
     *
     * @param image 入力画像データ
     * @return paddingされた画像データ
     */
    public double[][] padding(double[][] image) {
        int srcHeight = image.length;
        int[] srcWidth = {0};
        BoolEx.ifTrueElse(
            srcHeight > 0,
            () -> srcWidth[0] = image[0].length
        );

        boolean needsRowPad = (srcHeight % 2 != 0);
        boolean needsColPad = (srcWidth[0] % 2 != 0);

        int[] dstHeight   = {srcHeight};
        int[] dstWidth    = {srcWidth[0]};
        BoolEx.ifTrueElse(needsRowPad, () -> dstHeight[0] = srcHeight + 1);
        BoolEx.ifTrueElse(needsColPad, () -> dstWidth[0]  = srcWidth[0] + 1);

        double[][] padded = new double[dstHeight[0]][dstWidth[0]];

        int[] row = {0};
        BoolEx.forTrue(0, srcHeight, () -> {
            System.arraycopy(image[row[0]], 0, padded[row[0]], 0, srcWidth[0]);
            BoolEx.ifTrueElse(
                needsColPad,
                () -> padded[row[0]][srcWidth[0]] = image[row[0]][srcWidth[0] - 1]
            );
            row[0]++;
        });

        BoolEx.ifTrueElse(
            needsRowPad,
            () -> System.arraycopy(padded[srcHeight - 1], 0, padded[srcHeight], 0, dstWidth[0])
        );

        return padded;
    }

    /**
     * paddingを追加した部分を削除し、元の画像サイズに戻す
     * originalWidth / originalHeight は ImageWaveletModel から取得する
     *
     * @param image padded後の画像データ
     * @return padding除去後の画像データ
     */
    public double[][] removePadding(double[][] image) {
        int height        = imageWaveletModel.getOriginalHeight();
        int width         = imageWaveletModel.getOriginalWidth();
        double[][] result = new double[height][width];
        int[] row         = {0};
        BoolEx.forTrue(0, height, () -> {
            System.arraycopy(image[row[0]], 0, result[row[0]], 0, width);
            row[0]++;
        });
        return result;
    }

    /**
     * 1行分のウェーブレット変換を行う
     *
     * @param row 入力1行データ
     * @return ウェーブレット変換後の1行データ
     */
    public double[] applyWaveletToRow(double[] row) {
        return decompose(row);
    }

    /**
     * 1行分のウェーブレット逆変換を行う
     *
     * @param row ウェーブレット係数の1行データ
     * @return 逆変換後の1行データ
     */
    public double[] applyInverseWaveletToRow(double[] row) {
        return reconstruct(row);
    }

    /**
     * ImageWaveletModelを返す
     *
     * @return imageWaveletModel
     */
    public ImageWaveletModel getImageWaveletModel() {
        return imageWaveletModel;
    }
}