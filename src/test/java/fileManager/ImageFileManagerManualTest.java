package fileManager;

import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * ImageFileManagerのファイル選択機能を確認する手動テスト。
 */
public class ImageFileManagerManualTest extends Application {

  @Override
  public void start(Stage stage) {
    ImageFileManager manager = new ImageFileManager();

    try {
      File inputFile = manager.chooseImageFile();

      if (inputFile == null) {
        System.out.println("入力ファイル選択がキャンセルされました。");
        return;
      }

      Image image = manager.importFile(inputFile);

      System.out.println(
          "読み込んだ画像サイズ: "
              + image.getWidth()
              + " x "
              + image.getHeight()
      );

      File outputFile = manager.chooseImageSaveFile();

      if (outputFile == null) {
        System.out.println("保存先選択がキャンセルされました。");
        return;
      }

      manager.exportFile(outputFile, image);

      Image savedImage = manager.importFile(outputFile);

      System.out.println(
          "保存した画像サイズ: "
              + savedImage.getWidth()
              + " x "
              + savedImage.getHeight()
      );

    } catch (IOException exception) {
      System.out.println(
          "入出力エラー: " + exception.getMessage()
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
