package uiView;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import listener.ActionListener;
import uiModel.SettingModel;

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

    private Stage settingStage;

    public SettingView(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    /**
     * 初期化処理をまとめる
     */
    public void initialize(SettingModel model) {
        initializeComponents(model);
        layoutComponents();
        setButtonActions();
        updateView(model.getAdoptionRate());

        settingStage = new Stage();
        settingStage.setTitle("設定");
        settingStage.initModality(Modality.APPLICATION_MODAL);
        settingStage.setScene(new Scene(root, 900, 500));
    }

    public void initializeComponents(SettingModel model) {
        rateSlider = new Slider(0, 100, model.getAdoptionRate());
        rateSlider.setShowTickLabels(true);
        rateSlider.setShowTickMarks(true);
        rateSlider.setMajorTickUnit(10);
        rateSlider.setMinorTickCount(0);
        rateSlider.setBlockIncrement(1);
        rateSlider.setSnapToTicks(true);
        rateSlider.setPrefWidth(450);

        rateTextField = new TextField(String.valueOf(model.getAdoptionRate()));
        rateTextField.setPrefWidth(80);

        applyButton = new Button(model.getAdoptButton().text());
        applyButton.setId(model.getAdoptButton().id());

        resetButton = new Button(model.getResetButton().text());
        resetButton.setId(model.getResetButton().id());

        exitButton = new Button(model.getFinishButton().text());
        exitButton.setId(model.getFinishButton().id());

        backButton = new Button(model.getReturnButton().text());
        backButton.setId(model.getReturnButton().id());

        creditButton = new Button(model.getCreditButton().text());
        creditButton.setId(model.getCreditButton().id());

        applyButton.setPrefWidth(100);
        resetButton.setPrefWidth(100);
        backButton.setPrefWidth(120);
        creditButton.setPrefWidth(120);

        exitButton.setPrefWidth(220);
        exitButton.setPrefHeight(60);
    }

    public void layoutComponents() {
        root = new BorderPane();
        root.setPadding(new Insets(30));

        Label titleLabel = new Label("設定画面");
        titleLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");

        HBox titleArea = new HBox(titleLabel);
        titleArea.setAlignment(Pos.CENTER_LEFT);
        titleArea.setPadding(new Insets(0, 0, 30, 0));

        Label adoptionLabel = new Label("採用率");
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

    public void setButtonActions() {
        applyButton.setOnAction(event -> actionListener.actionPerformed(event));
        resetButton.setOnAction(event -> actionListener.actionPerformed(event));
        exitButton.setOnAction(event -> actionListener.actionPerformed(event));
        backButton.setOnAction(event -> actionListener.actionPerformed(event));
        creditButton.setOnAction(event -> actionListener.actionPerformed(event));

        rateSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            rateTextField.setText(String.valueOf(newValue.intValue()));
        });
    }

    public void updateView(int rate) {
        rateSlider.setValue(rate);
        rateTextField.setText(String.valueOf(rate));
    }

    public int getRateValue() {
        return Integer.parseInt(rateTextField.getText());
    }

    public void open() {
        settingStage.show();
    }

    public void close() {
        settingStage.close();
    }

    public void showCredit() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("クレジット");
        alert.setHeaderText("クレジット");
        alert.setContentText("PumpkinPie");
        alert.showAndWait();
    }

    public void showMessage(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}
}