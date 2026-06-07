package uiView;

import listener.ActionListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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

  /**
   * HomeViewのシーンを作成するメソッド。HomeControllerから呼び出されることを想定している。
   * @param homeModel
   */
  public void createScene(HomeModel homeModel) {
  System.out.println("HomeView: createScene");

  

  Label title = createTitle(homeModel.getTitle());

  Button signalButton = createButton(homeModel.getSignalButtonData().text(), homeModel.getSignalButtonData().id());
  Button imageButton = createButton(homeModel.getImageButtonData().text(), homeModel.getImageButtonData().id());
  Button settingButton = createButton(homeModel.getSettingButtonData().text(), homeModel.getSettingButtonData().id());

  BorderPane root = new BorderPane();

  HBox buttonBox = new HBox(20);
  buttonBox.setAlignment(Pos.CENTER_LEFT);
  buttonBox.setPadding(new Insets(20));
  buttonBox.getChildren().addAll(signalButton, imageButton, settingButton);

  root.setCenter(title);
  root.setBottom(buttonBox);

  BorderPane.setAlignment(buttonBox, Pos.BOTTOM_LEFT);

  Scene scene = new Scene(
    root,
    homeModel.getDisplaySize().getX(),
    homeModel.getDisplaySize().getY()
  );

  aStage.setScene(scene);
  aStage.show();
}

  /**
   * ボタンを作成するメソッド。HomeViewのcreateSceneから呼び出されることを想定している。
   * @param text ボタンに表示するテキスト
   * @param id ボタンのID
   * @return 作成されたボタン
   */
  public Button createButton(String text, String id) {
    System.out.println("HomeView: createButton");

    Button button = new Button(text);
    button.setId(id);
    button.setOnAction(event -> uiListener.actionPerformed(event));

    return button;
  }

  /**
   * タイトルラベルを作成するメソッド。HomeViewのcreateSceneから呼び出されることを想定している。
   * @param text タイトルに表示するテキスト
   * @return 作成されたタイトルラベル
   */
  public Label createTitle(String text) {
    System.out.println("HomeView: createTitle");

    return new Label(text);
  }

}
