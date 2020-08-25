package org.boutry.core;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ImageCropperTest {

    private static ImageCropper imageCropper;


    @BeforeAll
    static void beforeAll() {
        imageCropper = new ImageCropper();
    }

    @AfterAll
    static void afterAll() {
        imageCropper = null;
    }

    @Test
    void cropImage() throws IOException {
        BufferedImage duck = ImageIO.read(getClass().getResourceAsStream("/duck.jpeg"));
        int nWidth = 2;
        int nHeight = 4;
        BufferedImage[][] croppedDuck = imageCropper.cropImages(duck, nWidth, nHeight);
        assertEquals(croppedDuck.length, nWidth);
        assertEquals(croppedDuck[0].length, nHeight);
    }
}
