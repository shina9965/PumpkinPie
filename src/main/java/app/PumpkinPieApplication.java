package app;

import javafx.application.Application;
import javafx.stage.Stage;
import uiController.WindowStateController;

public class PumpkinPieApplication extends Application {

  Stage aStage;
  WindowStateController windowStateController;
  /**
   * JavaFXの最初に呼ばれるメソッド
   * @param aStage
   */
  @Override
  public void start(Stage aStage) {

    this.aStage = aStage;
    this.windowStateController = new WindowStateController(aStage);
  }
  
  public static void main(String[] args) {
    launch(args);
  }
}