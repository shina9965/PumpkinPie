package waveletModel;
 
import java.io.File;
import java.io.IOException;

import app.BoolEx;
import fileManager.SignalFileManager;

/**
 * 信号ウェーブレット変換に使用するデータモデル
 * 元信号・変換係数・復元信号を保持し、ファイルI/Oも担う
 */
public class SignalWaveletModel extends WaveletModel {
 
    /** 元信号データ */
    private double[] originalSignal;
 
    /** 変換後ウェーブレット係数 */
    private double[] transformedSignal;
 
    /** 逆変換後信号 */
    private double[] reconstructedSignal;
 
    /** padding前の元信号長（奇数長の場合に活用） */
    private int originalLength;
 
    /** 信号ファイル管理 */
    private final SignalFileManager signalFileManager;
 
    // ===== コンストラクタ =====
 
    /**
     * SignalWaveletModelを初期化する
     */
    public SignalWaveletModel() {
        this.signalFileManager  = new SignalFileManager();
        this.originalSignal     = new double[0];
        this.transformedSignal  = new double[0];
        this.reconstructedSignal = new double[0];
        this.originalLength     = 0;
    }
 
    // ===== ファイルI/O =====
 
    /**
     * SignalFileManagerを使って信号をファイルから読み込み、originalSignalへ保存する
     *
     * @param file 読み込むファイル
     */
    public void loadSignal(File file) throws IOException {
        this.originalSignal = signalFileManager.importFile(file);
        this.originalLength = originalSignal.length;
    }
 
    /**
     * 復元信号または変換係数をファイルへ保存する
     * reconstructedSignal が存在する場合はそちらを、なければ transformedSignal を保存する
     *
     * @param file 保存先ファイル
     */
    public void saveSignal(File file) throws IOException {
        double[][] outputSignal = {reconstructedSignal};

        BoolEx.ifTrueElse(
                reconstructedSignal == null || reconstructedSignal.length == 0,
                () -> outputSignal[0] = transformedSignal
        );

        signalFileManager.exportFile(file, outputSignal[0]);
    }
 
    // ===== paddingユーティリティ =====
 
    /**
     * paddingで追加した末尾要素を削除し、originalLengthの長さに戻す
     *
     * @param signal padded後の信号
     * @return originalLength に切り詰めた信号
     */
    public double[] removePadding(double[] signal) {
        double[] trimmed = new double[originalLength];
        System.arraycopy(signal, 0, trimmed, 0, originalLength);
        return trimmed;
    }
 
    // ===== Getter / Setter =====
 
    /**
     * 元信号を取得する
     *
     * @return originalSignal
     */
    public double[] getOriginalSignal() {
        return originalSignal;
    }
 
    /**
     * 元信号を設定する
     *
     * @param signal 元信号データ
     */
    public void setOriginalSignal(double[] signal) {
        this.originalSignal = signal;
        this.originalLength = signal.length;
    }
 
    /**
     * 変換後のウェーブレット係数を取得する
     *
     * @return transformedSignal
     */
    public double[] getTransformedSignal() {
        return transformedSignal;
    }
 
    /**
     * 変換後のウェーブレット係数を設定する
     *
     * @param coefficients ウェーブレット係数配列
     */
    public void setTransformedSignal(double[] coefficients) {
        this.transformedSignal = coefficients;
    }
 
    /**
     * 逆変換後の復元信号を取得する
     *
     * @return reconstructedSignal
     */
    public double[] getReconstructedSignal() {
        return reconstructedSignal;
    }
 
    /**
     * 逆変換後の復元信号を設定する
     *
     * @param signal 復元信号データ
     */
    public void setReconstructedSignal(double[] signal) {
        this.reconstructedSignal = signal;
    }
 
    /**
     * padding前の元信号長を取得する
     *
     * @return originalLength
     */
    public int getOriginalLength() {
        return originalLength;
    }
}
 