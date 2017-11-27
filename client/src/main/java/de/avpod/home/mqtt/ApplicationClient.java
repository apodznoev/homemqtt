package de.avpod.home.mqtt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by apodznoev
 * date 26.11.2017.
 */
@SpringBootApplication
public class ApplicationClient {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ApplicationClient.class, args);
        MyMqttClient client = new MyMqttClient();
        client.start();
    }


}
