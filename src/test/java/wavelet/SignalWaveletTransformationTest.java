package wavelet;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import waveletModel.SignalWaveletModel;
import transformation.SignalWaveletTransformation;

class SignalWaveletTransformationTest {

    @Test
    void changeWaveletDataで元信号をModelに設定できる() {
        SignalWaveletTransformation transformation = new SignalWaveletTransformation();

        double[] signal = {1.0, 2.0, 3.0};

        transformation.changeWaveletData(signal);

        assertArrayEquals(
            signal,
            transformation.getSignalWaveletModel().getOriginalSignal()
        );
    }

    @Test
    void 偶数長の信号を変換できる() {
        SignalWaveletTransformation transformation = new SignalWaveletTransformation();

        transformation.changeWaveletData(new double[]{4.0, 2.0, 8.0, 6.0});

        SignalWaveletModel result = transformation.startWaveletTransformation();

        assertArrayEquals(
            new double[]{3.0, 7.0, 1.0, 1.0},
            result.getTransformedSignal(),
            0.000001
        );
    }

    @Test
    void 奇数長の信号はpaddingして変換できる() {
        SignalWaveletTransformation transformation = new SignalWaveletTransformation();

        transformation.changeWaveletData(new double[]{4.0, 2.0, 8.0});

        SignalWaveletModel result = transformation.startWaveletTransformation();

        // 8.0 が末尾に複製されて {4,2,8,8} として変換される
        assertArrayEquals(
            new double[]{3.0, 8.0, 1.0, 0.0},
            result.getTransformedSignal(),
            0.000001
        );
    }

    @Test
    void 偶数長の信号を変換して逆変換すると元に戻る() {
        SignalWaveletTransformation transformation = new SignalWaveletTransformation();

        double[] original = {10.0, 6.0, 4.0, 2.0};

        transformation.changeWaveletData(original);
        transformation.startWaveletTransformation();

        SignalWaveletModel result = transformation.startInverseWaveletTransformation();

        assertArrayEquals(
            original,
            result.getReconstructedSignal(),
            0.000001
        );
    }

    @Test
    void 奇数長の信号を変換して逆変換するとpaddingが除去されて元に戻る() {
        SignalWaveletTransformation transformation = new SignalWaveletTransformation();

        double[] original = {10.0, 6.0, 4.0};

        transformation.changeWaveletData(original);
        transformation.startWaveletTransformation();

        SignalWaveletModel result = transformation.startInverseWaveletTransformation();

        assertArrayEquals(
            original,
            result.getReconstructedSignal(),
            0.000001
        );
    }

    
  @Test
  void getSignalWaveletModelで同じModelを取得できる() {
    SignalWaveletTransformation transformation = new SignalWaveletTransformation();

    transformation.changeWaveletData(new double[]{1.0, 2.0});

    SignalWaveletModel model1 = transformation.getSignalWaveletModel();
    SignalWaveletModel model2 = transformation.startWaveletTransformation();

    assertSame(model1, model2);
  }

  @Test
  void 入力が空の配列の場合例外を投げる() {
    SignalWaveletTransformation transformation = new SignalWaveletTransformation();

    transformation.changeWaveletData(new double[]{});

    assertThrows(
        IllegalArgumentException.class,
        () -> transformation.startWaveletTransformation()
    );
  }

  @Test
  void 入力が1要素の場合例外を投げる() {
    SignalWaveletTransformation transformation = new SignalWaveletTransformation();

    transformation.changeWaveletData(new double[]{1.0});

    assertThrows(
        IllegalArgumentException.class,
        () -> transformation.startWaveletTransformation()
    );
  }
}