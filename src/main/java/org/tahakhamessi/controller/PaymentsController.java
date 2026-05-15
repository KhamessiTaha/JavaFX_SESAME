package org.tahakhamessi.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import org.tahakhamessi.dao.PaiementDAO;
import org.tahakhamessi.dao.ReservationDAO;
import org.tahakhamessi.dao.ClientDAO;
import org.tahakhamessi.model.Paiement;
import org.tahakhamessi.model.Reservation;
import org.tahakhamessi.util.ValidationUtil;
import org.tahakhamessi.util.DocumentGenerator;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.time.LocalDate;

public class PaymentsController {
    @FXML private TableView<Paiement> paiementTable;
    @FXML private TableColumn<Paiement, Number> idCol, resIdCol, montantTotalCol, montantPayeCol, resteCol;
    @FXML private TableColumn<Paiement, String> modePaiementCol, statutCol;

    @FXML private ComboBox<Reservation> reservationCombo;
    @FXML private TextField montantTotalField, montantPayeField, resteAPayerField;
    @FXML private ComboBox<String> modePaiementCombo, statutPaiementCombo;
    @FXML private Label errorLabel, successLabel;

    private PaiementDAO paiementDAO = new PaiementDAO();
    private ReservationDAO reservationDAO = new ReservationDAO();
    private ClientDAO clientDAO = new ClientDAO();
    private Paiement selectedPaiement;

    @FXML
    public void initialize() {
        setupTableColumns();
        loadPaiements();
        reservationCombo.setItems(reservationDAO.getAll());
        modePaiementCombo.setItems(FXCollections.observableArrayList("Espèces", "Carte Bancaire", "Chèque", "Virement"));
        statutPaiementCombo.setItems(FXCollections.observableArrayList("Payé", "Partiel", "Non payé"));
    }

    private void setupTableColumns() {
        idCol.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        resIdCol.setCellValueFactory(cellData -> cellData.getValue().reservationIdProperty());
        montantTotalCol.setCellValueFactory(cellData -> cellData.getValue().montantTotalProperty());
        montantPayeCol.setCellValueFactory(cellData -> cellData.getValue().montantPayeProperty());
        resteCol.setCellValueFactory(cellData -> cellData.getValue().resteAPayerProperty());
        modePaiementCol.setCellValueFactory(cellData -> cellData.getValue().modePaiementProperty());
        statutCol.setCellValueFactory(cellData -> cellData.getValue().statutPaiementProperty());

        paiementTable.setOnMouseClicked(event -> {
            Paiement p = paiementTable.getSelectionModel().getSelectedItem();
            if (p != null) {
                selectedPaiement = p;
                populateForm(p);
            }
        });
    }

    private void loadPaiements() {
        paiementTable.setItems(paiementDAO.getAll());
    }

    @FXML
    public void handleAddPaiement() {
        errorLabel.setText("");
        successLabel.setText("");

        if (reservationCombo.getValue() == null ||
            ValidationUtil.isRequiredFieldEmpty(montantTotalField.getText()) ||
            ValidationUtil.isRequiredFieldEmpty(montantPayeField.getText()) ||
            modePaiementCombo.getValue() == null ||
            statutPaiementCombo.getValue() == null) {
            errorLabel.setText("All fields are required");
            return;
        }

        try {
            double montantTotal = Double.parseDouble(montantTotalField.getText());
            double montantPaye = Double.parseDouble(montantPayeField.getText());

            if (montantTotal <= 0 || montantPaye < 0) {
                errorLabel.setText("Amounts must be positive");
                return;
            }

            if (montantPaye > montantTotal) {
                errorLabel.setText("Paid amount cannot exceed total");
                return;
            }

            double resteAPayer = montantTotal - montantPaye;

            Paiement p = new Paiement();
            p.setReservationId(reservationCombo.getValue().getId());
            p.setMontantTotal(montantTotal);
            p.setMontantPaye(montantPaye);
            p.setResteAPayer(resteAPayer);
            p.setModePaiement(modePaiementCombo.getValue());
            p.setStatutPaiement(statutPaiementCombo.getValue());

            paiementDAO.add(p);
            successLabel.setText("Payment added successfully");
            clearForm();
            loadPaiements();
        } catch (NumberFormatException e) {
            errorLabel.setText("Invalid number format");
        } catch (Exception e) {
            errorLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void handleUpdatePaiement() {
        errorLabel.setText("");
        successLabel.setText("");
        if (selectedPaiement == null) {
            errorLabel.setText("Please select a payment");
            return;
        }

        try {
            double montantTotal = Double.parseDouble(montantTotalField.getText());
            double montantPaye = Double.parseDouble(montantPayeField.getText());

            if (montantTotal <= 0 || montantPaye < 0) {
                errorLabel.setText("Amounts must be positive");
                return;
            }

            if (montantPaye > montantTotal) {
                errorLabel.setText("Paid amount cannot exceed total");
                return;
            }

            double resteAPayer = montantTotal - montantPaye;

            selectedPaiement.setMontantTotal(montantTotal);
            selectedPaiement.setMontantPaye(montantPaye);
            selectedPaiement.setResteAPayer(resteAPayer);
            selectedPaiement.setModePaiement(modePaiementCombo.getValue());
            selectedPaiement.setStatutPaiement(statutPaiementCombo.getValue());

            paiementDAO.update(selectedPaiement);
            successLabel.setText("Payment updated successfully");
            clearForm();
            loadPaiements();
        } catch (NumberFormatException e) {
            errorLabel.setText("Invalid number format");
        } catch (Exception e) {
            errorLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    public void handleCalculateReste() {
        try {
            double montantTotal = Double.parseDouble(montantTotalField.getText());
            double montantPaye = Double.parseDouble(montantPayeField.getText());
            double resteAPayer = montantTotal - montantPaye;
            resteAPayerField.setText(String.format("%.2f", resteAPayer));
        } catch (NumberFormatException e) {
            resteAPayerField.setText("0.00");
        }
    }

    private void populateForm(Paiement p) {
        reservationCombo.setValue(reservationDAO.getById(p.getReservationId()));
        montantTotalField.setText(String.valueOf(p.getMontantTotal()));
        montantPayeField.setText(String.valueOf(p.getMontantPaye()));
        resteAPayerField.setText(String.valueOf(p.getResteAPayer()));
        modePaiementCombo.setValue(p.getModePaiement());
        statutPaiementCombo.setValue(p.getStatutPaiement());
    }

    private void clearForm() {
        reservationCombo.getSelectionModel().clearSelection();
        montantTotalField.clear();
        montantPayeField.clear();
        resteAPayerField.clear();
        modePaiementCombo.getSelectionModel().clearSelection();
        statutPaiementCombo.getSelectionModel().clearSelection();
        selectedPaiement = null;
        paiementTable.getSelectionModel().clearSelection();
    }

    @FXML
    public void handleGenerateInvoice() {
        errorLabel.setText("");
        successLabel.setText("");
        if (selectedPaiement == null) {
            errorLabel.setText("Please select a payment");
            return;
        }
        
        try {
            String fileName = "Invoice_" + selectedPaiement.getId() + ".pdf";
            String filePath = DocumentGenerator.showSaveDialog(paiementTable.getScene().getWindow(), fileName);
            if (filePath == null) return;
            
            Reservation res = reservationDAO.getById(selectedPaiement.getReservationId());
            org.tahakhamessi.model.Client client = clientDAO.getById(res.getClientId());
            
            PDDocument doc = new PDDocument();
            PDPage page = new PDPage();
            doc.addPage(page);
            
            try (PDPageContentStream content = new PDPageContentStream(doc, page)) {
                float yPos = DocumentGenerator.addProfessionalHeader(content, "INVOICE");
                
                yPos = DocumentGenerator.addSectionHeader(content, "INVOICE DETAILS", yPos);
                yPos = DocumentGenerator.addKeyValuePair(content, "Invoice Number", "INV-" + selectedPaiement.getId(), yPos);
                yPos = DocumentGenerator.addKeyValuePair(content, "Issue Date", LocalDate.now().toString(), yPos);
                yPos = DocumentGenerator.addKeyValuePair(content, "Reservation ID", String.valueOf(res.getId()), yPos);
                yPos -= 8;
                
                yPos = DocumentGenerator.addSectionHeader(content, "CUSTOMER INFORMATION", yPos);
                yPos = DocumentGenerator.addKeyValuePair(content, "Full Name", client.getNom() + " " + client.getPrenom(), yPos);
                yPos = DocumentGenerator.addKeyValuePair(content, "ID Number", client.getCin(), yPos);
                yPos = DocumentGenerator.addKeyValuePair(content, "Email", client.getEmail(), yPos);
                yPos -= 8;
                
                yPos = DocumentGenerator.addSectionHeader(content, "PAYMENT DETAILS", yPos);
                yPos = DocumentGenerator.addKeyValuePair(content, "Total Amount Due", String.format("%.3f TND", selectedPaiement.getMontantTotal()), yPos);
                yPos = DocumentGenerator.addKeyValuePair(content, "Amount Paid", String.format("%.3f TND", selectedPaiement.getMontantPaye()), yPos);
                yPos = DocumentGenerator.addKeyValuePair(content, "Outstanding Balance", String.format("%.3f TND", selectedPaiement.getResteAPayer()), yPos);
                yPos = DocumentGenerator.addKeyValuePair(content, "Payment Method", selectedPaiement.getModePaiement(), yPos);
                yPos = DocumentGenerator.addKeyValuePair(content, "Payment Status", selectedPaiement.getStatutPaiement(), yPos);
                yPos -= 15;
                
                yPos = DocumentGenerator.addMultilineText(content,
                    "Thank you for your business! This invoice is valid for 30 days. For any inquiries, please contact our customer service team.", 
                    8, yPos, 500);
                
                DocumentGenerator.addProfessionalFooter(content, "INV-" + selectedPaiement.getId());
            }
            
            doc.save(filePath);
            doc.close();
            successLabel.setText("Invoice saved successfully!");
        } catch (Exception e) {
            errorLabel.setText("Error generating invoice: " + e.getMessage());
        }
    }
}

