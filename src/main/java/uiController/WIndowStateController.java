package uiController;

import javafx.stage.Stage;
import listener.IWindowState;
import listener.ActionListener;
import javafx.event.ActionEvent;


public class WIndowStateController implements ActionListener {

  private Stage aStage;
  private IWindowState aWindowState;
  private IWindowState nextWindowState;
  private SettingController settingController;

  public WIndowStateController(Stage aStage) {
    this.aStage = aStage;

    this.settingController = new SettingController();
    this.aWindowState = new HomeConstroller(aStage, this, settingController);

    aWindowState.initState();
  }

  public void changeWindowState(IWindowState nextWindowState) {
    if (aWindowState != null) {
      aWindowState.endState();
    }
    this.nextWindowState = nextWindowState;
    this.aWindowState = nextWindowState;
    aWindowState.initState();
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    
  }
}
