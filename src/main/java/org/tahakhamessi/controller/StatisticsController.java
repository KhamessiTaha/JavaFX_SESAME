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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
        int totalReservations = allReservations.size();
        double totalRevenue = 0;

        for (var payment : paiementDAO.getAll()) {
            totalRevenue += payment.getMontantPaye();
        }

        ObservableList<Vehicule> allVehicles = vehiculeDAO.getAll();
        long availableVehicles = allVehicles.stream().filter(v -> "Disponible".equals(v.getStatut())).count();
        
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
        String mostRented = rentedCount.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("N/A");

        totalReservationsLabel.setText(String.valueOf(totalReservations));
        totalRevenueLabel.setText(String.format("%.3f TND", totalRevenue));
        availableVehiclesLabel.setText(availableVehicles + "/" + allVehicles.size());
        mostRentedLabel.setText(mostRented);

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
}

