package fileManager;

import app.BoolEx;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;


/**
 * 信号ファイルの入力を管理するクラス。
 * MVC上では、画面操作ではなくファイル入力処理を担当する。
 */
public class SignalFileManager {

  /** 対応する拡張子のリスト */
  private final String[] supportedExtensions;

  /**
   * コンストラクタ。
   * 対応拡張子を初期化する。
   */
  public SignalFileManager() {
    this.supportedExtensions = new String[] {
        "bin",
        "dat",
        "raw",
        "txt",
        "csv"
    };
  }

  /**
   * ファイル選択画面を表示し、選択されたファイルの絶対パスを返す。
   *
   * @return 選択されたファイルの絶対パス。
   *         キャンセルされた場合はnull
   */
  public String chooseSignalFilePath() {
    FileChooser fileChooser = new FileChooser();

    fileChooser.setTitle("信号ファイルを選択");

    fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter(
            "対応する信号ファイル",
            "*.bin",
            "*.dat",
            "*.raw",
            "*.txt",
            "*.csv"
        ),
        new FileChooser.ExtensionFilter(
            "すべてのファイル",
            "*.*"
        )
    );

    File selectedFile = fileChooser.showOpenDialog(null);

    return Optional.ofNullable(selectedFile)
        .map(File::getAbsolutePath)
        .orElse(null);
  }

  /**
   * ファイル選択画面で選択した信号ファイルを読み込む。
   *
   * @return 読み込んだ信号データ
   * @throws IOException 読み込みに失敗した場合
   */
  public double[] importSelectedFile() throws IOException {
    String selectedPath = chooseSignalFilePath();

    BoolEx.ifTrueElse(
        selectedPath == null,
        () -> {
          throw new IllegalArgumentException(
              "ファイルが選択されませんでした。"
          );
        }
    );

    File file = new File(selectedPath);

    return importFile(file);
  }

  /**
   * ファイル入力するためのメソッド。
   *
   * @param file 読み込むファイル
   * @return 読み込んだ信号データ
   * @throws IOException 読み込みに失敗した場合
   */
  public double[] importFile(File file) throws IOException {
    BoolEx.ifTrueElse(
        file == null,
        () -> {
          throw new IllegalArgumentException("fileがnullです。");
        });

    BoolEx.ifTrueElse(
        !file.exists(),
        () -> {
          throw new IllegalArgumentException("ファイルが存在しません: " + file.getPath());
        });

    BoolEx.ifTrueElse(
        !file.isFile(),
        () -> {
          throw new IllegalArgumentException("通常ファイルではありません: " + file.getPath());
        });

    BoolEx.ifTrueElse(
        !isSupportedFileType(file),
        () -> {
          throw new IllegalArgumentException("対応していない拡張子です: " + getExtension(file));
        });

    String extension = getExtension(file);

    // bin・dat・rawならバイナリ、それ以外の対応形式はtxt・csvとして読み込む。
    boolean isBinaryType =
        List.of("bin", "dat", "raw").contains(extension);

    // 要修正
    return isBinaryType
        ? readBinaryFile(file)
        : readTextFile(file);
  }

  /**
   * 信号ファイルの保存場所を選択し、
   * 選択された保存先の絶対パスを返す。
   *
   * @return 保存先の絶対パス。
   *         キャンセルされた場合はnull
   */
  public String chooseSignalSavePath() {
    FileChooser fileChooser = new FileChooser();

    fileChooser.setTitle(
        "信号ファイルの保存先を選択"
    );

    // 保存画面に最初から表示するファイル名
    fileChooser.setInitialFileName(
        "reconstructed_signal.bin"
    );

    // 現在はfloat型のバイナリ形式で出力する
    fileChooser.getExtensionFilters().add(
        new FileChooser.ExtensionFilter(
            "バイナリ信号ファイル",
            "*.bin"
        )
    );

    // 保存場所を選択する画面を表示する
    File selectedFile =
        fileChooser.showSaveDialog(null);

    // 選択された保存先の絶対パスを返す
    return Optional.ofNullable(selectedFile)
        .map(File::getAbsolutePath)
        .orElse(null);
  }

  /**
   * 保存場所を選択し、再構成された信号データを出力する。
   *
   * @param reconstructedSignal 再構成された信号データ
   * @throws IOException 書き込みに失敗した場合
   */
  public void exportSelectedFile(
      double[] reconstructedSignal
  ) throws IOException {

    // 出力する信号データが存在するか確認する
    BoolEx.ifTrueElse(
        reconstructedSignal == null,
        () -> {
          throw new IllegalArgumentException(
              "reconstructedSignalがnullです。"
          );
        }
    );

    // 保存場所を選択して、絶対パスを取得する
    String selectedPath = chooseSignalSavePath();

    // 保存画面でキャンセルされた場合
    BoolEx.ifTrueElse(
        selectedPath == null,
        () -> {
          throw new IllegalArgumentException(
              "保存先が選択されませんでした。"
          );
        }
    );

    // 取得したパスをFileへ変換する
    File selectedFile = new File(selectedPath);
    File[] outputFile = { selectedFile };

    /*
     * ファイル名に.binが付いていない場合は、
     * 自動的に.binを追加する。
     */
    boolean[] addedExtension = { false };
    BoolEx.ifTrueElse(
        !getExtension(selectedFile).equals("bin"),
        () -> {
          outputFile[0] = new File(selectedPath + ".bin");
          addedExtension[0] = true;
        }
    );

    // .binを自動補完した結果、既に同名ファイルが存在する場合は警告を出し直す
    BoolEx.ifTrueElse(
        addedExtension[0] && outputFile[0].exists(),
        () -> {
          Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
          alert.setTitle("上書き保存の確認");
          alert.setHeaderText(null);
          alert.setContentText(outputFile[0].getName() + " は既に存在します。上書きしますか？");
          
          Optional<ButtonType> result = alert.showAndWait();
          BoolEx.ifTrueElse(
              result.isPresent() && result.get() == ButtonType.OK,
              () -> {
                // 上書きに同意した場合はそのまま継続
              },
              () -> {
                throw new IllegalArgumentException(
                    "上書き保存がキャンセルされました。"
                );
              }
          );
        }
    );

    // 既存のexportFile()を呼び出して保存する
    exportFile(
        outputFile[0],
        reconstructedSignal
    );
  }

  /**
   * ファイル出力するためのメソッド。
   * 再構成された信号データをバイナリファイルとして書き込む。
   *
   * @param file 書き込み先ファイル
   * @param reconstructedSignal 再構成された信号データ
   * @throws IOException 書き込みに失敗した場合
   */
  public void exportFile(
      File file,
      double[] reconstructedSignal
  ) throws IOException {

    BoolEx.ifTrueElse(
        file == null,
        () -> {
          throw new IllegalArgumentException(
              "fileがnullです。"
          );
        }
    );

    BoolEx.ifTrueElse(
        reconstructedSignal == null,
        () -> {
          throw new IllegalArgumentException(
              "reconstructedSignalがnullです。"
          );
        }
    );

    writeBinaryFile(
        file,
        reconstructedSignal
    );
  }

  /**
   * ファイルは正しい拡張子かどうかを確認する。
   *
   * @param file 確認するファイル
   * @return 対応している場合true、対応していない場合false
   */
  public boolean isSupportedFileType(File file) {
    String extension = getExtension(file);

    return Arrays.asList(supportedExtensions).contains(extension);
  }

  /**
   * バイナリファイルを読み込む。
   *
   * @param file 読み込むバイナリファイル
   * @return 読み込んだ信号データ
   * @throws IOException 読み込みに失敗した場合
   */
  private double[] readBinaryFile(File file) throws IOException {
    long fileSize = file.length();

    BoolEx.ifTrueElse(
        fileSize % Float.BYTES != 0,
        () -> {
          throw new IllegalArgumentException(
              "バイナリファイルのサイズが4の倍数ではありません: " + file.getPath());
        });

    int dataSize = Math.toIntExact(fileSize / Float.BYTES);
    double[] signalData = new double[dataSize];

    try (DataInputStream inputStream = new DataInputStream(
        new BufferedInputStream(Files.newInputStream(file.toPath())))) {
      int[] index = { 0 };

      BoolEx.whileTrue(
          () -> index[0] < dataSize,
          () -> {
            try {
              signalData[index[0]] = inputStream.readFloat();
              index[0]++;
            } catch (IOException exception) {
              throw new RuntimeException(exception);
            }
          });
    } catch (RuntimeException exception) {
      if (exception.getCause() instanceof IOException) {
        throw (IOException) exception.getCause();
      }
      throw exception;
    }

    return signalData;
  }

  /**
   * txtとcsvファイルを読み込む。
   *
   * @param file 読み込むテキストファイル
   * @return 読み込んだ信号データ
   * @throws IOException 読み込みに失敗した場合
   */
  private double[] readTextFile(File file) throws IOException {
    List<String> lines = Files.readAllLines(file.toPath());
    List<Double> values = new ArrayList<>();

    BoolEx.forEach(
        lines,
        line -> {
          String trimmedLine = line.trim();

          BoolEx.ifTrueElse(
              !trimmedLine.isEmpty(),
              () -> {
                List<String> tokens = Arrays.asList(trimmedLine.split(","));

                BoolEx.forEach(
                    tokens,
                    token -> {
                      String trimmedToken = token.trim();

                      BoolEx.ifTrueElse(
                          !trimmedToken.isEmpty(),
                          () -> {
                            try {
                              values.add(Double.parseDouble(trimmedToken));
                            } catch (NumberFormatException exception) {
                              throw new IllegalArgumentException(
                                  "数値に変換できない値が含まれています: " + trimmedToken,
                                  exception);
                            }
                          });
                    });
              });
        });

    double[] signalData = new double[values.size()];
    int[] index = { 0 };

    BoolEx.whileTrue(
        () -> index[0] < values.size(),
        () -> {
          signalData[index[0]] = values.get(index[0]);
          index[0]++;
        });

    return signalData;
  }

  /**
   * バイナリファイルを書き込む。
   *
   * @param file                書き込み先ファイル
   * @param reconstructedSignal 再構成された信号データ
   * @throws IOException 書き込みに失敗した場合
   */
  private void writeBinaryFile(File file, double[] reconstructedSignal) throws IOException {
    BoolEx.ifTrueElse(
        file == null,
        () -> {
          throw new IllegalArgumentException("fileがnullです。");
        });

    BoolEx.ifTrueElse(
        reconstructedSignal == null,
        () -> {
          throw new IllegalArgumentException("reconstructedSignalがnullです。");
        });
    // ディレクトリが存在しない場合、ディレクトリを作成する
    File parentDir = file.getParentFile();
    BoolEx.ifTrueElse(
        parentDir != null && !parentDir.exists(),
        () -> parentDir.mkdirs()
    );

    try (DataOutputStream outputStream = new DataOutputStream(
        new BufferedOutputStream(Files.newOutputStream(file.toPath())))) {
      int[] index = { 0 };

      BoolEx.whileTrue(
          () -> index[0] < reconstructedSignal.length,
          () -> {
            try {
              outputStream.writeFloat((float) reconstructedSignal[index[0]]);
              index[0]++;
            } catch (IOException exception) {
              throw new RuntimeException(exception);
            }
          });
    } catch (RuntimeException exception) {
      if (exception.getCause() instanceof IOException) {
        throw (IOException) exception.getCause();
      }
      throw exception;
    }
  }

  /**
   * 入力されたファイルの拡張子をゲットする。
   *
   * @param file 対象ファイル
   * @return 拡張子
   */
  private String getExtension(File file) {
    String fileName = file == null ? "" : file.getName();
    int dotIndex = fileName.lastIndexOf(".");
    String[] extension = { "" };

    BoolEx.ifTrueElse(
        dotIndex >= 0 && dotIndex < fileName.length() - 1,
        () -> extension[0] = fileName.substring(dotIndex + 1).toLowerCase(Locale.ROOT));

    return extension[0];
  }
}
