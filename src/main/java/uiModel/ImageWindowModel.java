package uiModel;

import app.BoolEx;
import app.Graphs;
import fileManager.ImageFileManager;
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

    private ImageWaveletTransformation rTransformation;
    private ImageWaveletTransformation gTransformation;
    private ImageWaveletTransformation bTransformation;
    
    private RGB rgbHelper;
    
    private Mode currentMode = Mode.NORMAL;
    
    private Graphs graphs = new Graphs();
    private ImageFileManager fileManager = new ImageFileManager();
    
    private Map<String, Double> savedCoeffs = new HashMap<>();

    public record ButtonData(String text, String id) {}
    private ButtonData loadButtonData = new ButtonData("画像読込", "LOAD_IMAGE");
    private ButtonData saveButtonData = new ButtonData("画像保存", "SAVE_IMAGE");
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
        
        this.bTransformation = new ImageWaveletTransformation(bModel);
        this.gTransformation = new ImageWaveletTransformation(gModel);
        this.rTransformation = new ImageWaveletTransformation(rModel);
        
        bTransformation.startWaveletTransformation();
        gTransformation.startWaveletTransformation();
        rTransformation.startWaveletTransformation();
        
        inverseTransformAll();
    }
    
    private void inverseTransformAll() {
        bTransformation.startInverseWaveletTransformation();
        gTransformation.startInverseWaveletTransformation();
        rTransformation.startInverseWaveletTransformation();
    }

    public void toggleCoefficient(int x, int y, int type) {
        BoolEx.ifTrueElse(currentMode == Mode.NORMAL || currentMode == Mode.R, () -> toggleValue(rModel, x, y, type, "R"));
        BoolEx.ifTrueElse(currentMode == Mode.NORMAL || currentMode == Mode.G, () -> toggleValue(gModel, x, y, type, "G"));
        BoolEx.ifTrueElse(currentMode == Mode.NORMAL || currentMode == Mode.B, () -> toggleValue(bModel, x, y, type, "B"));
        inverseTransformAll();
    }
    
    private void toggleValue(
        ImageWaveletModel model,
        int x,
        int y,
        int type,
        String channel
    ) {
        double[][] transformed = model.getTransformedImage();

        BoolEx.ifTrueElse(
            transformed != null
                && transformed.length > 0
                && transformed[0].length > 0,
            () -> {
                int halfHeight = transformed.length / 2;
                int halfWidth = transformed[0].length / 2;

                int[] actualX = {x};
                int[] actualY = {y};

                // LH：右上
                BoolEx.ifTrueElse(type == 1, () -> {
                    actualX[0] += halfWidth;
                });

                // HL：左下
                BoolEx.ifTrueElse(type == 2, () -> {
                    actualY[0] += halfHeight;
                });

                // HH：右下
                BoolEx.ifTrueElse(type == 3, () -> {
                    actualX[0] += halfWidth;
                    actualY[0] += halfHeight;
                });

                boolean inBounds =
                    actualY[0] >= 0
                    && actualY[0] < transformed.length
                    && actualX[0] >= 0
                    && actualX[0] < transformed[0].length;

                BoolEx.ifTrueElse(inBounds, () -> {
                    String key =
                        channel + "_" + type + "_" + x + "_" + y;

                    double value =
                        transformed[actualY[0]][actualX[0]];

                    BoolEx.ifTrueElse(
                        value != 0.0,
                        () -> {
                            savedCoeffs.put(key, value);
                            transformed[actualY[0]][actualX[0]] = 0.0;
                        }
                    );

                    model.setTransformedImage(transformed);
                });
            }
        );
    }

    public javafx.scene.image.Image getOriginalImage() {
        javafx.scene.image.Image[] result = {null};
        BoolEx.ifTrueElse(rgbHelper != null, () -> {
            BoolEx.ifTrueElse(currentMode == Mode.NORMAL, 
                () -> result[0] = graphs.createColorImage(rModel.getOriginalImage(), gModel.getOriginalImage(), bModel.getOriginalImage()),
                () -> {
                    Mat[] matWrapper = {null};
                    BoolEx.ifTrueElse(currentMode == Mode.R, () -> matWrapper[0] = rgbHelper.createRedImage(rgbHelper.doubleArrayToMat(rModel.getOriginalImage())));
                    BoolEx.ifTrueElse(currentMode == Mode.G, () -> matWrapper[0] = rgbHelper.createGreenImage(rgbHelper.doubleArrayToMat(gModel.getOriginalImage())));
                    BoolEx.ifTrueElse(currentMode == Mode.B, () -> matWrapper[0] = rgbHelper.createBlueImage(rgbHelper.doubleArrayToMat(bModel.getOriginalImage())));
                    
                    BoolEx.ifTrueElse(matWrapper[0] != null, () -> {
                        java.awt.image.BufferedImage bImg = (java.awt.image.BufferedImage) rgbHelper.matToImage(matWrapper[0]);
                        result[0] = javafx.embed.swing.SwingFXUtils.toFXImage(bImg, null);
                    });
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
                    Mat[] matWrapper = {null};
                    BoolEx.ifTrueElse(currentMode == Mode.R, () -> matWrapper[0] = rgbHelper.createRedImage(rgbHelper.doubleArrayToMat(rModel.getInverseImage())));
                    BoolEx.ifTrueElse(currentMode == Mode.G, () -> matWrapper[0] = rgbHelper.createGreenImage(rgbHelper.doubleArrayToMat(gModel.getInverseImage())));
                    BoolEx.ifTrueElse(currentMode == Mode.B, () -> matWrapper[0] = rgbHelper.createBlueImage(rgbHelper.doubleArrayToMat(bModel.getInverseImage())));
                    
                    BoolEx.ifTrueElse(matWrapper[0] != null, () -> {
                        java.awt.image.BufferedImage bImg = (java.awt.image.BufferedImage) rgbHelper.matToImage(matWrapper[0]);
                        result[0] = javafx.embed.swing.SwingFXUtils.toFXImage(bImg, null);
                    });
                }
            );
        });
        return result[0];
    }

        private javafx.scene.image.Image getCoefficientImage(int type) {
        javafx.scene.image.Image[] result = {null};

        BoolEx.ifTrueElse(
            currentMode == Mode.NORMAL,
            () -> {
                double[][] rData = getCoefficientData(rModel, type);
                double[][] gData = getCoefficientData(gModel, type);
                double[][] bData = getCoefficientData(bModel, type);

                double[][] average =
                    averageCoefficient(rData, gData, bData);

                BoolEx.ifTrueElse(
                    type == 0,
                    () -> result[0] =
                        graphs.createGrayscaleImage(average),
                    () -> result[0] =
                        graphs.createCoefficientImage(average)
                );
            },
            () -> {
                BoolEx.ifTrueElse(
                    currentMode == Mode.R,
                    () -> result[0] =
                        getSingleChannelCoefficientImage(rModel, type)
                );

                BoolEx.ifTrueElse(
                    currentMode == Mode.G,
                    () -> result[0] =
                        getSingleChannelCoefficientImage(gModel, type)
                );

                BoolEx.ifTrueElse(
                    currentMode == Mode.B,
                    () -> result[0] =
                        getSingleChannelCoefficientImage(bModel, type)
                );
            }
        );

        return result[0];
    }

    private double[][] getCoefficientData(
        ImageWaveletModel model,
        int type
    ) {
        double[][][] result = {null};

        BoolEx.ifTrueElse(
            type == 0,
            () -> result[0] = model.getLl()
        );

        BoolEx.ifTrueElse(
            type == 1,
            () -> result[0] = model.getLh()
        );

        BoolEx.ifTrueElse(
            type == 2,
            () -> result[0] = model.getHl()
        );

        BoolEx.ifTrueElse(
            type == 3,
            () -> result[0] = model.getHh()
        );

        return result[0];
    }
    
    private javafx.scene.image.Image getSingleChannelCoefficientImage(ImageWaveletModel model, int type) {
        javafx.scene.image.Image[] result = {null};
        BoolEx.ifTrueElse(type == 0, () -> result[0] = graphs.createGrayscaleImage(model.getLl()));
        BoolEx.ifTrueElse(type == 1, () -> result[0] = graphs.createCoefficientImage(model.getLh()));
        BoolEx.ifTrueElse(type == 2, () -> result[0] = graphs.createCoefficientImage(model.getHl()));
        BoolEx.ifTrueElse(type == 3, () -> result[0] = graphs.createCoefficientImage(model.getHh()));
        return result[0];
    }

    private double[][] averageCoefficient(double[][] rData,double[][] gData,double[][] bData) {
        int rows = rData.length;
        int cols = rData[0].length;

        double[][] average = new double[rows][cols];

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                average[y][x] =
                    (rData[y][x] + gData[y][x] + bData[y][x]) / 3.0;
            }
        }

        return average;
    }

    public javafx.scene.image.Image getLlImage() { return getCoefficientImage(0); }
    public javafx.scene.image.Image getLhImage() { return getCoefficientImage(1); }
    public javafx.scene.image.Image getHlImage() { return getCoefficientImage(2); }
    public javafx.scene.image.Image getHhImage() { return getCoefficientImage(3); }

    public ImageFileManager getFileManager() { return fileManager; }
}
