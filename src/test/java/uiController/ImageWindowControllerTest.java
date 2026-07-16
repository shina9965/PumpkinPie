package uiController;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Field;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import listener.StateChangeListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import uiModel.ImageWindowModel;
import uiView.ImageWindowView;
import fileManager.ImageFileManager;

public class ImageWindowControllerTest extends ApplicationTest {

    private ImageWindowController controller;
    private StateChangeListener mockStateChangeListener;
    private SettingController mockSettingController;
    private ImageWindowModel mockModel;
    private ImageWindowView mockView;
    private ImageFileManager mockFileManager;
    private Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
    }

    @BeforeEach
    public void setUp() throws Exception {
        mockStateChangeListener = mock(StateChangeListener.class);
        mockSettingController = mock(SettingController.class);
        mockModel = mock(ImageWindowModel.class);
        mockView = mock(ImageWindowView.class);
        mockFileManager = mock(ImageFileManager.class);

        controller = new ImageWindowController(mockStateChangeListener, mockSettingController);

        // リフレクションを使用してモックをコントローラーに注入
        Field modelField = ImageWindowController.class.getDeclaredField("model");
        modelField.setAccessible(true);
        modelField.set(controller, mockModel);

        Field viewField = ImageWindowController.class.getDeclaredField("view");
        viewField.setAccessible(true);
        viewField.set(controller, mockView);

        // ファイルマネージャーのモックを返すように設定
        when(mockModel.getFileManager()).thenReturn(mockFileManager);

        // ボタンデータのデフォルトの挙動をモック
        when(mockModel.getLoadButtonData()).thenReturn(new ImageWindowModel.ButtonData("画像を読み込む", "LOAD_IMAGE"));
        when(mockModel.getSaveButtonData()).thenReturn(new ImageWindowModel.ButtonData("画像を保存", "SAVE_IMAGE"));
        when(mockModel.getReturnButtonData()).thenReturn(new ImageWindowModel.ButtonData("戻る", "RETURN_HOME"));
    }

    @Test
    void ロードボタン押下時のルーティングテスト() throws Exception {
        Image dummyImage = mock(Image.class);
        when(mockFileManager.importSelectedFile()).thenReturn(dummyImage);

        Button loadBtn = new Button();
        loadBtn.setId("LOAD_IMAGE");
        ActionEvent event = new ActionEvent(loadBtn, null);

        interact(() -> {
            controller.actionPerformed(event);
        });

        // importSelectedFileが呼ばれ、モデルにセットされ、Viewの表示更新が呼ばれることを検証
        verify(mockFileManager, times(1)).importSelectedFile();
        verify(mockModel, times(1)).setOriginalImage(dummyImage);
        verify(mockView, atLeastOnce()).updateOriginalImage(any());
    }

    @Test
    void 保存ボタン押下時のルーティングテスト() throws Exception {
        Image dummyImage = mock(Image.class);
        when(mockModel.getInverseImage()).thenReturn(dummyImage);

        Button saveBtn = new Button();
        saveBtn.setId("SAVE_IMAGE");
        ActionEvent event = new ActionEvent(saveBtn, null);

        interact(() -> {
            controller.actionPerformed(event);
        });

        // getInverseImageが呼ばれ、ファイルマネージャーのexportSelectedFileが呼ばれることを検証
        verify(mockModel, times(1)).getInverseImage();
        verify(mockFileManager, times(1)).exportSelectedFile(dummyImage);
    }

    @Test
    void 戻るボタン押下時のルーティングテスト() {
        Button returnBtn = new Button();
        returnBtn.setId("RETURN_HOME");
        ActionEvent event = new ActionEvent(returnBtn, null);

        // コントローラー内のstageフィールドが初期化されていないためリフレクションでセット
        try {
            Field stageField = ImageWindowController.class.getDeclaredField("stage");
            stageField.setAccessible(true);
            stageField.set(controller, stage);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        interact(() -> {
            controller.actionPerformed(event);
        });

        // StateChangeListener経由で画面遷移メソッドが呼ばれることを検証
        verify(mockStateChangeListener, times(1)).changeWindowState(any(HomeController.class));
    }

    @Test
    void モード切り替えボタンのテスト() {
        ToggleButton rToggle = new ToggleButton();
        rToggle.setId("MODE_R");
        ActionEvent event = new ActionEvent(rToggle, null);

        interact(() -> {
            controller.actionPerformed(event);
        });

        // モデルのモードが切り替えられ、Viewの再描画処理が走ることを検証
        verify(mockModel, times(1)).setMode(ImageWindowModel.Mode.R);
        verify(mockView, atLeastOnce()).updateOriginalImage(any());
    }
}
