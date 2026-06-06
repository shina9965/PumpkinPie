package app;

import javafx.application.Application;
import javafx.stage.Stage;

public class PumpkinPieApplication extends Application {

  Stage aStage;

  @Override
  public void start(Stage aStage) {
    this.aStage = aStage;
    aStage.setTitle("PumpkinPie");
    aStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}