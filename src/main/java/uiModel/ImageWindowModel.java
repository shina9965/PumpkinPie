package uiModel;

import javafx.scene.image.Image;
import uiController.ButtonRecord;

public class ImageWindowModel {

  // 入力された画像を保持する
  private Image originalImage;

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
  }

  // 入力された画像を取得する
  public Image getOriginalImage() {
    return originalImage;
  }

  // 入力された画像を保持する
  public void setOriginalImage(Image image) {
    originalImage = image;
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
