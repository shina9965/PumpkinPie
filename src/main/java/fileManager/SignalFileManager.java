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

    return switch (extension) {
      case "bin", "dat", "raw" -> readBinaryFile(file);
      case "txt", "csv" -> readTextFile(file);
      default -> throw new IllegalArgumentException("対応していない拡張子です: " + extension);
    };
  }

  /**
   * ファイル出力するためのメソッド。
   * 再構成された信号データをバイナリファイルとして書き込む。
   *
   * @param file                書き込み先ファイル
   * @param reconstructedSignal 再構成された信号データ
   * @throws IOException 書き込みに失敗した場合
   */
  public void exportFile(File file, double[] reconstructedSignal) throws IOException {
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

    writeBinaryFile(file, reconstructedSignal);
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
      BoolEx.ifTrueElse(
          exception.getCause() instanceof IOException,
          () -> {
            throw new RuntimeException(exception.getCause());
          });

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
      BoolEx.ifTrueElse(
          exception.getCause() instanceof IOException,
          () -> {
            throw new RuntimeException(exception.getCause());
          });

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
        String[] extension = {""};

        BoolEx.ifTrueElse(
                dotIndex >= 0 && dotIndex < fileName.length() - 1,
                () -> extension[0] = fileName.substring(dotIndex + 1).toLowerCase(Locale.ROOT)
        );

        return extension[0];
    }
}

