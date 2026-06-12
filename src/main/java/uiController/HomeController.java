package uiController;

import app.BoolEx;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import listener.StateChangeListener;
import uiModel.HomeModel;
import uiView.HomeView;

public class HomeController extends WindowController {

    private ActionEvent latestActionEvent;
    private HomeView homeView;
    private HomeModel homeModel;
    private SettingController settingController;

    /**
     * HomeControllerのコンストラクタ
     *
     * @param stateChangeListener State変化のためのリスナー
     * @param settingController 設定画面のコントローラー
     * @param stage JavaFXのStage
     */
    HomeController(StateChangeListener stateChangeListener, SettingController settingController, Stage stage) {
        super(stateChangeListener, settingController);

        this.homeView = new HomeView(this, stage);
        this.homeModel = new HomeModel();
        this.settingController = settingController;
    }

    /**
     * HomeControllerの状態を初期化するメソッド。
     */
    @Override
    public void initState() {
        System.out.println("HomeController: initState");
        homeView.createScene(homeModel);
    }

    /**
     * HomeControllerの状態を終了するメソッド。
     */
    @Override
    public void endState() {
        System.out.println("HomeController: endState");
    }

    /**
     * ボタンがクリックされた際の処理を行うメソッド。
     *
     * @param event イベントオブジェクト
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        System.out.println("HomeController: actionPerformed");

        this.latestActionEvent = event;

        Object source = event.getSource();

        BoolEx.ifTrueElse(
                source instanceof Button,
                () -> {
                    Button button = (Button) source;
                    String buttonText = button.getText();

                    BoolEx.ifTrueElse(
                            "設定".equals(buttonText),
                            () -> onSetting()
                    );

                    BoolEx.ifTrueElse(
                            "信号処理".equals(buttonText),
                            () -> System.out.println("HomeController: signal button clicked")
                    );

                    BoolEx.ifTrueElse(
                            "画像処理".equals(buttonText),
                            () -> System.out.println("HomeController: image button clicked")
                    );
                }
        );
    }

    /**
     * 戻るボタンがクリックされた際の処理を行うメソッド。
     */
    @Override
    public void onReturn() {
        System.out.println("HomeController: onReturn");
    }

    /**
     * 設定ボタンがクリックされた際の処理を行うメソッド。
     * ホーム画面のSceneを保存し、設定画面の戻るボタンで戻れるようにする。
     */
    @Override
    public void onSetting() {
        System.out.println("HomeController: onSetting");

        BoolEx.ifTrueElse(
                latestActionEvent != null,
                () -> {
                    Stage stage = (Stage) ((Node) latestActionEvent.getSource()).getScene().getWindow();

                    Scene homeScene = stage.getScene();

                    this.settingController = new SettingController(
                            stage,
                            homeScene,
                            "Pumpkin Pie"
                    );

                    Scene settingScene = new Scene(
                            settingController.getView().getRoot(),
                            900,
                            500
                    );

                    stage.setTitle("設定画面");
                    stage.setScene(settingScene);
                    stage.show();
                }
        );
    }
}