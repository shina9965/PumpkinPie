package uiView;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import listener.ActionListener;
import uiModel.ImageWindowModel;

import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

public class ImageWindowViewTest extends ApplicationTest {

    private Stage stage;
    private ImageWindowModel model;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.model = new ImageWindowModel();
    }

    @Test
    void createSceneで画面の初期構成が正しく生成されること() {
        ActionListener listener = event -> {};

        interact(() -> {
            ImageWindowView view = new ImageWindowView(listener);
            Scene scene = view.createScene(model);
            stage.setScene(scene);
        });

        assertNotNull(stage.getScene());
        assertEquals(1200, stage.getScene().getWidth());
        assertEquals(650, stage.getScene().getHeight());

        BorderPane root = (BorderPane) stage.getScene().getRoot();
        GridPane grid = (GridPane) root.getCenter();
        HBox bottom = (HBox) root.getBottom();

        // 画像パネルの構成確認
        assertNotNull(grid);
        assertEquals(6, grid.getChildren().size());

        // ボタンの構成確認
        assertNotNull(bottom);
        assertEquals(2, bottom.getChildren().size()); // leftButtons と rightButtons
        HBox leftButtons = (HBox) bottom.getChildren().get(0);
        HBox rightButtons = (HBox) bottom.getChildren().get(1);

        Button loadButton = (Button) leftButtons.getChildren().get(0);
        Button saveButton = (Button) leftButtons.getChildren().get(1);
        HBox modeBox = (HBox) leftButtons.getChildren().get(2);

        Button returnButton = (Button) rightButtons.getChildren().get(0);

        assertEquals("画像を読み込む", loadButton.getText());
        assertEquals("画像を保存", saveButton.getText());
        assertEquals("戻る", returnButton.getText());

        // トグルボタンの構成確認 (モードBox内の2番目以降の要素)
        ToggleButton normalMode = (ToggleButton) modeBox.getChildren().get(1);
        assertTrue(normalMode.isSelected());
        assertEquals("通常", normalMode.getText());
    }

    @Test
    void 各ボタンをクリックした際にリスナーが正しく呼ばれること() {
        final int[] callCount = {0};
        ActionListener listener = event -> {
            callCount[0]++;
        };

        interact(() -> {
            ImageWindowView view = new ImageWindowView(listener);
            stage.setScene(view.createScene(model));
            stage.show();
        });

        BorderPane root = (BorderPane) stage.getScene().getRoot();
        HBox bottom = (HBox) root.getBottom();
        HBox leftButtons = (HBox) bottom.getChildren().get(0);
        HBox rightButtons = (HBox) bottom.getChildren().get(1);
        HBox modeBox = (HBox) leftButtons.getChildren().get(2);

        Button loadButton = (Button) leftButtons.getChildren().get(0);
        Button saveButton = (Button) leftButtons.getChildren().get(1);
        Button returnButton = (Button) rightButtons.getChildren().get(0);

        ToggleButton normalMode = (ToggleButton) modeBox.getChildren().get(1);
        ToggleButton rMode = (ToggleButton) modeBox.getChildren().get(2);
        ToggleButton gMode = (ToggleButton) modeBox.getChildren().get(3);
        ToggleButton bMode = (ToggleButton) modeBox.getChildren().get(4);

        interact(() -> {
            loadButton.fire();
            saveButton.fire();
            returnButton.fire();
            rMode.fire();
            gMode.fire();
            bMode.fire();
            normalMode.fire();
        });

        // 7回クリックされたか確認
        assertEquals(7, callCount[0]);
    }

    @Test
    void 各画像の更新メソッドを呼んだ際にImageViewの画像が更新されること() {
        ActionListener listener = event -> {};
        ImageWindowView view = new ImageWindowView(listener);

        interact(() -> {
            stage.setScene(view.createScene(model));
        });

        // test/java/data 以下のダミー画像を読み込む
        File file = new File("src/test/java/data/dummy.png");
        Image dummyImage = new Image(file.toURI().toString());

        interact(() -> {
            view.updateOriginalImage(dummyImage);
            view.updateInverseImage(dummyImage);
            view.updateLlImage(dummyImage);
            view.updateLhImage(dummyImage);
            view.updateHlImage(dummyImage);
            view.updateHhImage(dummyImage);
        });

        BorderPane root = (BorderPane) stage.getScene().getRoot();
        GridPane grid = (GridPane) root.getCenter();

        int imagePanelCount = 0;
        for (Node node : grid.getChildren()) {
            if (node instanceof Pane) {
                Pane pane = (Pane) node;
                // パネルの2番目の要素がImageViewである前提
                ImageView imageView = (ImageView) pane.getChildren().get(1);
                assertEquals(dummyImage, imageView.getImage());
                imagePanelCount++;
            }
        }
        
        // 6つのパネル全てが更新されたか確認
        assertEquals(6, imagePanelCount);
    }
}
