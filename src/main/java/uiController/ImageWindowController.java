package uiController;

import javafx.stage.Stage;
import listener.ActionListener;

public class ImageWindowController extends WindowController{


  public ImageWindowController(Stage stage, ActionListener actionListener, SettingController settingController) {
    super(stage, actionListener, settingController);
  }


    public void initState() {
    System.out.println("ImageWindowController: initState");
  }

  public void endState() {
    System.out.println("ImageWindowController: endState");
  }

  public void actionPerformed(javafx.event.ActionEvent event) {
    System.out.println("ImageWindowController: actionPerformed");
  }

  public void onReturn() {
    System.out.println("ImageWindowController: onReturn");
  }

  public void onSetting() {
    System.out.println("ImageWindowController: onSetting");
  }

  
}
