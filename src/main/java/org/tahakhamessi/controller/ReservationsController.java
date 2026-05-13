package org.tahakhamessi.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import org.tahakhamessi.dao.ReservationDAO;
import org.tahakhamessi.dao.ClientDAO;
import org.tahakhamessi.dao.VehiculeDAO;
import org.tahakhamessi.model.Reservation;
import org.tahakhamessi.model.Client;
import org.tahakhamessi.model.Utilisateur;
import org.tahakhamessi.model.Vehicule;
import org.tahakhamessi.util.ValidationUtil;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReservationsController {
    @FXML private TableView<Reservation> reservationTable;
    @FXML private TableColumn<Reservation, Number> idCol, joursCol;
    @FXML private TableColumn<Reservation, String> clientNomCol, vehiculeNomCol, dateDebCol, dateFinCol, statutCol;
    @FXML private TableColumn<Reservation, Number> prixCol;

    @FXML private ComboBox<Client> clientCombo;
    @FXML private ComboBox<Vehicule> vehiculeCombo;
    @FXML private DatePicker dateDebut, dateFin;
    @FXML private TextField searchField;
    @FXML private TextField optionsField, prixOptionsField;
    @FXML private ComboBox<String> statutCombo;
    @FXML private Label errorLabel, successLabel, prixTotalLabel;
    @FXML private VBox formSection;
    @FXML private Button btnAdd, btnUpdate, btnDelete;

    private ReservationDAO reservationDAO = new ReservationDAO();
    private ClientDAO clientDAO = new ClientDAO();
    private VehiculeDAO vehiculeDAO = new VehiculeDAO();
    private Reservation selectedReservation;
    private Utilisateur currentUser;

    public void setCurrentUser(Utilisateur user) {
        this.currentUser = user;
        if (currentUser != null && "agent".equalsIgnoreCase(currentUser.getRole())) {
            btnDelete.setVisible(false);
            btnDelete.setManaged(false);
        }
    }

    @FXML
    public void initialize() {
        setupTableColumns();
        loadReservations();
        clientCombo.setItems(clientDAO.getAll());
        vehiculeCombo.setItems(vehiculeDAO.getAll());
        statutCombo.setItems(FXCollections.observableArrayList("En attente", "Confirmée", "Annulée", "Terminée"));
    }

    private void setupTableColumns() {
        idCol.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        clientNomCol.setCellValueFactory(cellData -> cellData.getValue().clientNomProperty());
        vehiculeNomCol.setCellValueFactory(cellData -> cellData.getValue().vehiculeNomProperty());
        dateDebCol.setCellValueFactory(cellData -> cellData.getValue().dateDebutProperty());
        dateFinCol.setCellValueFactory(cellData -> cellData.getValue().dateFinProperty());
        joursCol.setCellValueFactory(cellData -> cellData.getValue().nombreJoursProperty());
        prixCol.setCellValueFactory(cellData -> cellData.getValue().prixTotalProperty());
        statutCol.setCellValueFactory(cellData -> cellData.getValue().statutProperty());

        reservationTable.setOnMouseClicked(event -> {
            Reservation r = reservationTable.getSelectionModel().getSelectedItem();
            if (r != null) {
                selectedReservation = r;
                populateForm(r);
            }
        });
    }

    private void loadReservations() {
        reservationTable.setItems(reservationDAO.getAll());
    }

    @FXML
    public void handleAddReservation() {
        errorLabel.setText("");
        successLabel.setText("");

        if (clientCombo.getValue() == null || vehiculeCombo.getValue() == null ||
            dateDebut.getValue() == null || dateFin.getValue() == null) {
            errorLabel.setText("All fields are required");
            return;
        }

        String startDate = dateDebut.getValue().toString();
        String endDate = dateFin.getValue().toString();

        if (!ValidationUtil.isEndDateValid(startDate, endDate)) {
            errorLabel.setText("End date must be after or equal to start date");
            return;
        }

        Vehicule v = vehiculeCombo.getValue();
        if (!"Disponible".equals(v.getStatut())) {
            errorLabel.setText("Vehicle is not available");
            return;
        }

        if (reservationDAO.hasOverlappingReservation(v.getId(), startDate, endDate, -1)) {
            errorLabel.setText("Vehicle has overlapping reservation");
            return;
        }

        try {
            int days = ValidationUtil.calculateDays(startDate, endDate);
            double prixOptions = prixOptionsField.getText().isEmpty() ? 0 : Double.parseDouble(prixOptionsField.getText());
            double totalPrice = days * v.getPrixParJour() + prixOptions;
            prixTotalLabel.setText(String.format("Total: %.3f TND", totalPrice));

            Reservation r = new Reservation();
            r.setClientId(clientCombo.getValue().getId());
            r.setVehiculeId(v.getId());
            r.setDateDebut(startDate);
            r.setDateFin(endDate);
            r.setNombreJours(days);
            r.setPrixTotal(totalPrice);
            r.setStatut("En attente");
            r.setOptions(optionsField.getText());
            r.setPrixOptions(prixOptions);

            reservationDAO.add(r);
            // Update vehicle status
            v.setStatut("Louée");
            vehiculeDAO.update(v);

            successLabel.setText("Reservation created successfully");
            clearForm();
            loadReservations();
        } catch (Exception e) {
            errorLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void handleUpdateReservation() {
        errorLabel.setText("");
        successLabel.setText("");
        if (selectedReservation == null) {
            errorLabel.setText("Please select a reservation");
            return;
        }

        String startDate = dateDebut.getValue().toString();
        String endDate = dateFin.getValue().toString();

        if (!ValidationUtil.isEndDateValid(startDate, endDate)) {
            errorLabel.setText("End date must be after or equal to start date");
            return;
        }

        try {
            int days = ValidationUtil.calculateDays(startDate, endDate);
            Vehicule v = vehiculeCombo.getValue();
            double prixOptions = prixOptionsField.getText().isEmpty() ? 0 : Double.parseDouble(prixOptionsField.getText());
            double totalPrice = days * v.getPrixParJour() + prixOptions;
            prixTotalLabel.setText(String.format("Total: %.3f TND", totalPrice));

            selectedReservation.setClientId(clientCombo.getValue().getId());
            selectedReservation.setVehiculeId(v.getId());
            selectedReservation.setDateDebut(startDate);
            selectedReservation.setDateFin(endDate);
            selectedReservation.setNombreJours(days);
            selectedReservation.setPrixTotal(totalPrice);
            selectedReservation.setStatut(statutCombo.getValue());
            selectedReservation.setOptions(optionsField.getText());
            selectedReservation.setPrixOptions(prixOptions);

            reservationDAO.update(selectedReservation);
            
            // Update vehicle status if reservation is confirmed or finished
            Vehicule vSelected = vehiculeCombo.getValue();
            if ("Confirmée".equals(selectedReservation.getStatut())) {
                vSelected.setStatut("Louée");
                vehiculeDAO.update(vSelected);
            } else if ("Terminée".equals(selectedReservation.getStatut()) || "Annulée".equals(selectedReservation.getStatut())) {
                vSelected.setStatut("Disponible");
                vehiculeDAO.update(vSelected);
            }

            successLabel.setText("Reservation updated successfully");
            clearForm();
            loadReservations();
        } catch (Exception e) {
            errorLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void handleDeleteReservation() {
        errorLabel.setText("");
        successLabel.setText("");
        if (selectedReservation == null) {
            errorLabel.setText("Please select a reservation");
            return;
        }

        try {
            reservationDAO.delete(selectedReservation.getId());
            successLabel.setText("Reservation deleted successfully");
            clearForm();
            loadReservations();
        } catch (Exception e) {
            errorLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void handleCalculateDays() {
        if (dateDebut.getValue() != null && dateFin.getValue() != null) {
            String startDate = dateDebut.getValue().toString();
            String endDate = dateFin.getValue().toString();
            int days = ValidationUtil.calculateDays(startDate, endDate);

            if (vehiculeCombo.getValue() != null) {
                double prix = vehiculeCombo.getValue().getPrixParJour();
                double total = days * prix;
                prixTotalLabel.setText("Jours: " + days + ", Prix: " + total);
            }
        }
    }

    @FXML
    public void handleSearch() {
        String query = searchField.getText();
        if (query.isEmpty()) {
            loadReservations();
        } else {
            reservationTable.setItems(reservationDAO.search(query));
        }
    }

    private void populateForm(Reservation r) {
        clientCombo.setValue(clientDAO.getById(r.getClientId()));
        vehiculeCombo.setValue(vehiculeDAO.getById(r.getVehiculeId()));
        dateFin.setValue(java.time.LocalDate.parse(r.getDateFin()));
        dateDebut.setValue(java.time.LocalDate.parse(r.getDateDebut()));
        statutCombo.setValue(r.getStatut());
        optionsField.setText(r.getOptions());
        prixOptionsField.setText(String.valueOf(r.getPrixOptions()));
        prixTotalLabel.setText(String.format("Total: %.3f TND", r.getPrixTotal()));
    }

    private void clearForm() {
        clientCombo.getSelectionModel().clearSelection();
        vehiculeCombo.getSelectionModel().clearSelection();
        dateDebut.setValue(null);
        dateFin.setValue(null);
        searchField.clear();
        optionsField.clear();
        prixOptionsField.clear();
        selectedReservation = null;
        reservationTable.getSelectionModel().clearSelection();
    }
}

