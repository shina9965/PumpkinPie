package uiController;

import java.util.Optional;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import listener.ActionListener;
import uiModel.SettingModel;
import uiView.SettingView;

/**
 * SettingModel と SettingView を仲介するコントローラクラス。
 * スライダー、テキストボックス、ボタンのイベント処理を担当する。
 */
public class SettingController implements ActionListener {

    /** 採用率のデータを管理するモデル */
    private SettingModel model;

    /** 設定画面の表示を担当するビュー */
    private SettingView view;

    /** View更新中にイベントが連続発生するのを防ぐためのフラグ */
    private boolean updatingView;

    /**
     * コンストラクタ。
     * ModelとViewを作成し、入力イベントを設定する。
     */
    public SettingController() {
        this.model = new SettingModel();
        this.view = new SettingView(this);
        this.updatingView = false;

        view.updateView(model.getAdoptionRate());
        setInputListeners();
    }

    /**
     * MainなどからSettingViewを取得するためのメソッド。
     *
     * @return 設定画面のView
     */
    public SettingView getView() {
        return view;
    }

    /**
     * スライダーとテキストボックスにイベントを設定する。
     */
    private void setInputListeners() {
        view.getRateSlider().valueProperty().addListener((observable, oldValue, newValue) -> {
            if (updatingView) {
                return;
            }

            int intValue = newValue.intValue();
            onSliderChanged(intValue);
        });

        TextField textField = view.getRateTextField();

        textField.setOnAction(event -> onTextChanged(textField.getText()));

        textField.focusedProperty().addListener((observable, oldFocused, newFocused) -> {
            if (updatingView) {
                return;
            }

            if (!newFocused) {
                onTextChanged(textField.getText());
            }
        });
    }

    /**
     * ボタンが押されたときに呼ばれる処理。
     * 既存の listener.ActionListener に合わせて実装している。
     *
     * @param event ボタンイベント
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();

        if (source == view.getApplyButton()) {
            onApplyButtonClicked();
            return;
        }

        if (source == view.getResetButton()) {
            onResetButtonClicked();
            return;
        }

        if (source == view.getExitButton()) {
            onExitButtonClicked();
            return;
        }

        if (source == view.getBackButton()) {
            onBackButtonClicked();
            return;
        }

        if (source == view.getCreditButton()) {
            onCreditButtonClicked();
        }
    }

    /**
     * スライダーを動かしたときの処理。
     *
     * @param newValue 新しい採用率
     */
    public void onSliderChanged(int newValue) {
        try {
            model.setAdoptionRate(newValue);
            updateViewSafely(model.getAdoptionRate());
        } catch (IllegalArgumentException exception) {
            showErrorDialog(exception.getMessage());
            updateViewSafely(model.getAdoptionRate());
        }
    }

    /**
     * テキストボックスに入力したときの処理。
     *
     * @param newText 入力された文字列
     */
    public void onTextChanged(String newText) {
        try {
            int value = parseAdoptionRate(newText);
            model.setAdoptionRate(value);
            updateViewSafely(model.getAdoptionRate());
        } catch (NumberFormatException exception) {
            showErrorDialog("採用率は整数で入力してください。");
            updateViewSafely(model.getAdoptionRate());
        } catch (IllegalArgumentException exception) {
            showErrorDialog(exception.getMessage());
            updateViewSafely(model.getAdoptionRate());
        }
    }

    /**
     * 適用ボタンが押されたときの処理。
     */
    public void onApplyButtonClicked() {
        int currentRate = model.getAdoptionRate();

        showInformationDialog(
                "設定を適用しました",
                "採用率を " + currentRate + "% に設定しました。"
        );
    }

    /**
     * リセットボタンが押されたときの処理。
     */
    public void onResetButtonClicked() {
        model.resetToDefault();
        updateViewSafely(model.getAdoptionRate());

        showInformationDialog(
                "リセットしました",
                "採用率をデフォルト値の " + model.getDefaultAdoptionRate() + "% に戻しました。"
        );
    }

    /**
     * アプリ終了ボタンが押されたときの処理。
     */
    public void onExitButtonClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("終了確認");
        alert.setHeaderText("アプリを終了しますか？");
        alert.setContentText("OKを押すとアプリを終了します。");

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Platform.exit();
        }
    }

    /**
     * 戻るボタンが押されたときの処理。
     * Home画面との接続は未実装なので、今は仮処理にしている。
     */
    public void onBackButtonClicked() {
        showInformationDialog(
                "戻る",
                "ホーム画面へ戻る処理は未実装です。"
        );
    }

    /**
     * クレジットボタンが押されたときの処理。
     */
    public void onCreditButtonClicked() {
        showInformationDialog(
                "クレジット",
                "作成者情報\n\nJavaFX 設定画面\nVersion 1.0"
        );
    }

    /**
     * 入力文字列を採用率の整数に変換する。
     *
     * @param text 入力文字列
     * @return 採用率
     */
    private int parseAdoptionRate(String text) {
        if (text == null || text.trim().isEmpty()) {
            throw new NumberFormatException("空文字です。");
        }

        return Integer.parseInt(text.trim());
    }

    /**
     * スライダーとテキストボックスを安全に更新する。
     *
     * @param rate 採用率
     */
    private void updateViewSafely(int rate) {
        updatingView = true;
        view.updateView(rate);
        updatingView = false;
    }

    /**
     * エラーダイアログを表示する。
     *
     * @param message 表示するメッセージ
     */
    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("入力エラー");
        alert.setHeaderText("入力内容が正しくありません。");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * 情報ダイアログを表示する。
     *
     * @param title タイトル
     * @param message 表示するメッセージ
     */
    private void showInformationDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}