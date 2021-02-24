package central;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import grpc.serverGrpc.serverBlockingStub;
import grpc.Psqlserver.Confirmation;
import grpc.Psqlserver.Data;

public class GrpcClient {
    public void clientSendData(String json) {
        try {
            ManagedChannel channel = ManagedChannelBuilder.forAddress("haproxy", 1337).usePlaintext().build();



            serverBlockingStub serverGrpc = grpc.serverGrpc.newBlockingStub(channel);

            Data data = Data.newBuilder()
                    .setJsonString(json)
                    .setSenderName("central")
                    .build();

            Confirmation confirmation = serverGrpc.send(data);

            //System.out.println(confirmation.getConfirmationText());

            channel.shutdown();
        } catch (Exception e) {
            System.out.println("ERROR in Central:");
            System.out.println(e);
        }
    }
}
