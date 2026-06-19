package app;

import fileManager.SignalFileManager;
import java.io.IOException;
import java.util.Arrays;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * SignalFileManagerのファイル選択機能を確認する手動テスト。
 */
public class SignalFileChooserManualTest extends Application {

  @Override
  public void start(Stage stage) {
    SignalFileManager manager = new SignalFileManager();

    try {
      // ファイル選択画面を表示し、選択したファイルを読み込む。
      double[] signalData = manager.importSelectedFile();

      System.out.println(
          "読み込んだデータ数: " + signalData.length
      );

      System.out.println(
          "読み込んだデータ: " + Arrays.toString(signalData)
      );

    } catch (IOException exception) {
      System.out.println(
          "読み込みエラー: " + exception.getMessage()
      );

    } catch (IllegalArgumentException exception) {
      System.out.println(
          "入力エラー: " + exception.getMessage()
      );

    } finally {
      stage.close();
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}