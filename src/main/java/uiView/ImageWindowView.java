package uiView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import listener.ActionListener;
import uiModel.ImageWindowModel;

public class ImageWindowView {

  // 画面全体の横幅
  private static final double WINDOW_WIDTH = 1200;

  // 画面全体の縦幅
  private static final double WINDOW_HEIGHT = 650;

  // 画像表示領域の横幅
  private static final double IMAGE_VIEW_WIDTH = 900;

  // 画像表示領域の縦幅
  private static final double IMAGE_VIEW_HEIGHT = 450;

  // ボタン操作などのイベントをControllerへ渡すためのリスナー
  private ActionListener actionListener;

  // 入力画像を表示する部品
  private ImageView originalImageView;

  // 画像未入力時の表示
  private Label emptyLabel;

  // Viewを初期化し、Controllerへイベントを渡せるようにする
  public ImageWindowView(ActionListener actionListener) {
    this.actionListener = actionListener;
  }

  // 画像処理画面全体のSceneを作成する
  public Scene createScene(ImageWindowModel imageWindowModel) {
    System.out.println("ImageWindowView: createScene");

    StackPane imagePane = createImagePane();
    HBox buttonBox = createButtons(imageWindowModel);

    BorderPane root = new BorderPane();
    root.setPadding(new Insets(30));
    root.setCenter(imagePane);
    root.setBottom(buttonBox);

    updateOriginalImage(imageWindowModel.getOriginalImage());

    return new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
  }

  // 画像を表示する領域を作成する
  private StackPane createImagePane() {
    originalImageView = new ImageView();
    originalImageView.setFitWidth(IMAGE_VIEW_WIDTH);
    originalImageView.setFitHeight(IMAGE_VIEW_HEIGHT);
    originalImageView.setPreserveRatio(true);
    originalImageView.setSmooth(true);

    emptyLabel = new Label("画像が選択されていません");
    emptyLabel.setStyle("-fx-font-size: 18px;");

    StackPane imagePane = new StackPane();
    imagePane.setAlignment(Pos.CENTER);
    imagePane.setStyle(
        "-fx-border-color: black;" +
        "-fx-border-width: 1;" +
        "-fx-background-color: white;"
    );
    imagePane.getChildren().addAll(originalImageView, emptyLabel);

    return imagePane;
  }

  // ImageWindowModelが持つボタン情報を使って、画像入力・戻るボタンを作成する
  private HBox createButtons(ImageWindowModel imageWindowModel) {
    Button inputButton = createButton(
        imageWindowModel.getInputImageButtonData().text(),
        imageWindowModel.getInputImageButtonData().id()
    );

    Button returnButton = createButton(
        imageWindowModel.getReturnButtonData().text(),
        imageWindowModel.getReturnButtonData().id()
    );

    HBox leftButtons = new HBox(30);
    leftButtons.getChildren().add(inputButton);

    HBox rightButtons = new HBox();
    rightButtons.getChildren().add(returnButton);

    HBox buttonBox = new HBox(820);
    buttonBox.setPadding(new Insets(25, 40, 10, 40));
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.getChildren().addAll(leftButtons, rightButtons);

    return buttonBox;
  }

  // ボタンを作成し、押されたときのイベントをControllerへ渡す
  private Button createButton(String text, String id) {
    Button button = new Button(text);
    button.setId(id);
    button.setPrefSize(110, 55);
    button.setStyle("-fx-font-size: 20px;");
    button.setOnAction(event -> actionListener.actionPerformed(event));

    return button;
  }

  // 入力画像表示を更新する
  public void updateOriginalImage(Image image) {
    originalImageView.setImage(image);
    emptyLabel.setVisible(image == null);
  }
}
