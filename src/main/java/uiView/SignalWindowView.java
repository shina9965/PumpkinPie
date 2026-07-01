package uiView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import listener.ActionListener;
import uiModel.SignalWindowModel;

public class SignalWindowView {

  // 画面全体の横幅
  private static final double WINDOW_WIDTH = 1200;

  // 画面全体の縦幅
  private static final double WINDOW_HEIGHT = 650;

  // 元信号表示・逆変換後信号表示に使う大きいパネルの横幅
  private static final double LARGE_PANEL_WIDTH = 520;

  // スケーリング係数・ウェーブレット係数表示に使う小さいパネルの横幅
  private static final double SMALL_PANEL_WIDTH = 260;

  // 各表示パネルの縦幅
  private static final double PANEL_HEIGHT = 220;

  // ボタン操作などのイベントをControllerへ渡すためのリスナー
  private ActionListener actionListener;

  // 元信号を表示するパネル
  private Pane originalSignalPanel;

  // 逆変換後信号を表示するパネル
  private Pane inverseSignalPanel;

  // 変換後のスケーリング係数を表示するパネル
  private Pane scalingCoefficientPanel;

  // 逆変換側にも表示するスケーリング係数のパネル
  private Pane inverseScalingCoefficientPanel;

  // ウェーブレット展開係数を表示するパネル
  private Pane waveletCoefficientPanel;

  // 編集用のウェーブレット展開係数を表示するパネル
  private Pane waveletCoefficientEditPanel;

  // Viewを初期化し、Controllerへイベントを渡せるようにする
  public SignalWindowView(ActionListener actionListener) {
    this.actionListener = actionListener;
  }

  // 信号変換表示画面全体のSceneを作成する
  public Scene createScene(SignalWindowModel signalWindowModel) {
    System.out.println("SignalWindowView: createScene");

    createSignalPanels();

    GridPane signalGrid = new GridPane();
    signalGrid.setPadding(new Insets(40));

    signalGrid.add(originalSignalPanel, 0, 0);
    signalGrid.add(scalingCoefficientPanel, 1, 0);
    signalGrid.add(waveletCoefficientPanel, 2, 0);

    signalGrid.add(inverseSignalPanel, 0, 1);
    signalGrid.add(inverseScalingCoefficientPanel, 1, 1);
    signalGrid.add(waveletCoefficientEditPanel, 2, 1);

    HBox buttonBox = createButtons(signalWindowModel);

    BorderPane root = new BorderPane();
    root.setCenter(signalGrid);
    root.setBottom(buttonBox);

    return new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
  }

  // 信号や係数を表示する各パネルを作成する
  public void createSignalPanels() {
    System.out.println("SignalWindowView: createSignalPanels");

    originalSignalPanel = createPanel("元信号表示", LARGE_PANEL_WIDTH, "1 0 1 1");
    scalingCoefficientPanel = createPanel("スケーリング係数表示", SMALL_PANEL_WIDTH, "1 0 1 1");
    waveletCoefficientPanel = createPanel("ウェーブレット展開係数表示", SMALL_PANEL_WIDTH, "1 1 1 1");

    inverseSignalPanel = createPanel("逆変換後信号表示", LARGE_PANEL_WIDTH, "0 0 1 1");
    inverseScalingCoefficientPanel = createPanel("スケーリング係数表示", SMALL_PANEL_WIDTH, "0 0 1 1");
    waveletCoefficientEditPanel = createPanel("ウェーブレット展開係数編集", SMALL_PANEL_WIDTH, "0 1 1 1");
  }

  // SignalWindowModelが持つボタン情報を使って、信号保存・信号入力・戻るボタンを作成する
  public HBox createButtons(SignalWindowModel signalWindowModel) {
    System.out.println("SignalWindowView: createButtons");

    Button saveButton = createButton(
        signalWindowModel.getSaveSignalButtonData().text(),
        signalWindowModel.getSaveSignalButtonData().id()
    );

    Button inputButton = createButton(
        signalWindowModel.getInputSignalButtonData().text(),
        signalWindowModel.getInputSignalButtonData().id()
    );

    Button returnButton = createButton(
        signalWindowModel.getReturnButtonData().text(),
        signalWindowModel.getReturnButtonData().id()
    );

    HBox leftButtons = new HBox(30);
    leftButtons.getChildren().addAll(saveButton, inputButton);

    HBox rightButtons = new HBox();
    rightButtons.getChildren().add(returnButton);

    HBox buttonBox = new HBox(650);
    buttonBox.setPadding(new Insets(25, 40, 35, 120));
    buttonBox.setAlignment(Pos.CENTER);
    buttonBox.getChildren().addAll(leftButtons, rightButtons);

    return buttonBox;
  }

  // ボタンを作成し、押されたときのイベントをControllerへ渡す
  public Button createButton(String text, String id) {
    Button button = new Button(text);
    button.setId(id);
    button.setPrefSize(110, 55);
    button.setStyle("-fx-font-size: 20px;");
    button.setOnAction(event -> actionListener.actionPerformed(event));

    return button;
  }

  // 元信号表示パネルを更新する
  public void updateOriginalSignal(double[] signal) {
    drawSignal(originalSignalPanel, signal, "元信号表示", false);
  }

  // 逆変換後信号表示パネルを更新する
  public void updateInverseSignal(double[] signal) {
    drawSignal(inverseSignalPanel, signal, "逆変換後信号表示", false);
  }

  // スケーリング係数表示パネルを更新する
  public void updateScalingCoefficient(double[] coefficient) {
    drawSignal(scalingCoefficientPanel, coefficient, "スケーリング係数表示", false);
    drawSignal(inverseScalingCoefficientPanel, coefficient, "スケーリング係数表示", false);
  }

  // ウェーブレット展開係数表示パネルを更新する
  public void updateWaveletCoefficient(double[] coefficient) {
    drawSignal(waveletCoefficientPanel, coefficient, "ウェーブレット展開係数表示", false);
  }

  // 編集用ウェーブレット展開係数表示パネルを更新する
  public void updateWaveletEditDisplay(double[] coefficient, int selectedIndex) {
    drawClickableSignal(waveletCoefficientEditPanel, coefficient, "ウェーブレット展開係数編集", true);
  }

  // 画面全体を更新する
  public void update() {
    System.out.println("SignalWindowView: update");
  }

  // タイトル付きの表示パネルを作成する
  private Pane createPanel(String title, double width, String borderWidth) {
    Pane pane = new Pane();
    pane.setPrefSize(width, PANEL_HEIGHT);
    pane.setStyle(
        "-fx-border-color: black;" +
        "-fx-border-width: " + borderWidth + ";" +
        "-fx-background-color: white;"
    );

    Label label = new Label(title);
    label.setLayoutX(10);
    label.setLayoutY(10);
    label.setStyle("-fx-font-size: 16px;");
    pane.getChildren().add(label);

    return pane;
  }

  // 渡された数値配列を点列としてパネルに描画する
  // TODO: SignalGraph完成後、この描画処理はSignalGraphへ移す
  private void drawSignal(Pane pane, double[] values, String title, boolean editable) {
    pane.getChildren().clear();

    Label label = new Label(title);
    label.setLayoutX(10);
    label.setLayoutY(10);
    label.setStyle("-fx-font-size: 16px;");
    pane.getChildren().add(label);

    double width = pane.getPrefWidth();
    double height = pane.getPrefHeight();
    double centerY = height / 2;

    Line baseLine = new Line(10, centerY, width - 10, centerY);
    pane.getChildren().add(baseLine);

    if (values.length == 0) {
      return;
    }

    double maxAbs = getMaxAbs(values);
    double xStep = (width - 40) / Math.max(values.length - 1, 1);

    for (int index = 0; index < values.length; index++) {
      double x = 20 + index * xStep;
      double y = centerY - values[index] / maxAbs * (height / 3);

      Circle point = new Circle(x, y, 3);
      pane.getChildren().add(point);
    }
  }
  
  private void drawClickableSignal(Pane pane, double[] values, String title, boolean editable) {
  pane.getChildren().clear();

  Label label = new Label(title);
  label.setLayoutX(10);
  label.setLayoutY(10);
  label.setStyle("-fx-font-size: 16px;");
  pane.getChildren().add(label);

  double width = pane.getPrefWidth();
  double height = pane.getPrefHeight();
  double centerY = height / 2;

  Line baseLine = new Line(10, centerY, width - 10, centerY);
  pane.getChildren().add(baseLine);

  if (values.length == 0) {
    return;
  }

  double maxAbs = getMaxAbs(values);
  double xStep = (width - 40) / Math.max(values.length - 1, 1);

  for (int index = 0; index < values.length; index++) {
    double x = 20 + index * xStep;
    double y = centerY - values[index] / maxAbs * (height / 3);

    Circle point = new Circle(x, y, 3);

    int selectedIndex = index;
    double selectedValue = values[index];

    if (editable) {
      point.setOnMouseClicked(event -> {
        System.out.println("クリックされた点 index = " + selectedIndex);
        System.out.println("値 = " + selectedValue);
      });
    }

    pane.getChildren().add(point);
  }
}

  // 配列の中で絶対値が最大の値を求める
  private double getMaxAbs(double[] values) {
    double max = 1.0;

    for (double value : values) {
      max = Math.max(max, Math.abs(value));
    }

    return max;
  }
}