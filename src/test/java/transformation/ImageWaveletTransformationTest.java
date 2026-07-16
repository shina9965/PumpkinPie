package transformation;

import org.junit.jupiter.api.Test;

import uiModel.SettingModel;
import waveletModel.ImageWaveletModel;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class ImageWaveletTransformationTest {


    @BeforeEach
        void setUp() {
            SettingModel settingModel = new SettingModel();
            settingModel.setAdoptionRate(100);
            settingModel.applyAdoptionRate();
        }

        @AfterEach
        void tearDown() {
            SettingModel settingModel = new SettingModel();
            settingModel.resetAppliedRateToDefault();
        }

@Test
public void testEvenSizeMatrix() {

    ImageWaveletModel model = new ImageWaveletModel();

    double[][] original = {
        {1.0, 2.0, 3.0, 4.0},
        {5.0, 6.0, 7.0, 8.0},
        {9.0, 10.0, 11.0, 12.0},
        {13.0, 14.0, 15.0, 16.0}
    };

    model.setOriginalImage(original);

    ImageWaveletTransformation transform =
        new ImageWaveletTransformation(model);

    transform.startWaveletTransformation();
    transform.startInverseWaveletTransformation();

    double[][] reconstructed = model.getInverseImage();

    for (int i = 0; i < 4; i++) {
        assertArrayEquals(original[i], reconstructed[i], 0.001);
    }
}

    @Test
    public void testOddSizeMatrix() {
        ImageWaveletModel model = new ImageWaveletModel();
        double[][] original = {
            {1.0, 2.0, 3.0},
            {4.0, 5.0, 6.0},
            {7.0, 8.0, 9.0}
        };
        model.setOriginalImage(original);

        ImageWaveletTransformation transform = new ImageWaveletTransformation(model);
        transform.startWaveletTransformation();
        
        // Padded to 4x4, so LL should be 2x2
        assertEquals(2, model.getLl().length);
        assertEquals(2, model.getLl()[0].length);

        transform.startInverseWaveletTransformation();

        double[][] reconstructed = model.getInverseImage();
        
        // Should unpad back to 3x3
        assertEquals(3, reconstructed.length);
        assertEquals(3, reconstructed[0].length);

        for (int i = 0; i < 3; i++) {
            assertArrayEquals(original[i], reconstructed[i], 0.001);
        }
    }
}

