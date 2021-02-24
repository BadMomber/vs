package central;

import java.io.IOException;

public class main {

    public static synchronized void main(String[] args) throws ClassNotFoundException {
        System.out.println("Station started");


        //init args
        Central c = new Central();

        try {
            c.centralname = args[0];
            c.sensorcount = Integer.parseInt(args[1]);
            c.apiport = Integer.parseInt(args[2]);
            c.redisIp = args[3];
        } catch (ArrayIndexOutOfBoundsException e) {
            c.centralname = "Generic Central";
            c.sensorcount = 4;
            c.sensorstartport = 51020;
            c.apiport = 8080;
            c.redisIp = "localhost";
            System.out.println("Default to Generic Central with 1 Sensor at :51020 and REST API at :8080");
        }

        c.init(); //open threads that listen for udp packages from sensors

        //init web api
        try {
            String dburl;
            if (args[0].equals("Toyota")) {
                dburl = "redis://yourpassword@redis:6379/0";
            } else {
                dburl = "redis://yourpassword@redis-two:6380/0";
            }
            CarAPI api = new CarAPI(c.apiport,dburl , c.centralname);
            Thread t = new Thread(api);
            t.setName("REST API");
            t.start();
        } catch (IOException ex) {
            System.out.println("ERROR in API:");
            System.out.println(ex);
            return;
        }

    }

}