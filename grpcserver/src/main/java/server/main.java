package server;

import java.sql.*;

public class main {
    public static void main(String[] args) {

        Boolean testMode = false;

        if (args.length == 3 && args[2].equals("test")) {
            testMode = true;
        }

        System.out.println("Started grpc: " + args[1]);

        System.out.println("Server started!");

        Server s = new Server(testMode, args);

        s.init();
    }
}
