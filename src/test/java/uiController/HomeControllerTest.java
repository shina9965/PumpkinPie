package uiController;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import listener.IWindowState;
import listener.StateChangeListener;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import uiModel.HomeModel;

public class HomeControllerTest extends ApplicationTest {

  private HomeController homeController;
  private HomeModel homeModel;

  private StateChangeListener stateChangeListener;
  private SettingController settingController;

  @Override
  public void start(Stage stage) {
    stateChangeListener = mock(StateChangeListener.class);
    settingController = mock(SettingController.class);

    homeController = new HomeController(
        stateChangeListener,
        settingController,
        stage
    );

    homeModel = new HomeModel();
  }

  @Test
  public void initState_例外が発生しないこと() {
    assertDoesNotThrow(() -> {
      interact(() -> homeController.initState());
    });
  }

  @Test
  public void endState_例外が発生しないこと() {
    assertDoesNotThrow(() -> homeController.endState());
  }

  @Test
  public void onReturn_例外が発生しないこと() {
    assertDoesNotThrow(() -> homeController.onReturn());
  }

  @Test
  public void onSetting_設定画面が開いていない場合_openSettingが呼ばれること() {
    when(settingController.getIsOpened()).thenReturn(false);

    homeController.onSetting();

    verify(settingController, times(1)).openSetting();
  }

  @Test
  public void onSetting_設定画面が既に開いている場合_openSettingが呼ばれないこと() {
    when(settingController.getIsOpened()).thenReturn(true);

    homeController.onSetting();

    verify(settingController, never()).openSetting();
  }

  @Test
  public void actionPerformed_Signalボタンの場合_SignalWindowControllerへ遷移すること() {
    Button button = new Button();
    button.setId(homeModel.getSignalButtonData().id());

    ActionEvent event = new ActionEvent(button, null);

    homeController.actionPerformed(event);

    verify(stateChangeListener, times(1))
        .changeWindowState(any(SignalWindowController.class));
  }

  @Test
  public void actionPerformed_Imageボタンの場合_ImageWindowControllerへ遷移すること() {
    Button button = new Button();
    button.setId(homeModel.getImageButtonData().id());

    ActionEvent event = new ActionEvent(button, null);

    homeController.actionPerformed(event);

    verify(stateChangeListener, times(1))
        .changeWindowState(any(ImageWindowController.class));
  }

  @Test
  public void actionPerformed_Settingボタンの場合_onSettingが呼ばれてopenSettingされること() {
    when(settingController.getIsOpened()).thenReturn(false);

    Button button = new Button();
    button.setId(homeModel.getSettingButtonData().id());

    ActionEvent event = new ActionEvent(button, null);

    homeController.actionPerformed(event);

    verify(settingController, times(1)).openSetting();
  }

  @Test
  public void actionPerformed_Button以外の場合_画面遷移しないこと() {
    Object source = new Object();
    ActionEvent event = new ActionEvent(source, null);

    homeController.actionPerformed(event);

    verify(stateChangeListener, never())
        .changeWindowState(any(IWindowState.class));
  }
}
