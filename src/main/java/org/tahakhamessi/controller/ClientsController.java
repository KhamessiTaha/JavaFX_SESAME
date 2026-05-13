package org.tahakhamessi.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import org.tahakhamessi.dao.ClientDAO;
import org.tahakhamessi.dao.ReservationDAO;
import org.tahakhamessi.model.Client;
import org.tahakhamessi.model.Reservation;
import org.tahakhamessi.util.ValidationUtil;

public class ClientsController {
    @FXML private TableView<Client> clientTable;
    @FXML private TableColumn<Client, Number> idCol;
    @FXML private TableColumn<Client, String> nomCol, prenomCol, cinCol, emailCol, telephoneCol, adresseCol, permisCol, expirationCol;

    @FXML private TextField nomField, prenomField, cinField, emailField, telephoneField, adresseField,
                             numeroPermisField, expirationPermisField, searchField;
    @FXML private Label errorLabel, successLabel;
    @FXML private Button historyButton;

    private ClientDAO clientDAO = new ClientDAO();
    private ReservationDAO reservationDAO = new ReservationDAO();
    private Client selectedClient;

    @FXML
    public void initialize() {
        setupTableColumns();
        loadClients();
        historyButton.setDisable(true);
    }

    private void setupTableColumns() {
        idCol.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        nomCol.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        prenomCol.setCellValueFactory(cellData -> cellData.getValue().prenomProperty());
        cinCol.setCellValueFactory(cellData -> cellData.getValue().cinProperty());
        emailCol.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        telephoneCol.setCellValueFactory(cellData -> cellData.getValue().telephoneProperty());
        adresseCol.setCellValueFactory(cellData -> cellData.getValue().adresseProperty());
        permisCol.setCellValueFactory(cellData -> cellData.getValue().numeroPermisProperty());
        expirationCol.setCellValueFactory(cellData -> cellData.getValue().expirationPermisProperty());

        clientTable.setOnMouseClicked(event -> {
            Client c = clientTable.getSelectionModel().getSelectedItem();
            if (c != null) {
                selectedClient = c;
                populateForm(c);
                historyButton.setDisable(false);
            }
        });
    }

    private void loadClients() {
        clientTable.setItems(clientDAO.getAll());
    }

    @FXML
    public void handleAddClient() {
        errorLabel.setText("");
        successLabel.setText("");
        if (ValidationUtil.isRequiredFieldEmpty(nomField.getText()) ||
            ValidationUtil.isRequiredFieldEmpty(prenomField.getText()) ||
            ValidationUtil.isRequiredFieldEmpty(cinField.getText()) ||
            ValidationUtil.isRequiredFieldEmpty(emailField.getText())) {
            errorLabel.setText("All fields are required");
            return;
        }

        if (!ValidationUtil.isValidEmail(emailField.getText())) {
            errorLabel.setText("Invalid email format");
            return;
        }

        if (!ValidationUtil.isValidPhone(telephoneField.getText())) {
            errorLabel.setText("Invalid phone number");
            return;
        }

        if (ValidationUtil.isPermisExpired(expirationPermisField.getText())) {
            errorLabel.setText("Permis expired");
            return;
        }

        if (clientDAO.cinExists(cinField.getText(), -1)) {
            errorLabel.setText("CIN already exists");
            return;
        }

        try {
            Client c = new Client();
            c.setNom(nomField.getText());
            c.setPrenom(prenomField.getText());
            c.setCin(cinField.getText());
            c.setEmail(emailField.getText());
            c.setTelephone(telephoneField.getText());
            c.setAdresse(adresseField.getText());
            c.setNumeroPermis(numeroPermisField.getText());
            c.setExpirationPermis(expirationPermisField.getText());

            clientDAO.add(c);
            successLabel.setText("Client added successfully");
            clearForm();
            loadClients();
        } catch (Exception e) {
            errorLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void handleUpdateClient() {
        errorLabel.setText("");
        successLabel.setText("");
        if (selectedClient == null) {
            errorLabel.setText("Please select a client");
            return;
        }

        if (!ValidationUtil.isValidEmail(emailField.getText())) {
            errorLabel.setText("Invalid email format");
            return;
        }

        if (!ValidationUtil.isValidPhone(telephoneField.getText())) {
            errorLabel.setText("Invalid phone number");
            return;
        }

        if (ValidationUtil.isPermisExpired(expirationPermisField.getText())) {
            errorLabel.setText("Permis expired");
            return;
        }

        if (!selectedClient.getCin().equals(cinField.getText()) &&
            clientDAO.cinExists(cinField.getText(), selectedClient.getId())) {
            errorLabel.setText("CIN already exists");
            return;
        }

        try {
            selectedClient.setNom(nomField.getText());
            selectedClient.setPrenom(prenomField.getText());
            selectedClient.setCin(cinField.getText());
            selectedClient.setEmail(emailField.getText());
            selectedClient.setTelephone(telephoneField.getText());
            selectedClient.setAdresse(adresseField.getText());
            selectedClient.setNumeroPermis(numeroPermisField.getText());
            selectedClient.setExpirationPermis(expirationPermisField.getText());

            clientDAO.update(selectedClient);
            successLabel.setText("Client updated successfully");
            clearForm();
            loadClients();
        } catch (Exception e) {
            errorLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void handleDeleteClient() {
        errorLabel.setText("");
        successLabel.setText("");
        if (selectedClient == null) {
            errorLabel.setText("Please select a client");
            return;
        }

        try {
            clientDAO.delete(selectedClient.getId());
            successLabel.setText("Client deleted successfully");
            clearForm();
            loadClients();
        } catch (Exception e) {
            errorLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void handleSearch() {
        String query = searchField.getText();
        if (query.isEmpty()) {
            loadClients();
        } else {
            clientTable.setItems(clientDAO.search(query));
        }
    }

    @FXML
    public void handleViewHistory() {
        if (selectedClient != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Reservation History");
            StringBuilder sb = new StringBuilder();
            for (Reservation r : reservationDAO.getByClientId(selectedClient.getId())) {
                sb.append("ID: ").append(r.getId()).append(", Vehicle: ").append(r.getVehiculeNom()).append(", Dates: ").append(r.getDateDebut()).append(" to ").append(r.getDateFin()).append("\n");
            }
            alert.setContentText(sb.toString().isEmpty() ? "No reservations found" : sb.toString());
            alert.show();
        }
    }

    private void populateForm(Client c) {
        nomField.setText(c.getNom());
        prenomField.setText(c.getPrenom());
        cinField.setText(c.getCin());
        emailField.setText(c.getEmail());
        telephoneField.setText(c.getTelephone());
        adresseField.setText(c.getAdresse());
        numeroPermisField.setText(c.getNumeroPermis());
        expirationPermisField.setText(c.getExpirationPermis());
    }

    private void clearForm() {
        nomField.clear();
        prenomField.clear();
        cinField.clear();
        emailField.clear();
        telephoneField.clear();
        adresseField.clear();
        numeroPermisField.clear();
        expirationPermisField.clear();
        searchField.clear();
        selectedClient = null;
        clientTable.getSelectionModel().clearSelection();
    }
}

