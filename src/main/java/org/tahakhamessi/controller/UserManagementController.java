package org.tahakhamessi.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.tahakhamessi.dao.UtilisateurDAO;
import org.tahakhamessi.model.Utilisateur;

public class UserManagementController {

    @FXML private TableView<Utilisateur> userTable;
    @FXML private TableColumn<Utilisateur, Integer> idCol;
    @FXML private TableColumn<Utilisateur, String> usernameCol;
    @FXML private TableColumn<Utilisateur, String> roleCol;
    @FXML private TableColumn<Utilisateur, String> statusCol;

    private UtilisateurDAO userDAO = new UtilisateurDAO();
    private ObservableList<Utilisateur> userList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTable();
        loadUsers();
    }

    private void setupTable() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Custom cell factory for status to add color
        statusCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    switch (item) {
                        case "ACTIF": setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;"); break;
                        case "INACTIF": setStyle("-fx-text-fill: #F44336; -fx-font-weight: bold;"); break;
                        case "EN_ATTENTE": setStyle("-fx-text-fill: #FF9800; -fx-font-weight: bold;"); break;
                        default: setStyle("");
                    }
                }
            }
        });
    }

    private void loadUsers() {
        userList.setAll(userDAO.getAll());
        userTable.setItems(userList);
    }

    @FXML
    public void handleActivate() {
        Utilisateur selected = userTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (userDAO.updateStatus(selected.getId(), "ACTIF")) {
                loadUsers();
            }
        }
    }

    @FXML
    public void handleDeactivate() {
        Utilisateur selected = userTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (userDAO.updateStatus(selected.getId(), "INACTIF")) {
                loadUsers();
            }
        }
    }

    @FXML
    public void handleRefuse() {
        Utilisateur selected = userTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this registration request?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                if (userDAO.delete(selected.getId())) {
                    loadUsers();
                }
            }
        }
    }
}
