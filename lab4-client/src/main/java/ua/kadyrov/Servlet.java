package ua.kadyrov;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class Servlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Registry registry = LocateRegistry.getRegistry("localhost", 12000);
        PlantService service;
        try {
            service = (PlantService) registry.lookup("plantService");
        } catch (NotBoundException exception) {
            exception.printStackTrace();
            return;
        }
        List<Plant> plants = service.fetchAllPlants();
        request.setAttribute("plants", plants);
        request.getRequestDispatcher("/fetch.jsp").forward(request, response);
    }
}