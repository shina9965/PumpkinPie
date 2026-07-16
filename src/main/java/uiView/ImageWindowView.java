package uiView;

import app.BoolEx;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import listener.ActionListener;
import listener.ImageClickListener;
import uiController.ImageWindowController;
import uiModel.ImageWindowModel;
import javafx.util.Duration;
import listener.ImageClickListener;

public class ImageWindowView {

    private static final double WINDOW_WIDTH = 1200;
    private static final double WINDOW_HEIGHT = 650;
    private static final double LARGE_PANEL_WIDTH = 300;
    private static final double SMALL_PANEL_WIDTH = 250;
    private static final double PANEL_HEIGHT = 250;

    private ActionListener actionListener;
    
    private Pane originalImagePanel;
    private Pane inverseImagePanel;
    private Pane llPanel;
    private Pane lhPanel;
    private Pane hlPanel;
    private Pane hhPanel;

    private ImageView originalImageView = new ImageView();
    private ImageView inverseImageView = new ImageView();
    private ImageView llImageView = new ImageView();
    private ImageView lhImageView = new ImageView();
    private ImageView hlImageView = new ImageView();
    private ImageView hhImageView = new ImageView();

    public ImageWindowView(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public Scene createScene(ImageWindowModel model) {
        createImagePanels();

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(20);
        grid.setVgap(20);

        grid.add(originalImagePanel, 0, 0);
        grid.add(inverseImagePanel, 0, 1);

        grid.add(llPanel, 1, 0);
        grid.add(hlPanel, 2, 0);
        grid.add(lhPanel, 1, 1);
        grid.add(hhPanel, 2, 1);

        HBox buttonBox = createButtons(model);

        BorderPane root = new BorderPane();
        root.setCenter(grid);
        root.setBottom(buttonBox);

        return new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
    }

    public void createImagePanels() {
        originalImagePanel = createPanel("元画像表示", LARGE_PANEL_WIDTH, originalImageView);
        inverseImagePanel = createPanel("逆変換後画像表示", LARGE_PANEL_WIDTH, inverseImageView);
        
        llPanel = createPanel("スケーリング係数 (LL)", SMALL_PANEL_WIDTH, llImageView);
        lhPanel = createPanel("水平係数 (LH)", SMALL_PANEL_WIDTH, lhImageView);
        hlPanel = createPanel("垂直係数 (HL)", SMALL_PANEL_WIDTH, hlImageView);
        hhPanel = createPanel("対角係数 (HH)", SMALL_PANEL_WIDTH, hhImageView);
        
        setupMouseEvents(lhImageView, 1);
        setupMouseEvents(hlImageView, 2);
        setupMouseEvents(hhImageView, 3);
    }
    
    private void setupMouseEvents(ImageView imageView, int type) {
        double[] mouseX = {0};
        double[] mouseY = {0};

        Timeline holdTimeline = new Timeline(
            new KeyFrame(
                Duration.millis(100),
                event -> executeImageClick(
                    imageView,
                    type,
                    mouseX[0],
                    mouseY[0]
                )
            )
        );

        holdTimeline.setCycleCount(Timeline.INDEFINITE);

        imageView.setOnMousePressed(event -> {
            mouseX[0] = event.getX();
            mouseY[0] = event.getY();

            executeImageClick(
                imageView,
                type,
                mouseX[0],
                mouseY[0]
            );

            holdTimeline.playFromStart();
        });

        imageView.setOnMouseDragged(event -> {
            mouseX[0] = event.getX();
            mouseY[0] = event.getY();
        });

        imageView.setOnMouseReleased(event -> {
            holdTimeline.stop();
        });

        imageView.setOnMouseExited(event -> {
            holdTimeline.stop();
        });
    }

    private void executeImageClick(
            ImageView imageView,
            int type,
            double x,
            double y
    ) {
        BoolEx.ifTrueElse(
            actionListener instanceof ImageClickListener,
            () -> {
                ImageClickListener controller =
                    (ImageClickListener) actionListener;

                controller.handleImageClick(
                    x,
                    y,
                    imageView.getBoundsInLocal().getWidth(),
                    imageView.getBoundsInLocal().getHeight(),
                    type
                );
            }
        );
    }

    public HBox createButtons(ImageWindowModel model) {
        Button loadButton = createButton(model.getLoadButtonData().text(), model.getLoadButtonData().id());
        Button saveButton = createButton(model.getSaveButtonData().text(), model.getSaveButtonData().id());
        Button returnButton = createButton(model.getReturnButtonData().text(), model.getReturnButtonData().id());

        ToggleGroup group = new ToggleGroup();
        ToggleButton normalMode = createToggleButton("通常", "MODE_NORMAL", group, true);
        ToggleButton rMode = createToggleButton("R", "MODE_R", group, false);
        ToggleButton gMode = createToggleButton("G", "MODE_G", group, false);
        ToggleButton bMode = createToggleButton("B", "MODE_B", group, false);

        HBox modeBox = new HBox(10, new Label("モード: "), normalMode, rMode, gMode, bMode);
        modeBox.setAlignment(Pos.CENTER);

        HBox leftButtons = new HBox(30, saveButton, loadButton, modeBox);
        HBox rightButtons = new HBox(returnButton);

        HBox buttonBox = new HBox(300);
        buttonBox.setPadding(new Insets(20));
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(leftButtons, rightButtons);

        return buttonBox;
    }

    public Button createButton(String text, String id) {
        Button button = new Button(text);
        button.setId(id);
        button.setPrefSize(110, 55);
        button.setStyle("-fx-font-size: 16px;");
        button.setOnAction(event -> actionListener.actionPerformed(event));
        return button;
    }
    
    public ToggleButton createToggleButton(String text, String id, ToggleGroup group, boolean selected) {
        ToggleButton button = new ToggleButton(text);
        button.setId(id);
        button.setToggleGroup(group);
        button.setSelected(selected);
        button.setStyle("-fx-font-size: 14px;");
        button.setOnAction(event -> actionListener.actionPerformed(event));
        return button;
    }

    private Pane createPanel(String title, double width, ImageView imageView) {
        Pane pane = new Pane();
        pane.setPrefSize(width, PANEL_HEIGHT);
        pane.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-background-color: white;");

        Label label = new Label(title);
        label.setLayoutX(10);
        label.setLayoutY(10);
        label.setStyle("-fx-font-size: 16px;");
        
        imageView.setFitWidth(width - 20);
        imageView.setFitHeight(PANEL_HEIGHT - 40);
        imageView.setLayoutX(10);
        imageView.setLayoutY(30);
        imageView.setPreserveRatio(true);

        pane.getChildren().addAll(label, imageView);
        return pane;
    }

    public void updateOriginalImage(Image image) { originalImageView.setImage(image); }
    public void updateInverseImage(Image image) { inverseImageView.setImage(image); }
    public void updateLlImage(Image image) { llImageView.setImage(image); }
    public void updateLhImage(Image image) { lhImageView.setImage(image); }
    public void updateHlImage(Image image) { hlImageView.setImage(image); }
    public void updateHhImage(Image image) { hhImageView.setImage(image); }
}
