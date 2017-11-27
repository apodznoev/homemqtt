package de.avpod.home.mqtt;

import io.moquette.BrokerConstants;
import io.moquette.server.Server;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.mqtt.*;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * Created by apodznoev
 * date 27.11.2017.
 */
@Component
public class MyMqttServer {

    public void start() throws Exception {
        io.moquette.server.Server server = new Server();
        Properties sslProps = new Properties();
        sslProps.put(BrokerConstants.SSL_PORT_PROPERTY_NAME, "8883");
        sslProps.put(BrokerConstants.JKS_PATH_PROPERTY_NAME, "mqttserverkeystore.jks");
        String jksPassword = System.getProperty("serverJksPass");
        if(jksPassword == null)
            throw new IllegalArgumentException("JKS password parameter 'serverJksPass' cannot be null");

        sslProps.put(BrokerConstants.KEY_STORE_PASSWORD_PROPERTY_NAME, jksPassword);
        sslProps.put(BrokerConstants.KEY_MANAGER_PASSWORD_PROPERTY_NAME, jksPassword);
        server.startServer(sslProps);
        MqttFixedHeader header = new MqttFixedHeader(MqttMessageType.PUBLISH, false, MqttQoS.AT_LEAST_ONCE, false, 4);
        MqttPublishVariableHeader variableHeader = new MqttPublishVariableHeader("testTopic", 1);
        ByteBuf content = Unpooled.buffer();
        content.writeInt(42);
        content.writeInt(43);
        content.writeInt(44);
        content.writeInt(45);
        MqttPublishMessage message = new MqttPublishMessage(header,variableHeader, content);
        while (true) {
            server.internalPublish(message, "serverAsIs");
            Thread.sleep(5000);
        }
    }

}
