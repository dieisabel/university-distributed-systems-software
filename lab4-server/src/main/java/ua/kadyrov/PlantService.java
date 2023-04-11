package ua.kadyrov;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface PlantService extends Remote {
    List<Plant> fetchAllPlants() throws RemoteException;
}
