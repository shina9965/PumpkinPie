package fileManager;

public class SignalFileManager extends IFileManager {


  /*テスト用クラス。あとで中身変更してOK */
    public double[] importFile(java.io.File file) {
      System.out.println("SignalFileManager: importFile called with " + file.getName());

        return new double[0]; 
    }
    /*テスト用クラス。あとで中身変更してOK */
    public void exportFile(java.io.File file, double[] data) {
        System.out.println("SignalFileManager: exportFile called with " + file.getName());
    }
}
