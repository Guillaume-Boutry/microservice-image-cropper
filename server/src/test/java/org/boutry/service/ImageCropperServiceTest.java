package org.boutry.service;

import com.google.protobuf.ByteString;
import io.quarkus.grpc.runtime.annotations.GrpcService;
import io.quarkus.test.junit.QuarkusTest;
import org.boutry.common.BoutryUtilities.CropImageRequest;
import org.boutry.common.BoutryUtilities.CropImageResponse;
import org.boutry.common.ImageCropperGrpc;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;


@QuarkusTest
class ImageCropperServiceTest {

    @GrpcService("image-cropper")
    ImageCropperGrpc.ImageCropperBlockingStub grpcService;


    @Test
    void cropImage() throws IOException {
        int nWidth = 2;
        int nHeight = 3;
        CropImageRequest cropImageRequest = CropImageRequest
                .newBuilder()
                .setImage(ByteString
                        .copyFrom(getClass()
                                .getResourceAsStream("/duck.jpeg")
                                .readAllBytes()))
                .setNWidth(nWidth)
                .setNHeight(nHeight)
                .build();

        Iterator<CropImageResponse> cropImageResponseIterator = grpcService.cropImage(cropImageRequest);
        int nImages = 0;
        while (cropImageResponseIterator.hasNext()) {
            CropImageResponse cropImageResponse = cropImageResponseIterator.next();
            nImages++;
        }

        assertEquals(nImages, nWidth * nHeight);

    }
}
