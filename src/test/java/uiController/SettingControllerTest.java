package uiController;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sun.misc.Unsafe;
import uiModel.SettingModel;
import uiView.SettingView;

/**
 * SettingControllerがModelとViewを正しく仲介できるか確認するテスト。
 */
class SettingControllerTest {

  // メモ: SettingModelの適用済み採用率はstaticなので、各テスト前に初期化する。
  @BeforeEach
  void resetSystemAdoptionRate() {
    new SettingModel().resetAppliedRateToDefault();
  }

  @Test
  void openSettingOpensViewWhenClosed() throws ReflectiveOperationException {
    // メモ: 閉じている状態でopenSetting()を呼ぶと、Viewのopen()が1回だけ呼ばれる。
    SettingModel model = new SettingModel();
    RecordingSettingView view = new RecordingSettingView();
    SettingController controller = controllerWith(model, view);

    controller.openSetting();
    controller.openSetting();

    assertEquals(1, view.openCount);
    assertTrue(controller.getIsOpened());
  }

  @Test
  void closeSettingClosesViewWhenOpened() throws ReflectiveOperationException {
    // メモ: 開いている状態でcloseSetting()を呼ぶと、Viewのclose()が呼ばれる。
    SettingModel model = new SettingModel();
    RecordingSettingView view = new RecordingSettingView();
    SettingController controller = controllerWith(model, view);

    controller.openSetting();
    controller.closeSetting();

    assertEquals(1, view.closeCount);
    assertFalse(controller.getIsOpened());
  }

  @Test
  void closeSettingDoesNothingWhenAlreadyClosed() throws ReflectiveOperationException {
    // メモ: すでに閉じている場合はclose()を呼ばない。
    SettingModel model = new SettingModel();
    RecordingSettingView view = new RecordingSettingView();
    SettingController controller = controllerWith(model, view);

    controller.closeSetting();

    assertEquals(0, view.closeCount);
    assertFalse(controller.getIsOpened());
  }

  @Test
  void onApplyButtonClickedAppliesRateAndUpdatesView() throws ReflectiveOperationException {
    // メモ: 適用ボタン処理で採用率を保存し、画面更新と完了メッセージを出す。
    SettingModel model = new SettingModel();
    RecordingSettingView view = new RecordingSettingView();
    view.rateValue = 35;
    SettingController controller = controllerWith(model, view);

    controller.onApplyButtonClicked();

    assertEquals(35, model.getAdoptionRate());
    assertEquals(35, SettingModel.getSystemAdoptionRate());
    assertEquals(35, view.updatedRate);
    assertEquals("適用完了", view.messageTitle);
    assertTrue(view.messageText.contains("35%"));
  }

  @Test
  void onResetButtonClickedResetsModelAndUpdatesView() throws ReflectiveOperationException {
    // メモ: リセットボタン処理で採用率を初期値10に戻し、画面も更新する。
    SettingModel model = new SettingModel();
    model.setAdoptionRate(70);
    RecordingSettingView view = new RecordingSettingView();
    SettingController controller = controllerWith(model, view);

    controller.onResetButtonClicked();

    assertEquals(10, model.getAdoptionRate());
    assertEquals(10, view.updatedRate);
    assertEquals("リセット完了", view.messageTitle);
    assertTrue(view.messageText.contains("初期値"));
  }

  @Test
  void onBackButtonClickedClosesSettingWindow() throws ReflectiveOperationException {
    // メモ: 戻るボタン処理は設定画面を閉じる処理につながる。
    SettingModel model = new SettingModel();
    RecordingSettingView view = new RecordingSettingView();
    SettingController controller = controllerWith(model, view);

    controller.openSetting();
    controller.onBackButtonClicked();

    assertEquals(1, view.closeCount);
    assertFalse(controller.getIsOpened());
  }

  @Test
  void onCreditButtonClickedShowsCreditDialog() throws ReflectiveOperationException {
    // メモ: クレジットボタン処理でView側のクレジット表示が呼ばれる。
    SettingModel model = new SettingModel();
    RecordingSettingView view = new RecordingSettingView();
    SettingController controller = controllerWith(model, view);

    controller.onCreditButtonClicked();

    assertEquals(1, view.creditCount);
  }

  private SettingController controllerWith(SettingModel model, SettingView view)
      throws ReflectiveOperationException {
    // メモ: 本物のコンストラクタはJavaFX画面を作るため、テストでは依存だけ差し込む。
    SettingController controller = newControllerWithoutConstructor();
    setField(controller, "model", model);
    setField(controller, "view", view);
    return controller;
  }

  private void setField(Object target, String fieldName, Object value)
      throws ReflectiveOperationException {
    Field field = SettingController.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  private SettingController newControllerWithoutConstructor()
      throws ReflectiveOperationException {
    // メモ: 画面を開かない自動テストにするため、コンストラクタを通さず生成する。
    Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
    unsafeField.setAccessible(true);
    Unsafe unsafe = (Unsafe) unsafeField.get(null);
    return (SettingController) unsafe.allocateInstance(SettingController.class);
  }

  /**
   * メモ: 実画面を開かず、呼ばれた処理だけを記録するテスト用View。
   */
  private static class RecordingSettingView extends SettingView {
    private int rateValue;
    private int updatedRate = -1;
    private int openCount;
    private int closeCount;
    private int creditCount;
    private String messageTitle = "";
    private String messageText = "";

    private RecordingSettingView() {
      super(event -> {
      });
    }

    @Override
    public int getRateValue() {
      return rateValue;
    }

    @Override
    public void updateView(int rate) {
      updatedRate = rate;
    }

    @Override
    public void open() {
      openCount++;
    }

    @Override
    public void close() {
      closeCount++;
    }

    @Override
    public void showCredit() {
      creditCount++;
    }

    @Override
    public void showMessage(String title, String message) {
      messageTitle = title;
      messageText = message;
    }
  }
}
