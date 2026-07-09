package wavelet;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import transformation.WaveletTransformation;
import waveletModel.SignalWaveletModel;

class WaveletTransformationTest {

    // テスト用サブクラス
    // WaveletTransformation は抽象クラスかつ別パッケージのため、
    // サブクラスを定義して protected メソッドを public ラッパーで公開する
    static class TestableWaveletTransformation
            extends WaveletTransformation<SignalWaveletModel> {

        @Override public SignalWaveletModel startWaveletTransformation()        { return null; }
        @Override public SignalWaveletModel startInverseWaveletTransformation() { return null; }
        @Override public void changeWaveletData(double[] value)                 {}

        public double   exposedCalculateLow(double a, double b)         { return calculateLow(a, b); }
        public double   exposedCalculateHigh(double a, double b)        { return calculateHigh(a, b); }
        public double   exposedRestoreLeft(double low, double high)      { return restoreLeft(low, high); }
        public double   exposedRestoreRight(double low, double high)     { return restoreRight(low, high); }
        public double[] exposedMerge(double[] low, double[] high)       { return merge(low, high); }
        public double[][] exposedSplit(double[] c)                      { return split(c); }
        public double[] exposedDecompose(double[] s)                    { return decompose(s); }
        public double[] exposedReconstruct(double[] c)                  { return reconstruct(c); }
    }

    // ===== calculateLow =====

    @Test
    void calculateLowは正の値の平均を返す() {
        TestableWaveletTransformation wt = new TestableWaveletTransformation();
        assertEquals(3.0, wt.exposedCalculateLow(4.0, 2.0), 1e-10);
    }

    @Test
    void calculateLowは負の値を含む場合も正しく計算される() {
        TestableWaveletTransformation wt = new TestableWaveletTransformation();
        assertEquals(0.0, wt.exposedCalculateLow(-1.0, 1.0), 1e-10);
    }

    @Test
    void calculateLowは同じ値を渡すとその値を返す() {
        TestableWaveletTransformation wt = new TestableWaveletTransformation();
        assertEquals(5.0, wt.exposedCalculateLow(5.0, 5.0), 1e-10);
    }

    @Test
    void calculateLowは0を含む場合も正しく計算される() {
        TestableWaveletTransformation wt = new TestableWaveletTransformation();
        assertEquals(3.0, wt.exposedCalculateLow(6.0, 0.0), 1e-10);
    }

    // ===== calculateHigh =====

    @Test
    void calculateHighは正の差分を返す() {
        TestableWaveletTransformation wt = new TestableWaveletTransformation();
        assertEquals(1.0, wt.exposedCalculateHigh(4.0, 2.0), 1e-10);
    }

    @Test
    void calculateHighはaがbより小さいとき負の値を返す() {
        TestableWaveletTransformation wt = new TestableWaveletTransformation();
        assertEquals(-1.0, wt.exposedCalculateHigh(-1.0, 1.0), 1e-10);
    }

    @Test
    void calculateHighは同じ値を渡すと0を返す() {
        TestableWaveletTransformation wt = new TestableWaveletTransformation();
        assertEquals(0.0, wt.exposedCalculateHigh(5.0, 5.0), 1e-10);
    }

    @Test
    void calculateHighは0を含む場合も正しく計算される() {
        TestableWaveletTransformation wt = new TestableWaveletTransformation();
        assertEquals(3.0, wt.exposedCalculateHigh(6.0, 0.0), 1e-10);
    }

    // ===== restoreLeft =====

    @Test
    void restoreLeftはlowとhighの和を返す() {
        TestableWaveletTransformation wt = new TestableWaveletTransformation();
        assertEquals(7.0, wt.exposedRestoreLeft(5.0, 2.0), 1e-10);
    }

    @Test
    void restoreLeftはhighが負のとき差を返す() {
        TestableWaveletTransformation wt = new TestableWaveletTransformation();
        assertEquals(3.0, wt.exposedRestoreLeft(5.0, -2.0), 1e-10);
    }

    @Test
    void restoreLeftは0同士のとき0を返す() {
        TestableWaveletTransformation wt = new TestableWaveletTransformation();
        assertEquals(0.0, wt.exposedRestoreLeft(0.0, 0.0), 1e-10);
    }

    // ===== restoreRight =====

    @Test
    void restoreRightはlowとhighの差を返す() {
        TestableWaveletTransformation wt = new TestableWaveletTransformation();
        assertEquals(3.0, wt.exposedRestoreRight(5.0, 2.0), 1e-10);
    }

    @Test
    void restoreRightはhighが負のとき和を返す() {
        TestableWaveletTransformation wt = new TestableWaveletTransformation();
        assertEquals(7.0, wt.exposedRestoreRight(5.0, -2.0), 1e-10);
    }

    @Test
    void restoreRightは0同士のとき0を返す() {
        TestableWaveletTransformation wt = new TestableWaveletTransformation();
        assertEquals(0.0, wt.exposedRestoreRight(0.0, 0.0), 1e-10);
    }

    // ===== calculateLow/High → restoreLeft/Right ラウンドトリップ =====

    @Test
    void calculateとrestoreで正の値のペアが完全復元される() {
        TestableWaveletTransformation wt = new TestableWaveletTransformation();
        double a = 7.0, b = 3.0;
        double low  = wt.exposedCalculateLow(a, b);
        double high = wt.exposedCalculateHigh(a, b);
        assertEquals(a, wt.exposedRestoreLeft(low, high),  1e-10);
        assertEquals(b, wt.exposedRestoreRight(low, high), 1e-10);
    }

    @Test
    void calculateとrestoreで負の値のペアが完全復元される() {
        TestableWaveletTransformation wt = new TestableWaveletTransformation();
        double a = -4.0, b = -8.0;
        double low  = wt.exposedCalculateLow(a, b);
        double high = wt.exposedCalculateHigh(a, b);
        assertEquals(a, wt.exposedRestoreLeft(low, high),  1e-10);
        assertEquals(b, wt.exposedRestoreRight(low, high), 1e-10);
    }

    // ===== merge =====

    @Test
    void mergeはlowとhighをこの順に結合する() {
        TestableWaveletTransformation wt = new TestableWaveletTransformation();
        assertArrayEquals(
            new double[]{1.0, 2.0, 3.0, 4.0},
            wt.exposedMerge(new double[]{1.0, 2.0}, new double[]{3.0, 4.0}),
            1e-10
        );
    }

    @Test
    void mergeは長さ1同士を結合すると長さ2になる() {
        TestableWaveletTransformation wt = new TestableWaveletTransformation();
        assertArrayEquals(
            new double[]{5.0, 9.0},
            wt.exposedMerge(new double[]{5.0}, new double[]{9.0}),
            1e-10
        );
    }

    @Test
    void mergeは空配列同士を結合すると空配列を返す() {
        TestableWaveletTransformation wt = new TestableWaveletTransformation();
        assertArrayEquals(
            new double[]{},
            wt.exposedMerge(new double[]{}, new double[]{})
        );
    }

    @Test
    void mergeの結合後の長さはlowとhighの長さの合計() {
        TestableWaveletTransformation wt = new TestableWaveletTransformation();
        assertEquals(6,
            wt.exposedMerge(new double[]{1.0, 2.0, 3.0}, new double[]{4.0, 5.0, 6.0}).length
        );
    }

    // ===== split =====

    @Test
    void splitはlowとhighに正しく分離される() {
        TestableWaveletTransformation wt = new TestableWaveletTransformation();

        // {4, 2, 6, 8}
        // low[0]=(4+2)/2=3, low[1]=(6+8)/2=7
        // high[0]=(4-2)/2=1, high[1]=(6-8)/2=-1
        double[][] result = wt.exposedSplit(new double[]{4.0, 2.0, 6.0, 8.0});

        assertArrayEquals(new double[]{3.0, 7.0},  result[0], 1e-10);
        assertArrayEquals(new double[]{1.0, -1.0}, result[1], 1e-10);
    }

    @Test
    void splitの結果はそれぞれ元の長さの半分() {
        TestableWaveletTransformation wt = new TestableWaveletTransformation();
        double[][] result = wt.exposedSplit(new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0});
        assertEquals(3, result[0].length);
        assertEquals(3, result[1].length);
    }

    @Test
    void splitは長さ2の入力に対して長さ1のlowとhighを返す() {
        TestableWaveletTransformation wt = new TestableWaveletTransformation();
        double[][] result = wt.exposedSplit(new double[]{6.0, 2.0});
        assertArrayEquals(new double[]{4.0}, result[0], 1e-10);
        assertArrayEquals(new double[]{2.0}, result[1], 1e-10);
    }

    @Test
    void splitは同じ値のペアのときhighが0になる() {
        TestableWaveletTransformation wt = new TestableWaveletTransformation();
        double[][] result = wt.exposedSplit(new double[]{5.0, 5.0, 3.0, 3.0});
        assertArrayEquals(new double[]{0.0, 0.0}, result[1], 1e-10);
    }

    // ===== decompose =====

    @Test
    void decomposeはlowとhighを結合した係数配列を返す() {
        TestableWaveletTransformation wt = new TestableWaveletTransformation();
        assertArrayEquals(
            new double[]{3.0, 7.0, 1.0, -1.0},
            wt.exposedDecompose(new double[]{4.0, 2.0, 6.0, 8.0}),
            1e-10
        );
    }

    @Test
    void decomposeの出力長は入力長と等しい() {
        TestableWaveletTransformation wt = new TestableWaveletTransformation();
        double[] signal = {1.0, 2.0, 3.0, 4.0};
        assertEquals(signal.length, wt.exposedDecompose(signal).length);
    }

    @Test
    void decomposeは長さ2の最小入力でも動作する() {
        TestableWaveletTransformation wt = new TestableWaveletTransformation();
        // {6, 2} → low=4, high=2 → [4, 2]
        assertArrayEquals(
            new double[]{4.0, 2.0},
            wt.exposedDecompose(new double[]{6.0, 2.0}),
            1e-10
        );
    }

    // ===== reconstruct =====

    @Test
    void reconstructは係数から正しく元信号を復元する() {
        TestableWaveletTransformation wt = new TestableWaveletTransformation();
        // [3, 7, 1, -1] → {4, 2, 6, 8}
        assertArrayEquals(
            new double[]{4.0, 2.0, 6.0, 8.0},
            wt.exposedReconstruct(new double[]{3.0, 7.0, 1.0, -1.0}),
            1e-10
        );
    }

    @Test
    void reconstructの出力長は係数長と等しい() {
        TestableWaveletTransformation wt = new TestableWaveletTransformation();
        double[] coeff = {3.0, 7.0, 1.0, -1.0};
        assertEquals(coeff.length, wt.exposedReconstruct(coeff).length);
    }

    @Test
    void reconstructは長さ2の最小係数でも動作する() {
        TestableWaveletTransformation wt = new TestableWaveletTransformation();
        // [4, 2] → {6, 2}
        assertArrayEquals(
            new double[]{6.0, 2.0},
            wt.exposedReconstruct(new double[]{4.0, 2.0}),
            1e-10
        );
    }

    // ===== decompose → reconstruct ラウンドトリップ =====

    @Test
    void decomposeとreconstructで偶数長信号が完全復元される() {
        TestableWaveletTransformation wt = new TestableWaveletTransformation();
        double[] original = {4.0, 2.0, 6.0, 8.0};
        assertArrayEquals(original, wt.exposedReconstruct(wt.exposedDecompose(original)), 1e-10);
    }

    @Test
    void decomposeとreconstructで長さ2の最小信号が復元される() {
        TestableWaveletTransformation wt = new TestableWaveletTransformation();
        double[] original = {10.0, 4.0};
        assertArrayEquals(original, wt.exposedReconstruct(wt.exposedDecompose(original)), 1e-10);
    }

    @Test
    void decomposeとreconstructですべて0の信号が復元される() {
        TestableWaveletTransformation wt = new TestableWaveletTransformation();
        double[] original = {0.0, 0.0, 0.0, 0.0};
        assertArrayEquals(original, wt.exposedReconstruct(wt.exposedDecompose(original)), 1e-10);
    }

    @Test
    void decomposeとreconstructで負の値を含む信号が復元される() {
        TestableWaveletTransformation wt = new TestableWaveletTransformation();
        double[] original = {-3.0, 5.0, -1.0, 7.0};
        assertArrayEquals(original, wt.exposedReconstruct(wt.exposedDecompose(original)), 1e-10);
    }
}