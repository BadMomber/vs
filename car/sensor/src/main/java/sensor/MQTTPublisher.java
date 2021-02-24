package sensor;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTTPublisher {

    private String broker;
    //private int port = "1883";

    /**
     * Default constructor that initializes
     * various class attributes.
     */
    public MQTTPublisher() {
        // Create the broker string from command line arguments.
        broker = "tcp://mosquitto:1883";
    }

    /**
     * Runs the MQTT client and publishes a message.
     */
    public void publishMessage(String sensortype, String sensorvalue) {

        // Create some MQTT connection options.
        MqttConnectOptions mqttConnectOpts = new MqttConnectOptions();
        mqttConnectOpts.setCleanSession(true);

        try {
            MqttClient client = new MqttClient(broker, MqttClient.generateClientId());

            // Connect to the MQTT broker using the connection options.
            client.connect(mqttConnectOpts);
            //System.out.println("Connected to MQTT broker: " + client.getServerURI());

            // Create the message and set a quality-of-service parameter.
            MqttMessage message = new MqttMessage(sensorvalue.getBytes());
            message.setQos(0);

            // Publish the message.
            client.publish(sensortype, message);
            //System.out.println("Published message: " + message);

            // Disconnect from the MQTT broker.
            client.disconnect();
            //System.out.println("Disconnected from MQTT broker.");

            // Exit the app explicitly.
            // System.exit(0);

        } catch (MqttException e) {
            //System.out.println("An error occurred: " + e.getMessage());
            System.out.println(e);
        }
    }
}
