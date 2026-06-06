package uiController;

import listener.IWindowState;
import listener.ActionListener;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import uiController.SettingController;

public abstract class WindowController extends UIController implements IWindowState, ActionListener {

  protected Stage stage;
  protected ActionListener actionListener;
  protected SettingController settingController;

  public WindowController(Stage stage, ActionListener actionListener, SettingController settingController) {
    this.stage = stage;
    this.actionListener = actionListener;
    this.settingController = settingController;
  }

  abstract public void initState();
  abstract public void endState();

  abstract public void actionPerformed(ActionEvent event);
  abstract public void onReturn();
  abstract public void onSetting();

}
