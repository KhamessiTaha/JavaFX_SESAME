package org.tahakhamessi.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.tahakhamessi.dao.UtilisateurDAO;
import org.tahakhamessi.model.Utilisateur;

public class RegisterController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private ComboBox<String> roleCombo;
    @FXML private Label statusLabel;

    private UtilisateurDAO userDAO = new UtilisateurDAO();

    @FXML
    public void initialize() {
        roleCombo.getItems().addAll("Agent", "Admin");
        roleCombo.setValue("Agent");
    }

    @FXML
    public void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String role = roleCombo.getValue().toLowerCase();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            statusLabel.setText("Please fill in all fields");
            statusLabel.setStyle("-fx-text-fill: #F44336;");
            return;
        }

        if (!password.equals(confirmPassword)) {
            statusLabel.setText("Passwords do not match");
            statusLabel.setStyle("-fx-text-fill: #F44336;");
            return;
        }

        Utilisateur newUser = new Utilisateur(username, password, role, "EN_ATTENTE");
        if (userDAO.register(newUser)) {
            statusLabel.setText("Registration successful! Waiting for admin approval.");
            statusLabel.setStyle("-fx-text-fill: #4CAF50;");
            // Clear fields
            usernameField.clear();
            passwordField.clear();
            confirmPasswordField.clear();
        } else {
            statusLabel.setText("Registration failed. Username may already exist.");
            statusLabel.setStyle("-fx-text-fill: #F44336;");
        }
    }

    @FXML
    public void handleBackToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Login.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 1200, 700));
            stage.setTitle("Login");
        } catch (Exception e) {
            statusLabel.setText("Error returning to login: " + e.getMessage());
        }
    }
}
