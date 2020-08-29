package org.boutry.service;

import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import org.boutry.common.BoutryUtilities.CropImageRequest;
import org.boutry.common.BoutryUtilities.CropImageResponse;
import org.boutry.common.ImageCropperGrpc;
import org.boutry.core.ImageCropper;
import org.boutry.wrapper.natimage.NatImage;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Singleton
public class ImageCropperService extends ImageCropperGrpc.ImageCropperImplBase {

    private static final Logger LOGGER = Logger.getLogger(ImageCropperService.class);


    @Inject
    ImageCropper imageCropper;

    @Override
    public void cropImage(CropImageRequest request, StreamObserver<CropImageResponse> responseObserver) {
        long start = System.nanoTime();
        byte[] buffer = request.getImage().toByteArray();
        int nWidth = request.getNWidth();
        int nHeight = request.getNHeight();
        NatImage image = new NatImage(buffer);


        imageCropper.cropImages(image, nWidth, nHeight, (croppedImage, xPos, yPos) -> {
            LOGGER.debug(String.format("Image cropped : gridPos(%d,%d)", xPos, yPos));
            responseObserver.onNext(CropImageResponse
                    .newBuilder()
                    .setImage(ByteString.copyFrom(croppedImage.getImage()))
                    .setXPosition(xPos)
                    .setYPosition(yPos)
                    .build());
        });

        LOGGER.info("Total time: " + (System.nanoTime() - start) / 1_000_000d + "ms");

        responseObserver.onCompleted();

    }
}
