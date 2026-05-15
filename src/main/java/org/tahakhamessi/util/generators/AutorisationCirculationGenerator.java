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
 * Generator for "Autorisation de Circulation" (Circulation Authorization)
 * Official document authorizing vehicle circulation under Tunisian law
 */
public class AutorisationCirculationGenerator {

    public static void generateCirculationAuthorization(String outputPath, Client client, Vehicule vehicule,
                                                       Reservation reservation) throws IOException {
        PDDocument document = new PDDocument();
        
        PDPage page = new PDPage();
        document.addPage(page);
        PDPageContentStream content = new PDPageContentStream(document, page);
        
        String refNumber = FormatTunisienUtil.genererRefDocument("AUTH-CIRC");
        float yPos = DocumentGenerator.addTunisianHeader(content,
            "AUTORISATION DE CIRCULATION",
            refNumber);
        
        content.setNonStrokingColor(0.7f, 0, 0);
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 12);
        content.beginText();
        content.newLineAtOffset(DocumentGenerator.getMarginLeft(), yPos + 15);
        content.showText("DOCUMENT OFFICIEL DE CIRCULATION - VALIDE POUR ROUTES TUNISIENNES");
        content.endText();
        content.setNonStrokingColor(0, 0, 0);
        
        yPos -= DocumentGenerator.SECTION_SPACING * 1.5f;
        
        // 1. AUTHORIZATION NOTICE
        yPos = addSectionHeader(content, yPos, "1. AUTORISATION OFFICIELLE");
        
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
        yPos = DocumentGenerator.addMultilineText(content,
            "Par la présente, SESAME Car Rental autorise le détenteur de ce document à circuler " +
            "sur les routes publiques tunisiennes avec le véhicule décrit ci-dessous, conformément " +
            "à la Loi n° 2004-33 sur l'organisation des transports terrestres et au Code de la route tunisien.",
            10, yPos, DocumentGenerator.getPageWidth() - DocumentGenerator.getMarginLeft() - DocumentGenerator.getMarginRight() - 10);
        
        yPos -= DocumentGenerator.SECTION_SPACING;
        
        // 2. AUTORISÉ À
        yPos = addSectionHeader(content, yPos, "2. AUTORISÉ À");
        
        String[][] authorizedData = {
            {"Nom et Prénom", client.getNom() + " " + client.getPrenom()},
            {"CIN/Passeport", FormatTunisienUtil.formatCIN(client.getCin())},
            {"Permis de Conduire N°", client.getNumeroPermis()},
            {"Validité du Permis", client.getExpirationPermis()},
            {"Lieu de Résidence", client.getAdresse()},
            {"Téléphone", FormatTunisienUtil.formatTelephone(client.getTelephone())}
        };
        
        yPos = DocumentGenerator.addInformationTable(content, authorizedData, yPos);
        yPos -= DocumentGenerator.SECTION_SPACING;
        
        // 3. VEHICULE AUTORISA
        yPos = addSectionHeader(content, yPos, "3. VÉHICULE AUTORISÉ");
        
        String[][] vehicleData = {
            {"Marque et Modèle", vehicule.getMarque() + " " + vehicule.getModele()},
            {"Immatriculation Tunisienne", FormatTunisienUtil.formatPlaqueImmatriculation(vehicule.getImmatriculation())},
            {"Numéro de Châssis", "[À remplir par propriétaire]"},
            {"Catégorie", vehicule.getCategorie()},
            {"Carburant", vehicule.getCarburant()},
            {"Nombre de Places", String.valueOf(vehicule.getNombrePlaces())},
            {"Assurance de Responsabilité Civile", "Souscrite - Couverture 100%"}
        };
        
        yPos = DocumentGenerator.addInformationTable(content, vehicleData, yPos);
        yPos -= DocumentGenerator.SECTION_SPACING;
        
        // 4. PÉRIODE DE VALIDITÉ
        yPos = addSectionHeader(content, yPos, "4. PÉRIODE DE VALIDITÉ");
        
        LocalDate dateDebut = FormatTunisienUtil.parseDate(reservation.getDateDebut());
        LocalDate dateFin = FormatTunisienUtil.parseDate(reservation.getDateFin());
        
        String[][] validityData = {
            {"Date de Début de Circulation", FormatTunisienUtil.formatDateLongue(dateDebut) + " à 09:00"},
            {"Date de Fin de Circulation", FormatTunisienUtil.formatDateLongue(dateFin) + " à 18:00"},
            {"Durée d'Autorisation", reservation.getNombreJours() + " jours consécutifs"},
            {"Jours spécifiés", "Lundi - Dimanche (24h/24)"}
        };
        
        yPos = DocumentGenerator.addInformationTable(content, validityData, yPos);
        yPos -= DocumentGenerator.SECTION_SPACING;
        
        // 5. CONDITIONS DE CIRCULATION
        yPos = addSectionHeader(content, yPos, "5. CONDITIONS DE CIRCULATION");
        
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);
        
        String[] conditions = {
            "Le véhicule doit être en bon état technique et conforme aux normes de sécurité routière.",
            "Le conducteur doit respecter les limitations de vitesse conformément au Code de la route.",
            "Le conducteur doit posséder un permis de conduire valide et non expiré.",
            "La circulation est interdite de 22h00 à 06h00 sauf autorisation spéciale.",
            "Le conducteur doit avoir des documents d'assurance valides en sa possession.",
            "Toute infraction routière est de la responsabilité du locataire.",
            "Le transport de passagers est limité au nombre de places prévues.",
            "L'utilisation du véhicule pour transport commercial ou de marchandises est interdite."
        };
        
        for (int i = 0; i < conditions.length; i++) {
            content.beginText();
            content.newLineAtOffset(DocumentGenerator.getMarginLeft() + 5, yPos);
            content.showText((i + 1) + ". " + conditions[i]);
            content.endText();
            yPos -= 14;  // Increased from 12
        }
        
        yPos -= 18;
        
        // 6. VIOLATIONS ET SANCTIONS
        yPos = addSectionHeader(content, yPos, "6. VIOLATIONS ET SANCTIONS");
        
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 8);
        yPos = DocumentGenerator.addMultilineText(content,
            "Toute violation des conditions énoncées peut entraîner: (i) Retrait du véhicule; " +
            "(ii) Amendes conformément au Code de la route; (iii) Poursuites judiciaires; " +
            "(iv) Confiscation du véhicule par les autorités compétentes.",
            8, yPos, DocumentGenerator.getPageWidth() - DocumentGenerator.getMarginLeft() - DocumentGenerator.getMarginRight() - 10);
        
        yPos -= 25;
        
        // 7. REFERENCES LÉGALES
        yPos = addSectionHeader(content, yPos, "7. RÉFÉRENCES RÉGLEMENTAIRES");
        
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 8);
        content.beginText();
        content.newLineAtOffset(DocumentGenerator.getMarginLeft() + 5, yPos);
        content.showText("• Loi n° 2004-33 du 25 juillet 2004 sur l'organisation des transports terrestres");
        content.endText();
        yPos -= 12;
        
        content.beginText();
        content.newLineAtOffset(DocumentGenerator.getMarginLeft() + 5, yPos);
        content.showText("• Code de la route tunisien - Décret n° 2000-3061 du 5 décembre 2000");
        content.endText();
        yPos -= 12;
        
        content.beginText();
        content.newLineAtOffset(DocumentGenerator.getMarginLeft() + 5, yPos);
        content.showText("• Loi n° 94-33 du 27 avril 1994 relative à l'assurance automobile");
        content.endText();
        yPos -= 12;
        
        content.beginText();
        content.newLineAtOffset(DocumentGenerator.getMarginLeft() + 5, yPos);
        content.showText("• Code du Code Civil tunisien - Articles relatifs à la responsabilité civile");
        content.endText();
        
        yPos -= 25;
        
        // CERTIFICATION WITH OFFICIAL STAMP AREA
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 11);
        content.setNonStrokingColor(0.7f, 0, 0);
        content.beginText();
        content.newLineAtOffset(DocumentGenerator.getMarginLeft(), yPos);
        content.showText("CERTIFICATION ET SCEAU OFFICIEL");
        content.endText();
        content.setNonStrokingColor(0, 0, 0);
        
        yPos -= 15;
        
        // Draw company stamp area placeholder
        DocumentGenerator.drawBox(content,
            DocumentGenerator.getMarginLeft() + 50, yPos - 80,
            150, 80);
        
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
        content.beginText();
        content.newLineAtOffset(DocumentGenerator.getMarginLeft() + 70, yPos - 40);
        content.showText("[SCEAU OFFICIEL]");
        content.endText();
        
        yPos -= 100;
        
        // SIGNATURE
        String[] signatories = {"Le Locataire (Conducteur)", "Le Directeur SESAME"};
        DocumentGenerator.addSignatureSection(content, signatories, yPos);
        
        // Footer
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 7);
        content.setNonStrokingColor(0.5f, 0.5f, 0.5f);
        content.beginText();
        content.newLineAtOffset(DocumentGenerator.getMarginLeft(), 35);
        content.showText("DOCUMENT OFFICIEL - Ce document doit être conservé durant toute la période de circulation");
        content.endText();
        
        content.beginText();
        content.newLineAtOffset(DocumentGenerator.getMarginLeft(), 28);
        content.showText("Référence: " + refNumber + " | En cas d'accident: Contacter immédiatement SESAME: " + FormatTunisienUtil.COMPANY_PHONE);
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
