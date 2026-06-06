package uiView;

import listener.ActionListener;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class HomeView {

  private ActionListener uiListener;
  private Stage aStage;
  private final static String TITLE = "Pumpkin Pie"; 
  private final static double SPACING = 20;

  public HomeView(ActionListener uiListener, Stage aStage) {
    this.uiListener = uiListener;
    this.aStage = aStage;
  }

  public void createScene(Point2D size) {
    System.out.println("HomeView: createScene");

    VBox root = new VBox(SPACING);
    Label title = createTitle();
    Scene scene = new Scene(root, size.getX(), size.getY());

    Button signalButton = createButton("信号処理");
    Button imageButton = createButton("画像処理");
    Button settingButton = createButton("設定");
    signalButton.setOnAction(event -> uiListener.actionPerformed(event));
    imageButton.setOnAction(event -> uiListener.actionPerformed(event));
    settingButton.setOnAction(event -> uiListener.actionPerformed(event));


    root.getChildren().addAll(title, signalButton, imageButton, settingButton);
    aStage.setScene(scene);
    aStage.show(); 
  }

  public Button createButton(String text) {
    System.out.println("HomeView: createButton");

    return new Button(text);
  }

  public Label createTitle() {
    System.out.println("HomeView: createTitle");

    return new Label(TITLE);
  }

}
