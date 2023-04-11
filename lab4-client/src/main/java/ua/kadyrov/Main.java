package ua.kadyrov;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class Main  {
    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(10000);
        tomcat.getConnector();
        tomcat.addWebapp("/", new File("src/main/webapp").getAbsolutePath());
        tomcat.start();
        tomcat.getServer().await();
    }
}