package uiController;

import app.BoolEx;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import listener.StateChangeListener;
import uiModel.HomeModel;
import uiView.HomeView;

public class HomeController extends WindowController {

  private HomeView homeView;
  private HomeModel homeModel;

  private SettingController settingController;

  /**
   * HomeControllerのコンストラクタ
   *
   * @param stateChangeListener State変化のためのリスナー。中身はWindowStateControllerが想定される。
   * @param settingController 設定画面のコントローラー。HomeControllerから設定画面を開くために必要。
   * @param stage JavaFXのStage。HomeViewを作成するために必要。
   */
  HomeController(StateChangeListener stateChangeListener, SettingController settingController, Stage stage) {
    super(stateChangeListener, settingController);

    this.homeView = new HomeView(this, stage);
    this.homeModel = new HomeModel();

    this.settingController = settingController;
  }

  /**
   * HomeControllerの状態を初期化するメソッド。
   * WindowStateControllerから呼び出されることを想定している。
   */
  @Override
  public void initState() {
    System.out.println("HomeController: initState");

    homeView.createScene(homeModel);
  }

  /**
   * HomeControllerの状態を終了するメソッド。
   * WindowStateControllerから呼び出されることを想定している。
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

    BoolEx.ifTrueElse(
        event.getSource() instanceof Button,
        () -> {
          Button button = (Button) event.getSource();
          String buttonId = button.getId();

          BoolEx.ifTrueElse(
              this.homeModel.getSignalButtonData().id().equals(buttonId),
              () -> {
                stateChangeListener.changeWindowState(
                    new SignalWindowController(stateChangeListener, settingController)
                );
                System.out.println("HomeController: Signal Button Clicked");
              }
          );

          BoolEx.ifTrueElse(
              this.homeModel.getImageButtonData().id().equals(buttonId),
              () -> {
                stateChangeListener.changeWindowState(
                    new ImageWindowController(stateChangeListener, settingController)
                );
                System.out.println("HomeController: Image Button Clicked");
              }
          );

          BoolEx.ifTrueElse(
              this.homeModel.getSettingButtonData().id().equals(buttonId),
              () -> {
                onSetting();
                System.out.println("HomeController: Setting Button Clicked");
              }
          );
        },
        () -> System.out.println("Event source is not a Button")
    );
  }

  /**
   * 戻るボタンがクリックされた際の処理を行うメソッド。
   * Home画面では基本的に使用しない。
   */
  @Override
  public void onReturn() {
    System.out.println("HomeController: onReturn");
  }

  /**
   * 設定ボタンがクリックされた際の処理を行うメソッド。
   * 設定画面をポップアップとして表示する。
   */
  @Override
  public void onSetting() {
    System.out.println("HomeController: onSetting");

    Stage settingStage = new Stage();

    settingController.setPopupStage(settingStage);

    Scene settingScene = new Scene(
        settingController.getView().getRoot(),
        homeModel.getDisplaySize().getX(),
        homeModel.getDisplaySize().getY()
    );

    settingStage.setTitle(
        homeModel.getTitle() + " - " + homeModel.getSettingButtonData().text()
    );

    settingStage.setScene(settingScene);
    settingStage.initModality(Modality.APPLICATION_MODAL);
    settingStage.show();
  }
}