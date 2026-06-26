package uiController;

import app.BoolEx;
import fileManager.ImageFileManager;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;
import listener.StateChangeListener;
import uiModel.ImageWindowModel;
import uiView.ImageWindowView;

public class ImageWindowController extends WindowController {

  // 画像処理画面を表示・更新するView
  private ImageWindowView imageWindowView;

  // 画像データやボタン情報を管理するModel
  private ImageWindowModel imageWindowModel;

  // 画像ファイルの入出力を担当するFileManager
  private ImageFileManager imageFileManager;

  // 現在表示しているJavaFXの画面
  private Stage stage;

  public ImageWindowController(
      StateChangeListener stateChangeListener,
      SettingController settingController
  ) {
    super(stateChangeListener, settingController);

    this.imageWindowModel = new ImageWindowModel();
    this.imageWindowView = new ImageWindowView(this);
    this.imageFileManager = new ImageFileManager();
  }

  // 画像処理画面を表示する
  @Override
  public void initState() {
    System.out.println("ImageWindowController: initState");

    this.stage = getShowingStage();

    stage.setScene(imageWindowView.createScene(imageWindowModel));
    stage.show();

    updateView();
  }

  // 画像処理画面を終了する
  @Override
  public void endState() {
    System.out.println("ImageWindowController: endState");
  }

  // ボタン操作に応じた処理を実行する
  @Override
  public void actionPerformed(ActionEvent event) {
    System.out.println("ImageWindowController: actionPerformed");

    BoolEx.ifTrueElse(
        event.getSource() instanceof Button,
        () -> {
          Button button = (Button) event.getSource();
          String buttonId = button.getId();

          BoolEx.ifTrueElse(
              imageWindowModel.getInputImageButtonData().id().equals(buttonId),
              () -> {
                onInputImage();
                System.out.println("ImageWindowController: Input Button Clicked");
              }
          );

          BoolEx.ifTrueElse(
              imageWindowModel.getSaveImageButtonData().id().equals(buttonId),
              () -> {
                onSaveImage();
                System.out.println("ImageWindowController: Save Button Clicked");
              }
          );

          BoolEx.ifTrueElse(
              imageWindowModel.getReturnButtonData().id().equals(buttonId),
              () -> {
                onReturn();
                System.out.println("ImageWindowController: Return Button Clicked");
              }
          );
        },
        () -> System.out.println("Event source is not a Button")
    );
  }

  // 戻るボタンが押されたときにホーム画面へ戻る
  @Override
  public void onReturn() {
    System.out.println("ImageWindowController: onReturn");

    stateChangeListener.changeWindowState(
        new HomeController(stateChangeListener, settingController, stage)
    );
  }

  // WindowControllerの抽象メソッドを実装するために残している
  // ImageWindowViewには設定ボタンが存在しないため、現在は使用しない
  @Override
  public void onSetting() {
    System.out.println("ImageWindowController: onSetting");
  }

  // 画像入力ボタンが押されたときに画像ファイルを読み込む
  public void onInputImage() {
    System.out.println("ImageWindowController: onInputImage");

    try {
      Image image = imageFileManager.importSelectedFile();
      imageWindowModel.setOriginalImage(image);
      updateView();
    } catch (IOException exception) {
      exception.printStackTrace();
    } catch (IllegalArgumentException exception) {
      System.out.println(exception.getMessage());
    }
  }

  // 画像保存ボタンが押されたときに表示中の画像をPNG形式で保存する
  public void onSaveImage() {
    System.out.println("ImageWindowController: onSaveImage");

    try {
      Image image = imageWindowModel.getOriginalImage();

      BoolEx.ifTrueElse(
          image == null,
          () -> {
            throw new IllegalArgumentException(
                "保存する画像がありません。"
            );
          }
      );

      imageFileManager.exportSelectedFile(image);
    } catch (IOException exception) {
      exception.printStackTrace();
    } catch (IllegalArgumentException exception) {
      System.out.println(exception.getMessage());
    }
  }

  // ModelのデータをViewへ反映して画面を更新する
  private void updateView() {
    imageWindowView.updateOriginalImage(imageWindowModel.getOriginalImage());
  }

  // 現在表示中のStageを取得する
  private Stage getShowingStage() {
    Stage[] showingStage = {new Stage()};

    for (Window window : Window.getWindows()) {
      BoolEx.ifTrueElse(
          window instanceof Stage && window.isShowing(),
          () -> showingStage[0] = (Stage) window
      );
    }

    return showingStage[0];
  }
}
