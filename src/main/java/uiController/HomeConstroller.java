package uiController;

import javafx.stage.Stage;
import listener.ActionListener;
import javafx.event.ActionEvent;

import uiView.HomeView;
import uiModel.HomeModel;


public class HomeConstroller extends WindowController{

  private HomeView homeView;
  private HomeModel homeModel;

  HomeConstroller(ActionListener actionListener, SettingController settingController, Stage stage) {
    super(actionListener, settingController);

    this.homeView = new HomeView(this, stage);
    this.homeModel = new HomeModel();
  }


  public void initState() {

    System.out.println("HomeConstroller: initState");

    homeView.createScene(homeModel);
  }

  public void endState() {
    System.out.println("HomeConstroller: endState");
  }

  public void actionPerformed(ActionEvent event) {
    System.out.println("HomeConstroller: actionPerformed");

    
  }

  public void onReturn() {
    System.out.println("HomeConstroller: onReturn");
  }

  public void onSetting() {
    System.out.println("HomeConstroller: onSetting");
  }
}
