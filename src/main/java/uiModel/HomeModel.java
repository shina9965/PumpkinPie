package uiModel;

import javafx.geometry.Point2D;
import uiController.ButtonRecord;


public class HomeModel {

  private final Point2D displaySize;
  
  private final String title = "Pumpkin Pie"; 
  private final double spacing = 20;

  private final ButtonRecord signalButtonData = new ButtonRecord("信号処理", "SIGNAL");
  private final ButtonRecord imageButtonData = new ButtonRecord("画像処理", "IMAGE");
  private final ButtonRecord settingButtonData = new ButtonRecord("設定", "SETTING");

  /**
   * HomeModelのコンストラクタ
   */
  public HomeModel() {
    this.displaySize = new Point2D(800, 600);
  }
  
  /**
   * ディスプレイサイズを取得するメソッド
   * @return ディスプレイサイズ
   */
  public Point2D getDisplaySize() {
    return displaySize;
  }

  /**
   * タイトルを取得するメソッド
   * @return タイトル
   */
  public String getTitle() {
    return title;
  }

  /**
   * スペースを取得するメソッド
   * @return スペース
   */
  public double getSpacing() {
    return spacing;
  }

  /**
   * 信号ボタンのデータを取得するメソッド
   * @return 信号ボタンのデータ
   */
  public ButtonRecord getSignalButtonData() {
    return signalButtonData;
  }

  /**
   * 画像ボタンのデータを取得するメソッド
   * @return 画像ボタンのデータ
   */
  public ButtonRecord getImageButtonData() {
    return imageButtonData;
  }

  /**
   * 設定ボタンのデータを取得するメソッド
   * @return 設定ボタンのデータ
   */
  public ButtonRecord getSettingButtonData() {
    return settingButtonData;
  }
}
