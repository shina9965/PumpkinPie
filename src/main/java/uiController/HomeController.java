package uiController;

import javafx.stage.Stage;
import listener.StateChangeListener;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

import app.BoolEx;

import uiView.HomeView;
import uiModel.HomeModel;


public class HomeController extends WindowController{

  private HomeView homeView;
  private HomeModel homeModel;

  private SettingController settingController;

  /** HomeControllerのコンストラクタ
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
   * HomeControllerの状態を初期化するメソッド。WindowStateControllerから呼び出されることを想定している。HomeViewを作成するためにHomeModelを渡す。
   */
  public void initState() {

    System.out.println("HomeController: initState");

    homeView.createScene(homeModel);
  }

  /**
   * HomeControllerの状態を終了するメソッド。WindowStateControllerから呼び出されることを想定している。
   */
  public void endState() {
    System.out.println("HomeController: endState");
  }

  /**
   * ボタンがクリックされた際の処理を行うメソッド。WindowStateControllerから呼び出されることを想定している。
   * @param event イベントオブジェクト
   */
  @Override
  public void actionPerformed(ActionEvent event) {
    System.out.println("HomeController: actionPerformed");

    BoolEx.ifTrueElse(event.getSource() instanceof Button,
      () -> {
        Button button = (Button) event.getSource();
        String buttonId = button.getId();

        BoolEx.ifTrueElse(this.homeModel.getSignalButtonData().id().equals(buttonId),
          () -> {stateChangeListener.changeWindowState(new SignalWindowController(stateChangeListener, settingController));
            System.out.println("HomeController: Signal Button Clicked");
          }
        );

        BoolEx.ifTrueElse(this.homeModel.getImageButtonData().id().equals(buttonId),
          () -> {stateChangeListener.changeWindowState(new ImageWindowController(stateChangeListener, settingController));
            System.out.println("HomeController: Image Button Clicked");
          }
        );
      },
      () -> System.out.println("Event source is not a Button")
    );
  }

  /**
   * 戻るボタンがクリックされた際の処理を行うメソッド。たぶん使わない
   * */
  public void onReturn() {
    System.out.println("HomeController: onReturn");
  }

  /**
   * 設定ボタンがクリックされた際の処理を行うメソッド。
   */
  public void onSetting() {
    System.out.println("HomeController: onSetting");
    
  }
}
