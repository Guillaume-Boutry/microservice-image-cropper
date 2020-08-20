package org.boutry.core;

import io.smallrye.common.constraint.NotNull;

import javax.enterprise.context.ApplicationScoped;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@ApplicationScoped
public class ImageCropper {
    public BufferedImage cropImage(@NotNull BufferedImage image, int x, int y, int width, int height) {
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
