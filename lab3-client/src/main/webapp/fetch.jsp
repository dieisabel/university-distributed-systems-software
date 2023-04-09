<%@ page contentType="text/html" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="plants" type="java.util.List<ua.kadyrov.lab3_client.Plant>" scope="request" />
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Fetch</title>
</head>
<body>
  <div class="table">
    <table>
      <thead>
        <tr>
          <th>Name</th>
          <th>Family</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach items="${plants}" var="plant">
          <tr>
            <td>
              ${plant.name}
            </td>
            <td>
              ${plant.family}
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </div>
</body>
</html>
