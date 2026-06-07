package uiController;

import javafx.event.ActionEvent;
import listener.StateChangeListener;

public class SignalWindowController extends WindowController{

    public SignalWindowController(StateChangeListener stateChangeListener, SettingController settingController) {
      super(stateChangeListener, settingController);
    }


    public void initState() {
    System.out.println("SignalWindowController: initState");
  }

  public void endState() {
    System.out.println("SignalWindowController: endState");
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    System.out.println("SignalWindowController: actionPerformed");
  }

  public void onReturn() {
    System.out.println("SignalWindowController: onReturn");
  }

  public void onSetting() {
    System.out.println("SignalWindowController: onSetting");
  }
}
