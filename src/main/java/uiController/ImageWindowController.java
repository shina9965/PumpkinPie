package uiController;

import listener.StateChangeListener;

public class ImageWindowController extends WindowController{


  public ImageWindowController(StateChangeListener stateChangeListener, SettingController settingController) {
    super(stateChangeListener, settingController);
  }


    public void initState() {
    System.out.println("ImageWindowController: initState");
  }

  public void endState() {
    System.out.println("ImageWindowController: endState");
  }

  @Override
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
