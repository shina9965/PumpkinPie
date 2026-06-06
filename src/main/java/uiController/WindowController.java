package uiController;

import listener.IWindowState;
import listener.ActionListener;
import javafx.event.ActionEvent;

public abstract class WindowController extends UIController implements IWindowState, ActionListener {

  protected ActionListener actionListener;
  protected SettingController settingController;

  public WindowController(ActionListener actionListener, SettingController settingController) {
    this.actionListener = actionListener;
    this.settingController = settingController;
  }

  abstract public void initState();
  abstract public void endState();

  abstract public void actionPerformed(ActionEvent event);
  abstract public void onReturn();
  abstract public void onSetting();

}
