package de.avpod.home.mqtt;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.*;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by apodznoev
 * date 26.11.2017.
 */
public class MainClient {
    private final static Logger log = LoggerFactory.getLogger(MainClient.class);

    public static void main(String[] args) throws IOException, MqttException, InterruptedException {
        MqttClientPersistence persistence = new MemoryPersistence();
        MqttClient client = new MqttClient("tcp://0.0.0.0:1883", "SSLClientTest", persistence);
        MqttConnectOptions options = new MqttConnectOptions();
        while (true) {
            try {
                client.connect(options);
                client.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable cause) {
                        log.info("Connection lost", cause);
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                        ByteBuf byteBuf = Unpooled.copiedBuffer(message.getPayload());
                        String res = "";
                        res += byteBuf.readInt();
                        res += byteBuf.readInt();
                        res += byteBuf.readInt();
                        res += byteBuf.readInt();
                        log.info("Message arrived {}, {}", topic, res);
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {
                        log.info("Delivery done", token);
                    }
                });
                client.subscribe("testTopic");
            } catch (MqttException e) {
                e.printStackTrace();
            }
            Thread.sleep(5000);
        }
    }
}
