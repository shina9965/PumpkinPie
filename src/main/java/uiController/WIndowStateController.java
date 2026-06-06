package uiController;

import javafx.stage.Stage;
import listener.ActionListener;
import listener.IWindowState;
import javafx.event.ActionEvent;
import app.BoolEx;



public class WindowStateController implements ActionListener {

  private IWindowState aWindowState;
  private SettingController settingController;

  public WindowStateController(Stage aStage) {

    this.settingController = new SettingController();
    this.aWindowState = new HomeConstroller(this, settingController, aStage);

    aWindowState.initState();
  }

  public void changeWindowState(IWindowState nextWindowState) {

    BoolEx.ifTrueElse(this.aWindowState != null,
       () -> this.aWindowState.endState()
      );

    this.aWindowState = nextWindowState;
    aWindowState.initState();
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    
    
  }
}
