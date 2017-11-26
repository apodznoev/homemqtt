package de.avpod.home.mqtt;

import io.moquette.server.Server;
import io.moquette.server.config.ClasspathResourceLoader;
import io.moquette.server.config.ResourceLoaderConfig;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.mqtt.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

/**
 * Created by apodznoev
 * date 26.11.2017.
 */
@SpringBootApplication
public class MainServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        io.moquette.server.Server server = new Server();
        server.startServer(new ResourceLoaderConfig(new ClasspathResourceLoader()));
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
