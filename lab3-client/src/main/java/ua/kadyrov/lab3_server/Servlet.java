package ua.kadyrov.lab3_server;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

public class Servlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<Plant> plants;
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(PlantService.class);
        factory.setAddress("http://localhost:9000/plants?wsdl");
        PlantService service = (PlantService) factory.create();
        plants = service.fetchAllPlants();
        request.setAttribute("plants", plants);
        request.getRequestDispatcher("/fetch.jsp").forward(request, response);
    }
}
