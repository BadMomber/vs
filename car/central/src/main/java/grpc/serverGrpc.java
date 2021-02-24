package grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.34.0)",
    comments = "Source: psqlserver.proto")
public final class serverGrpc {

  private serverGrpc() {}

  public static final String SERVICE_NAME = "server";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<grpc.Psqlserver.Data,
      grpc.Psqlserver.Confirmation> getSendMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "send",
      requestType = grpc.Psqlserver.Data.class,
      responseType = grpc.Psqlserver.Confirmation.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<grpc.Psqlserver.Data,
      grpc.Psqlserver.Confirmation> getSendMethod() {
    io.grpc.MethodDescriptor<grpc.Psqlserver.Data, grpc.Psqlserver.Confirmation> getSendMethod;
    if ((getSendMethod = serverGrpc.getSendMethod) == null) {
      synchronized (serverGrpc.class) {
        if ((getSendMethod = serverGrpc.getSendMethod) == null) {
          serverGrpc.getSendMethod = getSendMethod =
              io.grpc.MethodDescriptor.<grpc.Psqlserver.Data, grpc.Psqlserver.Confirmation>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "send"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.Psqlserver.Data.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.Psqlserver.Confirmation.getDefaultInstance()))
              .setSchemaDescriptor(new serverMethodDescriptorSupplier("send"))
              .build();
        }
      }
    }
    return getSendMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static serverStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<serverStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<serverStub>() {
        @java.lang.Override
        public serverStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new serverStub(channel, callOptions);
        }
      };
    return serverStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static serverBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<serverBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<serverBlockingStub>() {
        @java.lang.Override
        public serverBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new serverBlockingStub(channel, callOptions);
        }
      };
    return serverBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static serverFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<serverFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<serverFutureStub>() {
        @java.lang.Override
        public serverFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new serverFutureStub(channel, callOptions);
        }
      };
    return serverFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class serverImplBase implements io.grpc.BindableService {

    /**
     */
    public void send(grpc.Psqlserver.Data request,
        io.grpc.stub.StreamObserver<grpc.Psqlserver.Confirmation> responseObserver) {
      asyncUnimplementedUnaryCall(getSendMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSendMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                grpc.Psqlserver.Data,
                grpc.Psqlserver.Confirmation>(
                  this, METHODID_SEND)))
          .build();
    }
  }

  /**
   */
  public static final class serverStub extends io.grpc.stub.AbstractAsyncStub<serverStub> {
    private serverStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected serverStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new serverStub(channel, callOptions);
    }

    /**
     */
    public void send(grpc.Psqlserver.Data request,
        io.grpc.stub.StreamObserver<grpc.Psqlserver.Confirmation> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class serverBlockingStub extends io.grpc.stub.AbstractBlockingStub<serverBlockingStub> {
    private serverBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected serverBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new serverBlockingStub(channel, callOptions);
    }

    /**
     */
    public grpc.Psqlserver.Confirmation send(grpc.Psqlserver.Data request) {
      return blockingUnaryCall(
          getChannel(), getSendMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class serverFutureStub extends io.grpc.stub.AbstractFutureStub<serverFutureStub> {
    private serverFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected serverFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new serverFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<grpc.Psqlserver.Confirmation> send(
        grpc.Psqlserver.Data request) {
      return futureUnaryCall(
          getChannel().newCall(getSendMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SEND = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final serverImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(serverImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SEND:
          serviceImpl.send((grpc.Psqlserver.Data) request,
              (io.grpc.stub.StreamObserver<grpc.Psqlserver.Confirmation>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class serverBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    serverBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return grpc.Psqlserver.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("server");
    }
  }

  private static final class serverFileDescriptorSupplier
      extends serverBaseDescriptorSupplier {
    serverFileDescriptorSupplier() {}
  }

  private static final class serverMethodDescriptorSupplier
      extends serverBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    serverMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (serverGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new serverFileDescriptorSupplier())
              .addMethod(getSendMethod())
              .build();
        }
      }
    }
    return result;
  }
}
