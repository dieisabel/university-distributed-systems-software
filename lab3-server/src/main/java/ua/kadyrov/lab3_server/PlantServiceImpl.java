package ua.kadyrov.lab3_server;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebService(endpointInterface = "ua.kadyrov.lab3_server.PlantService")
public class PlantServiceImpl implements PlantService {

    @WebMethod
    public List<Plant> fetchAllPlants() {
        List<Plant> plants = new ArrayList<>();
        String connectionString = "jdbc:postgresql://172.17.0.2:5432/postgres";
        String user = "postgres";
        String password = "123";
        Connection connection;
        try {
            connection = DriverManager.getConnection(connectionString, user, password);
        } catch (SQLException exception) {
            exception.printStackTrace();
            return plants;
        }
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM plants");
            String name;
            String family;
            while (result.next()) {
                name = result.getString("name");
                family = result.getString("family");
                plants.add(new Plant(name, family));
            }
            result.close();
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
            return plants;
        }
        try {
            connection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return plants;
    }
}
