package uiModel;

import app.BoolEx;
import app.Graphs;
import fileManager.ImageFileManager;
import javafx.scene.image.Image;
import org.opencv.core.Mat;
import transformation.ImageWaveletTransformation;
import waveletModel.ImageWaveletModel;
import transformation.RGB;

import java.util.HashMap;
import java.util.Map;

public class ImageWindowModel {

    public enum Mode { NORMAL, R, G, B }
    
    private ImageWaveletModel rModel = new ImageWaveletModel();
    private ImageWaveletModel gModel = new ImageWaveletModel();
    private ImageWaveletModel bModel = new ImageWaveletModel();
    
    private RGB rgbHelper;
    
    private Mode currentMode = Mode.NORMAL;
    
    private Graphs graphs = new Graphs();
    private ImageFileManager fileManager = new ImageFileManager();
    
    private Map<String, Double> savedCoeffs = new HashMap<>();

    public record ButtonData(String text, String id) {}
    private ButtonData loadButtonData = new ButtonData("画像を読み込む", "LOAD_IMAGE");
    private ButtonData saveButtonData = new ButtonData("画像を保存", "SAVE_IMAGE");
    private ButtonData returnButtonData = new ButtonData("戻る", "RETURN_HOME");

    public ButtonData getLoadButtonData() { return loadButtonData; }
    public ButtonData getSaveButtonData() { return saveButtonData; }
    public ButtonData getReturnButtonData() { return returnButtonData; }

    public void setMode(Mode mode) { this.currentMode = mode; }
    public Mode getMode() { return currentMode; }

    public void setOriginalImage(javafx.scene.image.Image fxImage) {
        java.awt.image.BufferedImage bImage = javafx.embed.swing.SwingFXUtils.fromFXImage(fxImage, null);
        this.rgbHelper = new RGB(bImage);
        this.rgbHelper.decomposeRGB();
        
        double[][] rData = rgbHelper.matToDoubleArray(rgbHelper.getR());
        double[][] gData = rgbHelper.matToDoubleArray(rgbHelper.getG());
        double[][] bData = rgbHelper.matToDoubleArray(rgbHelper.getB());
        
        bModel.setOriginalImage(bData);
        gModel.setOriginalImage(gData);
        rModel.setOriginalImage(rData);
        
        savedCoeffs.clear();
        
        new ImageWaveletTransformation(bModel).startWaveletTransformation();
        new ImageWaveletTransformation(gModel).startWaveletTransformation();
        new ImageWaveletTransformation(rModel).startWaveletTransformation();
        
        inverseTransformAll();
    }
    
    private void inverseTransformAll() {
        new ImageWaveletTransformation(bModel).startInverseWaveletTransformation();
        new ImageWaveletTransformation(gModel).startInverseWaveletTransformation();
        new ImageWaveletTransformation(rModel).startInverseWaveletTransformation();
    }

    public void toggleCoefficient(int x, int y, int type) {
        BoolEx.ifTrueElse(currentMode == Mode.NORMAL || currentMode == Mode.R, () -> toggleValue(rModel, x, y, type, "R"));
        BoolEx.ifTrueElse(currentMode == Mode.NORMAL || currentMode == Mode.G, () -> toggleValue(gModel, x, y, type, "G"));
        BoolEx.ifTrueElse(currentMode == Mode.NORMAL || currentMode == Mode.B, () -> toggleValue(bModel, x, y, type, "B"));
        inverseTransformAll();
    }
    
    private void toggleValue(ImageWaveletModel model, int x, int y, int type, String channel) {
        double[][] target = null;
        if (type == 1) target = model.getLh();
        if (type == 2) target = model.getHl();
        if (type == 3) target = model.getHh();
        
        if (target == null) return;
        
        String key = channel + "_" + type + "_" + x + "_" + y;
        double val = target[y][x];
        double[][] finalTarget = target;
        
        BoolEx.ifTrueElse(val != 0.0, () -> {
            savedCoeffs.put(key, val);
            finalTarget[y][x] = 0.0;
        }, () -> {
            Double orig = savedCoeffs.get(key);
            if (orig != null) {
                finalTarget[y][x] = orig;
                savedCoeffs.remove(key);
            }
        });
    }

    public javafx.scene.image.Image getOriginalImage() {
        javafx.scene.image.Image[] result = {null};
        BoolEx.ifTrueElse(rgbHelper != null, () -> {
            BoolEx.ifTrueElse(currentMode == Mode.NORMAL, 
                () -> result[0] = graphs.createColorImage(rModel.getOriginalImage(), gModel.getOriginalImage(), bModel.getOriginalImage()),
                () -> {
                    Mat mat = null;
                    if (currentMode == Mode.R) mat = rgbHelper.createRedImage(rgbHelper.doubleArrayToMat(rModel.getOriginalImage()));
                    if (currentMode == Mode.G) mat = rgbHelper.createGreenImage(rgbHelper.doubleArrayToMat(gModel.getOriginalImage()));
                    if (currentMode == Mode.B) mat = rgbHelper.createBlueImage(rgbHelper.doubleArrayToMat(bModel.getOriginalImage()));
                    
                    if (mat != null) {
                        java.awt.image.BufferedImage bImg = (java.awt.image.BufferedImage) rgbHelper.matToImage(mat);
                        result[0] = javafx.embed.swing.SwingFXUtils.toFXImage(bImg, null);
                    }
                }
            );
        });
        return result[0];
    }
    
    public javafx.scene.image.Image getInverseImage() {
        javafx.scene.image.Image[] result = {null};
        BoolEx.ifTrueElse(rgbHelper != null, () -> {
            BoolEx.ifTrueElse(currentMode == Mode.NORMAL, 
                () -> {
                    Mat merged = rgbHelper.mergedImage(
                        rgbHelper.doubleArrayToMat(rModel.getInverseImage()),
                        rgbHelper.doubleArrayToMat(gModel.getInverseImage()),
                        rgbHelper.doubleArrayToMat(bModel.getInverseImage())
                    );
                    java.awt.image.BufferedImage bImg = (java.awt.image.BufferedImage) rgbHelper.matToImage(merged);
                    result[0] = javafx.embed.swing.SwingFXUtils.toFXImage(bImg, null);
                },
                () -> {
                    Mat mat = null;
                    if (currentMode == Mode.R) mat = rgbHelper.createRedImage(rgbHelper.doubleArrayToMat(rModel.getInverseImage()));
                    if (currentMode == Mode.G) mat = rgbHelper.createGreenImage(rgbHelper.doubleArrayToMat(gModel.getInverseImage()));
                    if (currentMode == Mode.B) mat = rgbHelper.createBlueImage(rgbHelper.doubleArrayToMat(bModel.getInverseImage()));
                    
                    if (mat != null) {
                        java.awt.image.BufferedImage bImg = (java.awt.image.BufferedImage) rgbHelper.matToImage(mat);
                        result[0] = javafx.embed.swing.SwingFXUtils.toFXImage(bImg, null);
                    }
                }
            );
        });
        return result[0];
    }

    private javafx.scene.image.Image getCoefficientImage(int type) {
        javafx.scene.image.Image[] result = {null};
        BoolEx.ifTrueElse(currentMode == Mode.NORMAL, 
            () -> result[0] = getSingleChannelCoefficientImage(rModel, type),
            () -> {
                if (currentMode == Mode.R) result[0] = getSingleChannelCoefficientImage(rModel, type);
                if (currentMode == Mode.G) result[0] = getSingleChannelCoefficientImage(gModel, type);
                if (currentMode == Mode.B) result[0] = getSingleChannelCoefficientImage(bModel, type);
            }
        );
        return result[0];
    }
    
    private javafx.scene.image.Image getSingleChannelCoefficientImage(ImageWaveletModel model, int type) {
        if (type == 0) return graphs.createGrayscaleImage(model.getLl());
        if (type == 1) return graphs.createCoefficientImage(model.getLh());
        if (type == 2) return graphs.createCoefficientImage(model.getHl());
        if (type == 3) return graphs.createCoefficientImage(model.getHh());
        return null;
    }

    public javafx.scene.image.Image getLlImage() { return getCoefficientImage(0); }
    public javafx.scene.image.Image getLhImage() { return getCoefficientImage(1); }
    public javafx.scene.image.Image getHlImage() { return getCoefficientImage(2); }
    public javafx.scene.image.Image getHhImage() { return getCoefficientImage(3); }

    public ImageFileManager getFileManager() { return fileManager; }
}
