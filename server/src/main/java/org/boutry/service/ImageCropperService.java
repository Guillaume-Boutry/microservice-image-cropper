package org.boutry.service;

import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import org.boutry.common.BoutryUtilities.CropImageRequest;
import org.boutry.common.BoutryUtilities.CropImageResponse;
import org.boutry.common.ImageCropperGrpc;
import org.boutry.core.ImageCropper;
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
        byte[] buffer = request.getImage().toByteArray();
        int nWidth = request.getNWidth();
        int nHeight = request.getNHeight();
        BufferedImage bufferedImage;
        try {
            bufferedImage = imageCropper.fromByteArray(buffer);
        } catch (IOException e) {
            responseObserver.onError(e);
            responseObserver.onCompleted();
            return;
        }
        int height = bufferedImage.getHeight();
        int width = bufferedImage.getWidth();

        int widthToCrop = Math.round(width / (float) nWidth);
        int heightToCrop = Math.round(height / (float) nHeight);
        LOGGER.info(String.format("Image to crop : size(%d,%d), croppedSize(%d,%d), nCrop(%d,%d)", width, height, widthToCrop, heightToCrop, nWidth, nHeight));
        for (int i = 0; i < nWidth; i++) {
            for (int j = 0; j < nHeight; j++) {
                BufferedImage cropped = imageCropper.cropImage(bufferedImage, i, j, widthToCrop, heightToCrop);
                byte[] croppedByteArray;
                try {
                    croppedByteArray = imageCropper.toByteArray(cropped);
                } catch (IOException e) {
                    responseObserver.onError(new Exception(String.format("Error while writing (%d,%d)", i, j), e));
                    continue;
                }
                responseObserver.onNext(CropImageResponse
                        .newBuilder()
                        .setImage(ByteString.copyFrom(croppedByteArray))
                        .setXPosition(i)
                        .setYPosition(j)
                        .build());
            }
        }


        responseObserver.onCompleted();

    }
}
