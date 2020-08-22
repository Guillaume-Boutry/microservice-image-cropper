package org.boutry;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import org.jboss.logging.Logger;

import javax.inject.Singleton;

@Singleton
public class Interceptor implements ServerInterceptor {
    private static final Logger LOGGER = Logger.getLogger(Interceptor.class);

    public Interceptor() {

    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers,
                                                                 ServerCallHandler<ReqT, RespT> next) {
        LOGGER.info("Intercepting call " + call.getMethodDescriptor());
        return next.startCall(call, headers);
    }
}
