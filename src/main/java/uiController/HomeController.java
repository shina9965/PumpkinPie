package uiController;

import javafx.stage.Stage;
import listener.StateChangeListener;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

import app.BoolEx;

import uiView.HomeView;
import uiModel.HomeModel;


public class HomeController extends WindowController{

  private HomeView homeView;
  private HomeModel homeModel;

  private SettingController settingController;

  /** HomeCOntroller
   * @param stateChangeListener The listener to handle state changes.
   * @param settingController The controller for settings.
   * @param stage The JavaFX stage to display the view.
   */
  HomeController(StateChangeListener stateChangeListener, SettingController settingController, Stage stage) {
    super(stateChangeListener, settingController);

    this.homeView = new HomeView(this, stage);
    this.homeModel = new HomeModel();

    this.settingController = settingController;
  }


  public void initState() {

    System.out.println("HomeConstroller: initState");

    homeView.createScene(homeModel);
  }

  public void endState() {
    System.out.println("HomeConstroller: endState");
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    System.out.println("HomeConstroller: actionPerformed");

    BoolEx.ifTrueElse(event.getSource() instanceof Button,
      () -> {
        Button button = (Button) event.getSource();
        String buttonId = button.getId();

        BoolEx.ifTrueElse(this.homeModel.getSignalButtonData().id().equals(buttonId),
          () -> {stateChangeListener.changeWindowState(new SignalWindowController(stateChangeListener, settingController));
            System.out.println("HomeConstroller: Signal Button Clicked");
          }
        );

        BoolEx.ifTrueElse(this.homeModel.getImageButtonData().id().equals(buttonId),
          () -> {stateChangeListener.changeWindowState(new ImageWindowController(stateChangeListener, settingController));
            System.out.println("HomeConstroller: Image Button Clicked");
          }
        );
      },
      () -> System.out.println("Event source is not a Button")
    );
  }

  public void onReturn() {
    System.out.println("HomeConstroller: onReturn");
  }

  public void onSetting() {
    System.out.println("HomeConstroller: onSetting");
    
  }
}
