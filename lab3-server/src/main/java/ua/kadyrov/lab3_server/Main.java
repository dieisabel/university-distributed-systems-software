package ua.kadyrov.lab3_server;

import javax.xml.ws.Endpoint;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Endpoint.publish("http://localhost:9000/plants", new PlantServiceImpl());
        Thread.sleep(1000 * 60 * 60);
        System.exit(0);
    }
}
