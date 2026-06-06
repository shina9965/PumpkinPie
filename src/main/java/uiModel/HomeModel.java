package uiModel;

import javafx.geometry.Point2D;

public class HomeModel {

  private final Point2D displaySize;
  
  private final String title = "Pumpkin Pie"; 
  private final double spacing = 20;

  private final String signalButtonText = "信号処理";
  private final String imageButtonText = "画像処理";
  private final String settingButtonText = "設定";

  public HomeModel() {
    this.displaySize = new Point2D(800, 600);
  }

  public Point2D getDisplaySize() {
    return displaySize;
  }

  public String getTitle() {
    return title;
  }

  public double getSpacing() {
    return spacing;
  }

  public String getSignalButtonText() {
    return signalButtonText;
  }

  public String getImageButtonText() {
    return imageButtonText;
  }

  public String getSettingButtonText() {
    return settingButtonText;
  }
}
