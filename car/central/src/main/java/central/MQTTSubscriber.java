package central;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MQTTSubscriber {

    public String broker;

    String[] topicsArray = {"traffic", "mileage", "mean_speed", "tank"};
    String[] topcisTwoArray = { "traffic_two", "mileage_two", "mean_speed_two", "tank_two"};
    int[] qosTwoArray = {0, 0, 0, 0};
    int[] qosArray = {0, 0, 0, 0};

    public MQTTSubscriber() {
        broker = "tcp://mosquitto:1883";
    }

    public void subscribe(String centralname) {
        try {
            if(centralname.equals("Mercedes")){
                //System.out.println("//////////////////////////////////IF CENTRAL TWO ");
                MqttClient client = new MqttClient(broker, MqttClient.generateClientId());
                client.setCallback(new SimpleMqttCallback());

                // Connect to the MQTT broker.
                client.connect();
                System.out.println("Connected to MQTT broker: " + client.getServerURI());

                // Subscribe all topics.
                client.subscribe(topcisTwoArray, qosTwoArray);
                //System.out.println("Subscribed to topics: " + client.getTopic("traffic"));
                //System.out.println("Subscribed to topics: " + client.getTopic("mileage"));
                //System.out.println("Subscribed to topics: " + client.getTopic("mean_speed"));
                //System.out.println("Subscribed to topics: " + client.getTopic("tank"));
            }
            if(centralname.equals("Toyota")){
                //System.out.println("//////////////////////////////////IF CENTRAL ONE ");
            MqttClient client = new MqttClient(broker, MqttClient.generateClientId());
            client.setCallback(new SimpleMqttCallback());

            // Connect to the MQTT broker.
            client.connect();
            System.out.println("Connected to MQTT broker: " + client.getServerURI());

            // Subscribe all topics.
            client.subscribe(topicsArray, qosArray);
            //System.out.println("Subscribed to topics: " + client.getTopic("traffic"));
            //System.out.println("Subscribed to topics: " + client.getTopic("mileage"));
            //System.out.println("Subscribed to topics: " + client.getTopic("mean_speed"));
            //System.out.println("Subscribed to topics: " + client.getTopic("tank"));
            }
        } catch (MqttException e) {
            System.out.println(e);
        }
    }
}
