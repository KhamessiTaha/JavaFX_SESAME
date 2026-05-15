package org.tahakhamessi.util.generators;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.tahakhamessi.model.Vehicule;
import org.tahakhamessi.util.DocumentGenerator;
import org.tahakhamessi.util.FormatTunisienUtil;

import java.io.IOException;

/**
 * Generator for "Fiche d'État du Véhicule" (Vehicle Condition Sheet)
 * Includes vehicle damage diagram and detailed condition assessment
 */
public class FicheEtatVehiculeGenerator {

    public static void generateConditionSheet(String outputPath, Vehicule vehicule, 
                                            int initialKm, String damageReport) throws IOException {
        PDDocument document = new PDDocument();
        
        PDPage page = new PDPage();
        document.addPage(page);
        PDPageContentStream content = new PDPageContentStream(document, page);
        
        String refNumber = FormatTunisienUtil.genererRefDocument("FICHE-ETAT");
        float yPos = DocumentGenerator.addTunisianHeader(content,
            "FICHE D'ÉTAT DU VÉHICULE",
            refNumber);
        
        yPos -= DocumentGenerator.SECTION_SPACING;
        
        // 1. IDENTIFICATION VEHICULE
        yPos = addSectionHeader(content, yPos, "1. IDENTIFICATION DU VÉHICULE");
        
        String[][] vehicleData = {
            {"Marque", vehicule.getMarque()},
            {"Modèle", vehicule.getModele()},
            {"Immatriculation", FormatTunisienUtil.formatPlaqueImmatriculation(vehicule.getImmatriculation())},
            {"Numéro de Châssis", "[À remplir]"},
            {"Année de Fabrication", "[À remplir]"},
            {"Catégorie", vehicule.getCategorie()},
            {"Carburant", vehicule.getCarburant()},
            {"Kilométrage Initial", initialKm + " km"}
        };
        
        yPos = DocumentGenerator.addInformationTable(content, vehicleData, yPos);
        yPos -= DocumentGenerator.SECTION_SPACING * 1.1f;
        
        // 2. ETAT GENERAL DU VEHICULE
        yPos = addSectionHeader(content, yPos, "2. ÉTAT GÉNÉRAL DU VÉHICULE");
        
        // Draw vehicle diagram
        yPos = drawVehicleDiagram(content, yPos);
        
        // 3. ÉTAT DÉTAILLÉ DES COMPOSANTS
        yPos = addSectionHeader(content, yPos, "3. ÉTAT DÉTAILLÉ DES COMPOSANTS");
        
        String[] parts = {
            "Pare-brise", "Rétroviseurs", "Essuie-glaces", "Éclairage avant",
            "Éclairage arrière", "Pneus", "Freins", "Suspension",
            "Moteur", "Batterie", "Intérieur", "Sièges"
        };
        
        float colWidth = (DocumentGenerator.getPageWidth() - DocumentGenerator.getMarginLeft() - DocumentGenerator.getMarginRight()) / 3;
        
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);
        int idx = 0;
        float currentX = DocumentGenerator.getMarginLeft();
        float currentY = yPos;
        
        for (String part : parts) {
            if (idx % 3 == 0 && idx > 0) {
                currentY -= 18;  // Increased from 15
                currentX = DocumentGenerator.getMarginLeft();
            }
            
            content.beginText();
            content.newLineAtOffset(currentX, currentY);
            content.showText("[ ] " + part);
            content.endText();
            
            currentX += colWidth;
            idx++;
        }
        
        yPos = currentY - 35;  // Increased from 30
        
        // 4. DOMMAGES OBSERVÉS
        yPos = addSectionHeader(content, yPos, "4. DOMMAGES OBSERVÉS");
        
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);
        content.beginText();
        content.newLineAtOffset(DocumentGenerator.getMarginLeft(), yPos);
        content.showText("Description des dommages (localisation précise et détails):");
        content.endText();
        
        // Text box for damage description
        yPos -= 15;
        DocumentGenerator.drawBox(content,
            DocumentGenerator.getMarginLeft(),
            yPos - 80,
            DocumentGenerator.getPageWidth() - DocumentGenerator.getMarginLeft() - DocumentGenerator.getMarginRight(),
            80);
        
        if (damageReport != null && !damageReport.isEmpty()) {
            content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 8);
            yPos = DocumentGenerator.addMultilineText(content, damageReport, 8, yPos - 5,
                DocumentGenerator.getPageWidth() - DocumentGenerator.getMarginLeft() - DocumentGenerator.getMarginRight() - 20);
        }
        
        yPos -= 100;
        
        // 5. CARBURANT ET FLUIDES
        yPos = addSectionHeader(content, yPos, "5. CARBURANT ET FLUIDES");
        
        String[][] fluidsData = {
            {"Niveau de Carburant", "[ ] Plein / [ ] 3/4 / [ ] 1/2 / [ ] 1/4 / [ ] Vide"},
            {"Huile Moteur", "[ ] Normal / [ ] À compléter / [ ] À vérifier"},
            {"Liquide de Refroidissement", "[ ] Normal / [ ] À compléter"},
            {"Liquide de Frein", "[ ] Normal / [ ] À vérifier"},
            {"Liquide Lave-glace", "[ ] Plein / [ ] À compléter"}
        };
        
        yPos = DocumentGenerator.addInformationTable(content, fluidsData, yPos);
        yPos -= DocumentGenerator.SECTION_SPACING;
        
        // 6. ÉQUIPEMENTS PRÉSENTS
        yPos = addSectionHeader(content, yPos, "6. ÉQUIPEMENTS PRÉSENTS");
        
        String[][] equipmentData = {
            {"Clés", "[ ] Présentes"},
            {"Manuels du Véhicule", "[ ] Présents"},
            {"Certificat d'Immatriculation", "[ ] Présent"},
            {"Documents Assurance", "[ ] Présents"},
            {"Couverture de Secours", "[ ] Présente"},
            {"Triangle de Signalisation", "[ ] Présent"},
            {"Gilet de Sécurité", "[ ] Présent"},
            {"Extincteur", "[ ] Présent"}
        };
        
        yPos = DocumentGenerator.addInformationTable(content, equipmentData, yPos);
        yPos -= DocumentGenerator.SECTION_SPACING;
        
        // CERTIFICATION
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 10);
        content.setNonStrokingColor(0, 0.4f, 0.7f);
        content.beginText();
        content.newLineAtOffset(DocumentGenerator.getMarginLeft(), yPos);
        content.showText("CERTIFICATION DE L'ÉTAT");
        content.endText();
        content.setNonStrokingColor(0, 0, 0);
        
        yPos -= 15;
        
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);
        content.beginText();
        content.newLineAtOffset(DocumentGenerator.getMarginLeft(), yPos);
        content.showText("[ ] Certifié: Véhicule en bon état selon cette fiche");
        content.endText();
        
        yPos -= 12;
        content.beginText();
        content.newLineAtOffset(DocumentGenerator.getMarginLeft(), yPos);
        content.showText("[ ] Certifié: Véhicule avec dommages selon cette fiche (voir détails ci-dessus)");
        content.endText();
        
        yPos -= 20;
        
        // SIGNATURE
        String[] signatories = {"Inspecteur/Agent", "Responsable Agence"};
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

    /**
     * Draw a simple vehicle diagram showing areas for damage marking
     */
    private static float drawVehicleDiagram(PDPageContentStream content, float yPos) throws IOException {
        yPos -= 20;
        
        // Draw top-down vehicle diagram
        float diagramX = DocumentGenerator.getMarginLeft() + 100;
        float diagramY = yPos;
        float vehicleWidth = 150;
        float vehicleLength = 200;
        
        content.setLineWidth(2f);
        content.setNonStrokingColor(0.8f, 0.8f, 0.8f);
        
        // Main vehicle body
        content.addRect(diagramX, diagramY - vehicleLength, vehicleWidth, vehicleLength);
        content.fill();
        content.setNonStrokingColor(0, 0, 0);
        content.addRect(diagramX, diagramY - vehicleLength, vehicleWidth, vehicleLength);
        content.stroke();
        
        // Windows
        content.setNonStrokingColor(0.7f, 0.85f, 1);
        float windowWidth = vehicleWidth * 0.7f;
        float windowX = diagramX + (vehicleWidth - windowWidth) / 2;
        
        // Front window
        content.addRect(windowX, diagramY - 30, windowWidth, 20);
        content.fill();
        
        // Rear window
        content.addRect(windowX, diagramY - vehicleLength + 30, windowWidth, 20);
        content.fill();
        
        // Part labels with checkboxes
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 8);
        content.setNonStrokingColor(0, 0, 0);
        
        // Left side
        content.beginText();
        content.newLineAtOffset(diagramX - 80, diagramY - 50);
        content.showText("[ ] Côté Gauche");
        content.endText();
        
        // Right side
        content.beginText();
        content.newLineAtOffset(diagramX + vehicleWidth + 20, diagramY - 50);
        content.showText("[ ] Côté Droit");
        content.endText();
        
        // Front
        content.beginText();
        content.newLineAtOffset(diagramX + vehicleWidth / 2 - 30, diagramY + 10);
        content.showText("[ ] Avant");
        content.endText();
        
        // Rear
        content.beginText();
        content.newLineAtOffset(diagramX + vehicleWidth / 2 - 30, diagramY - vehicleLength - 20);
        content.showText("[ ] Arrière");
        content.endText();
        
        // Roof
        content.beginText();
        content.newLineAtOffset(diagramX + vehicleWidth / 2 - 30, diagramY - vehicleLength / 2);
        content.showText("[ ] Toit");
        content.endText();
        
        return yPos - vehicleLength - 40;
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
