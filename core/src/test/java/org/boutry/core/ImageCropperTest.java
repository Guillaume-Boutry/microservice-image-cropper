package org.boutry.core;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

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
        int size = 10;
        BufferedImage croppedDuck = imageCropper.cropImage(duck, 0, 0, size, size);
        assertEquals(croppedDuck.getHeight(), size);
        assertEquals(croppedDuck.getWidth(), size);
    }
}
