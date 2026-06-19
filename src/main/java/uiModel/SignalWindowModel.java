package uiModel;

import app.BoolEx;
import transformation.SignalWaveletTransformation;
import uiController.ButtonRecord;
import waveletModel.SignalWaveletModel;

public class SignalWindowModel {

  // 元信号を保持する
  private double[] originalSignal;

  // 逆変換後信号を保持する
  private double[] inverseSignal;

  // スケーリング係数を保持する
  private double[] scalingCoefficient;

  // ウェーブレット展開係数を保持する
  private double[] waveletCoefficient;

  // 編集後のウェーブレット展開係数を保持する
  private double[] editedWaveletCoefficient;

  // 選択中の係数番号を保持する
  private int selectedCoefficientIndex;

  // 信号用ウェーブレット変換を行うオブジェクト
  private SignalWaveletTransformation signalWaveletTransformation;

  // 信号保存ボタンの表示文字とIDを保持する
  private final ButtonRecord saveSignalButtonData =
      new ButtonRecord("信号保存", "SAVE_SIGNAL");

  // 信号入力ボタンの表示文字とIDを保持する
  private final ButtonRecord inputSignalButtonData =
      new ButtonRecord("信号入力", "INPUT_SIGNAL");

  // 戻るボタンの表示文字とIDを保持する
  private final ButtonRecord returnButtonData =
      new ButtonRecord("戻る", "RETURN");

  // Modelを初期化する
  public SignalWindowModel() {
    originalSignal = new double[0];
    inverseSignal = new double[0];
    scalingCoefficient = new double[0];
    waveletCoefficient = new double[0];
    editedWaveletCoefficient = new double[0];

    selectedCoefficientIndex = -1;

    signalWaveletTransformation = new SignalWaveletTransformation();
  }

  // 元信号を取得する
  public double[] getOriginalSignal() {
    return originalSignal;
  }

  // 逆変換後信号を取得する
  public double[] getInverseSignal() {
    return inverseSignal;
  }

  // スケーリング係数を取得する
  public double[] getScalingCoefficient() {
    return scalingCoefficient;
  }

  // ウェーブレット展開係数を取得する
  public double[] getWaveletCoefficient() {
    return waveletCoefficient;
  }

  // 編集後のウェーブレット展開係数を取得する
  public double[] getEditedWaveletCoefficient() {
    return editedWaveletCoefficient;
  }

  // 選択中の係数番号を取得する
  public int getSelectedCoefficientIndex() {
    return selectedCoefficientIndex;
  }

  // 信号保存ボタンのデータを取得する
  public ButtonRecord getSaveSignalButtonData() {
    return saveSignalButtonData;
  }

  // 信号入力ボタンのデータを取得する
  public ButtonRecord getInputSignalButtonData() {
    return inputSignalButtonData;
  }

  // 戻るボタンのデータを取得する
  public ButtonRecord getReturnButtonData() {
    return returnButtonData;
  }

  // 元信号を保持し、ウェーブレット変換結果を更新する
  public void setOriginalSignal(double[] signal) {
    originalSignal = signal;

    signalWaveletTransformation.changeWaveletData(originalSignal);

    SignalWaveletModel result =
        signalWaveletTransformation.startWaveletTransformation();

    setTransformedSignal(result.getTransformedSignal());

    updateInverseSignal();
  }

  // 逆変換後信号を保持する
  public void setInverseSignal(double[] signal) {
    inverseSignal = signal;
  }

  // スケーリング係数を保持する
  public void setScalingCoefficient(double[] coefficient) {
    scalingCoefficient = coefficient;
  }

  // ウェーブレット展開係数を保持する
  public void setWaveletCoefficient(double[] coefficient) {
    waveletCoefficient = coefficient;
    editedWaveletCoefficient = coefficient.clone();
  }

  // 変換結果をスケーリング係数とウェーブレット展開係数に分けて保持する
  private void setTransformedSignal(double[] transformedSignal) {
    int halfLength = transformedSignal.length / 2;

    scalingCoefficient = new double[halfLength];
    waveletCoefficient = new double[halfLength];

    System.arraycopy(transformedSignal, 0, scalingCoefficient, 0, halfLength);
    System.arraycopy(transformedSignal, halfLength, waveletCoefficient, 0, halfLength);

    editedWaveletCoefficient = waveletCoefficient.clone();
  }

  // 指定された係数の有効・無効を切り替える
  public void toggleCoefficient(int index) {
    BoolEx.ifTrueElse(
        index >= 0 && index < editedWaveletCoefficient.length,
        () -> {
          selectedCoefficientIndex = index;

          BoolEx.ifTrueElse(
              editedWaveletCoefficient[index] == 0.0,
              () -> editedWaveletCoefficient[index] = waveletCoefficient[index],
              () -> editedWaveletCoefficient[index] = 0.0
          );

          updateInverseSignal();
        }
    );
  }

  // 編集後の係数を使って逆変換後信号を更新する
  private void updateInverseSignal() {
    double[] transformedSignal = mergeCoefficient();

    SignalWaveletModel signalWaveletModel =
        signalWaveletTransformation.getSignalWaveletModel();

    signalWaveletModel.setTransformedSignal(transformedSignal);

    SignalWaveletModel result =
        signalWaveletTransformation.startInverseWaveletTransformation();

    inverseSignal = result.getReconstructedSignal();
  }

  // スケーリング係数と編集後ウェーブレット展開係数を1つの配列にまとめる
  private double[] mergeCoefficient() {
    double[] transformedSignal =
        new double[scalingCoefficient.length + editedWaveletCoefficient.length];

    System.arraycopy(
        scalingCoefficient,
        0,
        transformedSignal,
        0,
        scalingCoefficient.length
    );

    System.arraycopy(
        editedWaveletCoefficient,
        0,
        transformedSignal,
        scalingCoefficient.length,
        editedWaveletCoefficient.length
    );

    return transformedSignal;
  }
}