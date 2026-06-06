package uiView;

import static org.junit.jupiter.api.Assertions.*;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import listener.ActionListener;
import uiModel.HomeModel;

import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

public class HomeViewTest extends ApplicationTest {

  private Stage stage;
  private HomeModel homeModel;

  @Override
  public void start(Stage stage) {
    this.stage = stage;
    this.homeModel = new HomeModel();
  }

  @Test
  void createScene_createsHomeScene() {
    ActionListener listener = event -> {
      System.out.println("clicked: " + event.getSource());
    };

    interact(() -> {
      HomeView homeView = new HomeView(listener, stage);
      homeView.createScene(homeModel);
    });

    assertNotNull(stage.getScene());
    assertEquals(homeModel.getDisplaySize().getX(), stage.getScene().getWidth());
    assertEquals(homeModel.getDisplaySize().getY(), stage.getScene().getHeight());

    VBox root = (VBox) stage.getScene().getRoot();
    assertEquals(4, root.getChildren().size());

    Label title = (Label) root.getChildren().get(0);
    Button signalButton = (Button) root.getChildren().get(1);
    Button imageButton = (Button) root.getChildren().get(2);
    Button settingButton = (Button) root.getChildren().get(3);

    assertEquals("Pumpkin Pie", title.getText());
    assertEquals("信号処理", signalButton.getText());
    assertEquals("画像処理", imageButton.getText());
    assertEquals("設定", settingButton.getText());
  }

  @Test
  void clickAllButtons_callsListener() {
    final int[] callCount = {0};

    ActionListener listener = event -> {
      callCount[0]++;
    };

    interact(() -> {
      HomeView homeView = new HomeView(listener, stage);
      homeView.createScene(homeModel);
    });

    VBox root = (VBox) stage.getScene().getRoot();

    Button signalButton = (Button) root.getChildren().get(1);
    Button imageButton = (Button) root.getChildren().get(2);
    Button settingButton = (Button) root.getChildren().get(3);

    clickOn(signalButton);
    clickOn(imageButton);
    clickOn(settingButton);

    assertEquals(3, callCount[0]);
  }
}