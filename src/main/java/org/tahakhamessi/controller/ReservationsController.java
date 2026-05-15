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
import org.tahakhamessi.util.DocumentGenerator;
import org.tahakhamessi.util.generators.TunisianDocumentBundleGenerator;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.time.LocalDate;
import java.io.File;
import java.io.IOException;
import java.awt.Desktop;

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

    @FXML
    public void handleGenerateContract() {
        errorLabel.setText("");
        successLabel.setText("");
        if (selectedReservation == null) {
            errorLabel.setText("Veuillez sélectionner une réservation");
            return;
        }
        
        try {
            Client client = clientDAO.getById(selectedReservation.getClientId());
            Vehicule vehicle = vehiculeDAO.getById(selectedReservation.getVehiculeId());
            
            if (client == null || vehicle == null) {
                errorLabel.setText("Erreur: Client ou véhicule non trouvé");
                return;
            }

            // Ask user where to save documents
            javafx.stage.DirectoryChooser dirChooser = new javafx.stage.DirectoryChooser();
            dirChooser.setTitle("Sélectionnez le dossier de destination pour les documents");
            dirChooser.setInitialDirectory(new File(System.getProperty("user.home")));
            
            File selectedDir = dirChooser.showDialog(reservationTable.getScene().getWindow());
            if (selectedDir == null) {
                return; // User cancelled
            }

            // Use new Tunisian document generator with selected directory
            TunisianDocumentBundleGenerator generator = 
                new TunisianDocumentBundleGenerator(selectedDir.getAbsolutePath());

            // Generate all 5 documents as a complete bundle
            var bundle = generator.generateCompleteBundle(
                client,
                vehicle,
                selectedReservation,
                500.0,                                      // Security deposit (TND)
                0.50,                                       // Extra km rate (TND/km)
                120000,                                     // Initial mileage
                selectedReservation.getPrixTotal() + 500,  // Total paid (rental + deposit)
                "Espèces",                                  // Payment method
                ""                                          // No damage report initially
            );

            if (bundle.isSuccess()) {
                successLabel.setText("✓ " + bundle.getDocumentCount() + " documents générés avec succès!\n" +
                    "Location: " + bundle.getOutputDirectory());
                
                // Ask user if they want to open the documents folder
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Documents générés");
                alert.setHeaderText("Succès");
                alert.setContentText("Les " + bundle.getDocumentCount() + " documents ont été générés.\n\n" +
                    "Voulez-vous ouvrir le dossier?");
                
                var result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    try {
                        Desktop.getDesktop().open(new File(bundle.getOutputDirectory()));
                    } catch (IOException e) {
                        // Folder open not supported on this system
                    }
                }
            } else {
                errorLabel.setText("Erreur: " + bundle.getMessage());
            }

        } catch (Exception e) {
            errorLabel.setText("Erreur lors de la génération: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Generate only the rental contract (individual document)
     */
    @FXML
    public void handleGenerateContractOnly() {
        if (selectedReservation == null) {
            errorLabel.setText("Veuillez sélectionner une réservation");
            return;
        }
        
        try {
            Client client = clientDAO.getById(selectedReservation.getClientId());
            Vehicule vehicle = vehiculeDAO.getById(selectedReservation.getVehiculeId());
            
            TunisianDocumentBundleGenerator generator = 
                new TunisianDocumentBundleGenerator(
                    TunisianDocumentBundleGenerator.getDailyOutputDirectory()
                );

            String path = generator.generateContract(client, vehicle, selectedReservation, 500.0, 0.50);
            successLabel.setText("✓ Contrat généré: " + new File(path).getName());
            errorLabel.setText("");
        } catch (IOException e) {
            errorLabel.setText("Erreur: " + e.getMessage());
        }
    }

    /**
     * Generate only the invoice/receipt
     */
    @FXML
    public void handleGenerateReceipt() {
        if (selectedReservation == null) {
            errorLabel.setText("Veuillez sélectionner une réservation");
            return;
        }
        
        try {
            Client client = clientDAO.getById(selectedReservation.getClientId());
            Vehicule vehicle = vehiculeDAO.getById(selectedReservation.getVehiculeId());
            
            TunisianDocumentBundleGenerator generator = 
                new TunisianDocumentBundleGenerator(
                    TunisianDocumentBundleGenerator.getDailyOutputDirectory()
                );

            String path = generator.generateReceipt(client, vehicle, selectedReservation, 
                selectedReservation.getPrixTotal(), "Espèces");
            successLabel.setText("✓ Facture générée: " + new File(path).getName());
            errorLabel.setText("");
        } catch (IOException e) {
            errorLabel.setText("Erreur: " + e.getMessage());
        }
    }

    /**
     * Generate vehicle condition sheet
     */
    @FXML
    public void handleGenerateConditionSheet() {
        if (selectedReservation == null) {
            errorLabel.setText("Veuillez sélectionner une réservation");
            return;
        }
        
        try {
            Vehicule vehicle = vehiculeDAO.getById(selectedReservation.getVehiculeId());
            
            TunisianDocumentBundleGenerator generator = 
                new TunisianDocumentBundleGenerator(
                    TunisianDocumentBundleGenerator.getDailyOutputDirectory()
                );

            String path = generator.generateConditionSheet(vehicle, 120000, "");
            successLabel.setText("✓ Fiche d'état générée: " + new File(path).getName());
            errorLabel.setText("");
        } catch (IOException e) {
            errorLabel.setText("Erreur: " + e.getMessage());
        }
    }

    @FXML
    public void handleExportReservations() {
        try {
            String fileName = "Export_Reservations_" + LocalDate.now() + ".pdf";
            String filePath = DocumentGenerator.showSaveDialog(reservationTable.getScene().getWindow(), fileName);
            if (filePath == null) return;
            
            PDDocument doc = new PDDocument();
            PDPage page = new PDPage();
            doc.addPage(page);
            
            try (PDPageContentStream content = new PDPageContentStream(doc, page)) {
                float yPos = DocumentGenerator.addProfessionalHeader(content, "RESERVATIONS LIST");
                
                yPos -= 10;
                String[] headers = {"ID", "Client", "Vehicle", "Period", "Total (TND)"};
                float[] widths = {40, 120, 110, 130, 90};
                
                DocumentGenerator.drawTableHeader(content, headers, widths, yPos);
                yPos -= 20;
                
                for (Reservation r : reservationTable.getItems()) {
                    String period = r.getDateDebut() + " to " + r.getDateFin();
                    String[] values = {String.valueOf(r.getId()), r.getClientNom(), r.getVehiculeNom(), period, String.format("%.2f", r.getPrixTotal())};
                    yPos = DocumentGenerator.drawTableRow(content, values, widths, yPos);
                    if (yPos < 80) break;
                }
                
                DocumentGenerator.addProfessionalFooter(content, "EXP-RES-" + LocalDate.now());
            }
            
            doc.save(filePath);
            doc.close();
            successLabel.setText("Export saved successfully!");
        } catch (Exception e) {
            errorLabel.setText("Error exporting: " + e.getMessage());
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

