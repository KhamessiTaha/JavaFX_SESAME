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
 * Generator for "Reçu de Paiement / Facture" (Payment Receipt and Invoice)
 * Professional invoice with TVA (19%) calculation for Tunisian compliance
 */
public class RecePixementFactureGenerator {

    public static void generateReceipt(String outputPath, Client client, Vehicule vehicule,
                                       Reservation reservation, double montantPaye, String modePaiement) throws IOException {
        PDDocument document = new PDDocument();
        
        PDPage page = new PDPage();
        document.addPage(page);
        PDPageContentStream content = new PDPageContentStream(document, page);
        
        String refNumber = FormatTunisienUtil.genererNumeroFacture(reservation.getId());
        float yPos = DocumentGenerator.addTunisianHeader(content,
            "REÇU DE PAIEMENT / FACTURE",
            refNumber);
        
        yPos -= DocumentGenerator.SECTION_SPACING;
        
        // 1. INFORMATIONS FACTURE
        yPos = addSectionHeader(content, yPos, "1. INFORMATIONS DE LA FACTURE");
        
        String[][] invoiceData = {
            {"Numéro de Facture", refNumber},
            {"Date de Facture", FormatTunisienUtil.formatDateCourte(LocalDate.now())},
            {"P.V.C. (Point de Vente)", "Agence SESAME - Tunis"},
            {"Numéro d'Ordre PVC", String.format("%04d", reservation.getId())},
            {"Régime TVA", "Assujetti - TVA 19%"}
        };
        
        yPos = DocumentGenerator.addInformationTable(content, invoiceData, yPos);
        yPos -= DocumentGenerator.SECTION_SPACING;
        
        // 2. CLIENT/FACTURÉ À
        yPos = addSectionHeader(content, yPos, "2. CLIENT / FACTURÉ À");
        
        String[][] clientData = {
            {"Nom et Prénom", client.getNom() + " " + client.getPrenom()},
            {"CIN/Passeport", FormatTunisienUtil.formatCIN(client.getCin())},
            {"Adresse", client.getAdresse()},
            {"Téléphone", FormatTunisienUtil.formatTelephone(client.getTelephone())},
            {"Email", client.getEmail()}
        };
        
        yPos = DocumentGenerator.addInformationTable(content, clientData, yPos);
        yPos -= DocumentGenerator.SECTION_SPACING;
        
        // 3. DÉTAILS DE LOCATION
        yPos = addSectionHeader(content, yPos, "3. DÉTAILS DE LOCATION");
        
        String[][] rentalData = {
            {"Numéro de Réservation", "RES-" + reservation.getId()},
            {"Véhicule", vehicule.getMarque() + " " + vehicule.getModele()},
            {"Immatriculation", FormatTunisienUtil.formatPlaqueImmatriculation(vehicule.getImmatriculation())},
            {"Tarif par Jour", FormatTunisienUtil.formatMontant(vehicule.getPrixParJour()) + " TND"},
            {"Nombre de Jours", String.valueOf(reservation.getNombreJours())},
            {"Options", reservation.getOptions() != null ? reservation.getOptions() : "Aucune"}
        };
        
        yPos = DocumentGenerator.addInformationTable(content, rentalData, yPos);
        yPos -= DocumentGenerator.SECTION_SPACING;
        
        // 4. CALCUL DU MONTANT DÉDUIT/FACTURÉ
        yPos = addSectionHeader(content, yPos, "4. DÉTAIL DU CALCUL FINANCIER");
        
        double dailyRate = vehicule.getPrixParJour();
        double subTotalBeforeOptions = dailyRate * reservation.getNombreJours();
        double optionsAmount = reservation.getPrixOptions() > 0 ? reservation.getPrixOptions() : 0;
        double totalBeforeTVA = subTotalBeforeOptions + optionsAmount;
        double tvaAmount = FormatTunisienUtil.calculerTVA(totalBeforeTVA);
        double totalTTC = totalBeforeTVA + tvaAmount;
        
        // Create detailed calculation table
        String[] headers = {"Description", "Montant HT (TND)"};
        String[][] calcData = {
            {"Tarif location " + reservation.getNombreJours() + " jours", String.format("%.2f", subTotalBeforeOptions)},
            {"Options et suppléments", String.format("%.2f", optionsAmount)},
            {"", ""},
            {"SOUS-TOTAL HT", String.format("%.2f", totalBeforeTVA)},
            {"TVA 19% (Loi n° 2004-26)", String.format("%.2f", tvaAmount)},
            {"", ""},
            {"MONTANT TOTAL TTC", String.format("%.2f", totalTTC)}
        };
        
        float[] columnWidths = {400, 145};
        yPos = DocumentGenerator.addTable(content, headers, calcData, columnWidths, yPos);
        yPos -= DocumentGenerator.SECTION_SPACING;
        
        // 5. MODALITÉS DE PAIEMENT
        yPos = addSectionHeader(content, yPos, "5. MODALITÉS DE PAIEMENT");
        
        String[][] paymentData = {
            {"Mode de Paiement", modePaiement},
            {"Montant Payé", FormatTunisienUtil.formatMontant(montantPaye) + " TND"},
            {"Total Facturé", FormatTunisienUtil.formatMontant(totalTTC) + " TND"},
            {"Reste à Payer", montantPaye >= totalTTC ? "Soldé" : FormatTunisienUtil.formatMontant(totalTTC - montantPaye) + " TND"},
            {"Status", montantPaye >= totalTTC ? "PAYÉ EN INTÉGRALEMENT" : "PAIEMENT PARTIEL"}
        };
        
        yPos = DocumentGenerator.addInformationTable(content, paymentData, yPos);
        yPos -= DocumentGenerator.SECTION_SPACING * 1.2f;
        
        // 6. AVIS LÉGAL
        yPos = addSectionHeader(content, yPos, "6. AVIS LÉGAL");
        
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 8);
        content.setNonStrokingColor(0.4f, 0.4f, 0.4f);
        yPos = DocumentGenerator.addMultilineText(content,
            TunisianLegalUtil.getTVANotice(),
            8, yPos, DocumentGenerator.getPageWidth() - DocumentGenerator.getMarginLeft() - DocumentGenerator.getMarginRight() - 10);
        yPos -= 8;
        
        yPos = DocumentGenerator.addMultilineText(content,
            "Timbre Fiscal: 1.200 TND (obligatoire selon la loi tunisienne d'enregistrement)",
            8, yPos, DocumentGenerator.getPageWidth() - DocumentGenerator.getMarginLeft() - DocumentGenerator.getMarginRight() - 10);
        content.setNonStrokingColor(0, 0, 0);
        
        yPos -= 25;
        
        // CERTIFICATION AND SIGNATURE
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 10);
        content.beginText();
        content.newLineAtOffset(DocumentGenerator.getMarginLeft(), yPos);
        content.showText("CERTIFICATION DU REÇU");
        content.endText();
        
        yPos -= 18;
        
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);
        yPos = DocumentGenerator.addMultilineText(content,
            "Ce reçu certifie que SESAME Car Rental a reçu paiement conformément aux conditions ci-dessus. " +
            "Ce document est un titre de paiement officiel et doit être conservé par le client.",
            9, yPos, DocumentGenerator.getPageWidth() - DocumentGenerator.getMarginLeft() - DocumentGenerator.getMarginRight() - 10);
        
        yPos -= 30;
        
        // SIGNATURE
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
        String[] signatories = {"Caissier / Agent", "Directeur/Responsable"};
        DocumentGenerator.addSignatureSection(content, signatories, yPos);
        
        // Footer with legal info
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 6);
        content.setNonStrokingColor(0.5f, 0.5f, 0.5f);
        content.beginText();
        content.newLineAtOffset(DocumentGenerator.getMarginLeft(), 40);
        content.showText("Références: Code des Droits d'Enregistrement; Loi n° 2004-26 TVA; Loi n° 2004-33 Transports");
        content.endText();
        
        content.beginText();
        content.newLineAtOffset(DocumentGenerator.getMarginLeft(), 33);
        content.showText("Généré: " + FormatTunisienUtil.getDateTimeJour() + " | Validité du reçu: 5 ans");
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
