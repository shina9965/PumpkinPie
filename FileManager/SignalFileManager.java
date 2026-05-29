
import java.io.File;

public class SignalFileManager extends IFileManager {
    //フィールド
    private final String[] supportedExtensions = {"bin", "dat", "raw", "txt", "csv"};

    //コンストラクタ
    public SignalFileManager(){}

    //メソッド
    public double[] importFile(File file){
        return null;
    }
    public void exportFile(File file, double[] reconstructedSignal){}
    public boolean isSupportedFileType(File file){
        return true;
    }
    private double[] readBinaryFile(File file){
        return null;
    }
    private double[] readTextFile(File file){
        return null;
    }
    private void writeBinaryFile(File file, double[] reconstructedSignal){}
    private String getExtension(File file){
        return null;
    }
}
