package fileManager;

import app.BoolEx;
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
      runManualTest(manager);

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

  private void runManualTest(ImageFileManager manager) throws IOException {
    File inputFile = manager.chooseImageFile();

    BoolEx.ifTrueElse(
        inputFile == null,
        () -> {
          throw new IllegalArgumentException("入力ファイル選択がキャンセルされました。");
        });

    Image image = manager.importFile(inputFile);

    System.out.println(
        "読み込んだ画像サイズ: "
            + image.getWidth()
            + " x "
            + image.getHeight());

    File outputFile = manager.chooseImageSaveFile();

    BoolEx.ifTrueElse(
        outputFile == null,
        () -> {
          throw new IllegalArgumentException("保存先選択がキャンセルされました。");
        });

    manager.exportFile(outputFile, image);

    Image savedImage = manager.importFile(outputFile);

    System.out.println(
        "保存した画像サイズ: "
            + savedImage.getWidth()
            + " x "
            + savedImage.getHeight());
  }
}
