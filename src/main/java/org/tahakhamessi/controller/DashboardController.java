package org.tahakhamessi.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.tahakhamessi.model.Utilisateur;

import java.util.ArrayList;
import java.util.List;

public class DashboardController {
    @FXML private BorderPane mainPane;
    @FXML private Label userLabel;
    @FXML private Label roleLabel;
    @FXML private Button btnStatistics;
    @FXML private Button btnVehicles;
    @FXML private Button btnClients;
    @FXML private Button btnReservations;
    @FXML private Button btnPayments;
    @FXML private Button btnReturns;
    @FXML private Button btnUsers;

    private Utilisateur currentUser;
    private List<Button> sidebarButtons;

    public void setCurrentUser(Utilisateur user) {
        this.currentUser = user;
        updateUIBasedOnRole();
    }

    @FXML
    public void initialize() {
        sidebarButtons = new ArrayList<>(List.of(btnStatistics, btnVehicles, btnClients, btnReservations, btnPayments, btnReturns));
        if (btnUsers != null) sidebarButtons.add(btnUsers);
    }

    private void updateUIBasedOnRole() {
        if (currentUser == null) return;

        userLabel.setText("User: " + currentUser.getUsername());
        roleLabel.setText("Role: " + currentUser.getRole().toUpperCase());

        if ("agent".equalsIgnoreCase(currentUser.getRole())) {
            btnStatistics.setText("Operational Dashboard");
            btnPayments.setVisible(false);
            btnPayments.setManaged(false);
            btnUsers.setVisible(false);
            btnUsers.setManaged(false);
            
            handleAgentDashboard();
        } else {
            handleStatistics();
        }
    }

    private void setActiveButton(Button activeButton) {
        for (Button btn : sidebarButtons) {
            btn.getStyleClass().remove("sidebar-button-active");
        }
        if (activeButton != null) {
            activeButton.getStyleClass().add("sidebar-button-active");
        }
    }

    @FXML
    public void handleVehicles() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Vehicles.fxml"));
            VBox content = loader.load();
            VehiclesController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            mainPane.setCenter(content);
            setActiveButton(btnVehicles);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleClients() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Clients.fxml"));
            VBox content = loader.load();
            ClientsController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            mainPane.setCenter(content);
            setActiveButton(btnClients);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleReservations() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Reservations.fxml"));
            VBox content = loader.load();
            ReservationsController controller = loader.getController();
            controller.setCurrentUser(currentUser);
            mainPane.setCenter(content);
            setActiveButton(btnReservations);
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
            setActiveButton(btnPayments);
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
            setActiveButton(btnReturns);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAgentDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AgentDashboard.fxml"));
            VBox content = loader.load();
            mainPane.setCenter(content);
            setActiveButton(btnStatistics);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleStatistics() {
        if (currentUser != null && "agent".equalsIgnoreCase(currentUser.getRole())) {
            handleAgentDashboard();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Statistics.fxml"));
            VBox content = loader.load();
            mainPane.setCenter(content);
            setActiveButton(btnStatistics);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleUserManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/UserManagement.fxml"));
            VBox content = loader.load();
            mainPane.setCenter(content);
            setActiveButton(btnUsers);
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

