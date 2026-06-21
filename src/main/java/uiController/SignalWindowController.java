package uiController;

import java.io.IOException;

import app.BoolEx;
import fileManager.SignalFileManager;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.Window;
import listener.StateChangeListener;
import uiModel.SignalWindowModel;
import uiView.SignalWindowView;

public class SignalWindowController extends WindowController {

  // 信号変換表示画面を表示・更新するView
  private SignalWindowView signalWindowView;

  // 信号データや係数データ、ボタン情報を管理するModel
  private SignalWindowModel signalWindowModel;
  private SignalFileManager signalFileManager;

  // 現在表示しているJavaFXの画面
  private Stage stage;

  // SignalWindowControllerを初期化する
  public SignalWindowController(
      StateChangeListener stateChangeListener,
      SettingController settingController) {

    super(stateChangeListener, settingController);

    this.signalWindowModel = new SignalWindowModel();
    this.signalWindowView = new SignalWindowView(this);
    this.signalFileManager = new SignalFileManager();
  }

  // 信号変換表示画面を表示する
  @Override
  public void initState() {
    System.out.println("SignalWindowController: initState");

    this.stage = getShowingStage();

    stage.setScene(signalWindowView.createScene(signalWindowModel));
    stage.show();
  
    updateView();
  }

  // 信号変換表示画面を終了する
  @Override
  public void endState() {
    System.out.println("SignalWindowController: endState");
  }

  // ボタン操作に応じた処理を実行する
  @Override
  public void actionPerformed(ActionEvent event) {
    System.out.println("SignalWindowController: actionPerformed");

    BoolEx.ifTrueElse(
        event.getSource() instanceof Button,
        () -> {
          Button button = (Button) event.getSource();
          String buttonId = button.getId();

          BoolEx.ifTrueElse(
              signalWindowModel.getReturnButtonData().id().equals(buttonId),
              () -> {
                onReturn();
                System.out.println("SignalWindowController: Return Button Clicked");
              }
          );

          BoolEx.ifTrueElse(
              signalWindowModel.getSaveSignalButtonData().id().equals(buttonId),
              () -> {
                onSaveSignal();
                System.out.println("SignalWindowController: Save Button Clicked");
              }
          );

          BoolEx.ifTrueElse(
              signalWindowModel.getInputSignalButtonData().id().equals(buttonId),
              () -> {
                onInputSignal();
                System.out.println("SignalWindowController: Input Button Clicked");
              }
          );
        },
        () -> System.out.println("Event source is not a Button")
    );
  }

  // 戻るボタンが押されたときにホーム画面へ戻る
  @Override
  public void onReturn() {
    System.out.println("SignalWindowController: onReturn");

    stateChangeListener.changeWindowState(
        new HomeController(stateChangeListener, settingController, stage)
    );
  }

  // WindowControllerの抽象メソッドを実装するために残している
  // SignalWindowViewには設定ボタンが存在しないため、現在は使用しない
  @Override
  public void onSetting() {
    System.out.println("SignalWindowController: onSetting");
  }

  // 信号入力ボタンが押されたときに信号ファイルを読み込む
  public void onInputSignal() {
    System.out.println("SignalWindowController: onInputSignal");

    try {
      var signalData = signalFileManager.importSelectedFile();
      signalWindowModel.setOriginalSignal(signalData);
      updateView();
    } 
    catch (IOException e) {
      e.printStackTrace();
    } 
    catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }

     
  }

  // 信号保存ボタンが押されたときに逆変換後信号を保存する
  public void onSaveSignal() {
    System.out.println("SignalWindowController: onSaveSignal");

    // ファイル入出力担当が実装する。
     
  }

  // 係数がクリックされたときに係数の有効・無効を切り替える
  public void onEditCoefficient(int index) {
    System.out.println("SignalWindowController: onEditCoefficient");

    
    // マウス入力担当が実装する。
    
  }

  // ModelのデータをViewへ反映して画面を更新する
  private void updateView() {
    signalWindowView.updateOriginalSignal(signalWindowModel.getOriginalSignal());
    signalWindowView.updateInverseSignal(signalWindowModel.getInverseSignal());
    signalWindowView.updateScalingCoefficient(signalWindowModel.getScalingCoefficient());
    signalWindowView.updateWaveletCoefficient(signalWindowModel.getWaveletCoefficient());

    signalWindowView.updateWaveletEditDisplay(
        signalWindowModel.getEditedWaveletCoefficient(),
        signalWindowModel.getSelectedCoefficientIndex()
    );
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