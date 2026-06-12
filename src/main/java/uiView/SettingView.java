package uiView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import listener.ActionListener;

/**
 * 設定画面の見た目を作るクラス。
 * UI部品の作成と配置を担当する。
 */
public class SettingView {

    private Slider rateSlider;
    private TextField rateTextField;

    private Button applyButton;
    private Button resetButton;
    private Button exitButton;
    private Button backButton;
    private Button creditButton;

    private ActionListener actionListener;
    private BorderPane root;

    /**
     * コンストラクタ。
     *
     * @param actionListener ボタンイベントを受け取るリスナー
     */
    public SettingView(ActionListener actionListener) {
        this.actionListener = actionListener;

        initializeComponents();
        layoutComponents();
        setButtonActions();
    }

    /**
     * UI部品を初期化する。
     */
    private void initializeComponents() {
        rateSlider = new Slider(0, 100, 10);
        rateSlider.setShowTickLabels(true);
        rateSlider.setShowTickMarks(true);
        rateSlider.setMajorTickUnit(10);
        rateSlider.setMinorTickCount(0);
        rateSlider.setBlockIncrement(1);
        rateSlider.setSnapToTicks(true);
        rateSlider.setPrefWidth(450);

        rateTextField = new TextField("10");
        rateTextField.setPrefWidth(80);

        applyButton = new Button("適用");
        resetButton = new Button("リセット");
        exitButton = new Button("アプリを終了");
        backButton = new Button("戻る");
        creditButton = new Button("クレジット");

        applyButton.setPrefWidth(100);
        resetButton.setPrefWidth(100);
        backButton.setPrefWidth(120);
        creditButton.setPrefWidth(120);

        exitButton.setPrefWidth(220);
        exitButton.setPrefHeight(60);
    }

    /**
     * UI部品を配置する。
     */
    private void layoutComponents() {
        root = new BorderPane();
        root.setPadding(new Insets(30));

        Label titleLabel = new Label("設定画面");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");

        HBox titleArea = new HBox(titleLabel);
        titleArea.setAlignment(Pos.CENTER_LEFT);
        titleArea.setPadding(new Insets(0, 0, 30, 0));

        Label adoptionLabel = new Label("採用率");
        adoptionLabel.setStyle("-fx-font-size: 18px;");

        Label percentLabel = new Label("%");

        HBox rateInputArea = new HBox(10, rateTextField, percentLabel);
        rateInputArea.setAlignment(Pos.CENTER);

        HBox rateArea = new HBox(20, adoptionLabel, rateSlider, rateInputArea);
        rateArea.setAlignment(Pos.CENTER);

        HBox applyResetArea = new HBox(10, applyButton, resetButton);
        applyResetArea.setAlignment(Pos.CENTER);

        HBox creditArea = new HBox(creditButton);
        creditArea.setAlignment(Pos.CENTER_RIGHT);

        HBox backArea = new HBox(backButton);
        backArea.setAlignment(Pos.CENTER_RIGHT);

        VBox rightArea = new VBox(15, creditArea, applyResetArea, backArea);
        rightArea.setAlignment(Pos.CENTER_RIGHT);

        HBox centerArea = new HBox(30, rateArea, rightArea);
        centerArea.setAlignment(Pos.CENTER);

        HBox exitArea = new HBox(exitButton);
        exitArea.setAlignment(Pos.CENTER);
        exitArea.setPadding(new Insets(40, 0, 0, 0));

        VBox mainArea = new VBox(30, centerArea, exitArea);
        mainArea.setAlignment(Pos.CENTER);

        root.setTop(titleArea);
        root.setCenter(mainArea);
    }

    /**
     * ボタンにイベントを設定する。
     */
    private void setButtonActions() {
        applyButton.setOnAction(event -> actionListener.actionPerformed(event));
        resetButton.setOnAction(event -> actionListener.actionPerformed(event));
        exitButton.setOnAction(event -> actionListener.actionPerformed(event));
        backButton.setOnAction(event -> actionListener.actionPerformed(event));
        creditButton.setOnAction(event -> actionListener.actionPerformed(event));
    }

    /**
     * スライダーとテキストボックスを更新する。
     *
     * @param rate 採用率
     */
    public void updateView(int rate) {
        rateSlider.setValue(rate);
        rateTextField.setText(String.valueOf(rate));
    }

    public Parent getRoot() {
        return root;
    }

    public Slider getRateSlider() {
        return rateSlider;
    }

    public TextField getRateTextField() {
        return rateTextField;
    }

    public Button getApplyButton() {
        return applyButton;
    }

    public Button getResetButton() {
        return resetButton;
    }

    public Button getExitButton() {
        return exitButton;
    }

    public Button getBackButton() {
        return backButton;
    }

    public Button getCreditButton() {
        return creditButton;
    }
}