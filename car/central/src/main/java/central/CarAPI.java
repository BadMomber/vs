package central;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;

import io.lettuce.core.*;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CarAPI implements Runnable {

    private ServerSocket socket;
    private int port;
    private String dburl;
    private String centralname;

    public CarAPI(int port, String dburl, String centralname) throws IOException {
        this.dburl = dburl;
        this.centralname = centralname;
        this.port = port;
        this.socket = new ServerSocket(this.port);
    }

    private String writeHttpHeader(String status, String length) {
        String httpHeader = "HTTP/1.1 " + status + "\r\n";
        httpHeader += "Content-Length: " + length + "\r\n";
        httpHeader += "Access-Control-Allow-Origin: *\r\n";
        httpHeader += "Content-Type: application/json\r\n\r\n";

        return httpHeader;
    }

    private void restGetSensorMeta(Socket connection, DataOutputStream httpOut, HTTPRequest httpReq) throws IOException {
        try {
            RedisClient redisClient = RedisClient.create(dburl);

            StatefulRedisConnection<String, String> redisConnection = redisClient.connect();

            RedisCommands<String, String> syncCommands = redisConnection.sync();

            List<String> resultList = new ArrayList<String>();

            if(dburl.equals("redis://yourpassword@redis:6379/0")) {
                resultList = syncCommands.lrange("tank", 0, 0);
                resultList.add(syncCommands.lrange("mean_speed", 0, 0).get(0));
                resultList.add(syncCommands.lrange("mileage", 0, 0).get(0));
                resultList.add(syncCommands.lrange("traffic", 0, 0).get(0));
            } else {
                resultList = syncCommands.lrange("tank_two", 0, 0);
                resultList.add(syncCommands.lrange("mean_speed_two", 0, 0).get(0));
                resultList.add(syncCommands.lrange("mileage_two", 0, 0).get(0));
                resultList.add(syncCommands.lrange("traffic_two", 0, 0).get(0));
            }

            redisConnection.close();
            redisClient.shutdown();

            JSONArray json = new JSONArray();
            for (String strTemp : resultList) {

                try {
                    JSONObject jsonObject = new JSONObject(strTemp);
                    json.put(jsonObject);
                } catch (JSONException err) {
                    System.out.println("Error: " + err.toString());
                }
            }

            if (json.length() == 0) { //No Sensors found or no values
                String header = this.writeHttpHeader("404 Not Found", "0");
                httpOut.writeBytes(header);
                return;
            } else { //Sensors found
                String body = json.toString();
                String header = this.writeHttpHeader("200 Ok", Integer.toString(body.length()));
                httpOut.writeBytes(header.concat(body));
                return;
            }

        } catch (Exception ex) {
            String header = this.writeHttpHeader("500 Internal Server Error", "0");
            System.out.println(ex.getMessage());
            httpOut.writeBytes(header);
            return;
        }
    }


    private void restGetSensorValues(Socket connection, DataOutputStream httpOut, HTTPRequest httpReq, int limit) throws IOException {
        try {
            String sensortype = httpReq.path.split("/")[2].replaceAll("%20", " ");

            System.out.println("dburl: " + dburl);
            RedisClient redisClient = RedisClient.create(dburl);

            StatefulRedisConnection<String, String> redisConnection = redisClient.connect();
            RedisCommands<String, String> syncCommands = redisConnection.sync();

            List<String> resultList = syncCommands.lrange(sensortype, 0, -1);

            System.out.println("resultList:");
            System.out.println(resultList);

            redisConnection.close();
            redisClient.shutdown();

            JSONArray json = new JSONArray();
            for (String strTemp : resultList) {
                try {
                    JSONObject jsonObject = new JSONObject(strTemp);
                    json.put(jsonObject);
                } catch (JSONException err) {
                    System.out.println("Error: " + err.toString());
                }
            }

            if (json.length() == 0) { //Sensor not found or no values
                String header = this.writeHttpHeader("404 Not Found", "0");
                httpOut.writeBytes(header);
                return;
            } else { //Sensor found values
                String body = json.toString();
                String header = this.writeHttpHeader("200 Ok", Integer.toString(body.length()));
                httpOut.writeBytes(header.concat(body));
                return;
            }

        } catch (Exception ex) {
            String header = this.writeHttpHeader("500 Internal Server Error", "0");
            System.out.println(ex.getMessage());
            httpOut.writeBytes(header);
            return;
        }
    }


    public void run() {
        System.out.println("API running");
        boolean error = false;
        while (!error) {

            try {

                Socket connection = this.socket.accept();
                BufferedReader httpIn = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                DataOutputStream httpOut = new DataOutputStream(connection.getOutputStream());

                HTTPRequest httpReq = new HTTPRequest(httpIn);

                if (httpReq.method.equals("GET")) {

                    if (httpReq.path.equals("/")) {
                        String body = null;
                        try {
                            body = (new JSONObject()).put("centralname", this.centralname).toString();
                        } catch (JSONException e) {
                            System.out.println(e);
                        }
                        String header = this.writeHttpHeader("200 Ok", Integer.toString(body.length()));
                        httpOut.writeBytes(header.concat(body));
                    } else if (httpReq.path.equals("/sensors/")) {

                        this.restGetSensorMeta(connection, httpOut, httpReq);

                    } else if ((boolean) Pattern.matches("\\/sensors\\/.*\\/.*\\/$", httpReq.path)) { //get n values of this sensor

                        if ((boolean) Pattern.matches("\\/sensors\\/.*\\/[0-9]{1,}\\/$", httpReq.path)) { //catch bad request
                            this.restGetSensorValues(connection, httpOut, httpReq, Integer.parseInt(httpReq.path.split("/")[3]));
                        } else {
                            String header = this.writeHttpHeader("401 Bad Request", "0");
                            httpOut.writeBytes(header);
                        }

                    } else if ((boolean) Pattern.matches("\\/sensors\\/.*\\/$", httpReq.path)) {  //get all values of this sensor

                        this.restGetSensorValues(connection, httpOut, httpReq, -1);

                    } else {
                        String header = this.writeHttpHeader("404 Not Found", "0");
                        httpOut.writeBytes(header);
                    }

                } else if (!httpReq.method.equals("GET")) { //Method is not GET
                    String header = this.writeHttpHeader("501 Not Implemented", "0");
                    httpOut.writeBytes(header);
                }

                connection.close();

            } catch (IOException ex) {
                error = true;
            }
        }
    }
}


