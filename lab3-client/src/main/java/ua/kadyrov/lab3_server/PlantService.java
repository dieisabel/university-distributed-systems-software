package ua.kadyrov.lab3_server;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface PlantService {

    @WebMethod
    List<Plant> fetchAllPlants();
}
