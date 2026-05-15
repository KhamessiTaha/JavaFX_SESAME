package org.tahakhamessi.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.chart.PieChart;
import org.tahakhamessi.dao.ReservationDAO;
import org.tahakhamessi.dao.VehiculeDAO;
import org.tahakhamessi.dao.PaiementDAO;
import org.tahakhamessi.model.Reservation;
import org.tahakhamessi.model.Vehicule;
import org.tahakhamessi.util.DocumentGenerator;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

public class StatisticsController {
    @FXML private Label totalReservationsLabel;
    @FXML private Label totalRevenueLabel;
    @FXML private Label availableVehiclesLabel;
    @FXML private Label mostRentedLabel;

    @FXML private PieChart statusChart;
    @FXML private TableView<Reservation> recentTable;
    @FXML private TableColumn<Reservation, String> recentClientCol;
    @FXML private TableColumn<Reservation, String> recentVehicleCol;
    @FXML private TableColumn<Reservation, String> recentDateCol;
    @FXML private TableColumn<Reservation, String> recentStatusCol;

    private ReservationDAO reservationDAO = new ReservationDAO();
    private VehiculeDAO vehiculeDAO = new VehiculeDAO();
    private PaiementDAO paiementDAO = new PaiementDAO();
    
    private double currentRevenue;
    private int currentReservations;
    private long currentAvailable;
    private long currentTotal;
    private String currentMostRented;

    @FXML
    public void initialize() {
        setupTable();
        loadStatistics();
    }

    private void setupTable() {
        recentClientCol.setCellValueFactory(cellData -> cellData.getValue().clientNomProperty());
        recentVehicleCol.setCellValueFactory(cellData -> cellData.getValue().vehiculeNomProperty());
        recentDateCol.setCellValueFactory(cellData -> cellData.getValue().dateDebutProperty());
        recentStatusCol.setCellValueFactory(cellData -> cellData.getValue().statutProperty());
    }

    private void loadStatistics() {
        ObservableList<Reservation> allReservations = reservationDAO.getAll();
        currentReservations = allReservations.size();
        currentRevenue = 0;

        for (var payment : paiementDAO.getAll()) {
            currentRevenue += payment.getMontantPaye();
        }

        ObservableList<Vehicule> allVehicles = vehiculeDAO.getAll();
        currentAvailable = allVehicles.stream().filter(v -> "Disponible".equals(v.getStatut())).count();
        currentTotal = allVehicles.size();
        
        Map<String, Integer> rentedCount = new HashMap<>();
        Map<String, Integer> statusCount = new HashMap<>();

        for (var vehicle : allVehicles) {
            String status = vehicle.getStatut();
            statusCount.put(status, statusCount.getOrDefault(status, 0) + 1);
        }

        for (var res : allReservations) {
            String veh = res.getVehiculeNom();
            rentedCount.put(veh, rentedCount.getOrDefault(veh, 0) + 1);
        }
        currentMostRented = rentedCount.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("N/A");

        totalReservationsLabel.setText(String.valueOf(currentReservations));
        totalRevenueLabel.setText(String.format("%.3f TND", currentRevenue));
        availableVehiclesLabel.setText(currentAvailable + "/" + currentTotal);
        mostRentedLabel.setText(currentMostRented);

        // Pie Chart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                statusCount.entrySet().stream()
                        .map(e -> new PieChart.Data(e.getKey(), e.getValue()))
                        .collect(Collectors.toList())
        );
        statusChart.setData(pieChartData);

        // Recent Reservations (Top 10)
        List<Reservation> recentRes = allReservations.stream()
                .sorted((r1, r2) -> Integer.compare(r2.getId(), r1.getId()))
                .limit(10)
                .collect(Collectors.toList());
        recentTable.setItems(FXCollections.observableArrayList(recentRes));
    }

    @FXML
    public void handleGenerateStatisticsReport() {
        try {
            String fileName = "Business_Statistics_Report_" + LocalDate.now() + ".pdf";
            String filePath = DocumentGenerator.showSaveDialog(statusChart.getScene().getWindow(), fileName);
            if (filePath == null) return;
            
            PDDocument doc = new PDDocument();
            PDPage page = new PDPage();
            doc.addPage(page);
            
            try (PDPageContentStream content = new PDPageContentStream(doc, page)) {
                float yPos = DocumentGenerator.addProfessionalHeader(content, "BUSINESS STATISTICS REPORT");
                
                yPos = DocumentGenerator.addSectionHeader(content, "KEY PERFORMANCE INDICATORS", yPos);
                yPos = DocumentGenerator.addKeyValuePair(content, "Total Reservations", String.valueOf(currentReservations), yPos);
                yPos = DocumentGenerator.addKeyValuePair(content, "Total Revenue", String.format("%.3f TND", currentRevenue), yPos);
                yPos = DocumentGenerator.addKeyValuePair(content, "Fleet Utilization", currentAvailable + " / " + currentTotal + " vehicles available", yPos);
                yPos = DocumentGenerator.addKeyValuePair(content, "Most Rented Vehicle", currentMostRented, yPos);
                yPos -= 15;
                
                yPos = DocumentGenerator.addMultilineText(content,
                    "This report provides a comprehensive overview of business performance. The metrics shown reflect the current period statistics. For detailed analysis, please refer to the management dashboard.",
                    8, yPos, 500);
                
                yPos -= 20;
                yPos = DocumentGenerator.addSectionHeader(content, "REPORT PERIOD", yPos);
                yPos = DocumentGenerator.addKeyValuePair(content, "Report Date", LocalDate.now().toString(), yPos);
                
                DocumentGenerator.addProfessionalFooter(content, "REP-STA-" + LocalDate.now());
            }
            
            doc.save(filePath);
            doc.close();
            System.out.println("Statistics report saved successfully: " + filePath);
        } catch (Exception e) {
            System.err.println("Error generating statistics report: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static float addTextToPDF(PDPageContentStream content, String text, int fontSize, boolean bold, float yPosition) throws IOException {
        PDFont font = new PDType1Font(bold ? Standard14Fonts.FontName.HELVETICA_BOLD : Standard14Fonts.FontName.HELVETICA);
        content.setFont(font, (float) fontSize);
        content.beginText();
        content.newLineAtOffset(30, yPosition);
        content.showText(text);
        content.endText();
        return yPosition - 15;
    }
}
