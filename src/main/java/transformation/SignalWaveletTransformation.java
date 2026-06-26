package transformation;
 
import app.BoolEx;
import waveletModel.SignalWaveletModel;
import uiModel.SettingModel;
import java.util.Arrays;

/**
 * 信号向けウェーブレット変換の具象クラス
 * WaveletTransformationを継承し、SignalWaveletModelを用いて
 * 信号のウェーブレット変換・逆変換を実現する
 */
public class SignalWaveletTransformation extends WaveletTransformation<SignalWaveletModel> {
 
    /** Signal用Model：元信号やウェーブレット係数へのアクセスに使用 */
    private final SignalWaveletModel signalWaveletModel;
 
    // ===== コンストラクタ =====
 
    /**
     * SignalWaveletModelをここでnewし、インスタンスを初期化する
     */
    public SignalWaveletTransformation() {
        this.signalWaveletModel = new SignalWaveletModel();
    }
 
    // ===== WaveletTransformation オーバーライド =====
 
    /**
     * ウェーブレット変換を開始する
     * Modelから元信号を取得し、padding後にdecomposeを実行して変換係数をModelへ保存する
     */
    @Override
    public SignalWaveletModel startWaveletTransformation() {
        return startSignalWaveletTransformation();
    }
 
    /**
     * ウェーブレット逆変換を開始する
     * Modelから変換係数を取得し、reconstructを実行してpaddingを除去した後、復元信号をModelへ保存する
     */
    @Override
    public SignalWaveletModel startInverseWaveletTransformation() {
        return startInverseSignalWaveletTransformation();
    }
 
    /**
     * ウェーブレットデータ（元信号）をModelへ設定する
     *
     * @param value 新しい信号データ
     */
    @Override
    public void changeWaveletData(double[] value) {
        signalWaveletModel.setOriginalSignal(value);
    }
 
    // ===== Signal固有のメソッド =====
 
    /**
     * 信号のウェーブレット変換を実行し、変換後のModelを返す
     * ・信号データの要素数が1以下なら例外を投げる
     * ・元信号をpaddingして偶数長にする
     * ・decomposeでウェーブレット係数を計算する
     * ・結果をModelへ保存する
     *
     * @return 変換結果を保持したSignalWaveletModel
     */
    public SignalWaveletModel startSignalWaveletTransformation() {
        double[] original = signalWaveletModel.getOriginalSignal();
        BoolEx.ifTrueElse(
            original.length <= 1,
            () -> { throw new IllegalArgumentException("信号データは2要素以上必要です: 要素数=" + original.length); },
            () -> {}
        );
        double[] padded   = padding(original);
        double[] coefficients = decompose(padded);

        // 設定画面で設定した採用率を取得する
        int adoptionRate = SettingModel.getSystemAdoptionRate();
        
        // 採用率に応じて係数を残す
        coefficients = filterCoefficients(coefficients, adoptionRate);

        signalWaveletModel.setTransformedSignal(coefficients);
        return signalWaveletModel;
    }
 
    /**
     * スケーリング係数はそのまま残し、
     * ウェーブレット展開係数だけに採用率を適用する。
     *
     * @param coefficients ウェーブレット係数配列
     * @param adoptionRate 採用率
     * @return 採用率を適用したウェーブレット係数配列
     */
    private double[] filterCoefficients(double[] coefficients, int adoptionRate) {
        double[] filtered = Arrays.copyOf(coefficients, coefficients.length);
        int halfLength = coefficients.length / 2;

        BoolEx.ifTrueElse(
            adoptionRate >= 100,
            () -> {},
            () -> {
                BoolEx.ifTrueElse(
                    adoptionRate <= 0,
                    () -> {

                        for (int index = halfLength; index < filtered.length; index++) {
                            filtered[index] = 0.0;
                        }
                    },
                    () -> {
                        int waveletLength = coefficients.length - halfLength;
                        int keepCount = (int) Math.ceil(waveletLength * adoptionRate / 100.0);

                        double[] absoluteValues = new double[waveletLength];

                        for (int index = 0; index < waveletLength; index++) {
                            absoluteValues[index] = Math.abs(coefficients[halfLength + index]);
                        }

                        Arrays.sort(absoluteValues);

                        double threshold = absoluteValues[waveletLength - keepCount];

                        for (int index = halfLength; index < filtered.length; index++) {
                            int currentIndex = index;

                            BoolEx.ifTrueElse(
                                Math.abs(filtered[currentIndex]) < threshold,
                                () -> filtered[currentIndex] = 0.0,
                                () -> {}
                            );
                        }
                    }
                );
            }
        );

        return filtered;
    }

    /**
     * ウェーブレット逆変換を実行し、復元後のModelを返す
     * ・変換係数をreconstructで復元する
     * ・padding分を除去してoriginalLengthに戻す
     * ・結果をModelへ保存する
     *
     * @return 復元結果を保持したSignalWaveletModel
     */
    public SignalWaveletModel startInverseSignalWaveletTransformation() {
        double[] coefficients  = signalWaveletModel.getTransformedSignal();
        double[] reconstructed = reconstruct(coefficients);
        double[] trimmed       = signalWaveletModel.removePadding(reconstructed);
        signalWaveletModel.setReconstructedSignal(trimmed);
        return signalWaveletModel;
    }
 
    /**
     * 奇数長の場合に末尾画素を複製し、偶数長にpaddingする
     * 偶数長の場合はそのまま返す
     *
     * @param signal 入力信号
     * @return paddingされた（偶数長の）信号
     */
    protected double[] padding(double[] signal) {
        boolean isOdd = (signal.length % 2 != 0);
 
        double[][] result = {signal};   // 配列で包んでラムダ内から参照する
 
        BoolEx.ifTrueElse(
            isOdd,
            () -> {
                double[] padded = new double[signal.length + 1];
                System.arraycopy(signal, 0, padded, 0, signal.length);
                padded[signal.length] = signal[signal.length - 1]; // 末尾画素を複製
                result[0] = padded;
            },
            () -> result[0] = signal
        );
 
        return result[0];
    }
 
    /**
     * SignalWaveletModelを返す（外部からのモデル参照用）
     *
     * @return signalWaveletModel
     */
    public SignalWaveletModel getSignalWaveletModel() {
        return signalWaveletModel;
    }
}
 