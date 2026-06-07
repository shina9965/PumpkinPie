package uiController;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import listener.IWindowState;
import listener.StateChangeListener;

class HomeConstrollerTest {

  static class FakeStateChangeListener implements StateChangeListener {
    IWindowState nextWindowState;

    @Override
    public void changeWindowState(IWindowState nextWindowState) {
      this.nextWindowState = nextWindowState;
    }
  }

  @BeforeAll
  static void initJavaFx() throws Exception {
    CountDownLatch latch = new CountDownLatch(1);

    try {
      Platform.startup(latch::countDown);
    } catch (IllegalStateException e) {
      latch.countDown();
    }

    latch.await(5, TimeUnit.SECONDS);
  }

  @Test
  void signalButtonClickChangesToSignalWindowController() throws Exception {
    FakeStateChangeListener listener = new FakeStateChangeListener();
    SettingController settingController = new SettingController();

    runOnFxThread(() -> {
      Stage stage = new Stage();
      HomeConstroller controller =
          new HomeConstroller(listener, settingController, stage);

      Button button = new Button();
      button.setId("signalButton");

      controller.actionPerformed(new ActionEvent(button, null));
    });

    assertInstanceOf(SignalWindowController.class, listener.nextWindowState);
  }

  @Test
  void imageButtonClickChangesToImageWindowController() throws Exception {
    FakeStateChangeListener listener = new FakeStateChangeListener();
    SettingController settingController = new SettingController();

    runOnFxThread(() -> {
      Stage stage = new Stage();
      HomeConstroller controller =
          new HomeConstroller(listener, settingController, stage);

      Button button = new Button();
      button.setId("imageButton");

      controller.actionPerformed(new ActionEvent(button, null));
    });

    assertInstanceOf(ImageWindowController.class, listener.nextWindowState);
  }

  private static void runOnFxThread(Runnable action) throws Exception {
    CountDownLatch latch = new CountDownLatch(1);
    AtomicReference<Throwable> error = new AtomicReference<>();

    Platform.runLater(() -> {
      try {
        action.run();
      } catch (Throwable t) {
        error.set(t);
      } finally {
        latch.countDown();
      }
    });

    latch.await(5, TimeUnit.SECONDS);

    if (error.get() != null) {
      throw new RuntimeException(error.get());
    }
  }
}