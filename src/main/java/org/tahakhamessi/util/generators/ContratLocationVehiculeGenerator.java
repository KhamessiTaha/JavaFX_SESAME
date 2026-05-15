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

/**
 * Generator for "Contrat de Location de Véhicule" (Vehicle Rental Contract)
 * Produces a professional, legally-compliant Tunisian rental contract
 */
public class ContratLocationVehiculeGenerator {

    public static void generateContract(String outputPath, Client client, Vehicule vehicule,
                                       Reservation reservation, double depotCaution, double tauxKmSupp) throws IOException {
        PDDocument document = new PDDocument();
        
        // Create first copy (Original)
        addContractPage(document, client, vehicule, reservation, depotCaution, tauxKmSupp, "ORIGINAL");
        
        // Create second copy (Locataire)
        addContractPage(document, client, vehicule, reservation, depotCaution, tauxKmSupp, "COPIE LOCATAIRE");
        
        // Add legal notices page
        addLegalNoticesPage(document);
        
        document.save(outputPath);
        document.close();
    }

    private static void addContractPage(PDDocument document, Client client, Vehicule vehicule,
                                        Reservation reservation, double depotCaution, double tauxKmSupp,
                                        String copyType) throws IOException {
        PDPage page = new PDPage();
        document.addPage(page);
        PDPageContentStream content = new PDPageContentStream(document, page);
        
        float yPos = DocumentGenerator.addTunisianHeader(content,
            "CONTRAT DE LOCATION DE VÉHICULE",
            FormatTunisienUtil.genererNumeroContrat(reservation.getId()));
        
        // Copy type indicator
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 9);
        content.setNonStrokingColor(0.7f, 0, 0);
        content.beginText();
        content.newLineAtOffset(DocumentGenerator.getPageWidth() - DocumentGenerator.getMarginRight() - 80, yPos + 15);
        content.showText("[" + copyType + "]");
        content.endText();
        content.setNonStrokingColor(0, 0, 0);
        
        yPos -= DocumentGenerator.SECTION_SPACING;
        
        // 1. PARTIES AU CONTRAT
        yPos = addSectionHeader(content, yPos, "1. PARTIES AU CONTRAT");
        
        String[][] partiesData = {
            {"Le Locateur (Propriétaire)", FormatTunisienUtil.COMPANY_NAME},
            {"Immatriculation Fiscale", FormatTunisienUtil.COMPANY_MATRICULE_FISCALE},
            {"Adresse", FormatTunisienUtil.COMPANY_ADDRESS},
            {"Contact", FormatTunisienUtil.COMPANY_PHONE},
            {"", ""},
            {"Le Locataire (Renter)", client.getNom() + " " + client.getPrenom()},
            {"CIN/Passeport", FormatTunisienUtil.formatCIN(client.getCin())},
            {"Adresse", client.getAdresse()},
            {"Téléphone", FormatTunisienUtil.formatTelephone(client.getTelephone())},
            {"Email", client.getEmail()}
        };
        
        yPos = DocumentGenerator.addInformationTable(content, partiesData, yPos);
        yPos -= DocumentGenerator.SECTION_SPACING;
        
        // 2. VEHICULE
        yPos = addSectionHeader(content, yPos, "2. DESCRIPTION DU VÉHICULE");
        
        String[][] vehicleData = {
            {"Marque et Modèle", vehicule.getMarque() + " " + vehicule.getModele()},
            {"Immatriculation", FormatTunisienUtil.formatPlaqueImmatriculation(vehicule.getImmatriculation())},
            {"Catégorie", vehicule.getCategorie()},
            {"Carburant", vehicule.getCarburant()},
            {"Kilométrage Initial", String.valueOf(120000) + " km"}, // TODO: Get from vehicle
            {"Nombre de Places", String.valueOf(vehicule.getNombrePlaces())},
            {"État du Véhicule à la Remise", "Bon État (conforme fiche d'inspection)"}
        };
        
        yPos = DocumentGenerator.addInformationTable(content, vehicleData, yPos);
        yPos -= DocumentGenerator.SECTION_SPACING;
        
        // 3. CONDITIONS DE LOCATION
        yPos = addSectionHeader(content, yPos, "3. CONDITIONS DE LOCATION");
        
        LocalDate dateDebut = FormatTunisienUtil.parseDate(reservation.getDateDebut());
        LocalDate dateFin = FormatTunisienUtil.parseDate(reservation.getDateFin());
        
        String[][] rentalTermsData = {
            {"Date de Début", FormatTunisienUtil.formatDateCourte(dateDebut) + " à 09:00"},
            {"Date de Fin", FormatTunisienUtil.formatDateCourte(dateFin) + " à 18:00"},
            {"Durée", reservation.getNombreJours() + " jours"},
            {"Lieu de Remise", "Agence SESAME - Tunis"},
            {"Lieu de Restitution", "Agence SESAME - Tunis"},
            {"Kilométrage Inclus", (reservation.getNombreJours() * 200) + " km"},
            {"Tarif par km supplémentaire", FormatTunisienUtil.formatMontant(tauxKmSupp) + " TND/km"}
        };
        
        yPos = DocumentGenerator.addInformationTable(content, rentalTermsData, yPos);
        yPos -= DocumentGenerator.SECTION_SPACING;
        
        // 4. CONDITIONS TARIFAIRES
        yPos = addSectionHeader(content, yPos, "4. CONDITIONS TARIFAIRES");
        
        double dailyRate = vehicule.getPrixParJour();
        double subTotal = dailyRate * reservation.getNombreJours();
        double tva = FormatTunisienUtil.calculerTVA(subTotal);
        double totalBeforeDeposit = subTotal + tva;
        
        String[][] tariffData = {
            {"Tarif par Jour", FormatTunisienUtil.formatMontant(dailyRate) + " TND"},
            {"Nombre de jours", String.valueOf(reservation.getNombreJours())},
            {"Montant HT", FormatTunisienUtil.formatMontant(subTotal) + " TND"},
            {"TVA (19%)", FormatTunisienUtil.formatMontant(tva) + " TND"},
            {"Sous-total TTC", FormatTunisienUtil.formatMontant(totalBeforeDeposit) + " TND"},
            {"Dépôt de Caution", FormatTunisienUtil.formatMontant(depotCaution) + " TND"},
            {"MONTANT TOTAL À PAYER", FormatTunisienUtil.formatMontant(totalBeforeDeposit + depotCaution) + " TND"}
        };
        
        yPos = DocumentGenerator.addInformationTable(content, tariffData, yPos);
        yPos -= DocumentGenerator.SECTION_SPACING;
        
        // 5. OBLIGATIONS DU LOCATAIRE - Conditions légales
        yPos = addSectionHeader(content, yPos, "5. OBLIGATIONS DU LOCATAIRE et CONDITIONS LÉGALES");
        
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 8);
        yPos = DocumentGenerator.addMultilineText(content,
            TunisianLegalUtil.getResponsibilityDisclaimer(),
            8, yPos, DocumentGenerator.getPageWidth() - DocumentGenerator.getMarginLeft() - DocumentGenerator.getMarginRight() - 10);
        yPos -= 8;
        yPos = DocumentGenerator.addMultilineText(content,
            TunisianLegalUtil.getDriverLicenseRequirement(),
            8, yPos, DocumentGenerator.getPageWidth() - DocumentGenerator.getMarginLeft() - DocumentGenerator.getMarginRight() - 10);
        yPos -= 8;
        yPos = DocumentGenerator.addMultilineText(content,
            TunisianLegalUtil.getFuelPolicyNotice(),
            8, yPos, DocumentGenerator.getPageWidth() - DocumentGenerator.getMarginLeft() - DocumentGenerator.getMarginRight() - 10);
        yPos -= 8;
        yPos = DocumentGenerator.addMultilineText(content,
            TunisianLegalUtil.getMileageNotice(),
            8, yPos, DocumentGenerator.getPageWidth() - DocumentGenerator.getMarginLeft() - DocumentGenerator.getMarginRight() - 10);
        yPos -= 8;
        yPos = DocumentGenerator.addMultilineText(content,
            TunisianLegalUtil.getLateReturnNotice(),
            8, yPos, DocumentGenerator.getPageWidth() - DocumentGenerator.getMarginLeft() - DocumentGenerator.getMarginRight() - 10);
        
        // Handle page break if needed
        if (yPos < 220) {
            content.close();
            PDPage newPage = new PDPage();
            document.addPage(newPage);
            content = new PDPageContentStream(document, newPage);
            yPos = DocumentGenerator.MARGIN_TOP;
        }
        
        yPos -= 20;
        
        // 6. ASSURANCE
        yPos = addSectionHeader(content, yPos, "6. CONDITIONS D'ASSURANCE");
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 8);
        yPos = DocumentGenerator.addMultilineText(content,
            TunisianLegalUtil.getInsuranceNotice(),
            8, yPos, DocumentGenerator.getPageWidth() - DocumentGenerator.getMarginLeft() - DocumentGenerator.getMarginRight() - 10);
        
        yPos -= 20;
        
        // 7. REFERENCES LEGALES
        yPos = addSectionHeader(content, yPos, "7. RÉFÉRENCES LÉGALES");
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 7);
        content.beginText();
        content.newLineAtOffset(DocumentGenerator.getMarginLeft() + 5, yPos);
        content.showText("Ce contrat est conforme aux dispositions du Code des Obligations et des Contrats (COC),");
        content.endText();
        yPos -= 8;
        content.beginText();
        content.newLineAtOffset(DocumentGenerator.getMarginLeft() + 5, yPos);
        content.showText("de la Loi n° 2004-33 sur l'organisation des transports terrestres, et du Code de la route tunisien.");
        content.endText();
        yPos -= 8;
        content.beginText();
        content.newLineAtOffset(DocumentGenerator.getMarginLeft() + 5, yPos);
        content.showText("Timbre fiscal dû: 1.200 TND (Loi Tunisienne d'Enregistrement)");
        content.endText();
        
        yPos -= 30;
        
        // SIGNATURE SECTION
        String[] signatories = {"Le Locataire", "Le Locateur / Gérant"};
        DocumentGenerator.addSignatureSection(content, signatories, yPos);
        
        // Footer
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 7);
        content.setNonStrokingColor(0.5f, 0.5f, 0.5f);
        content.beginText();
        content.newLineAtOffset(DocumentGenerator.getMarginLeft(), 35);
        content.showText("Document officiel généré par SESAME Car Rental - Version 1.0");
        content.endText();
        
        content.close();
    }

    private static void addLegalNoticesPage(PDDocument document) throws IOException {
        PDPage page = new PDPage();
        document.addPage(page);
        PDPageContentStream content = new PDPageContentStream(document, page);
        
        float yPos = DocumentGenerator.MARGIN_TOP;
        
        // Title
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 14);
        content.beginText();
        content.newLineAtOffset(DocumentGenerator.getMarginLeft(), yPos);
        content.showText("CONDITIONS GÉNÉRALES DE LOCATION");
        content.endText();
        yPos -= 25;
        
        // Content
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);
        
        String[] notices = {
            "RESPONSABILITÉ: Le locataire est responsable de tous les dommages causés au véhicule loué durant la période de location.",
            "PAIEMENT: Le paiement intégral doit être effectué avant la remise du véhicule.",
            "CARBURANT: Le véhicule est fourni avec plein. Il doit être retourné avec le même niveau de carburant.",
            "KILOMÉTRAGE: Les km supplémentaires seront facturés selon le tarif convenu.",
            "ACCIDENT: Tout accident doit être signalé immédiatement aux autorités et au prestataire.",
            "RETARD: Les retours tardifs entraîneront des frais supplémentaires.",
            "ANNULATION: Annulation gratuite 48h avant. Sinon, 10% penalty.",
            "LOI APPLICABLE: Ce contrat est régi par la loi tunisienne."
        };
        
        for (String notice : notices) {
            yPos = DocumentGenerator.addMultilineText(content, "• " + notice, 9, yPos,
                DocumentGenerator.getPageWidth() - DocumentGenerator.getMarginLeft() - DocumentGenerator.getMarginRight() - 10);
            yPos -= 5;
        }
        
        content.close();
    }

    private static float addSectionHeader(PDPageContentStream content, float yPos, String headerText) throws IOException {
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 11);
        content.setNonStrokingColor(0, 0.4f, 0.7f);
        content.beginText();
        content.newLineAtOffset(DocumentGenerator.getMarginLeft(), yPos);
        content.showText(headerText);
        content.endText();
        content.setNonStrokingColor(0, 0, 0);
        
        // Underline
        DocumentGenerator.drawLine(content, DocumentGenerator.getMarginLeft(), yPos - 3,
            DocumentGenerator.getPageWidth() - DocumentGenerator.getMarginRight(), yPos - 3, 1f);
        
        return yPos - DocumentGenerator.SECTION_SPACING;
    }
}
