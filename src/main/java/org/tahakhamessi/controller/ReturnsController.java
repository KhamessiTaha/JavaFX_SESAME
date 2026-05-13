package org.tahakhamessi.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import org.tahakhamessi.dao.RetourDAO;
import org.tahakhamessi.dao.ReservationDAO;
import org.tahakhamessi.dao.VehiculeDAO;
import org.tahakhamessi.model.Retour;
import org.tahakhamessi.model.Reservation;
import org.tahakhamessi.model.Vehicule;

public class ReturnsController {
    @FXML private TableView<Retour> retourTable;
    @FXML private TableColumn<Retour, Number> idCol, resIdCol;
    @FXML private TableColumn<Retour, String> dateRetourCol, etatCol;
    @FXML private TableColumn<Retour, Number> fraisCol;

    @FXML private ComboBox<Reservation> reservationCombo;
    @FXML private DatePicker dateRetour;
    @FXML private ComboBox<String> etatVehiculeCombo;
    @FXML private TextField fraisSupplementairesField;
    @FXML private Label errorLabel, successLabel;

    private RetourDAO retourDAO = new RetourDAO();
    private ReservationDAO reservationDAO = new ReservationDAO();
    private VehiculeDAO vehiculeDAO = new VehiculeDAO();
    private Retour selectedRetour;

    @FXML
    public void initialize() {
        setupTableColumns();
        loadRetours();
        reservationCombo.setItems(reservationDAO.getAll());
        etatVehiculeCombo.setItems(FXCollections.observableArrayList("Excellent", "Bon", "Acceptable", "Endommagé"));
    }

    private void setupTableColumns() {
        idCol.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        resIdCol.setCellValueFactory(cellData -> cellData.getValue().reservationIdProperty());
        dateRetourCol.setCellValueFactory(cellData -> cellData.getValue().dateRetourProperty());
        etatCol.setCellValueFactory(cellData -> cellData.getValue().etatVehiculeProperty());
        fraisCol.setCellValueFactory(cellData -> cellData.getValue().fraisSupplementairesProperty());

        retourTable.setOnMouseClicked(event -> {
            Retour r = retourTable.getSelectionModel().getSelectedItem();
            if (r != null) {
                selectedRetour = r;
                populateForm(r);
            }
        });
    }

    private void loadRetours() {
        retourTable.setItems(retourDAO.getAll());
    }

    @FXML
    public void handleAddRetour() {
        errorLabel.setText("");
        successLabel.setText("");

        if (reservationCombo.getValue() == null || dateRetour.getValue() == null ||
            etatVehiculeCombo.getValue() == null || fraisSupplementairesField.getText().isEmpty()) {
            errorLabel.setText("All fields are required");
            return;
        }

        try {
            double frais = Double.parseDouble(fraisSupplementairesField.getText());
            if (frais < 0) {
                errorLabel.setText("Fees cannot be negative");
                return;
            }

            Retour r = new Retour();
            r.setReservationId(reservationCombo.getValue().getId());
            r.setDateRetour(dateRetour.getValue().toString());
            r.setEtatVehicule(etatVehiculeCombo.getValue());
            r.setFraisSupplementaires(frais);

            retourDAO.add(r);

            // Update vehicle status
            Reservation res = reservationCombo.getValue();
            Vehicule v = vehiculeDAO.getById(res.getVehiculeId());
            if ("Endommagé".equals(etatVehiculeCombo.getValue())) {
                v.setStatut("Maintenance");
            } else {
                v.setStatut("Disponible");
            }
            vehiculeDAO.update(v);

            successLabel.setText("Return recorded successfully");
            clearForm();
            loadRetours();
        } catch (NumberFormatException e) {
            errorLabel.setText("Invalid number format");
        } catch (Exception e) {
            errorLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void handleUpdateRetour() {
        errorLabel.setText("");
        successLabel.setText("");
        if (selectedRetour == null) {
            errorLabel.setText("Please select a return");
            return;
        }

        try {
            double frais = Double.parseDouble(fraisSupplementairesField.getText());
            if (frais < 0) {
                errorLabel.setText("Fees cannot be negative");
                return;
            }

            selectedRetour.setReservationId(reservationCombo.getValue().getId());
            selectedRetour.setDateRetour(dateRetour.getValue().toString());
            selectedRetour.setEtatVehicule(etatVehiculeCombo.getValue());
            selectedRetour.setFraisSupplementaires(frais);

            retourDAO.update(selectedRetour);
            
            // Update vehicle status
            Reservation res = reservationCombo.getValue();
            Vehicule v = vehiculeDAO.getById(res.getVehiculeId());
            if ("Endommagé".equals(etatVehiculeCombo.getValue())) {
                v.setStatut("Maintenance");
            } else {
                v.setStatut("Disponible");
            }
            vehiculeDAO.update(v);

            successLabel.setText("Return updated successfully");
            clearForm();
            loadRetours();
        } catch (NumberFormatException e) {
            errorLabel.setText("Invalid number format");
        } catch (Exception e) {
            errorLabel.setText("Error: " + e.getMessage());
        }
    }

    private void populateForm(Retour r) {
        reservationCombo.setValue(reservationDAO.getById(r.getReservationId()));
        dateRetour.setValue(java.time.LocalDate.parse(r.getDateRetour()));
        etatVehiculeCombo.setValue(r.getEtatVehicule());
        fraisSupplementairesField.setText(String.valueOf(r.getFraisSupplementaires()));
    }

    private void clearForm() {
        reservationCombo.getSelectionModel().clearSelection();
        dateRetour.setValue(null);
        etatVehiculeCombo.getSelectionModel().clearSelection();
        fraisSupplementairesField.clear();
        selectedRetour = null;
        retourTable.getSelectionModel().clearSelection();
    }
}

