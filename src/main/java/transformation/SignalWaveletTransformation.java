package transformation;
 
import app.BoolEx;
import waveletModel.SignalWaveletModel;

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
        signalWaveletModel.setTransformedSignal(coefficients);
        return signalWaveletModel;
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
 