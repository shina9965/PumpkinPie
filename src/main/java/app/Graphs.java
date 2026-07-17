package app;

import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Graphs {

    // 係数画像（LL以外）を白黒で描画する（見やすいように正規化して白い線を描画）
    public Image createCoefficientImage(double[][] data) {
        javafx.scene.image.Image[] result = {null};
        BoolEx.ifTrueElse(data != null && data.length > 0, () -> {
        int rows = data.length;
        int cols = data[0].length;
        WritableImage image = new WritableImage(cols, rows);
        PixelWriter writer = image.getPixelWriter();

        // 最大絶対値を求める
        double[] maxAbs = {0.0};
        int[] r = {0};
        BoolEx.forTrue(0, rows, _y -> {
            int[] c = {0};
            BoolEx.forTrue(0, cols, _x -> {
                double abs = Math.abs(data[r[0]][c[0]]);
                BoolEx.ifTrueElse(abs > maxAbs[0], () -> maxAbs[0] = abs);
                c[0]++;
            });
            r[0]++;
        });

        // 0を黒、最大値を白として描画
        int[] y = {0};
        BoolEx.forTrue(0, rows, _y -> {
            int[] x = {0};
            BoolEx.forTrue(0, cols, _x -> {
                double val = Math.abs(data[y[0]][x[0]]);
                double norm = maxAbs[0] == 0 ? 0 : val / maxAbs[0];
                writer.setColor(x[0], y[0], Color.gray(norm));
                x[0]++;
            });
            y[0]++;
        });

        result[0] = image;
        });
        return result[0];
    }

    // グレースケール画像を生成する（主に単一チャネルやLL成分など）
    public Image createGrayscaleImage(double[][] data) {
        javafx.scene.image.Image[] result = {null};
        BoolEx.ifTrueElse(data != null && data.length > 0, () -> {
        int rows = data.length;
        int cols = data[0].length;
        WritableImage image = new WritableImage(cols, rows);
        PixelWriter writer = image.getPixelWriter();

        int[] y = {0};
        BoolEx.forTrue(0, rows, _y -> {
            int[] x = {0};
            BoolEx.forTrue(0, cols, _x -> {
                double val = data[y[0]][x[0]];
                double[] clamped = {val};
                BoolEx.ifTrueElse(clamped[0] < 0, () -> clamped[0] = 0);
                BoolEx.ifTrueElse(clamped[0] > 255, () -> clamped[0] = 255);
                writer.setColor(x[0], y[0], Color.gray(clamped[0] / 255.0));
                x[0]++;
            });
            y[0]++;
        });

        result[0] = image;
        });
        return result[0];
    }

    // R, G, B の配列からカラー画像を生成する
    public Image createColorImage(double[][] rData, double[][] gData, double[][] bData) {
        javafx.scene.image.Image[] result = {null};
        BoolEx.ifTrueElse(rData != null && rData.length > 0, () -> {
        int rows = rData.length;
        int cols = rData[0].length;
        WritableImage image = new WritableImage(cols, rows);
        PixelWriter writer = image.getPixelWriter();

        int[] y = {0};
        BoolEx.forTrue(0, rows, _y -> {
            int[] x = {0};
            BoolEx.forTrue(0, cols, _x -> {
                double r = rData[y[0]][x[0]];
                double g = gData[y[0]][x[0]];
                double b = bData[y[0]][x[0]];
                
                double[] clampedR = {r};
                double[] clampedG = {g};
                double[] clampedB = {b};
                
                BoolEx.ifTrueElse(clampedR[0] < 0, () -> clampedR[0] = 0);
                BoolEx.ifTrueElse(clampedR[0] > 255, () -> clampedR[0] = 255);
                BoolEx.ifTrueElse(clampedG[0] < 0, () -> clampedG[0] = 0);
                BoolEx.ifTrueElse(clampedG[0] > 255, () -> clampedG[0] = 255);
                BoolEx.ifTrueElse(clampedB[0] < 0, () -> clampedB[0] = 0);
                BoolEx.ifTrueElse(clampedB[0] > 255, () -> clampedB[0] = 255);
                
                writer.setColor(x[0], y[0], Color.color(clampedR[0] / 255.0, clampedG[0] / 255.0, clampedB[0] / 255.0));
                x[0]++;
            });
            y[0]++;
        });

        result[0] = image;
        });
        return result[0];
    }
}
