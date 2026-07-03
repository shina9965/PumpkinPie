package uiModel;

import app.BoolEx;
import transformation.ImageWaveletTransformation;
import javafx.scene.image.Image;
import uiController.ButtonRecord;
import waveletModel.ImageWaveletModel;

public class ImageWindowModel {

  // 入力された画像を保持する
  private Image originalImage;

  // 逆変換後の画像を保持する
  private Image reconstructedImage;

  // 画像用ウェーブレット変換を行うオブジェクト
  private ImageWaveletTransformation imageWaveletTransformation;

  // 画像入力ボタンの表示文字とIDを保持する
  private final ButtonRecord inputImageButtonData =
      new ButtonRecord("画像入力", "INPUT_IMAGE");

  // 画像保存ボタンの表示文字とIDを保持する
  private final ButtonRecord saveImageButtonData =
      new ButtonRecord("画像保存", "SAVE_IMAGE");

  // 戻るボタンの表示文字とIDを保持する
  private final ButtonRecord returnButtonData =
      new ButtonRecord("戻る", "RETURN");

  // Modelを初期化する
  public ImageWindowModel() {
    originalImage = null;
    reconstructedImage = null;
    imageWaveletTransformation = new ImageWaveletTransformation();
  }

  // 入力された画像を取得する
  public Image getOriginalImage() {
    return originalImage;
  }

  // 逆変換後の画像を取得する
  public Image getReconstructedImage() {
    return reconstructedImage;
  }

  // 保存に使う画像を取得する
  public Image getOutputImage() {
    Image[] outputImage = {reconstructedImage};

    BoolEx.ifTrueElse(
        outputImage[0] == null,
        () -> outputImage[0] = originalImage);

    return outputImage[0];
  }

  // 入力された画像を保持する
  public void setOriginalImage(Image image) {
    originalImage = image;
    reconstructedImage = null;

    BoolEx.ifTrueElse(
        image != null,
        () -> {
          imageWaveletTransformation.changeWaveletImage(image);
          imageWaveletTransformation.startWaveletTransformation();

          ImageWaveletModel result =
              imageWaveletTransformation.startInverseWaveletTransformation();

          reconstructedImage = result.getReconstructedFxImage();
        });
  }

  // 画像入力ボタンのデータを取得する
  public ButtonRecord getInputImageButtonData() {
    return inputImageButtonData;
  }

  // 画像保存ボタンのデータを取得する
  public ButtonRecord getSaveImageButtonData() {
    return saveImageButtonData;
  }

  // 戻るボタンのデータを取得する
  public ButtonRecord getReturnButtonData() {
    return returnButtonData;
  }
}
