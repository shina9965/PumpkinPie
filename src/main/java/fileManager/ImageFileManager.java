package fileManager;

import app.BoolEx;
import java.awt.FileDialog;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

/**
 * 画像ファイルの入力と出力を管理するクラス。
 * MVC上では、画面表示や変換処理ではなく画像ファイルの入出力だけを担当する。
 */
public class ImageFileManager extends IFileManager {

  /** 入力対象として選択された画像ファイル */
  private File inputFile;

  /** 出力先として選択された画像ファイル */
  private File outputFile;

  /** 読み込み可能な拡張子 */
  private final String[] acceptedExtensions;

  /** 出力する拡張子 */
  private final String outputExtension;

  /** ファイル選択画面 */
  private FileDialog fileDialog;

  /**
   * コンストラクタ。
   * 入出力に使用する拡張子を初期化する。
   */
  public ImageFileManager() {
    this.inputFile = null;
    this.outputFile = null;
    this.acceptedExtensions = new String[] {
        "png",
        "jpg",
        "jpeg"
    };
    this.outputExtension = "png";
    this.fileDialog = null;
  }

  /**
   * 画像ファイル選択画面を表示する。
   *
   * @return 選択された画像ファイル。
   *         キャンセルされた場合はnull
   */
  public File chooseImageFile() {
    fileDialog = new FileDialog(
        (java.awt.Frame) null,
        "画像ファイルを選択",
        FileDialog.LOAD
    );

    FilenameFilter imageFilter =
        (directory, name) -> isSupportedFileType(new File(directory, name));
    fileDialog.setFilenameFilter(imageFilter);

    showFileDialog(fileDialog);

    String fileName = fileDialog.getFile();

    if (fileName == null) {
      return null;
    }

    File selectedFile = new File(
        fileDialog.getDirectory(),
        fileName
    );

    BoolEx.ifTrueElse(
        !isSupportedFileType(selectedFile),
        () -> {
          throw new IllegalArgumentException(
              "対応していない拡張子です: " + getExtension(selectedFile)
          );
        }
    );

    inputFile = selectedFile;

    return inputFile;
  }

  /**
   * 画像ファイルの保存先選択画面を表示する。
   *
   * @return 選択された保存先ファイル。
   *         キャンセルされた場合はnull
   */
  public File chooseImageSaveFile() {
    fileDialog = new FileDialog(
        (java.awt.Frame) null,
        "画像ファイルの保存先を選択",
        FileDialog.SAVE
    );

    fileDialog.setFile("reconstructed_image.png");

    showFileDialog(fileDialog);

    String fileName = fileDialog.getFile();

    if (fileName == null) {
      return null;
    }

    File selectedFile = new File(
        fileDialog.getDirectory(),
        fileName
    );

    outputFile = correctOutputExtension(selectedFile);

    return outputFile;
  }

  /**
   * ファイル選択画面で選択した画像ファイルを読み込む。
   *
   * @return 読み込んだ画像
   * @throws IOException 読み込みに失敗した場合
   */
  public Image importSelectedFile() throws IOException {
    File selectedFile = chooseImageFile();

    BoolEx.ifTrueElse(
        selectedFile == null,
        () -> {
          throw new IllegalArgumentException(
              "ファイルが選択されませんでした。"
          );
        }
    );

    return importFile(selectedFile);
  }

  /**
   * 保存場所を選択し、再構成された画像をPNG形式で出力する。
   *
   * @param reconstructedImage 再構成された画像
   * @throws IOException 書き込みに失敗した場合
   */
  public void exportSelectedFile(Image reconstructedImage) throws IOException {
    BoolEx.ifTrueElse(
        reconstructedImage == null,
        () -> {
          throw new IllegalArgumentException(
              "reconstructedImageがnullです。"
          );
        }
    );

    File selectedFile = chooseImageSaveFile();

    BoolEx.ifTrueElse(
        selectedFile == null,
        () -> {
          throw new IllegalArgumentException(
              "保存先が選択されませんでした。"
          );
        }
    );

    exportFile(selectedFile, reconstructedImage);
  }

  /**
   * 指定された画像ファイルを読み込む。
   *
   * @param file 読み込む画像ファイル
   * @return 読み込んだ画像
   * @throws IOException 読み込みに失敗した場合
   */
  public Image importFile(File file) throws IOException {
    BoolEx.ifTrueElse(
        file == null,
        () -> {
          throw new IllegalArgumentException("fileがnullです。");
        }
    );

    BoolEx.ifTrueElse(
        !file.exists(),
        () -> {
          throw new IllegalArgumentException(
              "ファイルが存在しません: " + file.getPath()
          );
        }
    );

    BoolEx.ifTrueElse(
        !file.isFile(),
        () -> {
          throw new IllegalArgumentException(
              "通常ファイルではありません: " + file.getPath()
          );
        }
    );

    BoolEx.ifTrueElse(
        !isSupportedFileType(file),
        () -> {
          throw new IllegalArgumentException(
              "対応していない拡張子です: " + getExtension(file)
          );
        }
    );

    Image image = readImageFile(file);

    validateImage(image, file.getPath());

    inputFile = file;

    return image;
  }

  /**
   * 指定されたファイルへ再構成された画像をPNG形式で出力する。
   *
   * @param file 書き込み先ファイル
   * @param reconstructedImage 再構成された画像
   * @throws IOException 書き込みに失敗した場合
   */
  public void exportFile(
      File file,
      Image reconstructedImage
  ) throws IOException {

    BoolEx.ifTrueElse(
        file == null,
        () -> {
          throw new IllegalArgumentException("fileがnullです。");
        }
    );

    BoolEx.ifTrueElse(
        reconstructedImage == null,
        () -> {
          throw new IllegalArgumentException(
              "reconstructedImageがnullです。"
          );
        }
    );

    validateImage(reconstructedImage, "reconstructedImage");

    File correctedFile = correctOutputExtension(file);
    validateOutputFile(correctedFile);

    writeImageFile(correctedFile, reconstructedImage);

    outputFile = correctedFile;
  }

  /**
   * ファイルは対応している拡張子かどうかを確認する。
   *
   * @param file 確認するファイル
   * @return 対応している場合true、対応していない場合false
   */
  public boolean isSupportedFileType(File file) {
    String extension = getExtension(file);

    return Arrays.asList(acceptedExtensions).contains(extension);
  }

  /**
   * 実際の画像読み込み処理を行う。
   *
   * @param file 読み込む画像ファイル
   * @return 読み込んだ画像
   * @throws IOException 読み込みに失敗した場合
   */
  private Image readImageFile(File file) throws IOException {
    BufferedImage bufferedImage;

    try {
      bufferedImage = ImageIO.read(file);
    } catch (IOException exception) {
      throw new IOException(
          "画像ファイルの読み込みに失敗しました: " + file.getPath(),
          exception
      );
    }

    if (bufferedImage == null) {
      throw new IOException(
          "画像として読み込めません: " + file.getPath()
      );
    }

    Image image = SwingFXUtils.toFXImage(bufferedImage, null);

    if (image == null) {
      throw new IOException(
          "JavaFX Imageへの変換に失敗しました: " + file.getPath()
      );
    }

    return image;
  }

  /**
   * JavaFX ImageをPNG形式で保存する。
   *
   * @param file 書き込み先ファイル
   * @param reconstructedImage 再構成された画像
   * @throws IOException 書き込みに失敗した場合
   */
  private void writeImageFile(
      File file,
      Image reconstructedImage
  ) throws IOException {
    BufferedImage bufferedImage =
        SwingFXUtils.fromFXImage(reconstructedImage, null);

    if (bufferedImage == null) {
      throw new IOException(
          "BufferedImageへの変換に失敗しました: " + file.getPath()
      );
    }

    boolean written = ImageIO.write(
        bufferedImage,
        outputExtension,
        file
    );

    if (!written) {
      throw new IOException(
          "PNG形式で書き込めませんでした: " + file.getPath()
      );
    }
  }

  /**
   * 入力されたファイルの拡張子を取得する。
   *
   * @param file 対象ファイル
   * @return 拡張子
   */
  private String getExtension(File file) {
    String fileName = file == null ? "" : file.getName();
    int dotIndex = fileName.lastIndexOf(".");

    if (dotIndex <= 0 || dotIndex >= fileName.length() - 1) {
      return "";
    }

    return fileName.substring(dotIndex + 1).toLowerCase(Locale.ROOT);
  }

  /**
   * 出力ファイルの拡張子をPNGへ補正する。
   *
   * @param file 補正対象のファイル
   * @return PNG拡張子へ補正したファイル
   */
  private File correctOutputExtension(File file) {
    String fileName = file.getName();
    int dotIndex = fileName.lastIndexOf(".");
    String baseName = fileName;

    if (dotIndex > 0) {
      baseName = fileName.substring(0, dotIndex);
    }

    String correctedName = baseName + "." + outputExtension;
    File parentDirectory = file.getParentFile();

    if (parentDirectory == null) {
      return new File(correctedName);
    }

    return new File(parentDirectory, correctedName);
  }

  /**
   * 画像として有効か確認する。
   *
   * @param image 確認する画像
   * @param targetName エラーメッセージへ含める対象名
   */
  private void validateImage(Image image, String targetName) {
    BoolEx.ifTrueElse(
        image == null,
        () -> {
          throw new IllegalArgumentException(
              "画像がnullです: " + targetName
          );
        }
    );

    BoolEx.ifTrueElse(
        image.getWidth() <= 0 || image.getHeight() <= 0,
        () -> {
          throw new IllegalArgumentException(
              "画像の幅または高さが0以下です: " + targetName
          );
        }
    );

    BoolEx.ifTrueElse(
        image.isError(),
        () -> {
          throw new IllegalArgumentException(
              "JavaFX Imageがエラー状態です: " + targetName
          );
        }
    );
  }

  /**
   * 出力先として有効か確認する。
   *
   * @param file 確認する出力先ファイル
   */
  private void validateOutputFile(File file) {
    BoolEx.ifTrueElse(
        file.exists() && file.isDirectory(),
        () -> {
          throw new IllegalArgumentException(
              "出力先がフォルダです: " + file.getPath()
          );
        }
    );

    File parentDirectory = file.getAbsoluteFile().getParentFile();

    BoolEx.ifTrueElse(
        parentDirectory == null || !parentDirectory.exists(),
        () -> {
          throw new IllegalArgumentException(
              "保存先フォルダが存在しません: " + file.getPath()
          );
        }
    );

    BoolEx.ifTrueElse(
        !parentDirectory.isDirectory(),
        () -> {
          throw new IllegalArgumentException(
              "保存先がフォルダではありません: " + parentDirectory.getPath()
          );
        }
    );

    BoolEx.ifTrueElse(
        !parentDirectory.canWrite(),
        () -> {
          throw new IllegalArgumentException(
              "保存先フォルダへ書き込めません: " + parentDirectory.getPath()
          );
        }
    );
  }

  private void showFileDialog(FileDialog dialog) {
    dialog.setVisible(true);
  }
}
