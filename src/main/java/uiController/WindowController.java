package uiController;

import listener.IWindowState;
import listener.StateChangeListener;
import listener.ActionListener;
import javafx.event.ActionEvent;


public abstract class WindowController extends UIController implements IWindowState, ActionListener {

  protected StateChangeListener stateChangeListener;
  protected SettingController settingController;

  public WindowController(StateChangeListener stateChangeListener, SettingController settingController) {
    this.stateChangeListener = stateChangeListener;
    this.settingController = settingController;
  }

  abstract public void initState();
  abstract public void endState();

  abstract public void actionPerformed(ActionEvent event);
  abstract public void onReturn();
  abstract public void onSetting();

}
