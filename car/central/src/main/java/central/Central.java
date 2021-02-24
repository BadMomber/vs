package central;

//import java.io.IOException;

public class Central {
    
    //params
    public String centralname;
    public int sensorcount;
    public int sensorstartport;
    public int apiport;
    public String redisIp;

    public MQTTSubscriber mqttSub = new MQTTSubscriber();

    // mqtt
    public void init() {
        try {
            mqttSub.subscribe(centralname);
        } catch(Exception e) {
            System.out.println("ERROR in Central:");
            System.out.println(e);
        }
    }
}
