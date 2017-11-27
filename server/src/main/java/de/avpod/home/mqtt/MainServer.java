package de.avpod.home.mqtt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by apodznoev
 * date 26.11.2017.
 */
@SpringBootApplication
public class MainServer {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MainServer.class, args);
        MyMqttServer myMqttServer = new MyMqttServer();
        myMqttServer.start();
    }


}
