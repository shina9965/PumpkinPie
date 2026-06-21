package transformation;

import waveletModel.WaveletModel;
import app.BoolEx;

public abstract class WaveletTransformation<T1 extends WaveletModel, D> { //changeWaveletDataのための変更有(ジェネリクス化)
    // ウェーブレット変換を開始する抽象メソッド
    public abstract T1 startWaveletTransformation();
 
    //ウェーブレット逆変換を開始する抽象メソッド
    public abstract T1 startInverseWaveletTransformation();
 
    // ウェーブレットデータを更新する抽象メソッド
    // @param value 新しいデータ
    public abstract void changeWaveletData(D value); 
 

    // ===== 共通で使うウェーブレット変換処理部分 =====
 
    /**
     * ウェーブレット変換を実行する
     * 信号を再帰的にlow(スケーリング係数)/high係数(ウェーブレット係数)へ分解する
     * @param signal 元信号
     * @return low/high係数配列
     */
    protected double[] decompose(double[] signal) {
        double[][] splitResult   = split(signal);  
        double[]   low           = splitResult[0];
        double[]   high          = splitResult[1];
        return merge(low, high);
    }
 
    /**
     * ウェーブレット逆変換を実行する
     * ウェーブレット係数から元信号を復元する
     * @param coefficients ウェーブレット係数配列
     * @return 復元された信号
     */
    protected double[] reconstruct(double[] coefficients) {
        int      halfLen  = coefficients.length / 2;
        double[] low      = new double[halfLen];
        double[] high     = new double[halfLen];
        double[] restored = new double[coefficients.length];
        int[]    idx      = {0};

        System.arraycopy(coefficients, 0,      low,  0, halfLen);
        System.arraycopy(coefficients, halfLen, high, 0, halfLen);

        BoolEx.forTrue(0, halfLen, () -> {
            restored[idx[0] * 2]     = restoreLeft (low[idx[0]], high[idx[0]]);
            restored[idx[0] * 2 + 1] = restoreRight(low[idx[0]], high[idx[0]]);
            idx[0]++;
        });

        return restored;
    }
 
    /**
     * low係数とhigh係数を結合して出力用配列を生成する
     * [low0, low1, ..., high0, high1, ...] の順に結合する
     * @param low  スケーリング係数配列
     * @param high ウェーブレット展開係数配列
     * @return 結合された配列
     */
    protected double[] merge(double[] low, double[] high) {
        double[] merged = new double[low.length + high.length];
        System.arraycopy(low,  0, merged, 0, low.length);
        System.arraycopy(high, 0, merged, low.length, high.length);
        return merged;
    }
 
    /**
     * 信号をlow/high係数へ分離する（Haarウェーブレット変換）
     * 隣り合う2要素ずつ calculateLow / calculateHigh を適用する
     * @param coefficients 入力配列(係数配列)（偶数長を前提）
     * @return [low[], high[]] の2次元配列
     */
    protected double[][] split(double[] coefficients) {
        int halfLen = coefficients.length / 2;
        double[] low  = new double[halfLen];
        double[] high = new double[halfLen];
        int[] idx = {0};
 
        BoolEx.forTrue(0, halfLen, () -> {
            low [idx[0]] = calculateLow (coefficients[idx[0] * 2], coefficients[idx[0] * 2 + 1]);
            high[idx[0]] = calculateHigh(coefficients[idx[0] * 2], coefficients[idx[0] * 2 + 1]);
            idx[0]++;
        });
 
        return new double[][]{low, high};
    }
 
    /**
     * スケーリング係数（平均）を計算する
     * @param a 左側の値
     * @param b 右側の値
     * @return (a + b) / 2
     */
    protected double calculateLow(double a, double b) {
        return (a + b) / 2.0;
    }
 
    /**
     * ウェーブレット展開係数（差分）を計算する
     * @param a 左側の値
     * @param b 右側の値
     * @return (a - b) / 2
     */
    protected double calculateHigh(double a, double b) {
        return (a - b) / 2.0;
    }
 
    /**
     * ペアの左側の画素を復元する
     * @param low  スケーリング係数
     * @param high ウェーブレット展開係数
     * @return low + high
     */
    protected double restoreLeft(double low, double high) {
        return low + high;
    }
 
    /**
     * ペアの右側の画素を復元する
     * @param low  スケーリング係数
     * @param high ウェーブレット展開係数
     * @return low - high
     */
    protected double restoreRight(double low, double high) {
        return low - high;
    }
}
