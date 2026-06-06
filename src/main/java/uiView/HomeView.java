package uiView;

import listener.ActionListener;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uiModel.HomeModel;


public class HomeView {

  private ActionListener uiListener;
  private Stage aStage;

  public HomeView(ActionListener uiListener, Stage aStage) {
    this.uiListener = uiListener;
    this.aStage = aStage;
  }

  public void createScene(HomeModel homeModel) {
    System.out.println("HomeView: createScene");

    VBox root = new VBox(homeModel.getSpacing());
    Label title = createTitle(homeModel.getTitle());
    Scene scene = new Scene(root, homeModel.getDisplaySize().getX(), homeModel.getDisplaySize().getY());

    Button signalButton = createButton(homeModel.getSignalButtonText());
    Button imageButton = createButton(homeModel.getImageButtonText());
    Button settingButton = createButton(homeModel.getSettingButtonText());

    root.getChildren().addAll(title, signalButton, imageButton, settingButton);
    aStage.setScene(scene);
    aStage.show(); 
  }

  public Button createButton(String text) {
    System.out.println("HomeView: createButton");

    Button button = new Button(text);
    button.setOnAction(event -> uiListener.actionPerformed(event));

    return button;
  }

  public Label createTitle(String text) {
    System.out.println("HomeView: createTitle");

    return new Label(text);
  }

}
