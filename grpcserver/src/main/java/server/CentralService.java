package server;

import grpc.Psqlserver.Confirmation;
import grpc.Psqlserver.Data;
import grpc.serverGrpc;
import grpc.serverGrpc.serverImplBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;


public class CentralService extends serverImplBase {
    public Boolean testMode;
    public String name;
    public int messageCounter = 0;
    public int numMessagesSavedInDb = 0;
    public String sql = "SELECT * FROM messages ORDER BY timestamp DESC LIMIT 32";
    public int timeout = 50;
    public long start_time;

    public void setStartTime() {
        System.out.println("Setting new start_time to: " + System.currentTimeMillis());
        this.start_time = System.currentTimeMillis();
    }

    List<String> valueSetsArray = new ArrayList<String>();

    PsqlDatabase server = new PsqlDatabase();

    public Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://" + this.server.server + ":5432/server_db", "lekko", "123456789");
    }

    public CentralService(Boolean _testMode, String _name, String _server) {
        this.testMode = _testMode;
        this.name = _name;
        this.server.server = _server;
        System.out.println("_____CentralService.java testMode is: " + this.testMode + "_____");
        start_time = System.currentTimeMillis();
    }

    public void sendToGrpcOne(String json, ManagedChannel channel) {
        try {
            System.out.println("Processing message to grpc 1");
            serverGrpc.serverBlockingStub serverGrpc = grpc.serverGrpc.newBlockingStub(channel);

            Data data = Data.newBuilder()
                    .setJsonString(json)
                    .setSenderName(this.name)
                    .build();

            Confirmation confirmation = serverGrpc.send(data);
        } catch (Exception e) {
            System.out.println("ERROR in grpc: " + this.name);
            System.out.println(e);
        }
    }

    public void sendToGrpcTwo(String json, ManagedChannel channel) {
        try {
            System.out.println("Processing message to grpc 2");

            serverGrpc.serverBlockingStub serverGrpc = grpc.serverGrpc.newBlockingStub(channel);

            Data data = Data.newBuilder()
                    .setJsonString(json)
                    .setSenderName(this.name)
                    .build();

            Confirmation confirmation = serverGrpc.send(data);
        } catch (Exception e) {
            System.out.println("ERROR in Central:");
            System.out.println(e);
        }
    }

    public void sendToGrpcThree(String json, ManagedChannel channel) {
        try {
            serverGrpc.serverBlockingStub serverGrpc = grpc.serverGrpc.newBlockingStub(channel);

            Data data = Data.newBuilder()
                    .setJsonString(json)
                    .setSenderName(this.name)
                    .build();

            Confirmation confirmation = serverGrpc.send(data);
        } catch (Exception e) {
            System.out.println("ERROR in Central:");
            System.out.println(e);
        }
    }

    @Override
    public void send(Data data, StreamObserver<Confirmation> responseObserver) {

        //System.out.println("Inside Data Transfer!");
        String json = data.getJsonString();

        Confirmation.Builder confirm = Confirmation.newBuilder();

        if (!json.equals("")) {
            //success
            System.out.println("Message received from: " + data.getSenderName());
            if (data.getSenderName().equals("central")) {
                if (this.name.equals("grpc-server-one")) {
                    ManagedChannel channelGrpcTwo = ManagedChannelBuilder.forAddress("grpc-server-two", 60020).usePlaintext().build();
                    ManagedChannel channelGrpcThree = ManagedChannelBuilder.forAddress("grpc-server-three", 60030).usePlaintext().build();
                    this.sendToGrpcTwo(json, channelGrpcTwo);
                    this.sendToGrpcThree(json, channelGrpcThree);
                    channelGrpcTwo.shutdown();
                    channelGrpcThree.shutdown();
                } else if (this.name.equals("grpc-server-two")) {
                    ManagedChannel channelGrpcOne = ManagedChannelBuilder.forAddress("grpc-server-one", 60010).usePlaintext().build();
                    ManagedChannel channelGrpcThree = ManagedChannelBuilder.forAddress("grpc-server-three", 60030).usePlaintext().build();
                    this.sendToGrpcOne(json, channelGrpcOne);
                    this.sendToGrpcThree(json, channelGrpcThree);
                    channelGrpcOne.shutdown();
                    channelGrpcThree.shutdown();
                } else if (this.name.equals("grpc-server-three")) {
                    ManagedChannel channelGrpcOne = ManagedChannelBuilder.forAddress("grpc-server-one", 60010).usePlaintext().build();
                    ManagedChannel channelGrpcTwo = ManagedChannelBuilder.forAddress("grpc-server-two", 60020).usePlaintext().build();
                    this.sendToGrpcOne(json, channelGrpcOne);
                    this.sendToGrpcTwo(json, channelGrpcTwo);
                    channelGrpcOne.shutdown();
                    channelGrpcTwo.shutdown();
                }
            }

            this.messageCounter = server.sendData(json, this.testMode);
            System.out.println(this.messageCounter);
            confirm.setConfirmationText("SUCCESS");

            System.out.println("Now active grpc server name: " + name);

            if (this.messageCounter == 1600) {
                this.numMessagesSavedInDb = server.getMessageCount();
                System.out.println("CentralService messageCounter: " + messageCounter);
                System.out.println("number of messages in table tank: " + this.numMessagesSavedInDb);

                Timestamp fts = server.getFirstMessage();
                Timestamp lts = server.getLastMessage();
                System.out.println("first Timestamp: " + fts);
                System.out.println("last Timestamp: " + lts);
            }

        } else {
            confirm.setConfirmationText("FAILURE; JSON STRING: " + json);
        }
        responseObserver.onNext(confirm.build());
        responseObserver.onCompleted();

        long end_time = System.currentTimeMillis();

        if(end_time - start_time >= 30000) {
            System.out.println("Sync time baby!");
            System.out.println("start_time: " + start_time);
            System.out.println("end_time: " + end_time);
            System.out.println("time diff: " + (end_time - start_time));
            getAllTankEntries();
        }
    }

    public void getAllTankEntries() {
        System.out.println("now in getAllTankEntries()");
        try {
            Connection con = connect();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            con.close();

            ManagedChannel channelGrpcOne = ManagedChannelBuilder.forAddress("grpc-server-one", 60010).usePlaintext().build();
            ManagedChannel channelGrpcTwo = ManagedChannelBuilder.forAddress("grpc-server-two", 60020).usePlaintext().build();
            ManagedChannel channelGrpcThree = ManagedChannelBuilder.forAddress("grpc-server-three", 60030).usePlaintext().build();

            while (rs.next()) {
                try {
                    setStartTime();
                    Thread.sleep(this.timeout);
                    String valueSet = rs.getString(2);
                    if (this.name.equals("grpc-server-one")) {
                        this.sendToGrpcTwo(valueSet, channelGrpcTwo);
                        this.sendToGrpcThree(valueSet, channelGrpcThree);
                    } else if (this.name.equals("grpc-server-two")) {
                        this.sendToGrpcOne(valueSet, channelGrpcOne);
                        this.sendToGrpcThree(valueSet, channelGrpcThree);
                    } else if (this.name.equals("grpc-server-three")) {
                        this.sendToGrpcOne(valueSet, channelGrpcOne);
                        this.sendToGrpcTwo(valueSet, channelGrpcTwo);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            channelGrpcOne.shutdown();
            channelGrpcTwo.shutdown();
            channelGrpcThree.shutdown();

        } catch (SQLException ex) {
            System.out.println("Error while getting tank entries from db: " + ex);
        }
    }
}
