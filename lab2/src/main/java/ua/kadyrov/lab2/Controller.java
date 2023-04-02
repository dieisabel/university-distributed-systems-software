package ua.kadyrov.lab2;

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
    private TableView<Plant> afterTransactionMySqlTable;

    @FXML
    private TableColumn<Plant, String> afterTransactionMySqlTableFamilyColumn;

    @FXML
    private TableColumn<Plant, String> afterTransactionMySqlTableNameColumn;

    @FXML
    private TableView<Plant> afterTransactionPostgreSqlTable;

    @FXML
    private TableColumn<Plant, String> afterTransactionPostgreSqlTableFamilyColumn;

    @FXML
    private TableColumn<Plant, String> afterTransactionPostgreSqlTableNameColumn;

    @FXML
    private TableView<Plant> beforeTransactionMySqlTable;

    @FXML
    private TableColumn<Plant, String> beforeTransactionMySqlTableFamilyColumn;

    @FXML
    private TableColumn<Plant, String> beforeTransactionMySqlTableNameColumn;

    @FXML
    private TableColumn<Plant, String> beforeTransactionPostgreSqlTableFamilyColumn;

    @FXML
    private TableView<Plant> beforeTransactionPostgreSqlTable;

    @FXML
    private TableColumn<Plant, String> beforeTransactionPostgreSqlTableNameColumn;

    @FXML
    private ChoiceBox<String> databaseChoice;

    @FXML
    private Label errorLabel;

    @FXML
    private TextArea scriptTextArea;

    @FXML
    private Button sendButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        beforeTransactionPostgreSqlTableNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );
        beforeTransactionPostgreSqlTableFamilyColumn.setCellValueFactory(
                new PropertyValueFactory<>("family")
        );
        beforeTransactionMySqlTableNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );
        beforeTransactionMySqlTableFamilyColumn.setCellValueFactory(
                new PropertyValueFactory<>("family")
        );
        afterTransactionPostgreSqlTableNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );
        afterTransactionPostgreSqlTableFamilyColumn.setCellValueFactory(
                new PropertyValueFactory<>("family")
        );
        afterTransactionMySqlTableNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );
        afterTransactionMySqlTableFamilyColumn.setCellValueFactory(
                new PropertyValueFactory<>("family")
        );
        databaseChoice.setItems(
                FXCollections.observableArrayList(
                        "PostgreSQL", "MySQL"
                )
        );
    }

    @FXML
    void sendScript(ActionEvent event) {
        errorLabel.setText("");
        String script = scriptTextArea.getText();
        if (script.isEmpty()) {
            showError("Enter SQL script");
            return;
        }
        String database = databaseChoice.getValue();
        if (database == null) {
            showError("Select database");
            return;
        }
        final String postgreSqlConnectionString = "jdbc:postgresql://172.17.0.2:5432/postgres";
        final String postgreSqlUser = "postgres";
        final String postgreSqlPassword = "123";
        final String mySqlConnectionString = "jdbc:mysql://172.17.0.3:3306/plants";
        final String mySqlUser = "root";
        final String mySqlPassword = "123";
        Connection postgreSqlConnection = null;
        Connection mySqlConnection = null;
        try {
            postgreSqlConnection = DriverManager.getConnection(postgreSqlConnectionString, postgreSqlUser, postgreSqlPassword);
        } catch (SQLException exception) {
            exception.printStackTrace();
            showError("Unable to connect to PostgreSQL database");
            return;
        }
        try {
            mySqlConnection = DriverManager.getConnection(mySqlConnectionString, mySqlUser, mySqlPassword);
        } catch (SQLException exception) {
            exception.printStackTrace();
            showError("Unable to connect to MySQL database");
            return;
        }
        ObservableList<Plant> beforeTransactionPostgreSqlItems = FXCollections.observableArrayList(
                findAllPlants(postgreSqlConnection)
        );
        beforeTransactionPostgreSqlTable.setItems(beforeTransactionPostgreSqlItems);
        ObservableList<Plant> beforeTransactionMySqlItems = FXCollections.observableArrayList(
                findAllPlants(mySqlConnection)
        );
        beforeTransactionMySqlTable.setItems(beforeTransactionMySqlItems);
        if (database.equals("PostgreSQL")) {
            executeScript(postgreSqlConnection, script);
        } else if (database.equals("MySQL")) {
            executeScript(mySqlConnection, script);
        }
        ObservableList<Plant> afterTransactionPostgreSqlItems = FXCollections.observableArrayList(
                findAllPlants(postgreSqlConnection)
        );
        afterTransactionPostgreSqlTable.setItems(afterTransactionPostgreSqlItems);
        ObservableList<Plant> afterTransactionMySqlItems = FXCollections.observableArrayList(
                findAllPlants(mySqlConnection)
        );
        afterTransactionMySqlTable.setItems(afterTransactionMySqlItems);
        try {
            postgreSqlConnection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
            showError("Unable to close PostgreSQL database connection");
        }
        try {
            mySqlConnection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
            showError("Unable to close MySQL database connection");
        }
    }

    void showError(String message) {
        errorLabel.setText(message);
        beforeTransactionPostgreSqlTable.setItems(FXCollections.observableArrayList());
        beforeTransactionMySqlTable.setItems(FXCollections.observableArrayList());
        afterTransactionPostgreSqlTable.setItems(FXCollections.observableArrayList());
        afterTransactionMySqlTable.setItems(FXCollections.observableArrayList());
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
            statement.executeUpdate(script);
            statement.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
            showError("Unable to execute script");
        }
    }
}