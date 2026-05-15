package org.tahakhamessi.util.generators;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.tahakhamessi.model.Client;
import org.tahakhamessi.model.Reservation;
import org.tahakhamessi.model.Vehicule;
import org.tahakhamessi.util.DocumentGenerator;
import org.tahakhamessi.util.FormatTunisienUtil;
import org.tahakhamessi.util.TunisianLegalUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Generator for "Procès-verbal de remise du véhicule" (Vehicle Handover Report)
 * Official document confirming vehicle condition and handover details
 */
public class ProcesVerbalRemiseVehiculeGenerator {

    public static void generateHandoverReport(String outputPath, Client client, Vehicule vehicule,
                                             Reservation reservation, int initialKm) throws IOException {
        PDDocument document = new PDDocument();
        
        PDPage page = new PDPage();
        document.addPage(page);
        PDPageContentStream content = new PDPageContentStream(document, page);
        
        String refNumber = FormatTunisienUtil.genererRefDocument("PV-REMISE");
        float yPos = DocumentGenerator.addTunisianHeader(content,
            "PROCÈS-VERBAL DE REMISE DU VÉHICULE",
            refNumber);
        
        yPos -= DocumentGenerator.SECTION_SPACING;
        
        // 1. INFORMATIONS GÉNÉRALES
        yPos = addSectionHeader(content, yPos, "1. INFORMATIONS GÉNÉRALES");
        
        LocalDate dateDebut = FormatTunisienUtil.parseDate(reservation.getDateDebut());
        LocalDateTime now = LocalDateTime.now();
        
        String[][] generalData = {
            {"Date et Heure de Remise", FormatTunisienUtil.formatDateTimeLong(now)},
            {"Numéro de Contrat", "CTR-" + reservation.getId()},
            {"Numéro de Réservation", "RES-" + reservation.getId()},
            {"Lieu de Remise", "Agence SESAME - Tunis"}
        };
        
        yPos = DocumentGenerator.addInformationTable(content, generalData, yPos);
        yPos -= DocumentGenerator.SECTION_SPACING;
        
        // 2. IDENTITE DU LOCATAIRE
        yPos = addSectionHeader(content, yPos, "2. IDENTITÉ DU LOCATAIRE");
        
        String[][] clientData = {
            {"Nom et Prénom", client.getNom() + " " + client.getPrenom()},
            {"CIN/Passeport", FormatTunisienUtil.formatCIN(client.getCin())},
            {"Téléphone", FormatTunisienUtil.formatTelephone(client.getTelephone())},
            {"Adresse", client.getAdresse()}
        };
        
        yPos = DocumentGenerator.addInformationTable(content, clientData, yPos);
        yPos -= DocumentGenerator.SECTION_SPACING;
        
        // 3. DESCRIPTION DU VEHICULE
        yPos = addSectionHeader(content, yPos, "3. DESCRIPTION DU VÉHICULE REMIS");
        
        String[][] vehicleData = {
            {"Marque et Modèle", vehicule.getMarque() + " " + vehicule.getModele()},
            {"Immatriculation", FormatTunisienUtil.formatPlaqueImmatriculation(vehicule.getImmatriculation())},
            {"Numéro de Châssis", "[À remplir]"},
            {"Catégorie", vehicule.getCategorie()},
            {"Carburant", vehicule.getCarburant()},
            {"Nombre de Places", String.valueOf(vehicule.getNombrePlaces())},
            {"Kilométrage Initial", initialKm + " km"}
        };
        
        yPos = DocumentGenerator.addInformationTable(content, vehicleData, yPos);
        yPos -= DocumentGenerator.SECTION_SPACING;
        
        // 4. ETAT DU VEHICULE
        yPos = addSectionHeader(content, yPos, "4. ÉTAT DU VÉHICULE À LA REMISE");
        
        // Vehicle condition checkboxes
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
        String[] conditions = {
            "[ ] Avant du véhicule - Bon état",
            "[ ] Arrière du véhicule - Bon état",
            "[ ] Côté gauche - Bon état",
            "[ ] Côté droit - Bon état",
            "[ ] Intérieur - Bon état",
            "[ ] Pneus - Bon état",
            "[ ] Vitres - Bon état",
            "[ ] Sièges - Bon état"
        };
        
        float colWidth = (DocumentGenerator.getPageWidth() - DocumentGenerator.getMarginLeft() - DocumentGenerator.getMarginRight()) / 2;
        float xPos = DocumentGenerator.getMarginLeft();
        
        for (int i = 0; i < conditions.length; i++) {
            if (i == 4) {
                xPos = DocumentGenerator.getMarginLeft() + colWidth;
                yPos -= (conditions.length / 2) * 12;
                yPos += 12;
            }
            
            content.beginText();
            content.newLineAtOffset(xPos, yPos - (i % 4) * 12);
            content.showText(conditions[i]);
            content.endText();
        }
        
        yPos -= 65;
        
        // Damage report section
        yPos = addSectionHeader(content, yPos, "DOMMAGES OBSERVÉS À LA REMISE");
        
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);
        content.beginText();
        content.newLineAtOffset(DocumentGenerator.getMarginLeft(), yPos);
        content.showText("Description détaillée des dommages (s'il y a lieu):");
        content.endText();
        
        // Text box for damage description
        yPos -= 15;
        DocumentGenerator.drawBox(content,
            DocumentGenerator.getMarginLeft(),
            yPos - 60,
            DocumentGenerator.getPageWidth() - DocumentGenerator.getMarginLeft() - DocumentGenerator.getMarginRight(),
            60);
        yPos -= 75;
        
        // 5. EQUIPEMENTS
        yPos = addSectionHeader(content, yPos, "5. ÉQUIPEMENTS ET ACCESSOIRES");
        
        String[][] equipmentData = {
            {"Clés du véhicule", "Remises au locataire: [ ]"},
            {"Contrats d'assurance", "Remis au locataire: [ ]"},
            {"Documents du véhicule", "Remis au locataire: [ ]"},
            {"Couverture de secours", "Présente: [ ]"},
            {"Triangle de signalisation", "Présent: [ ]"},
            {"Extincteur", "Présent: [ ]"}
        };
        
        yPos = DocumentGenerator.addInformationTable(content, equipmentData, yPos);
        yPos -= DocumentGenerator.SECTION_SPACING * 1.2f;
        
        // 6. CONDITIONS DE CIRCULATION
        yPos = addSectionHeader(content, yPos, "6. CONDITIONS INITIALES");
        
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);
        yPos = DocumentGenerator.addMultilineText(content,
            "Le locataire accepte de recevoir le véhicule en bon état et " +
            "reconnaît avoir inspecté le véhicule en présence du représentant de l'agence. " +
            "Tout dommage ultérieur sera enregistré lors de la restitution.",
            9, yPos, DocumentGenerator.getPageWidth() - DocumentGenerator.getMarginLeft() - DocumentGenerator.getMarginRight() - 10);
        
        yPos -= 30;
        
        // ACCEPTANCE CHECKBOX
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 11);
        content.setNonStrokingColor(0, 0.4f, 0.7f);
        content.beginText();
        content.newLineAtOffset(DocumentGenerator.getMarginLeft(), yPos);
        content.showText("[ ] Le locataire confirme avoir reçu le véhicule en bon état comme décrit ci-dessus.");
        content.endText();
        
        yPos -= 30;
        
        // SIGNATURE SECTION
        String[] signatories = {"Le Locataire", "L'Agent SESAME"};
        DocumentGenerator.addSignatureSection(content, signatories, yPos);
        
        // Footer
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 7);
        content.setNonStrokingColor(0.5f, 0.5f, 0.5f);
        content.beginText();
        content.newLineAtOffset(DocumentGenerator.getMarginLeft(), 35);
        content.showText("Référence: " + refNumber + " | Généré: " + FormatTunisienUtil.getDateTimeJour());
        content.endText();
        
        content.close();
        
        document.save(outputPath);
        document.close();
    }

    private static float addSectionHeader(PDPageContentStream content, float yPos, String headerText) throws IOException {
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 11);
        content.setNonStrokingColor(0, 0.4f, 0.7f);
        content.beginText();
        content.newLineAtOffset(DocumentGenerator.getMarginLeft(), yPos);
        content.showText(headerText);
        content.endText();
        content.setNonStrokingColor(0, 0, 0);
        
        DocumentGenerator.drawLine(content, DocumentGenerator.getMarginLeft(), yPos - 3,
            DocumentGenerator.getPageWidth() - DocumentGenerator.getMarginRight(), yPos - 3, 1f);
        
        return yPos - DocumentGenerator.SECTION_SPACING;
    }
}
