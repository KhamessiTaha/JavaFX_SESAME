package org.tahakhamessi.util.generators;

import org.tahakhamessi.model.Client;
import org.tahakhamessi.model.Reservation;
import org.tahakhamessi.model.Vehicule;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * TunisianDocumentBundleGenerator - Orchestrator for generating complete document sets
 * Generates all required Tunisian-compliant documents for a vehicle rental transaction
 */
public class TunisianDocumentBundleGenerator {

    private final String outputDirectory;
    private final String dateFormat = "yyyyMMdd_HHmmss";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);

    public TunisianDocumentBundleGenerator(String outputDirectory) {
        this.outputDirectory = outputDirectory;
        ensureDirectoryExists();
    }

    /**
     * Generate complete document bundle for a rental transaction
     */
    public DocumentBundle generateCompleteBundle(Client client, Vehicule vehicule,
                                                 Reservation reservation, double depotCaution,
                                                 double tauxKmSupp, int initialKm,
                                                 double montantPaye, String modePaiement,
                                                 String damageReport) throws IOException {
        
        String timestamp = LocalDateTime.now().format(formatter);
        String baseName = String.format("RENTAL_%d_%s_%s",
            reservation.getId(),
            vehicule.getImmatriculation().replaceAll("[^A-Z0-9]", ""),
            timestamp);
        
        DocumentBundle bundle = new DocumentBundle(baseName, outputDirectory);
        
        try {
            // 1. Generate Contract
            String contractPath = bundle.getFilePath("01_CONTRAT_LOCATION_VEHICULE");
            ContratLocationVehiculeGenerator.generateContract(contractPath, client, vehicule, 
                reservation, depotCaution, tauxKmSupp);
            bundle.addDocument("Contrat de Location", contractPath);
            
            // 2. Generate Handover Report
            String handoverPath = bundle.getFilePath("02_PROCES_VERBAL_REMISE");
            ProcesVerbalRemiseVehiculeGenerator.generateHandoverReport(handoverPath, client, vehicule,
                reservation, initialKm);
            bundle.addDocument("Procès-verbal de Remise", handoverPath);
            
            // 3. Generate Receipt/Invoice
            String receiptPath = bundle.getFilePath("03_RECEU_PAIEMENT_FACTURE");
            RecePixementFactureGenerator.generateReceipt(receiptPath, client, vehicule, 
                reservation, montantPaye, modePaiement);
            bundle.addDocument("Reçu de Paiement/Facture", receiptPath);
            
            // 4. Generate Vehicle Condition Sheet
            String conditionPath = bundle.getFilePath("04_FICHE_ETAT_VEHICULE");
            FicheEtatVehiculeGenerator.generateConditionSheet(conditionPath, vehicule, 
                initialKm, damageReport);
            bundle.addDocument("Fiche d'État du Véhicule", conditionPath);
            
            // 5. Generate Circulation Authorization
            String authorizationPath = bundle.getFilePath("05_AUTORISATION_CIRCULATION");
            AutorisationCirculationGenerator.generateCirculationAuthorization(authorizationPath,
                client, vehicule, reservation);
            bundle.addDocument("Autorisation de Circulation", authorizationPath);
            
            bundle.setSuccess(true);
            bundle.setMessage("Bundle de documents généré avec succès");
            
        } catch (IOException e) {
            bundle.setSuccess(false);
            bundle.setMessage("Erreur lors de la génération: " + e.getMessage());
            throw e;
        }
        
        return bundle;
    }

    /**
     * Generate individual documents on demand
     */
    public String generateContract(Client client, Vehicule vehicule, Reservation reservation,
                                  double depotCaution, double tauxKmSupp) throws IOException {
        String path = outputDirectory + File.separator + "CONTRAT_" + reservation.getId() + "_" +
                     LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
        ContratLocationVehiculeGenerator.generateContract(path, client, vehicule, 
            reservation, depotCaution, tauxKmSupp);
        return path;
    }

    public String generateHandoverReport(Client client, Vehicule vehicule,
                                        Reservation reservation, int initialKm) throws IOException {
        String path = outputDirectory + File.separator + "HANDOVER_" + reservation.getId() + "_" +
                     LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
        ProcesVerbalRemiseVehiculeGenerator.generateHandoverReport(path, client, vehicule,
            reservation, initialKm);
        return path;
    }

    public String generateReceipt(Client client, Vehicule vehicule, Reservation reservation,
                                 double montantPaye, String modePaiement) throws IOException {
        String path = outputDirectory + File.separator + "RECEIPT_" + reservation.getId() + "_" +
                     LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
        RecePixementFactureGenerator.generateReceipt(path, client, vehicule, 
            reservation, montantPaye, modePaiement);
        return path;
    }

    public String generateConditionSheet(Vehicule vehicule, int initialKm,
                                        String damageReport) throws IOException {
        String path = outputDirectory + File.separator + "CONDITION_" + vehicule.getId() + "_" +
                     LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
        FicheEtatVehiculeGenerator.generateConditionSheet(path, vehicule, initialKm, damageReport);
        return path;
    }

    public String generateCirculationAuthorization(Client client, Vehicule vehicule,
                                                  Reservation reservation) throws IOException {
        String path = outputDirectory + File.separator + "CIRCULATION_" + reservation.getId() + "_" +
                     LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
        AutorisationCirculationGenerator.generateCirculationAuthorization(path, client, vehicule, reservation);
        return path;
    }

    /**
     * Ensure output directory exists
     */
    private void ensureDirectoryExists() {
        try {
            Files.createDirectories(Paths.get(outputDirectory));
        } catch (IOException e) {
            System.err.println("Failed to create output directory: " + outputDirectory);
            e.printStackTrace();
            throw new RuntimeException("Unable to create output directory: " + outputDirectory, e);
        }
    }

    /**
     * Inner class representing a bundle of generated documents
     */
    public static class DocumentBundle {
        private final String bundleName;
        private final String outputDirectory;
        private final java.util.Map<String, String> documents;
        private boolean success = false;
        private String message = "";

        public DocumentBundle(String bundleName, String outputDirectory) {
            this.bundleName = bundleName;
            this.outputDirectory = outputDirectory;
            this.documents = new java.util.LinkedHashMap<>();
        }

        public void addDocument(String documentName, String filePath) {
            documents.put(documentName, filePath);
        }

        public String getFilePath(String documentType) {
            return outputDirectory + File.separator + bundleName + "_" + documentType + ".pdf";
        }

        public java.util.Map<String, String> getDocuments() {
            return new java.util.HashMap<>(documents);
        }

        public String getBundleName() {
            return bundleName;
        }

        public String getOutputDirectory() {
            return outputDirectory;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getDocumentCount() {
            return documents.size();
        }

        @Override
        public String toString() {
            return String.format("DocumentBundle[name=%s, documents=%d, success=%b, message=%s]",
                bundleName, documents.size(), success, message);
        }
    }

    /**
     * Get default documents output directory (user documents folder)
     */
    public static String getDefaultOutputDirectory() {
        String userHome = System.getProperty("user.home");
        String docsDir = userHome + File.separator + "Documents" + File.separator + "SESAME_Rentals";
        return docsDir;
    }

    /**
     * Get timestamp-based directory for daily documents
     */
    public static String getDailyOutputDirectory() {
        String defaultDir = getDefaultOutputDirectory();
        String dateDir = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return defaultDir + File.separator + dateDir;
    }
}

