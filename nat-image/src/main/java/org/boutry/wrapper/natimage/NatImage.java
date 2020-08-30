package org.boutry.wrapper.natimage;

import org.scijava.nativelib.NativeLoader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;

public class NatImage {
    private long _handleImage;
    private SoftReference<BufferedImage> softImage;

    static {
        try {
            NativeLoader.loadLibrary("natimage");
        } catch (IOException e) {
            System.err.println("Unable to load libnativeimage, shutting down...");
            System.err.println(e);
            System.exit(1);
        }
    }

    public static void main(String... args) throws IOException, InterruptedException {
        byte[] image = NatImage.class
                .getResourceAsStream("/leelou_0_0.png")
                .readAllBytes();

        boolean isPng = NatImage.isPng(image);
        NatImage natImage = new NatImage(image);
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream("/tmp/test.png"))) {
            bufferedOutputStream.write(natImage.getImage());
        }
        natImage = null;
        System.gc();

        for (int i = 0; i < 100; i++) {
            Thread.sleep(100);
        }
    }

    public NatImage(byte[] image) {
        initPng(image);
    }

    @Override
    protected void finalize() throws Throwable {
        System.out.println("DISPOSED " + this._handleImage);
        this.dispose();
        super.finalize();
    }

    public static native boolean isPng(byte[] image);

    private native void initPng(byte[] image);

    public native byte[] getImage();

    private native void dispose();

    public NatImage cropImage(int x, int y, int width, int height) {
        BufferedImage image = toBufferedImage();
        return new NatImage(toByteArray(image.getSubimage(x * width, y * height, width, height)));
    }

    public BufferedImage toBufferedImage() {
        if (softImage == null || softImage.get() == null) {
            BufferedImage bufferedImage;
            try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(getImage())) {
                bufferedImage = ImageIO.read(byteArrayInputStream);
            } catch (IOException e) {
                //
                return null;
            }
            softImage = new SoftReference<>(bufferedImage);
            return bufferedImage;
        }
        return softImage.get();
    }

    private byte[] toByteArray(BufferedImage image) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            // silence exception
        }
        return null;
    }
}
