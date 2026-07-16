package uiModel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * SettingModelが採用率の値を正しく管理できるか確認するテスト。
 */
class SettingModelTest {

  // メモ: appliedAdoptionRateはstaticなので、各テストの前に初期値へ戻す。
  @BeforeEach
  void resetSystemAdoptionRate() {
    new SettingModel().resetAppliedRateToDefault();
  }

  @Test
  void constructorSetsDefaultAdoptionRate() {
    // メモ: 生成直後は編集値・デフォルト値・システム適用値がすべて10になる。
    SettingModel model = new SettingModel();

    assertEquals(10, model.getAdoptionRate());
    assertEquals(10, model.getDefaultAdoptionRate());
    assertEquals(10, SettingModel.getSystemAdoptionRate());
  }

  @Test
  void setAdoptionRateUpdatesEditingValue() {
    // メモ: setAdoptionRate()で画面上の編集中の採用率が更新される。
    SettingModel model = new SettingModel();

    model.setAdoptionRate(35);

    assertEquals(35, model.getAdoptionRate());
  }

  @Test
  void setAdoptionRateRejectsValueBelowMinimum() {
    // メモ: 採用率は0未満にできない。
    SettingModel model = new SettingModel();

    assertThrows(
        IllegalArgumentException.class,
        () -> model.setAdoptionRate(-1));
  }

  @Test
  void setAdoptionRateRejectsValueAboveMaximum() {
    // メモ: 採用率は100を超える値にできない。
    SettingModel model = new SettingModel();

    assertThrows(
        IllegalArgumentException.class,
        () -> model.setAdoptionRate(101));
  }

  @Test
  void applyAdoptionRateUpdatesSystemValue() {
    // メモ: applyAdoptionRate()で編集中の値をシステム全体の値へ反映する。
    SettingModel model = new SettingModel();

    model.setAdoptionRate(42);
    model.applyAdoptionRate();

    assertEquals(42, model.getAppliedAdoptionRate());
    assertEquals(42, SettingModel.getSystemAdoptionRate());
  }

  @Test
  void resetToDefaultUpdatesOnlyEditingValue() {
    // メモ: resetToDefault()は編集中の値だけを10に戻し、適用済みの値は変えない。
    SettingModel model = new SettingModel();

    model.setAdoptionRate(55);
    model.applyAdoptionRate();
    model.setAdoptionRate(80);
    model.resetToDefault();

    assertEquals(10, model.getAdoptionRate());
    assertEquals(55, model.getAppliedAdoptionRate());
  }

  @Test
  void resetAppliedRateToDefaultUpdatesEditingAndSystemValues() {
    // メモ: resetAppliedRateToDefault()は編集中の値と適用済みの値を両方10に戻す。
    SettingModel model = new SettingModel();

    model.setAdoptionRate(70);
    model.applyAdoptionRate();
    model.resetAppliedRateToDefault();

    assertEquals(10, model.getAdoptionRate());
    assertEquals(10, SettingModel.getSystemAdoptionRate());
  }
}
