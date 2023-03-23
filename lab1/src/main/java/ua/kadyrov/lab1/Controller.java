package ua.kadyrov.lab1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private TableView<Plant> beforeScriptTable;

    @FXML
    private TableColumn<Plant, String> beforeScriptTableFamilyColumn;

    @FXML
    private TableColumn<Plant, String> beforeScriptTableNameColumn;

    @FXML
    private TableView<Plant> afterScriptTable;

    @FXML
    private TableColumn<Plant, String> afterScriptTableFamilyColumn;

    @FXML
    private TableColumn<Plant, String> afterScriptTableNameColumn;

    @FXML
    private TextArea scriptTextArea;

    @FXML
    private ChoiceBox<String> connectionTypeChoice;

    @FXML
    private Label errorLabel;

    private String connectionType;

    private final String user = "postgres";

    private final String password = "123";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        beforeScriptTableNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );
        beforeScriptTableFamilyColumn.setCellValueFactory(
                new PropertyValueFactory<>("family")
        );
        afterScriptTableNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );
        afterScriptTableFamilyColumn.setCellValueFactory(
                new PropertyValueFactory<>("family")
        );
        connectionTypeChoice.setItems(
                FXCollections.observableArrayList(
                        "ODBC", "JDBC"
                )
        );
    }

    @FXML
    void sendScript(ActionEvent event) {
        errorLabel.setText("");
        String connectionType = connectionTypeChoice.getValue();
        if (connectionType == null) {
            showError("Select connection type");
            return;
        }
        String script = scriptTextArea.getText();
        if (script.isEmpty()) {
            showError("Enter SQL script");
            return;
        }
        this.connectionType = connectionType;
        String connectionString = "";
        if (connectionType.equals("JDBC")) {
            connectionString = "jdbc:postgresql://172.17.0.2:5432/postgres";
        } else if (connectionType.equals("ODBC")) {
            connectionString = "jdbc:odbc://172.17.0.2:5432/postgres";
        }
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionString, user, password);
        } catch (SQLException exception) {
            exception.printStackTrace();
            showError("Unable to connect to database");
            return;
        }
        ObservableList<Plant> beforeScriptTableItems = FXCollections.observableArrayList(
                findAllPlants(connection)
        );
        beforeScriptTable.setItems(beforeScriptTableItems);
        executeScript(connection, script);
        ObservableList<Plant> afterScriptTableItems = FXCollections.observableArrayList(
                findAllPlants(connection)
        );
        afterScriptTable.setItems(afterScriptTableItems);
        try {
            connection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
            showError("Unable to close database connection");
        }
    }

    void showError(String message) {
        errorLabel.setText(message);
        beforeScriptTable.setItems(FXCollections.observableArrayList());
        afterScriptTable.setItems(FXCollections.observableArrayList());
    }

    List<Plant> findAllPlants(Connection connection) {
        List<Plant> plants = new ArrayList<>();
        if (connection != null) {
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
                statement.close();
            } catch (SQLException exception) {
                exception.printStackTrace();
                showError("Unable to find all plants");
            }
        }
        return plants;
    }

    void executeScript(Connection connection, String script) {
        try {
            Statement statement = connection.createStatement();
            statement.executeQuery(script);
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
            showError("Unable to execute script");
        }
    }
}