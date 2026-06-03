public abstract class WaveletTransformation {
    // 抽象メソッド
    public abstract void startWaveletTransformation();
    public abstract void startInverseWaveletTransformation();
    public abstract void changeWaveletData(value : double[]);


    // ウェーブレット変換（分解）を行う。
    // 入力信号を1段階のHaar変換により低周波・高周波係数に分解し、
    // merge関数で結合した配列として返す。
    // [入力]signal : 入力信号配列(偶数長を想定)
    // [出力]変換後の係数配列 [低周波部(スケーリング係数) | 高周波部(ウェーブレット展開係数)]
    protected double[] decompose(double[] signal) {
        int half = signal.length / 2;
        double[] low  = new double[half];
        double[] high = new double[half];
 
        for (int i = 0; i < half; i++) {
            double a = signal[2 * i];
            double b = signal[2 * i + 1];
            low[i]  = calculateLow(a, b);
            high[i] = calculateHigh(a, b);
        }
 
        return merge(low, high);
    }
 

    //ウェーブレット逆変換（再構成）を行う。
    //split関数で低周波・高周波係数に分離した後、
    //ウェーブレット展開係数をもとに各スケーリング係数のペアから元の信号値を復元する。
    //[入力]coefficients : 係数配列 [低周波部(スケーリング係数) | 高周波部(ウェーブレット展開係数)](偶数長を想定)
    //[出力]復元された信号配列
    protected double[] reconstruct(double[] coefficients) {
        double[][] parts = split(coefficients);
        double[] low  = parts[0];
        double[] high = parts[1];
 
        int length = low.length * 2;
        double[] signal = new double[length];
 
        for (int i = 0; i < low.length; i++) {
            signal[2 * i]     = restoreLeft(low[i], high[i]);
            signal[2 * i + 1] = restoreRight(low[i], high[i]);
        }
        return signal;
    }
 

    // 低周波係数と高周波係数を1つの配列に結合する。(扱いやすい形で出力するために使用)
    // 結果配列の前半に[低周波部(スケーリング係数)]、後半に[高周波部(ウェーブレット展開係数)]を格納する。
    // [入力]低周波係数配列
    // [入力]高周波係数配列
    // [出力]結合された係数配列
    protected double[] merge(double[] low, double[] high) {
        double[] merged = new double[low.length + high.length];
        System.arraycopy(low,  0, merged, 0,          low.length);
        System.arraycopy(high, 0, merged, low.length, high.length);
        return merged;
    }
 

    // 係数配列を低周波部と高周波部に分離する。(計算時に扱いやすい形にするために使用)
    // 入力配列の前半を低周波係数、後半を高周波係数として返す。
    // [入力]coefficients 結合された係数配列（偶数長を想定）
    // [出力]coefficients : 低周波係数配列
    // [出力]code : 高周波係数配列
    protected double[][] split(double[] coefficients) {
        int half = coefficients.length / 2;
        double[] low  = new double[half];
        double[] high = new double[half];
 
        System.arraycopy(coefficients, 0,    low,  0, half);
        System.arraycopy(coefficients, half, high, 0, half);
 
        return new double[][]{ low, high };
    }
 
    
    // スケーリング係数(低周波成分)を計算する。
    // [入力]param a 左画素値
    // [入力]param b 右画素値
    // [出力]return (a + b) / 2
    protected double calculateLow(double a, double b) {
        return (a + b) / 2.0;
    }
 
    
    // ウェーブレット展開係数(高周波成分)を計算する。
    // [入力]param a 左画素値
    // [入力]param b 右画素値
    // [出力]return (a - b) / 2
    protected double calculateHigh(double a, double b) {
        return (a - b) / 2.0;
    }
 
    
    // ペアのうち左側の画素値を復元する。
    // [入力] 低周波係数
    // [入力] 高周波係数
    // [出力]low + high
    protected double restoreLeft(double low, double high) {
        return low + high;
    }
 

    // ペアのうち右側の画素値を復元する。
    // [入力]低周波係数
    // [入力]高周波係数
    // [出力]low - high
    protected double restoreRight(double low, double high) {
        return low - high;
    }
}
