package org.tahakhamessi.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import org.tahakhamessi.dao.VehiculeDAO;
import org.tahakhamessi.model.Utilisateur;
import org.tahakhamessi.model.Vehicule;
import org.tahakhamessi.util.ValidationUtil;

public class VehiclesController {
    @FXML private TableView<Vehicule> vehiculeTable;
    @FXML private TableColumn<Vehicule, Number> idCol, placesCol;
    @FXML private TableColumn<Vehicule, String> marqueCol, modeleCol, immatriculationCol, categorieCol, carburantCol, boiteCol, statutCol;
    @FXML private TableColumn<Vehicule, Number> prixCol;

    @FXML private TextField marqueField, modeleField, immatriculationField, categorieField, carburantField, 
                             boiteVitesseField, nombrePlacesField, prixField, searchField;
    @FXML private ComboBox<String> statutCombo;
    @FXML private ComboBox<String> filterCombo;
    @FXML private Label errorLabel, successLabel;
    @FXML private VBox formSection;
    @FXML private Button btnAdd, btnUpdate, btnDelete;

    private VehiculeDAO vehiculeDAO = new VehiculeDAO();
    private Vehicule selectedVehicule;
    private Utilisateur currentUser;

    public void setCurrentUser(Utilisateur user) {
        this.currentUser = user;
        if (currentUser != null && "agent".equalsIgnoreCase(currentUser.getRole())) {
            formSection.setVisible(false);
            formSection.setManaged(false);
        }
    }

    @FXML
    public void initialize() {
        setupTableColumns();
        loadVehicules();
        statutCombo.setItems(FXCollections.observableArrayList("Disponible", "Maintenance", "Loué"));
        filterCombo.setItems(FXCollections.observableArrayList("Tous", "Disponible", "Maintenance", "Loué"));
    }

    private void setupTableColumns() {
        idCol.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        marqueCol.setCellValueFactory(cellData -> cellData.getValue().marqueProperty());
        modeleCol.setCellValueFactory(cellData -> cellData.getValue().modeleProperty());
        immatriculationCol.setCellValueFactory(cellData -> cellData.getValue().immatriculationProperty());
        categorieCol.setCellValueFactory(cellData -> cellData.getValue().categorieProperty());
        carburantCol.setCellValueFactory(cellData -> cellData.getValue().carburantProperty());
        boiteCol.setCellValueFactory(cellData -> cellData.getValue().boiteVitesseProperty());
        placesCol.setCellValueFactory(cellData -> cellData.getValue().nombrePlacesProperty());
        prixCol.setCellValueFactory(cellData -> cellData.getValue().prixParJourProperty());
        statutCol.setCellValueFactory(cellData -> cellData.getValue().statutProperty());

        vehiculeTable.setOnMouseClicked(event -> {
            Vehicule v = vehiculeTable.getSelectionModel().getSelectedItem();
            if (v != null) {
                selectedVehicule = v;
                populateForm(v);
            }
        });
    }

    private void loadVehicules() {
        vehiculeTable.setItems(vehiculeDAO.getAll());
    }

    @FXML
    public void handleAddVehicule() {
        errorLabel.setText("");
        successLabel.setText("");
        if (ValidationUtil.isRequiredFieldEmpty(marqueField.getText()) ||
            ValidationUtil.isRequiredFieldEmpty(modeleField.getText()) ||
            ValidationUtil.isRequiredFieldEmpty(immatriculationField.getText()) ||
            ValidationUtil.isRequiredFieldEmpty(categorieField.getText()) ||
            ValidationUtil.isRequiredFieldEmpty(carburantField.getText()) ||
            ValidationUtil.isRequiredFieldEmpty(boiteVitesseField.getText()) ||
            ValidationUtil.isRequiredFieldEmpty(nombrePlacesField.getText()) ||
            ValidationUtil.isRequiredFieldEmpty(prixField.getText())) {
            errorLabel.setText("All fields are required");
            return;
        }

        if (vehiculeDAO.immatriculationExists(immatriculationField.getText(), -1)) {
            errorLabel.setText("Immatriculation already exists");
            return;
        }

        try {
            double prix = Double.parseDouble(prixField.getText());
            int places = Integer.parseInt(nombrePlacesField.getText());
            if (!ValidationUtil.isPositivePrice(prix)) {
                errorLabel.setText("Price must be positive");
                return;
            }

            Vehicule v = new Vehicule();
            v.setMarque(marqueField.getText());
            v.setModele(modeleField.getText());
            v.setImmatriculation(immatriculationField.getText());
            v.setCategorie(categorieField.getText());
            v.setCarburant(carburantField.getText());
            v.setBoiteVitesse(boiteVitesseField.getText());
            v.setNombrePlaces(places);
            v.setPrixParJour(prix);
            v.setStatut(statutCombo.getValue() != null ? statutCombo.getValue() : "Disponible");

            vehiculeDAO.add(v);
            successLabel.setText("Vehicle added successfully");
            clearForm();
            loadVehicules();
        } catch (NumberFormatException e) {
            errorLabel.setText("Invalid number format");
        } catch (Exception e) {
            errorLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void handleUpdateVehicule() {
        errorLabel.setText("");
        successLabel.setText("");
        if (selectedVehicule == null) {
            errorLabel.setText("Please select a vehicle");
            return;
        }

        try {
            double prix = Double.parseDouble(prixField.getText());
            int places = Integer.parseInt(nombrePlacesField.getText());
            if (!ValidationUtil.isPositivePrice(prix)) {
                errorLabel.setText("Price must be positive");
                return;
            }

            if (!selectedVehicule.getImmatriculation().equals(immatriculationField.getText()) &&
                vehiculeDAO.immatriculationExists(immatriculationField.getText(), selectedVehicule.getId())) {
                errorLabel.setText("Immatriculation already exists");
                return;
            }

            selectedVehicule.setMarque(marqueField.getText());
            selectedVehicule.setModele(modeleField.getText());
            selectedVehicule.setImmatriculation(immatriculationField.getText());
            selectedVehicule.setCategorie(categorieField.getText());
            selectedVehicule.setCarburant(carburantField.getText());
            selectedVehicule.setBoiteVitesse(boiteVitesseField.getText());
            selectedVehicule.setNombrePlaces(places);
            selectedVehicule.setPrixParJour(prix);
            selectedVehicule.setStatut(statutCombo.getValue() != null ? statutCombo.getValue() : "Disponible");

            vehiculeDAO.update(selectedVehicule);
            successLabel.setText("Vehicle updated successfully");
            clearForm();
            loadVehicules();
        } catch (NumberFormatException e) {
            errorLabel.setText("Invalid number format");
        } catch (Exception e) {
            errorLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void handleDeleteVehicule() {
        errorLabel.setText("");
        successLabel.setText("");
        if (selectedVehicule == null) {
            errorLabel.setText("Please select a vehicle");
            return;
        }

        try {
            vehiculeDAO.delete(selectedVehicule.getId());
            successLabel.setText("Vehicle deleted successfully");
            clearForm();
            loadVehicules();
        } catch (Exception e) {
            errorLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void handleSearch() {
        String query = searchField.getText().toLowerCase();
        String filter = filterCombo.getValue() != null ? filterCombo.getValue() : "Tous";
        vehiculeTable.setItems(vehiculeDAO.getFiltered(query, filter));
    }

    private void populateForm(Vehicule v) {
        marqueField.setText(v.getMarque());
        modeleField.setText(v.getModele());
        immatriculationField.setText(v.getImmatriculation());
        categorieField.setText(v.getCategorie());
        carburantField.setText(v.getCarburant());
        boiteVitesseField.setText(v.getBoiteVitesse());
        nombrePlacesField.setText(String.valueOf(v.getNombrePlaces()));
        prixField.setText(String.valueOf(v.getPrixParJour()));
        statutCombo.setValue(v.getStatut());
    }

    private void clearForm() {
        marqueField.clear();
        modeleField.clear();
        immatriculationField.clear();
        categorieField.clear();
        carburantField.clear();
        boiteVitesseField.clear();
        nombrePlacesField.clear();
        prixField.clear();
        searchField.clear();
        selectedVehicule = null;
        vehiculeTable.getSelectionModel().clearSelection();
    }
}

