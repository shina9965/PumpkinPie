package app;

import java.util.function.BooleanSupplier;
import java.util.List;
import java.util.function.Consumer;

public class BoolEx {

  /**
   * ifElseを実行するためのユーティリティメソッド
     * @param condition 条件
     * @param trueAction 条件がtrueのときに実行されるアクション
     * @param falseAction 条件がfalseのときに実行されるアクション
   */
  public static void ifTrueElse(boolean condition, Runnable trueAction, Runnable falseAction) {
    if (condition) {
      trueAction.run();
    } else {
      falseAction.run();
    }
  }

    /**
    * ifを実行するためのユーティリティメソッド
      * @param condition 条件
      * @param trueAction 条件がtrueのときに実行されるアクション
    */
  public static void ifTrueElse(boolean condition, Runnable trueAction) {
    if (condition) {
      trueAction.run();
    }
  }

  /**
  * whileを実行するためのユーティリティメソッド
    * @param condition ループの条件
    * @param action 条件がtrueのときに実行されるアクション
  */
  public static void whileTrue(BooleanSupplier condition, Runnable action) {
    while (condition.getAsBoolean()) {
      action.run();
    }
  }

  /**
   * forを実行するためのユーティリティメソッド
   * @param start
   * @param end
   * @param action
   */
  public static void forTrue(int start, int end, Runnable action) {
    for (int i = start; i < end; i++) {
      action.run();
    }
  }

  /**
   * forを実行するためのユーティリティメソッド
   * @param list
   * @param action
   */
  public static <T> void forTrue(List<T> list, Consumer<T> action) {
    for (T item : list) {
      action.accept(item);
    }
  }
}
