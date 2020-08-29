package org.boutry.core;

import org.boutry.wrapper.natimage.NatImage;
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
        NatImage duck = new NatImage(getClass().getResourceAsStream("/duck.jpeg").readAllBytes());
        int nWidth = 2;
        int nHeight = 4;
        NatImage[][] croppedDuck = imageCropper.cropImages(duck, nWidth, nHeight);
        assertEquals(croppedDuck.length, nWidth);
        assertEquals(croppedDuck[0].length, nHeight);
    }
}
