package server;

import java.io.IOException;

public class Server {
    public Boolean testMode;
    public String name;
    public String server;

    public Server(Boolean _testMode, String[] args) {
        this.testMode = _testMode;
        this.name = args[1];
        this.server = args[0];
    }

    public GrpcServer grpcServer = new GrpcServer();

    public void init() {
        try {
            grpcServer.serverStart(this.testMode, this.name, this.server);
        } catch(IOException |  InterruptedException ie){
            System.out.println("grpc failed: " + ie);
        }
    }
}
