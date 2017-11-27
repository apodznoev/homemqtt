package de.avpod.home.mqtt;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.net.ssl.*;
import java.io.InputStream;
import java.security.KeyStore;

/**
 * Created by apodznoev
 * date 27.11.2017.
 */
@Component
public class MyMqttClient {
    private final Logger log = LoggerFactory.getLogger(MyMqttClient.class);

    public void start() throws Exception {
        MqttClientPersistence persistence = new MemoryPersistence();
        SSLSocketFactory ssf = configureSSLSocketFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setSocketFactory(ssf);
        MqttClient client = new MqttClient("ssl://0.0.0.0:8883", "SSLClientTest", persistence);

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
    }

    private SSLSocketFactory configureSSLSocketFactory() throws Exception {
        KeyStore ks = KeyStore.getInstance("JKS");
        InputStream jksInputStream = new ClassPathResource("mqttclientkeystore.jks").getInputStream();
        String jksPassword = System.getProperty("clientJksPass");
        if(jksPassword == null)
            throw new IllegalArgumentException("JKS password parameter 'clientJksPass' cannot be null");

        ks.load(jksInputStream, jksPassword.toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, jksPassword.toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);

        SSLContext sc = SSLContext.getInstance("TLS");
        TrustManager[] trustManagers = tmf.getTrustManagers();
        sc.init(kmf.getKeyManagers(), trustManagers, null);

        return sc.getSocketFactory();
    }
}
