package org.tahakhamessi.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.tahakhamessi.model.Utilisateur;

public class DashboardController {
    @FXML private BorderPane mainPane;
    private Utilisateur currentUser;

    public void setCurrentUser(Utilisateur user) {
        this.currentUser = user;
    }

    @FXML
    public void initialize() {
    }

    @FXML
    public void handleVehicles() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Vehicles.fxml"));
            VBox content = loader.load();
            mainPane.setCenter(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleClients() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Clients.fxml"));
            VBox content = loader.load();
            mainPane.setCenter(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleReservations() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Reservations.fxml"));
            VBox content = loader.load();
            mainPane.setCenter(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handlePayments() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Payments.fxml"));
            VBox content = loader.load();
            mainPane.setCenter(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleReturns() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Returns.fxml"));
            VBox content = loader.load();
            mainPane.setCenter(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleStatistics() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Statistics.fxml"));
            VBox content = loader.load();
            mainPane.setCenter(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            javafx.stage.Stage stage = (javafx.stage.Stage) mainPane.getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(loader.load(), 600, 400));
            stage.setTitle("Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

