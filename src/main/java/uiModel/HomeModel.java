package uiModel;

import javafx.geometry.Point2D;

public class HomeModel {

  private Point2D displaySize;

  public HomeModel() {
    this.displaySize = new Point2D(800, 600);
  }

  public Point2D getDisplaySize() {
    return displaySize;
  }
}
