package org.tahakhamessi.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.tahakhamessi.dao.UtilisateurDAO;
import org.tahakhamessi.model.Utilisateur;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private UtilisateurDAO userDAO = new UtilisateurDAO();

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please fill in all fields");
            return;
        }

        Utilisateur user = userDAO.authenticate(username, password);
        if (user != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Dashboard.fxml"));
                loader.load();
                DashboardController controller = loader.getController();
                controller.setCurrentUser(user);
                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(new Scene(loader.getRoot(), 1200, 700));
                stage.setTitle("Car Rental Management");
            } catch (Exception e) {
                errorLabel.setText("Failed to load dashboard: " + e.getMessage());
            }
        } else {
            errorLabel.setText("Invalid credentials");
        }
    }
}

