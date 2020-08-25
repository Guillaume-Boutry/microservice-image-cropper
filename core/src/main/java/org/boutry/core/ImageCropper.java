package org.boutry.core;

import io.smallrye.common.constraint.NotNull;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@ApplicationScoped
public class ImageCropper {

    private static final Logger LOGGER = Logger.getLogger(ImageCropper.class);


    public BufferedImage[][] cropImages(@NotNull BufferedImage bufferedImage, int nWidth, int nHeight) {
        BufferedImage[][] imageArray = new BufferedImage[nWidth][nHeight];
        int height = bufferedImage.getHeight();
        int width = bufferedImage.getWidth();

        int widthToCrop = (int) Math.floor(width / (double) nWidth);
        int heightToCrop = (int) Math.floor(height / (double) nHeight);
        for (int i = 0; i < nWidth; i++) {
            for (int j = 0; j < nHeight; j++) {
                try {
                    imageArray[i][j] = cropImage(bufferedImage, i, j, widthToCrop, heightToCrop);
                } catch (Exception e) {
                    LOGGER.error(e);
                }
            }
        }

        return imageArray;
    }

    public void cropImages(@NotNull BufferedImage bufferedImage, int nWidth, int nHeight, TriConsumer<BufferedImage, Integer, Integer> callback) {
        int height = bufferedImage.getHeight();
        int width = bufferedImage.getWidth();

        int widthToCrop = (int) Math.floor(width / (double) nWidth);
        int heightToCrop = (int) Math.floor(height / (double) nHeight);
        for (int i = 0; i < nWidth; i++) {
            for (int j = 0; j < nHeight; j++) {
                try {
                    callback.accept(cropImage(bufferedImage, i, j, widthToCrop, heightToCrop), i, j);
                } catch (Exception e) {
                    LOGGER.error(e);
                }
            }
        }
    }


    private BufferedImage cropImage(@NotNull BufferedImage image, int x, int y, int width, int height) {
        return image.getSubimage(x * width, y * height, width, height);
    }

    public BufferedImage fromByteArray(@NotNull byte[] buffer) throws IOException {
        BufferedImage bufferedImage;
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer)) {
            bufferedImage = ImageIO.read(byteArrayInputStream);
        }
        return bufferedImage;
    }

    public byte[] toByteArray(@NotNull BufferedImage image) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }

}
