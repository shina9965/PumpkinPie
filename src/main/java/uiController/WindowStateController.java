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
    this.aWindowState = new HomeConstroller(this, settingController, aStage);

    aWindowState.initState();
  }

  @Override
  public void changeWindowState(IWindowState nextWindowState) {

    BoolEx.ifTrueElse(this.aWindowState != null,
       () -> this.aWindowState.endState()
      );

    this.aWindowState = nextWindowState;
    aWindowState.initState();
  }
}
