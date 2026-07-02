package transformation;

import waveletModel.ImageWaveletModel;
import app.BoolEx;

public class ImageWaveletTransformation extends WaveletTransformation<ImageWaveletModel> {

    private ImageWaveletModel model;

    public ImageWaveletTransformation(ImageWaveletModel model) {
        this.model = model;
    }

    @Override
    public ImageWaveletModel startWaveletTransformation() {
        if (model == null || model.getOriginalImage() == null) return model;

        double[][] original = model.getOriginalImage();
        int rows = original.length;
        int cols = original[0].length;
        int paddedRows = rows % 2 == 0 ? rows : rows + 1;
        int paddedCols = cols % 2 == 0 ? cols : cols + 1;

        double[][] padded = new double[paddedRows][paddedCols];
        int[] r = {0};
        BoolEx.forTrue(0, paddedRows, () -> {
            int[] c = {0};
            BoolEx.forTrue(0, paddedCols, () -> {
                int srcR = r[0] < rows ? r[0] : rows - 1;
                int srcC = c[0] < cols ? c[0] : cols - 1;
                padded[r[0]][c[0]] = original[srcR][srcC];
                c[0]++;
            });
            r[0]++;
        });

        double[][] rowTransformed = new double[paddedRows][paddedCols];
        int[] i = {0};
        BoolEx.forTrue(0, paddedRows, () -> {
            rowTransformed[i[0]] = decompose(padded[i[0]]);
            i[0]++;
        });

        double[][] transposed = transpose(rowTransformed, paddedRows, paddedCols);

        double[][] colTransformed = new double[paddedCols][paddedRows];
        int[] j = {0};
        BoolEx.forTrue(0, paddedCols, () -> {
            colTransformed[j[0]] = decompose(transposed[j[0]]);
            j[0]++;
        });

        double[][] finalTransform = transpose(colTransformed, paddedCols, paddedRows);

        int halfRows = paddedRows / 2;
        int halfCols = paddedCols / 2;

        double[][] ll = new double[halfRows][halfCols];
        double[][] hl = new double[halfRows][halfCols];
        double[][] lh = new double[halfRows][halfCols];
        double[][] hh = new double[halfRows][halfCols];

        int[] yr = {0};
        BoolEx.forTrue(0, halfRows, () -> {
            int[] xc = {0};
            BoolEx.forTrue(0, halfCols, () -> {
                ll[yr[0]][xc[0]] = finalTransform[yr[0]][xc[0]];
                hl[yr[0]][xc[0]] = finalTransform[yr[0]][xc[0] + halfCols];
                lh[yr[0]][xc[0]] = finalTransform[yr[0] + halfRows][xc[0]];
                hh[yr[0]][xc[0]] = finalTransform[yr[0] + halfRows][xc[0] + halfCols];
                xc[0]++;
            });
            yr[0]++;
        });

        model.setLl(ll);
        model.setHl(hl);
        model.setLh(lh);
        model.setHh(hh);

        return model;
    }

    @Override
    public ImageWaveletModel startInverseWaveletTransformation() {
        if (model == null || model.getLl() == null) return model;

        int halfRows = model.getLl().length;
        int halfCols = model.getLl()[0].length;
        int rows = halfRows * 2;
        int cols = halfCols * 2;

        double[][] merged = new double[rows][cols];
        int[] r = {0};
        BoolEx.forTrue(0, halfRows, () -> {
            int[] c = {0};
            BoolEx.forTrue(0, halfCols, () -> {
                merged[r[0]][c[0]] = model.getLl()[r[0]][c[0]];
                merged[r[0]][c[0] + halfCols] = model.getHl()[r[0]][c[0]];
                merged[r[0] + halfRows][c[0]] = model.getLh()[r[0]][c[0]];
                merged[r[0] + halfRows][c[0] + halfCols] = model.getHh()[r[0]][c[0]];
                c[0]++;
            });
            r[0]++;
        });

        double[][] transposed = transpose(merged, rows, cols);

        double[][] colReconstructed = new double[cols][rows];
        int[] i = {0};
        BoolEx.forTrue(0, cols, () -> {
            colReconstructed[i[0]] = reconstruct(transposed[i[0]]);
            i[0]++;
        });

        double[][] reconstructed = transpose(colReconstructed, cols, rows);

        double[][] finalReconstructed = new double[rows][cols];
        int[] j = {0};
        BoolEx.forTrue(0, rows, () -> {
            finalReconstructed[j[0]] = reconstruct(reconstructed[j[0]]);
            j[0]++;
        });

        double[][] unpadded = removePadding(finalReconstructed, model.getOriginalRows(), model.getOriginalCols());
        model.setInverseImage(unpadded);

        return model;
    }

    @Override
    public void changeWaveletData(double[] value) {
        // Not used for 2D.
    }

    private double[][] transpose(double[][] matrix, int rows, int cols) {
        double[][] t = new double[cols][rows];
        int[] r = {0};
        BoolEx.forTrue(0, rows, () -> {
            int[] c = {0};
            BoolEx.forTrue(0, cols, () -> {
                t[c[0]][r[0]] = matrix[r[0]][c[0]];
                c[0]++;
            });
            r[0]++;
        });
        return t;
    }

    public double[][] removePadding(double[][] matrix, int origRows, int origCols) {
        double[][] result = new double[origRows][origCols];
        int[] i = {0};
        BoolEx.forTrue(0, origRows, () -> {
            int[] j = {0};
            BoolEx.forTrue(0, origCols, () -> {
                result[i[0]][j[0]] = matrix[i[0]][j[0]];
                j[0]++;
            });
            i[0]++;
        });
        return result;
    }
}
