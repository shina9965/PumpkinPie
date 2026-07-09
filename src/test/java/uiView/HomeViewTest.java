package uiView;

import static org.junit.jupiter.api.Assertions.*;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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

    BorderPane root = (BorderPane) stage.getScene().getRoot();

    Label title = (Label) root.getCenter();
    HBox buttonBox = (HBox) root.getBottom();

    assertEquals("Pumpkin Pie", title.getText());
    assertEquals(3, buttonBox.getChildren().size());

    Button signalButton = (Button) buttonBox.getChildren().get(0);
    Button imageButton = (Button) buttonBox.getChildren().get(1);
    Button settingButton = (Button) buttonBox.getChildren().get(2);

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

    BorderPane root = (BorderPane) stage.getScene().getRoot();
    HBox buttonBox = (HBox) root.getBottom();

    Button signalButton = (Button) buttonBox.getChildren().get(0);
    Button imageButton = (Button) buttonBox.getChildren().get(1);
    Button settingButton = (Button) buttonBox.getChildren().get(2);

    clickOn(signalButton);
    clickOn(imageButton);
    clickOn(settingButton);

    assertEquals(3, callCount[0]);
  }
}