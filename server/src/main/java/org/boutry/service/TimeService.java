package org.boutry.service;

import io.grpc.stub.StreamObserver;
import org.boutry.common.TimeGrpc;
import org.boutry.common.BoutryUtilities.Locale;
import org.boutry.common.BoutryUtilities.TimeRequest;
import org.boutry.common.BoutryUtilities.TimeResponse;
import org.boutry.core.TimeHelper;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TimeService extends TimeGrpc.TimeImplBase {
    private static final Logger LOGGER = Logger.getLogger(TimeService.class);

    @Inject
    TimeHelper timeHelper;

    @Override
    public void getTime(TimeRequest request, StreamObserver<TimeResponse> responseObserver) {
        Locale locale = request.getLocale();
        int numberOfResponses = request.getNumberOfResponses();

        for (int i = 0; i < numberOfResponses; i++) {
            String date = timeHelper.getDateLocalized(locale);
            LOGGER.info("Sending the date " + date);
            responseObserver.onNext(TimeResponse
                    .newBuilder()
                    .setTime(date)
                    .build());
        }
        responseObserver.onCompleted();
    }
}
