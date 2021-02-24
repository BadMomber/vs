package server;

import java.io.IOException;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class GrpcServer {

    public void serverStart(Boolean _testMode, String _name, String _server) throws IOException, InterruptedException {
        if (_name.equals("grpc-server-one")) {
            Server server = ServerBuilder.forPort(60010).addService(

                    new CentralService(_testMode, _name, _server)).build();

            server.start();
            System.out.println("server started at " + server.getPort());
            server.awaitTermination();
        } else if (_name.equals("grpc-server-two")) {
            Server server = ServerBuilder.forPort(60020).addService(

                    new CentralService(_testMode, _name, _server)).build();

            server.start();
            System.out.println("server started at " + server.getPort());
            server.awaitTermination();
        } else if (_name.equals("grpc-server-three")) {
            Server server = ServerBuilder.forPort(60030).addService(

                    new CentralService(_testMode, _name, _server)).build();

            server.start();
            System.out.println("server started at " + server.getPort());
            server.awaitTermination();
        } else {
            Server server = ServerBuilder.forPort(60010).addService(

                    new CentralService(_testMode, _name, _server)).build();

            server.start();
            System.out.println("server started at " + server.getPort());
            server.awaitTermination();
        }
    }
}
