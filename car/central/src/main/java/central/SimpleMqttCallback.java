/*
 Copyright (c) 2017, Michael Bredel, H-DA
 ALL RIGHTS RESERVED.
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
      http://www.apache.org/licenses/LICENSE-2.0
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 Neither the name of the H-DA and Michael Bredel
 nor the names of its contributors may be used to endorse or promote
 products derived from this software without specific prior written
 permission.
 */
package central;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import io.lettuce.core.*;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.json.JSONObject;


/**
 * A simple MQTT callback implementation that
 * logs (prints) information if the connection to the
 * broker got lost, when a message arrived, and
 * when the delivery was completed, i.e., when
 * a delivery token arrived.
 *
 * @author Michael Bredel
 */
public class SimpleMqttCallback implements MqttCallback {

    GrpcClient grpcClient = new GrpcClient();

    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Connection to MQTT broker lost!");
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        //System.out.println("Message received: " + new String(mqttMessage.getPayload()));

        try {

            String msg = new String(mqttMessage.getPayload());
            grpcClient.clientSendData(msg);

            JSONObject json = new JSONObject(msg);
            String sensortype = json.getString("sensortype");
            String ip = json.getString("ip");
            //System.out.println("!!!!!!!!!!!!!!!!!!" + ip);

            // Redis

            if(ip.equals("central-two")){
                RedisClient redisClient = RedisClient.create("redis://yourpassword@redis-two:6380/0");

                StatefulRedisConnection<String, String> connection = redisClient.connect();

                RedisCommands<String, String> syncCommands = connection.sync();

                syncCommands.lpush(sensortype, msg);
                connection.close();
                redisClient.shutdown();
                return;
            } else if (ip.equals("central-one")){
            RedisClient redisClient = RedisClient.create("redis://yourpassword@redis:6379/0");

            StatefulRedisConnection<String, String> connection = redisClient.connect();

            RedisCommands<String, String> syncCommands = connection.sync();

            syncCommands.lpush(sensortype, msg);
            connection.close();
            redisClient.shutdown();
            return;
            }

            //System.out.println("Hier wäre eigentlich unser Redis");

        } catch (Exception ex) {
            System.out.println("ERROR in Central:");
            System.out.println(ex);
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken mqttDeliveryToken) {
        try {
            System.out.println("Delivery completed: " + mqttDeliveryToken.getMessage());
        } catch (MqttException e) {
            System.out.println("Failed to get delivery token message: " + e.getMessage());
        }
    }
}