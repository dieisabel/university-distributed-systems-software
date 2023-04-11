package ua.kadyrov;

import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.Registry;

public class Main  {
    public static void main(String[] args) {
        try {
            PlantServiceImpl plantServiceImplementation = new PlantServiceImpl();
            PlantService stub = (PlantService) UnicastRemoteObject.exportObject(plantServiceImplementation, 0);
            Registry registry = LocateRegistry.createRegistry(12000);
            registry.bind("plantService", stub);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
