package org.tahakhamessi.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import org.tahakhamessi.dao.RetourDAO;
import org.tahakhamessi.dao.ReservationDAO;
import org.tahakhamessi.dao.VehiculeDAO;
import org.tahakhamessi.dao.ClientDAO;
import org.tahakhamessi.model.Retour;
import org.tahakhamessi.model.Reservation;
import org.tahakhamessi.model.Vehicule;
import org.tahakhamessi.util.DocumentGenerator;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

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
    private ClientDAO clientDAO = new ClientDAO();
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

    @FXML
    public void handleGenerateReturnReceipt() {
        errorLabel.setText("");
        successLabel.setText("");
        if (selectedRetour == null) {
            errorLabel.setText("Please select a return");
            return;
        }
        
        try {
            String fileName = "Return_Receipt_" + selectedRetour.getId() + ".pdf";
            String filePath = DocumentGenerator.showSaveDialog(retourTable.getScene().getWindow(), fileName);
            if (filePath == null) return;
            
            Reservation res = reservationDAO.getById(selectedRetour.getReservationId());
            org.tahakhamessi.model.Client client = clientDAO.getById(res.getClientId());
            Vehicule vehicle = vehiculeDAO.getById(res.getVehiculeId());
            
            PDDocument doc = new PDDocument();
            PDPage page = new PDPage();
            doc.addPage(page);
            
            try (PDPageContentStream content = new PDPageContentStream(doc, page)) {
                float yPos = DocumentGenerator.addProfessionalHeader(content, "VEHICLE RETURN RECEIPT");
                
                yPos = DocumentGenerator.addSectionHeader(content, "RETURN DETAILS", yPos);
                yPos = DocumentGenerator.addKeyValuePair(content, "Receipt Number", "RCP-" + selectedRetour.getId(), yPos);
                yPos = DocumentGenerator.addKeyValuePair(content, "Return Date", selectedRetour.getDateRetour(), yPos);
                yPos = DocumentGenerator.addKeyValuePair(content, "Reservation ID", String.valueOf(res.getId()), yPos);
                yPos -= 8;
                
                yPos = DocumentGenerator.addSectionHeader(content, "CUSTOMER INFORMATION", yPos);
                yPos = DocumentGenerator.addKeyValuePair(content, "Full Name", client.getNom() + " " + client.getPrenom(), yPos);
                yPos = DocumentGenerator.addKeyValuePair(content, "ID Number", client.getCin(), yPos);
                yPos -= 8;
                
                yPos = DocumentGenerator.addSectionHeader(content, "VEHICLE INFORMATION", yPos);
                yPos = DocumentGenerator.addKeyValuePair(content, "Brand & Model", vehicle.getMarque() + " " + vehicle.getModele(), yPos);
                yPos = DocumentGenerator.addKeyValuePair(content, "Registration Number", vehicle.getImmatriculation(), yPos);
                yPos -= 8;
                
                yPos = DocumentGenerator.addSectionHeader(content, "VEHICLE CONDITION & CHARGES", yPos);
                yPos = DocumentGenerator.addKeyValuePair(content, "Vehicle Condition", selectedRetour.getEtatVehicule(), yPos);
                yPos = DocumentGenerator.addKeyValuePair(content, "Additional Charges", String.format("%.3f TND", selectedRetour.getFraisSupplementaires()), yPos);
                yPos -= 15;
                
                yPos = DocumentGenerator.addMultilineText(content,
                    "This receipt confirms the successful return of the above-mentioned vehicle. Please retain this document for your records. Any disputes must be reported within 7 days.", 
                    8, yPos, 500);
                
                DocumentGenerator.addProfessionalFooter(content, "RCP-" + selectedRetour.getId());
            }
            
            doc.save(filePath);
            doc.close();
            successLabel.setText("Receipt saved successfully!");
        } catch (Exception e) {
            errorLabel.setText("Error generating receipt: " + e.getMessage());
        }
    }
}

