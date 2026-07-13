package uiModel;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

public class ImageWindowModelTest extends ApplicationTest {

    private ImageWindowModel model;

    @Override
    public void start(Stage stage) {
        // JavaFX Toolkitの初期化のためにオーバーライド（空でOK）
    }

    @BeforeEach
    public void setUp() {
        model = new ImageWindowModel();
    }

    @Test
    void 初期状態とモード切り替えのテスト() {
        // 1. 初期状態でモードがNORMALであること
        assertEquals(ImageWindowModel.Mode.NORMAL, model.getMode());

        // 2. モード切り替えが正しく行われること
        model.setMode(ImageWindowModel.Mode.R);
        assertEquals(ImageWindowModel.Mode.R, model.getMode());

        model.setMode(ImageWindowModel.Mode.G);
        assertEquals(ImageWindowModel.Mode.G, model.getMode());

        model.setMode(ImageWindowModel.Mode.B);
        assertEquals(ImageWindowModel.Mode.B, model.getMode());

        model.setMode(ImageWindowModel.Mode.NORMAL);
        assertEquals(ImageWindowModel.Mode.NORMAL, model.getMode());

        // 3. 各ボタンデータが正しいテキストとIDを持っていること
        assertEquals("画像を読み込む", model.getLoadButtonData().text());
        assertEquals("LOAD_IMAGE", model.getLoadButtonData().id());

        assertEquals("画像を保存", model.getSaveButtonData().text());
        assertEquals("SAVE_IMAGE", model.getSaveButtonData().id());

        assertEquals("戻る", model.getReturnButtonData().text());
        assertEquals("RETURN_HOME", model.getReturnButtonData().id());
    }

    @Test
    void 画像セットと取得のテスト() {
        // ダミー画像を読み込む
        File file = new File("src/test/java/data/dummy.png");
        Image dummyImage = new Image(file.toURI().toString());

        // setOriginalImageの内部でSwingFXUtilsを使うため、JavaFX Application Thread上で実行
        interact(() -> {
            model.setOriginalImage(dummyImage);
        });

        // 1. 各モードでの取得確認
        // NORMALモード
        model.setMode(ImageWindowModel.Mode.NORMAL);
        assertNotNull(model.getOriginalImage(), "NORMALモードでの元画像取得失敗");
        assertNotNull(model.getInverseImage(), "NORMALモードでの逆変換後画像取得失敗");
        assertNotNull(model.getLlImage(), "NORMALモードでのLL画像取得失敗");
        assertNotNull(model.getLhImage(), "NORMALモードでのLH画像取得失敗");
        assertNotNull(model.getHlImage(), "NORMALモードでのHL画像取得失敗");
        assertNotNull(model.getHhImage(), "NORMALモードでのHH画像取得失敗");

        // Rモード
        model.setMode(ImageWindowModel.Mode.R);
        assertNotNull(model.getOriginalImage(), "Rモードでの元画像取得失敗");
        assertNotNull(model.getInverseImage(), "Rモードでの逆変換後画像取得失敗");
        assertNotNull(model.getLlImage(), "RモードでのLL画像取得失敗");

        // Gモード
        model.setMode(ImageWindowModel.Mode.G);
        assertNotNull(model.getOriginalImage(), "Gモードでの元画像取得失敗");
        assertNotNull(model.getInverseImage(), "Gモードでの逆変換後画像取得失敗");
        assertNotNull(model.getLlImage(), "GモードでのLL画像取得失敗");

        // Bモード
        model.setMode(ImageWindowModel.Mode.B);
        assertNotNull(model.getOriginalImage(), "Bモードでの元画像取得失敗");
        assertNotNull(model.getInverseImage(), "Bモードでの逆変換後画像取得失敗");
        assertNotNull(model.getLlImage(), "BモードでのLL画像取得失敗");
    }
}
