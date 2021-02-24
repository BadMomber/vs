package sensor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class main {

    public static void main(String[] args) {

        Sensor s = new Sensor();
        try {
            s.sensortype = args[0];
            s.ip = args[2];
            s.port = args[3];

            if(args.length == 5 && args[4].equals("test")) {
                System.out.println("Testrun detected!");
                s.testMode = true;
            } else {
                s.testMode = false;
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            s.sensortype = "Generic Sensor";
            System.out.println("Defaulting to 127.0.0.1:51020 [Generic Sensor]");
        }
        s.publisher = new MQTTPublisher();
        s.run();
    }

}

class Sensor {
    // params
    public Integer messageNumber = 1;
    public String sensortype;
    double prevValue = 0.0;
    public String ip;
    public String port;
    public Boolean testMode = false;
    public int messageCounter;
    public Boolean keepRunning = true;
    public int timeout;

    public String uuid = UUID.randomUUID().toString().replace("-", "");

    public MQTTPublisher publisher;

    public String buildJsonDataString(String sensortype, String msg, String ip, String port) {
        JSONObject obj = new JSONObject();
        try {
            DateFormat simple = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");
            Date result = new Date(System.currentTimeMillis());

            obj.put("run_uuid", uuid);
            obj.put("messageNumber", messageNumber++);
            obj.put("timestamp", simple.format(result));
            obj.put("sensortype", sensortype);
            obj.put("value", msg);
            obj.put("port", port);
            obj.put("ip", ip);
        } catch (JSONException e) {
            System.out.println(e);
        }
        String jsonText = obj.toString();
        return jsonText;
    }

    public void run() {
        Random rand = new Random();
        String[] verkehr = {"free", "moderate_traffic", "heavy_traffic", "jam"};

        if(testMode) {
            this.timeout = 250;
        } else {
            this.timeout = 15000;
        }

        while (keepRunning == true) {
            try {
                Thread.sleep(this.timeout);
                System.out.println(" -------------------- !!! Sensor send message !!! -------------------- ");
                String sensorvalue;
                switch (this.sensortype) {
                    default:
                        sensorvalue = Integer.toString(rand.nextInt(101) - 50); //-50 to 50
                        break;
                    case "tank":
                        if (prevValue == 0.0) {
                            prevValue = 100.0;
                        }
                        prevValue -= 0.01;
                        sensorvalue = String.valueOf(prevValue);
                        break;
                    case "mean_speed":
                        if (prevValue == 0.0) {
                            prevValue = 20.0;
                        }
                        prevValue = prevValue + rand.nextDouble();
                        sensorvalue = String.valueOf(prevValue);
                        break;
                    case "mileage":
                        prevValue = prevValue + 1;
                        sensorvalue = String.valueOf(prevValue);
                        break;
                    case "traffic":
                        sensorvalue = verkehr[rand.nextInt(3)];
                        break;
                    case "tank_two":
                        if (prevValue == 0.0) {
                            prevValue = 100.0;
                        }
                        prevValue -= 0.01;
                        sensorvalue = String.valueOf(prevValue);
                        break;
                    case "mean_speed_two":
                        if (prevValue == 0.0) {
                            prevValue = 20.0;
                        }
                        prevValue = prevValue + rand.nextDouble();
                        sensorvalue = String.valueOf(prevValue);
                        break;
                    case "mileage_two":
                        prevValue = prevValue + 1;
                        sensorvalue = String.valueOf(prevValue);
                        break;
                    case "traffic_two":
                        sensorvalue = verkehr[rand.nextInt(3)];
                        break;
                }

                //System.out.println("Send message: " + msg)
                publisher.publishMessage(this.sensortype, this.buildJsonDataString(this.sensortype, sensorvalue, this.ip, this.port));
                this.messageCounter++;

                if(testMode && this.messageCounter == 600) {
                    this.keepRunning = false;
                    System.out.println(sensortype + " is done!");
                }
            } catch (InterruptedException ex) {
                return;
            }
        }
    }
}