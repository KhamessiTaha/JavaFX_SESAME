package org.tahakhamessi.util;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class DocumentGenerator {

    static {
        Logger.getLogger("org.apache.pdfbox").setLevel(java.util.logging.Level.SEVERE);
    }

    // Page dimensions (A4: 210mm x 297mm)
    public static final float PAGE_WIDTH = 595;      // 210mm
    public static final float PAGE_HEIGHT = 842;     // 297mm
    public static final float MARGIN_LEFT = 50;      // 2cm
    public static final float MARGIN_RIGHT = 50;     // 2cm
    public static final float MARGIN_TOP = 760;       // ~2cm from top
    public static final float MARGIN_BOTTOM = 50;    // 2cm
    
    // Line spacing (improved spacing for better readability)
    public static final float LINE_HEIGHT = 16;          // Increased from 13 for better readability
    public static final float PARAGRAPH_SPACING = 12;    // Increased from 10
    public static final float SECTION_SPACING = 28;      // Increased from 20

    public static String showSaveDialog(Window owner, String fileName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF Document");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName(fileName);
        fileChooser.setInitialDirectory(new java.io.File(System.getProperty("user.home")));
        
        java.io.File selectedFile = fileChooser.showSaveDialog(owner);
        return selectedFile != null ? selectedFile.getAbsolutePath() : null;
    }

    // Professional header with company branding
    public static float addProfessionalHeader(PDPageContentStream content, String docTitle) throws IOException {
        float yPos = 800;
        
        // Company name and branding
        addTextBold(content, "SESAME CAR RENTAL", 20, yPos, new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD));
        yPos -= 18;
        
        // Company details
        addText(content, "Professional Vehicle Rental Services", 10, yPos, new PDType1Font(Standard14Fonts.FontName.HELVETICA));
        yPos -= 10;
        addText(content, "Email: info@sesamecarrental.com | Phone: +216 123 456 789 | Web: www.sesamecarrental.com", 8, yPos, new PDType1Font(Standard14Fonts.FontName.HELVETICA));
        yPos -= 15;
        
        // Top separator
        drawLine(content, MARGIN_LEFT, yPos, PAGE_WIDTH - MARGIN_RIGHT, yPos, 1.5f);
        yPos -= 18;
        
        // Document title
        addTextBold(content, docTitle, 16, yPos, new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD));
        yPos -= 20;
        
        return yPos;
    }

    // Professional footer
    public static void addProfessionalFooter(PDPageContentStream content, String reference) throws IOException {
        drawLine(content, MARGIN_LEFT, 55, PAGE_WIDTH - MARGIN_RIGHT, 55, 0.5f);
        
        addText(content, "Generated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + " | Reference: " + reference, 7, 42, new PDType1Font(Standard14Fonts.FontName.HELVETICA));
        addText(content, "This document is officially issued by SESAME Car Rental. All conditions and terms are binding.", 7, 32, new PDType1Font(Standard14Fonts.FontName.HELVETICA));
    }

    /**
     * Add professional Tunisian header with company info
     */
    public static float addTunisianHeader(PDPageContentStream content, String docTitle, String refNumber) throws IOException {
        float yPos = MARGIN_TOP;
        
        // Company name and branding
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 18);
        content.setNonStrokingColor(0, 0.4f, 0.7f);
        content.beginText();
        content.newLineAtOffset(MARGIN_LEFT, yPos);
        content.showText(FormatTunisienUtil.COMPANY_NAME);
        content.endText();
        yPos -= 18;
        
        // Reset color to black
        content.setNonStrokingColor(0, 0, 0);
        
        // Company details in smaller font
        float detailX = MARGIN_LEFT;
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 8);
        content.beginText();
        content.newLineAtOffset(detailX, yPos);
        content.showText("MF: " + FormatTunisienUtil.COMPANY_MATRICULE_FISCALE);
        content.endText();
        yPos -= 10;
        
        content.beginText();
        content.newLineAtOffset(detailX, yPos);
        content.showText("RNE: " + FormatTunisienUtil.COMPANY_RNE + " | " + FormatTunisienUtil.COMPANY_ADDRESS);
        content.endText();
        yPos -= 10;
        
        content.beginText();
        content.newLineAtOffset(detailX, yPos);
        content.showText("Tel: " + FormatTunisienUtil.COMPANY_PHONE + " | Email: " + FormatTunisienUtil.COMPANY_EMAIL);
        content.endText();
        yPos -= 15;
        
        // Reference and date in top right
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);
        String refText = "Ref: " + refNumber;
        float refWidth = refText.length() * 5;
        content.beginText();
        content.newLineAtOffset(PAGE_WIDTH - MARGIN_RIGHT - refWidth, yPos + 8);
        content.showText(refText);
        content.endText();
        
        // Date
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);
        String dateText = "Date: " + FormatTunisienUtil.formatDateCourte(java.time.LocalDate.now());
        float dateWidth = dateText.length() * 5;
        content.beginText();
        content.newLineAtOffset(PAGE_WIDTH - MARGIN_RIGHT - dateWidth, yPos - 8);
        content.showText(dateText);
        content.endText();
        yPos -= 18;
        
        // Top separator line
        drawLine(content, MARGIN_LEFT, yPos, PAGE_WIDTH - MARGIN_RIGHT, yPos, 2f);
        yPos -= 15;
        
        // Document title
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 16);
        content.setNonStrokingColor(0, 0, 0);
        content.beginText();
        content.newLineAtOffset(MARGIN_LEFT, yPos);
        content.showText(docTitle);
        content.endText();
        yPos -= 20;
        
        return yPos;
    }

    /**
     * Add a two-column information table (Key-Value pairs) with improved spacing
     */
    public static float addInformationTable(PDPageContentStream content, String[][] data, float yPos) throws IOException {
        float colWidth1 = 150;
        float colWidth2 = PAGE_WIDTH - MARGIN_LEFT - MARGIN_RIGHT - colWidth1;
        float rowHeight = 18;      // Increased from 15 for better spacing
        
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
        
        for (String[] row : data) {
            // Skip empty rows with extra spacing
            if (row == null || row.length == 0) {
                yPos -= rowHeight + 5; // Add extra spacing for empty row
                continue;
            }
            
            // Draw key column
            content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 10);
            content.beginText();
            content.newLineAtOffset(MARGIN_LEFT, yPos - 11);
            content.showText(row[0] + ":");
            content.endText();
            
            // Draw value column with better text wrapping
            content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 10);
            content.beginText();
            content.newLineAtOffset(MARGIN_LEFT + colWidth1 + 10, yPos - 11);
            String value = row.length > 1 ? row[1] : "";
            // Increase truncation limit for better content visibility
            content.showText(truncateText(value, 75));
            content.endText();
            
            // Draw separator line with better positioning
            drawLine(content, MARGIN_LEFT, yPos - 15, PAGE_WIDTH - MARGIN_RIGHT, yPos - 15, 0.3f);
            yPos -= rowHeight;
        }
        
        return yPos;
    }

    /**
     * Add a multi-column table with headers and improved spacing
     */
    public static float addTable(PDPageContentStream content, String[] headers, String[][] data, float[] columnWidths, float yPos) throws IOException {
        float rowHeight = 18;      // Increased from 16 for better spacing
        float headerHeight = 20;   // Increased from 18
        
        // ...existing code...
        // Draw header background
        content.setNonStrokingColor(0.2f, 0.2f, 0.2f);
        float xPos = MARGIN_LEFT;
        for (float width : columnWidths) {
            content.addRect(xPos, yPos - headerHeight, width, headerHeight);
            content.fill();
            xPos += width;
        }
        
        // Draw header text
        content.setNonStrokingColor(1, 1, 1);
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 10);
        xPos = MARGIN_LEFT + 4;
        for (int i = 0; i < headers.length; i++) {
            content.beginText();
            content.newLineAtOffset(xPos, yPos - 15);
            content.showText(headers[i]);
            content.endText();
            xPos += columnWidths[i];
        }
        
        yPos -= headerHeight;
        
        // Draw data rows with better spacing
        content.setNonStrokingColor(0, 0, 0);
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);
        
        for (String[] row : data) {
            xPos = MARGIN_LEFT + 4;
            for (int i = 0; i < row.length && i < columnWidths.length; i++) {
                content.beginText();
                content.newLineAtOffset(xPos, yPos - 11);
                content.showText(truncateText(row[i], 25));
                content.endText();
                xPos += columnWidths[i];
            }
            
            // Draw row separator with better positioning
            drawLine(content, MARGIN_LEFT, yPos - 15, PAGE_WIDTH - MARGIN_RIGHT, yPos - 15, 0.3f);
            yPos -= rowHeight;
        }
        
        return yPos;
    }

    /**
     * Add a text box with border (for important notices) with improved spacing
     */
    public static float addNoticeBox(PDPageContentStream content, String title, String text, float yPos) throws IOException {
        float boxHeight = 75;     // Increased from 60
        float boxWidth = PAGE_WIDTH - MARGIN_LEFT - MARGIN_RIGHT;
        
        // Draw box border
        content.setLineWidth(1.5f);
        content.setNonStrokingColor(0.95f, 0.95f, 0.95f);
        content.addRect(MARGIN_LEFT, yPos - boxHeight, boxWidth, boxHeight);
        content.fill();
        content.setNonStrokingColor(0, 0, 0);
        content.addRect(MARGIN_LEFT, yPos - boxHeight, boxWidth, boxHeight);
        content.stroke();
        
        // Draw title
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 10);
        content.beginText();
        content.newLineAtOffset(MARGIN_LEFT + 8, yPos - 15);
        content.showText(title);
        content.endText();
        
        // Draw text (wrapped) with better spacing
        float textYPos = yPos - 30;
        textYPos = addMultilineText(content, text, 8, textYPos, boxWidth - 20);
        
        return yPos - boxHeight - SECTION_SPACING;
    }

    /**
     * Add signature section with multiple signatories
     */
    public static float addSignatureSection(PDPageContentStream content, String[] signatories, float yPos) throws IOException {
        yPos -= 30;
        
        // Section title
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 11);
        content.beginText();
        content.newLineAtOffset(MARGIN_LEFT, yPos);
        content.showText("SIGNATURES ET ACCEPTATION");
        content.endText();
        
        yPos -= 20;
        
        float columnWidth = (PAGE_WIDTH - MARGIN_LEFT - MARGIN_RIGHT) / signatories.length;
        
        for (int i = 0; i < signatories.length; i++) {
            float colX = MARGIN_LEFT + (i * columnWidth);
            
            // Signature line
            drawLine(content, colX + 10, yPos - 40, colX + columnWidth - 10, yPos - 40, 1f);
            
            // Name label
            content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);
            String signingLine = "Signature de " + signatories[i];
            float lineWidth = signingLine.length() * 5;
            content.beginText();
            content.newLineAtOffset(colX + (columnWidth - lineWidth) / 2, yPos - 55);
            content.showText(signingLine);
            content.endText();
            
            // Date line label
            content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 8);
            content.beginText();
            content.newLineAtOffset(colX + 10, yPos - 65);
            content.showText("Date: ___/___/_____");
            content.endText();
        }
        
        return yPos - 75;
    }

    public static void addText(PDPageContentStream content, String text, int fontSize, float yPosition, PDType1Font font) throws IOException {
        content.setFont(font, (float) fontSize);
        content.beginText();
        content.newLineAtOffset(MARGIN_LEFT, yPosition);
        content.showText(truncateText(text, 100));
        content.endText();
    }

    public static void addTextBold(PDPageContentStream content, String text, int fontSize, float yPosition, PDType1Font font) throws IOException {
        content.setFont(font, (float) fontSize);
        content.beginText();
        content.newLineAtOffset(MARGIN_LEFT, yPosition);
        content.showText(text);
        content.endText();
    }

    public static void addTextRight(PDPageContentStream content, String text, int fontSize, float yPosition, PDType1Font font) throws IOException {
        content.setFont(font, (float) fontSize);
        content.beginText();
        float textWidth = text.length() * fontSize * 0.5f;
        content.newLineAtOffset(PAGE_WIDTH - MARGIN_RIGHT - textWidth, yPosition);
        content.showText(text);
        content.endText();
    }

    public static void drawLine(PDPageContentStream content, float x1, float y1, float x2, float y2, float width) throws IOException {
        content.setLineWidth(width);
        content.moveTo(x1, y1);
        content.lineTo(x2, y2);
        content.stroke();
    }

    public static void drawBox(PDPageContentStream content, float x, float y, float width, float height) throws IOException {
        content.setLineWidth(1);
        content.addRect(x, y, width, height);
        content.stroke();
    }

    public static float addSectionHeader(PDPageContentStream content, String header, float yPos) throws IOException {
        yPos -= 12;
        drawLine(content, MARGIN_LEFT, yPos + 2, PAGE_WIDTH - MARGIN_RIGHT, yPos + 2, 0.5f);
        addTextBold(content, "  " + header, 11, yPos, new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD));
        return yPos - 16;
    }

    public static float addKeyValuePair(PDPageContentStream content, String key, String value, float yPos) throws IOException {
        addTextBold(content, key + ":", 10, yPos, new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD));
        addTextRight(content, value, 10, yPos, new PDType1Font(Standard14Fonts.FontName.HELVETICA));
        return yPos - LINE_HEIGHT;
    }

    public static void drawTableHeader(PDPageContentStream content, String[] headers, float[] widths, float yPos) throws IOException {
        float xPos = MARGIN_LEFT;
        
        // Draw header background with darker shade
        content.setLineWidth(1);
        content.setNonStrokingColor(0.15f, 0.15f, 0.15f);
        for (float width : widths) {
            content.addRect(xPos, yPos - 16, width, 16);
            content.fill();
            xPos += width;
        }
        
        // Draw header text in white
        content.setNonStrokingColor(1, 1, 1);
        xPos = MARGIN_LEFT + 4;
        int idx = 0;
        for (String header : headers) {
            content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 9);
            content.beginText();
            content.newLineAtOffset(xPos, yPos - 12);
            content.showText(header);
            content.endText();
            xPos += widths[idx++];
        }
        
        // Reset color
        content.setNonStrokingColor(0, 0, 0);
    }

    public static float drawTableRow(PDPageContentStream content, String[] values, float[] widths, float yPos) throws IOException {
        float xPos = MARGIN_LEFT;
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 9);
        content.setNonStrokingColor(0, 0, 0);
        
        for (int i = 0; i < values.length; i++) {
            content.beginText();
            content.newLineAtOffset(xPos + 4, yPos - 5);
            content.showText(values[i] != null ? values[i] : "");
            content.endText();
            xPos += widths[i];
        }
        
        // Draw row separator line
        drawLine(content, MARGIN_LEFT, yPos - 12, PAGE_WIDTH - MARGIN_RIGHT, yPos - 12, 0.3f);
        return yPos - 14;
    }

    /**
     * Add multiline text with improved word wrapping and spacing
     */
    public static float addMultilineText(PDPageContentStream content, String text, int fontSize, float yPos, float maxWidth) throws IOException {
        content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), (float) fontSize);
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        float lineHeight = fontSize > 10 ? fontSize + 2 : LINE_HEIGHT;
        
        for (String word : words) {
            if ((line.length() + word.length()) * fontSize * 0.5f > maxWidth) {
                addText(content, line.toString(), fontSize, yPos, new PDType1Font(Standard14Fonts.FontName.HELVETICA));
                yPos -= lineHeight + 2;  // Add extra spacing between lines
                line = new StringBuilder(word + " ");
            } else {
                line.append(word).append(" ");
            }
        }
        
        if (line.length() > 0) {
            addText(content, line.toString(), fontSize, yPos, new PDType1Font(Standard14Fonts.FontName.HELVETICA));
            yPos -= lineHeight + 2;
        }
        
        return yPos;
    }

    private static String truncateText(String text, int maxLength) {
        if (text.length() > maxLength) {
            return text.substring(0, maxLength - 3) + "...";
        }
        return text;
    }

    public static float getPageWidth() {
        return PAGE_WIDTH;
    }

    public static float getPageHeight() {
        return PAGE_HEIGHT;
    }

    public static float getMarginLeft() {
        return MARGIN_LEFT;
    }

    public static float getMarginRight() {
        return MARGIN_RIGHT;
    }

    public static float getLineHeight() {
        return LINE_HEIGHT;
    }
}



