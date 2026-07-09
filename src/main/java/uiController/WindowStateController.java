package uiController;

import javafx.stage.Stage;
import listener.StateChangeListener;
import listener.IWindowState;
import app.BoolEx;




public class WindowStateController implements StateChangeListener{

  private IWindowState aWindowState;
  private SettingController settingController;

  public WindowStateController(Stage aStage) {

    this.settingController = new SettingController();
    this.aWindowState = new HomeController(this, settingController, aStage);

    aWindowState.initState();
  }

  /**
   * WindowStateControllerの状態を変更するメソッド。Obserberを用いてuiControllerから呼び出されることを想定している。
   * @param nextWindowState 次の状態
   */
  @Override
  public void changeWindowState(IWindowState nextWindowState) {

    BoolEx.ifTrueElse(this.aWindowState != null,
       () -> this.aWindowState.endState()
      );

    this.aWindowState = nextWindowState;
    aWindowState.initState();
  }
}
