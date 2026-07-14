package uiController;

import app.BoolEx;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import javafx.stage.Window;
import listener.StateChangeListener;
import uiModel.ImageWindowModel;
import uiView.ImageWindowView;
import listener.ImageClickListener;

public class ImageWindowController extends WindowController implements ImageClickListener {

    private ImageWindowModel model;
    private ImageWindowView view;
    private Stage stage;

    public ImageWindowController(StateChangeListener stateChangeListener, SettingController settingController) {
        super(stateChangeListener, settingController);
        this.model = new ImageWindowModel();
        this.view = new ImageWindowView(this);
    }

    @Override
    public void initState() {
        this.stage = getShowingStage();
        BoolEx.ifTrueElse(this.stage != null, () -> {
            stage.setScene(view.createScene(model));
            stage.show();
            updateView();
        });
    }

    @Override
    public void endState() {
        // No specific cleanup needed
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        
        BoolEx.ifTrueElse(source instanceof Button, () -> {
            Button btn = (Button) source;
            String id = btn.getId();
            BoolEx.ifTrueElse(id.equals(model.getLoadButtonData().id()), this::onLoadImage);
            BoolEx.ifTrueElse(id.equals(model.getSaveButtonData().id()), this::onSaveImage);
            BoolEx.ifTrueElse(id.equals(model.getReturnButtonData().id()), this::onReturn);
        });

        BoolEx.ifTrueElse(source instanceof ToggleButton, () -> {
            ToggleButton btn = (ToggleButton) source;
            String id = btn.getId();
            BoolEx.ifTrueElse(id.equals("MODE_NORMAL"), () -> model.setMode(ImageWindowModel.Mode.NORMAL));
            BoolEx.ifTrueElse(id.equals("MODE_R"), () -> model.setMode(ImageWindowModel.Mode.R));
            BoolEx.ifTrueElse(id.equals("MODE_G"), () -> model.setMode(ImageWindowModel.Mode.G));
            BoolEx.ifTrueElse(id.equals("MODE_B"), () -> model.setMode(ImageWindowModel.Mode.B));
            updateView();
        });
    }

    private void onLoadImage() {
        try {
            javafx.scene.image.Image img = model.getFileManager().importSelectedFile();
            model.setOriginalImage(img);
            updateView();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void onSaveImage() {
        try {
            javafx.scene.image.Image img = model.getInverseImage();
            model.getFileManager().exportSelectedFile(img);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void onReturn() {
        stateChangeListener.changeWindowState(new HomeController(stateChangeListener, settingController, stage));
    }

    @Override
    public void onSetting() {
        // Not used here
    }

    public void handleImageClick(double x, double y, double width, double height, int type) {
        javafx.scene.image.Image[] imgWrapper = {null};
        BoolEx.ifTrueElse(type == 1, () -> imgWrapper[0] = model.getLhImage());
        BoolEx.ifTrueElse(type == 2, () -> imgWrapper[0] = model.getHlImage());
        BoolEx.ifTrueElse(type == 3, () -> imgWrapper[0] = model.getHhImage());
        
        BoolEx.ifTrueElse(imgWrapper[0] != null, () -> {
            javafx.scene.image.Image img = imgWrapper[0];
        
            int arrayWidth = (int) img.getWidth();
            int arrayHeight = (int) img.getHeight();
            
            int[] aX = { (int) ((x / width) * arrayWidth) };
            int[] aY = { (int) ((y / height) * arrayHeight) };
            
            // Ensure within bounds
            BoolEx.ifTrueElse(aX[0] < 0, () -> aX[0] = 0);
            BoolEx.ifTrueElse(aX[0] >= arrayWidth, () -> aX[0] = arrayWidth - 1);
            BoolEx.ifTrueElse(aY[0] < 0, () -> aY[0] = 0);
            BoolEx.ifTrueElse(aY[0] >= arrayHeight, () -> aY[0] = arrayHeight - 1);
            
            model.toggleCoefficient(aX[0], aY[0], type);
            updateView();
        });
    }

    private void updateView() {
        view.updateOriginalImage(model.getOriginalImage());
        view.updateInverseImage(model.getInverseImage());
        view.updateLlImage(model.getLlImage());
        view.updateLhImage(model.getLhImage());
        view.updateHlImage(model.getHlImage());
        view.updateHhImage(model.getHhImage());
    }

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
