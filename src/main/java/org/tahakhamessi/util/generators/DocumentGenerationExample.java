package org.tahakhamessi.util.generators;

import org.tahakhamessi.model.Client;
import org.tahakhamessi.model.Reservation;
import org.tahakhamessi.model.Vehicule;

/**
 * Example usage demonstrating how to use the Tunisian Document Generator system
 * This shows how to integrate the document generation into your controllers
 */
public class DocumentGenerationExample {

    /**
     * Example 1: Generate complete document bundle for a rental transaction
     */
    public static void exampleGenerateCompleteBundle() {
        // Sample data (in real app, this comes from your database/UI)
        Client client = new Client(
            1, "Khamessi", "Taha", "12345678", 
            "taha@email.com", "216 12345678", 
            "123 Rue 1st Street, Tunis", "154631", 
            "2028-12-31"
        );

        Vehicule vehicule = new Vehicule(
            5, "Toyota", "Corolla", "TN 125 ABC 123",
            "Berline", "Essence", "Manuelle",
            5, 80.0, "Disponible"
        );

        Reservation reservation = new Reservation(
            10, 1, 5, "2026-05-15", "2026-05-18",
            3, 240.0, "Confirmée"
        );

        try {
            // Initialize generator with documents directory
            String outputDir = TunisianDocumentBundleGenerator.getDailyOutputDirectory();
            TunisianDocumentBundleGenerator generator = 
                new TunisianDocumentBundleGenerator(outputDir);

            // Generate complete bundle
            TunisianDocumentBundleGenerator.DocumentBundle bundle = 
                generator.generateCompleteBundle(
                    client,                    // Client information
                    vehicule,                  // Vehicle information
                    reservation,               // Reservation details
                    500.0,                    // Security deposit (TND)
                    0.50,                     // Extra km rate (TND)
                    120000,                   // Initial mileage
                    750.0,                    // Amount paid (TND)
                    "Carte Bancaire",        // Payment method
                    ""                       // Damage report (empty = no damage)
                );

            // Check results
            if (bundle.isSuccess()) {
                System.out.println("✓ Documents générés avec succès!");
                System.out.println("Bundle: " + bundle.getBundleName());
                System.out.println("Nombre de documents: " + bundle.getDocumentCount());
                
                // List all generated documents
                for (java.util.Map.Entry<String, String> doc : bundle.getDocuments().entrySet()) {
                    System.out.println("  - " + doc.getKey() + ": " + doc.getValue());
                }
            } else {
                System.err.println("✗ Erreur: " + bundle.getMessage());
            }

        } catch (Exception e) {
            System.err.println("Erreur lors de la génération: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Example 2: Generate individual documents
     */
    public static void exampleGenerateIndividualDocuments() {
        Client client = new Client(
            2, "Ahmed", "Ali", "87654321", 
            "ahmed@email.com", "216 98765432", 
            "456 Avenue Habib Bourguiba, Tunis", "154632", 
            "2027-06-30"
        );

        Vehicule vehicule = new Vehicule(
            3, "Renault", "Symbol", "TN 125 DEF 456",
            "Berline", "Essence", "Automatique",
            5, 60.0, "Disponible"
        );

        Reservation reservation = new Reservation(
            20, 2, 3, "2026-05-20", "2026-05-22",
            2, 120.0, "Confirmée"
        );

        try {
            String outputDir = TunisianDocumentBundleGenerator.getDefaultOutputDirectory();
            TunisianDocumentBundleGenerator generator = 
                new TunisianDocumentBundleGenerator(outputDir);

            // Generate just the contract
            String contractPath = generator.generateContract(
                client, vehicule, reservation, 400.0, 0.50
            );
            System.out.println("Contrat généré: " + contractPath);

            // Generate just the receipt
            String receiptPath = generator.generateReceipt(
                client, vehicule, reservation, 600.0, "Espèces"
            );
            System.out.println("Reçu généré: " + receiptPath);

            // Generate just the handover report
            String handoverPath = generator.generateHandoverReport(
                client, vehicule, reservation, 50000
            );
            System.out.println("Procès-verbal généré: " + handoverPath);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Example 3: How to integrate into a Controller
     * This demonstrates how to use the generator in your ReservationsController
     */
    public static class ReservationControllerExample {
        
        /**
         * Method to call when user wants to print rental documents
         */
        public void handlePrintDocuments(Reservation reservation, Client client, 
                                        Vehicule vehicule) {
            try {
                // Prepare parameters
                String outputDir = TunisianDocumentBundleGenerator.getDailyOutputDirectory();
                TunisianDocumentBundleGenerator generator = 
                    new TunisianDocumentBundleGenerator(outputDir);

                // Get rental details from database or UI
                double depotCaution = 500.0;
                double tauxKmSupp = 0.50;
                int initialKm = 120000;
                double montantPaye = 800.0;
                String modePaiement = "Carte Bancaire";
                String damageReport = "";

                // Generate all documents
                TunisianDocumentBundleGenerator.DocumentBundle bundle = 
                    generator.generateCompleteBundle(
                        client, vehicule, reservation,
                        depotCaution, tauxKmSupp, initialKm,
                        montantPaye, modePaiement, damageReport
                    );

                if (bundle.isSuccess()) {
                    // Show success message to user
                    showAlert("Succès", 
                        "Documents générés avec succès!\n" +
                        bundle.getDocumentCount() + " documents disponibles.\n" +
                        "Localisation: " + bundle.getOutputDirectory());
                    
                    // Optional: Open the documents folder
                    openFolderInExplorer(bundle.getOutputDirectory());
                } else {
                    showAlert("Erreur", "Échec de la génération: " + bundle.getMessage());
                }

            } catch (Exception e) {
                showAlert("Erreur", "Erreur lors de la génération: " + e.getMessage());
            }
        }

        /**
         * Generate and immediately open a specific document
         */
        public void generateAndViewContract(Reservation reservation, Client client, 
                                           Vehicule vehicule) {
            try {
                String outputDir = TunisianDocumentBundleGenerator.getDefaultOutputDirectory();
                TunisianDocumentBundleGenerator generator = 
                    new TunisianDocumentBundleGenerator(outputDir);

                String contractPath = generator.generateContract(
                    client, vehicule, reservation,
                    500.0, 0.50
                );

                // Open the PDF in default viewer
                openPdfFile(contractPath);

            } catch (Exception e) {
                showAlert("Erreur", e.getMessage());
            }
        }

        // Helper methods (implement based on your UI framework)
        private void showAlert(String title, String message) {
            // TODO: Implement using JavaFX Alert or similar
            System.out.println("[" + title + "] " + message);
        }

        private void openFolderInExplorer(String folderPath) {
            // TODO: Implement using Desktop.getDesktop().open() or similar
            System.out.println("Opening folder: " + folderPath);
        }

        private void openPdfFile(String filePath) {
            // TODO: Implement using Desktop.getDesktop().open() or similar
            System.out.println("Opening PDF: " + filePath);
        }
    }

    /**
     * Example 4: Error handling and validation
     */
    public static void exampleErrorHandling() {
        Client client = new Client();
        Vehicule vehicule = new Vehicule();
        Reservation reservation = new Reservation();

        try {
            String outputDir = TunisianDocumentBundleGenerator.getDailyOutputDirectory();
            TunisianDocumentBundleGenerator generator = 
                new TunisianDocumentBundleGenerator(outputDir);

            // This will handle errors gracefully
            TunisianDocumentBundleGenerator.DocumentBundle bundle = 
                generator.generateCompleteBundle(
                    client, vehicule, reservation,
                    500.0, 0.50, 120000,
                    750.0, "Espèces", ""
                );

            if (!bundle.isSuccess()) {
                // Log the error
                System.err.println("Generation failed: " + bundle.getMessage());
                
                // Optionally retry with different parameters
                // or notify the user
            }

        } catch (Exception e) {
            // Handle unexpected errors
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Tunisian Document Generator Examples ===\n");
        
        System.out.println("Example 1: Complete Bundle Generation");
        exampleGenerateCompleteBundle();
        
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        System.out.println("Example 2: Individual Document Generation");
        exampleGenerateIndividualDocuments();
    }
}

