package fileManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class SignalFileManagerTest {

    private SignalFileManager fileManager;

    @BeforeEach
    void setUp() {
        fileManager = new SignalFileManager();
    }

    @Test
    void exportFileでfileがnullの場合IllegalArgumentExceptionを投げる() {
        double[] data = {1.0, 2.0, 3.0};
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> fileManager.exportFile(null, data)
        );
        assertTrue(exception.getMessage().contains("fileがnullです"));
    }

    @Test
    void exportFileでreconstructedSignalがnullの場合IllegalArgumentExceptionを投げる(@TempDir Path tempDir) {
        File file = tempDir.resolve("output.bin").toFile();
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> fileManager.exportFile(file, null)
        );
        assertTrue(exception.getMessage().contains("reconstructedSignalがnullです"));
    }

    @Test
    void exportFileで空のデータ配列の場合に0バイトのファイルを生成してimportFileで正しく復元できる(@TempDir Path tempDir) throws IOException {
        File file = tempDir.resolve("empty.bin").toFile();
        double[] emptyData = new double[0];
        
        fileManager.exportFile(file, emptyData);
        
        assertTrue(file.exists());
        assertEquals(0, file.length());
        
        double[] importedData = fileManager.importFile(file);
        assertNotNull(importedData);
        assertEquals(0, importedData.length);
    }

    @Test
    void exportFileでデータを保存しimportFileで正常に読み込める(@TempDir Path tempDir) throws IOException {
        File file = tempDir.resolve("valid.bin").toFile();
        double[] data = {1.0, 2.0, 3.0};
        
        fileManager.exportFile(file, data);
        
        assertTrue(file.exists());
        assertEquals(12, file.length());
        
        double[] importedData = fileManager.importFile(file);
        assertArrayEquals(data, importedData, 0.0001);
    }

    @Test
    void exportFileで保存先のフォルダが存在しない場合に自動生成して保存できる(@TempDir Path tempDir) throws IOException {
        File file = tempDir.resolve("subdir").resolve("output.bin").toFile();
        double[] data = {1.5, 2.5};
        
        fileManager.exportFile(file, data);
        
        assertTrue(file.exists());
        assertEquals(8, file.length());
    }

    @Test
    void exportFileで書き込み先がディレクトリなどの場合IOExceptionを投げる(@TempDir Path tempDir) {
        File file = tempDir.toFile();
        double[] data = {1.0};
        
        assertThrows(
            IOException.class,
            () -> fileManager.exportFile(file, data)
        );
    }
}
