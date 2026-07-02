package waveletModel;

public class ImageWaveletModel extends WaveletModel {
    private double[][] originalImage;
    private double[][] ll; // Scaling coefficients
    private double[][] lh; // Horizontal coefficients
    private double[][] hl; // Vertical coefficients
    private double[][] hh; // Diagonal coefficients
    private double[][] inverseImage;
    
    // 元画像のサイズ（奇数の場合に偶数化された後のサイズではなく、元のサイズを保持しておくため）
    private int originalRows;
    private int originalCols;

    public double[][] getOriginalImage() { return originalImage; }
    public void setOriginalImage(double[][] originalImage) { 
        this.originalImage = originalImage;
        if (originalImage != null) {
            this.originalRows = originalImage.length;
            this.originalCols = originalImage[0].length;
        }
    }

    public double[][] getLl() { return ll; }
    public void setLl(double[][] ll) { this.ll = ll; }

    public double[][] getLh() { return lh; }
    public void setLh(double[][] lh) { this.lh = lh; }

    public double[][] getHl() { return hl; }
    public void setHl(double[][] hl) { this.hl = hl; }

    public double[][] getHh() { return hh; }
    public void setHh(double[][] hh) { this.hh = hh; }

    public double[][] getInverseImage() { return inverseImage; }
    public void setInverseImage(double[][] inverseImage) { this.inverseImage = inverseImage; }
    
    public int getOriginalRows() { return originalRows; }
    public int getOriginalCols() { return originalCols; }
}
