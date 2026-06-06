package uiController;

import javafx.stage.Stage;
import listener.ActionListener;

import uiView.HomeView;
import uiModel.HomeModel;


public class HomeConstroller extends WindowController{

  private HomeView homeView;
  private HomeModel homeModel;

  HomeConstroller(Stage stage, ActionListener actionListener, SettingController settingController) {
    super(stage, actionListener, settingController);

    this.homeView = new HomeView(this);
    this.homeModel = new HomeModel();
  }


  public void initState() {
    System.out.println("HomeConstroller: initState");
  }

  public void endState() {
    System.out.println("HomeConstroller: endState");
  }

  public void actionPerformed(javafx.event.ActionEvent event) {
    System.out.println("HomeConstroller: actionPerformed");
  }

  public void onReturn() {
    System.out.println("HomeConstroller: onReturn");
  }

  public void onSetting() {
    System.out.println("HomeConstroller: onSetting");
  }
}
