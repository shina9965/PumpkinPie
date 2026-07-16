package uiView;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import uiModel.SettingModel;

class SettingViewScreenCheck extends ApplicationTest {

  private SettingView view;

  @Override
  public void start(Stage stage) throws ReflectiveOperationException {
    SettingModel model = new SettingModel();
    model.resetAppliedRateToDefault();

    view = new SettingView(event -> {
    });
    view.initialize(model);
    view.open();
  }

  @AfterEach
  void closeSettingView() {
    interact(() -> view.close());
  }

  @Test
  void openShowsSettingStage() {
    interact(() -> {
      Stage settingStage = field("settingStage");

      assertTrue(settingStage.isShowing());
      assertEquals("設定", settingStage.getTitle());
    });
  }

  @Test
  void initializeDisplaysDefaultRate() {
    interact(() -> {
      Slider rateSlider = field("rateSlider");
      TextField rateTextField = field("rateTextField");

      assertEquals(10.0, rateSlider.getValue(), 0.000001);
      assertEquals("10", rateTextField.getText());
    });
  }

  @Test
  void updateViewChangesRateControls() {
    interact(() -> {
      Slider rateSlider = field("rateSlider");
      TextField rateTextField = field("rateTextField");

      view.updateView(65);

      assertEquals(65.0, rateSlider.getValue(), 0.000001);
      assertEquals("65", rateTextField.getText());
    });
  }

  @Test
  void buttonsHaveExpectedTextAndIds() {
    interact(() -> {
      Button applyButton = field("applyButton");
      Button resetButton = field("resetButton");
      Button exitButton = field("exitButton");
      Button backButton = field("backButton");
      Button creditButton = field("creditButton");

      assertEquals("適用", applyButton.getText());
      assertEquals("ADOPT", applyButton.getId());
      assertEquals("リセット", resetButton.getText());
      assertEquals("RESET", resetButton.getId());
      assertEquals("アプリを終了", exitButton.getText());
      assertEquals("FINISH", exitButton.getId());
      assertEquals("戻る", backButton.getText());
      assertEquals("RETURN", backButton.getId());
      assertEquals("クレジット", creditButton.getText());
      assertEquals("CREDIT", creditButton.getId());
    });
  }

  @SuppressWarnings("unchecked")
  private <T> T field(String fieldName) {
    try {
      Field field = SettingView.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      return (T) field.get(view);
    } catch (ReflectiveOperationException exception) {
      throw new AssertionError(exception);
    }
  }
}
