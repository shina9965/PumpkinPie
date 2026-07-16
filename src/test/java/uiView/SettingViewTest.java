package uiView;

import static org.junit.jupiter.api.Assertions.assertSame;

import java.lang.reflect.Field;
import listener.ActionListener;
import org.junit.jupiter.api.Test;

/**
 * SettingViewの画面なしで確認できる部分を確認するテスト。
 */
class SettingViewTest {

  @Test
  void constructorStoresActionListener() throws ReflectiveOperationException {
    // メモ: コンストラクタで受け取ったActionListenerを内部に保持できているか確認する。
    ActionListener actionListener = event -> {
    };

    SettingView view = new SettingView(actionListener);

    assertSame(actionListener, field(view, "actionListener"));
  }

  private Object field(Object target, String fieldName) throws ReflectiveOperationException {
    // メモ: actionListenerはprivateなので、テストではリフレクションで確認する。
    Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(target);
  }
}
