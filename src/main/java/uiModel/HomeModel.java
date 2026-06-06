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

  public ButtonRecord getSignalButtonData() {
    return signalButtonData;
  }

  public ButtonRecord getImageButtonData() {
    return imageButtonData;
  }

  public ButtonRecord getSettingButtonData() {
    return settingButtonData;
  }
}
