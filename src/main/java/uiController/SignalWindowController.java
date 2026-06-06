package uiController;

import javafx.event.ActionEvent;
import javafx.stage.Stage;
import listener.ActionListener;

public class SignalWindowController extends WindowController{

    public SignalWindowController(Stage stage, ActionListener actionListener, SettingController settingController) {
        super(stage, actionListener, settingController);
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
