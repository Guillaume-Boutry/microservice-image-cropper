package org.boutry.core;

import io.smallrye.common.constraint.NotNull;
import org.boutry.wrapper.natimage.NatImage;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.awt.image.BufferedImage;

@ApplicationScoped
public class ImageCropper {

    private static final Logger LOGGER = Logger.getLogger(ImageCropper.class);


    public NatImage[][] cropImages(@NotNull NatImage natImage, int nWidth, int nHeight) {
        BufferedImage bufferedImage = natImage.toBufferedImage();
        NatImage[][] imageArray = new NatImage[nWidth][nHeight];
        int height = bufferedImage.getHeight();
        int width = bufferedImage.getWidth();

        int widthToCrop = (int) Math.floor(width / (double) nWidth);
        int heightToCrop = (int) Math.floor(height / (double) nHeight);
        for (int i = 0; i < nWidth; i++) {
            for (int j = 0; j < nHeight; j++) {
                try {
                    imageArray[i][j] = natImage.cropImage(i, j, widthToCrop, heightToCrop);
                } catch (Exception e) {
                    LOGGER.error(e);
                }
            }
        }

        return imageArray;
    }

    public void cropImages(@NotNull NatImage natImage, int nWidth, int nHeight, TriConsumer<NatImage, Integer, Integer> callback) {
        BufferedImage bufferedImage = natImage.toBufferedImage();
        int height = bufferedImage.getHeight();
        int width = bufferedImage.getWidth();

        int widthToCrop = (int) Math.floor(width / (double) nWidth);
        int heightToCrop = (int) Math.floor(height / (double) nHeight);
        for (int i = 0; i < nWidth; i++) {
            // Fill the heap for GC show
            new NatImage(natImage.getImage());
            for (int j = 0; j < nHeight; j++) {
                // Fill the heap for GC show
                new NatImage(natImage.getImage());
                try {
                    callback.accept(natImage.cropImage(i, j, widthToCrop, heightToCrop), i, j);
                } catch (Exception e) {
                    LOGGER.error(e);
                }
            }
        }
    }

}
