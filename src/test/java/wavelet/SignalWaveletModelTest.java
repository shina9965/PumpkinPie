package wavelet;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import waveletModel.SignalWaveletModel;

class SignalWaveletModelTest {

    // ===== 初期状態 =====

    @Test
    void 初期状態でoriginalSignalは空配列() {
        SignalWaveletModel model = new SignalWaveletModel();
        assertArrayEquals(new double[]{}, model.getOriginalSignal());
    }

    @Test
    void 初期状態でtransformedSignalは空配列() {
        SignalWaveletModel model = new SignalWaveletModel();
        assertArrayEquals(new double[]{}, model.getTransformedSignal());
    }

    @Test
    void 初期状態でreconstructedSignalは空配列() {
        SignalWaveletModel model = new SignalWaveletModel();
        assertArrayEquals(new double[]{}, model.getReconstructedSignal());
    }

    @Test
    void 初期状態でoriginalLengthは0() {
        SignalWaveletModel model = new SignalWaveletModel();
        assertEquals(0, model.getOriginalLength());
    }

    // ===== setOriginalSignal / getOriginalSignal =====

    @Test
    void setOriginalSignalで設定した信号をgetOriginalSignalで取得できる() {
        SignalWaveletModel model = new SignalWaveletModel();
        double[] signal = {1.0, 2.0, 3.0};
        model.setOriginalSignal(signal);
        assertArrayEquals(signal, model.getOriginalSignal());
    }

    @Test
    void setOriginalSignalで上書きすると新しい値が返る() {
        SignalWaveletModel model = new SignalWaveletModel();
        model.setOriginalSignal(new double[]{1.0, 2.0});
        model.setOriginalSignal(new double[]{9.0, 8.0, 7.0});
        assertArrayEquals(new double[]{9.0, 8.0, 7.0}, model.getOriginalSignal());
    }

    @Test
    void setOriginalSignalで空配列を設定できる() {
        SignalWaveletModel model = new SignalWaveletModel();
        model.setOriginalSignal(new double[]{});
        assertArrayEquals(new double[]{}, model.getOriginalSignal());
    }

    // ===== getOriginalLength =====

    @Test
    void setOriginalSignalでoriginalLengthが信号長に更新される() {
        SignalWaveletModel model = new SignalWaveletModel();
        model.setOriginalSignal(new double[]{1.0, 2.0, 3.0});
        assertEquals(3, model.getOriginalLength());
    }

    @Test
    void setOriginalSignalで上書きするとoriginalLengthも更新される() {
        SignalWaveletModel model = new SignalWaveletModel();
        model.setOriginalSignal(new double[]{1.0, 2.0});
        model.setOriginalSignal(new double[]{5.0, 6.0, 7.0, 8.0});
        assertEquals(4, model.getOriginalLength());
    }

    @Test
    void setOriginalSignalで空配列を設定するとoriginalLengthは0() {
        SignalWaveletModel model = new SignalWaveletModel();
        model.setOriginalSignal(new double[]{});
        assertEquals(0, model.getOriginalLength());
    }

    @Test
    void setTransformedSignalはoriginalLengthに影響しない() {
        SignalWaveletModel model = new SignalWaveletModel();
        model.setOriginalSignal(new double[]{1.0, 2.0, 3.0});
        model.setTransformedSignal(new double[]{10.0, 20.0});
        assertEquals(3, model.getOriginalLength());
    }

    // ===== setTransformedSignal / getTransformedSignal =====

    @Test
    void setTransformedSignalで設定した係数をgetTransformedSignalで取得できる() {
        SignalWaveletModel model = new SignalWaveletModel();
        double[] coeff = {3.0, 7.0, 1.0, -1.0};
        model.setTransformedSignal(coeff);
        assertArrayEquals(coeff, model.getTransformedSignal());
    }

    @Test
    void setTransformedSignalで上書きすると新しい値が返る() {
        SignalWaveletModel model = new SignalWaveletModel();
        model.setTransformedSignal(new double[]{1.0, 2.0});
        model.setTransformedSignal(new double[]{9.0, 8.0});
        assertArrayEquals(new double[]{9.0, 8.0}, model.getTransformedSignal());
    }

    @Test
    void setTransformedSignalで空配列を設定できる() {
        SignalWaveletModel model = new SignalWaveletModel();
        model.setTransformedSignal(new double[]{});
        assertArrayEquals(new double[]{}, model.getTransformedSignal());
    }

    // ===== setReconstructedSignal / getReconstructedSignal =====

    @Test
    void setReconstructedSignalで設定した信号をgetReconstructedSignalで取得できる() {
        SignalWaveletModel model = new SignalWaveletModel();
        double[] signal = {4.0, 2.0, 6.0};
        model.setReconstructedSignal(signal);
        assertArrayEquals(signal, model.getReconstructedSignal());
    }

    @Test
    void setReconstructedSignalで上書きすると新しい値が返る() {
        SignalWaveletModel model = new SignalWaveletModel();
        model.setReconstructedSignal(new double[]{1.0});
        model.setReconstructedSignal(new double[]{5.0, 6.0});
        assertArrayEquals(new double[]{5.0, 6.0}, model.getReconstructedSignal());
    }

    @Test
    void setReconstructedSignalで空配列を設定できる() {
        SignalWaveletModel model = new SignalWaveletModel();
        model.setReconstructedSignal(new double[]{});
        assertArrayEquals(new double[]{}, model.getReconstructedSignal());
    }

    // ===== removePadding =====

    @Test
    void removePaddingでpadding要素が除去されてoriginalLengthに切り詰められる() {
        SignalWaveletModel model = new SignalWaveletModel();
        model.setOriginalSignal(new double[]{1.0, 2.0, 3.0}); // originalLength = 3
        assertArrayEquals(
            new double[]{1.0, 2.0, 3.0},
            model.removePadding(new double[]{1.0, 2.0, 3.0, 3.0})
        );
    }

    @Test
    void removePaddingでpaddingなしの偶数長はそのまま返る() {
        SignalWaveletModel model = new SignalWaveletModel();
        model.setOriginalSignal(new double[]{4.0, 2.0, 6.0, 8.0}); // originalLength = 4
        double[] signal = {4.0, 2.0, 6.0, 8.0};
        assertArrayEquals(signal, model.removePadding(signal));
    }

    @Test
    void removePaddingでoriginalLengthが1のとき先頭要素のみ返る() {
        SignalWaveletModel model = new SignalWaveletModel();
        model.setOriginalSignal(new double[]{7.0}); // originalLength = 1
        assertArrayEquals(
            new double[]{7.0},
            model.removePadding(new double[]{7.0, 7.0})
        );
    }

    @Test
    void removePaddingで入力長とoriginalLengthが同じとき全要素がそのまま返る() {
        SignalWaveletModel model = new SignalWaveletModel();
        model.setOriginalSignal(new double[]{1.0, 2.0});
        assertArrayEquals(
            new double[]{1.0, 2.0},
            model.removePadding(new double[]{1.0, 2.0})
        );
    }
}
